// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.intellij.plugin.powershell.ide.debugger.localize.PowerShellDebuggerLocalize;
import com.intellij.plugin.powershell.ide.run.PowerShellRunConfiguration;
import com.intellij.plugin.powershell.ide.run.PowerShellScriptCommandLineState;

import consulo.execution.debug.XDebugSession;
import consulo.execution.debugger.dap.protocol.DAP;
import consulo.execution.debugger.dap.protocol.DAPFactory;
import consulo.execution.ui.console.ConsoleViewContentType;
import consulo.platform.Platform;
import consulo.process.KillableProcessHandler;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilderFactory;
import consulo.process.ProcessOutputType;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.util.dataholder.Key;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

final class PowerShellDebugHost implements AutoCloseable {

    private final PowerShellRunConfiguration myConfiguration;

    private final XDebugSession mySession;

    private final ProcessHandlerBuilderFactory myProcessHandlerBuilderFactory;

    private final PowerShellDebugSettings mySettings;

    private final List<AutoCloseable> myConnections = new CopyOnWriteArrayList<>();

    private volatile @Nullable ProcessHandler myHandler;

    private volatile boolean myClosed;

    private @Nullable Path mySessionFile;

    PowerShellDebugHost(
            PowerShellRunConfiguration configuration,
            XDebugSession session,
            ProcessHandlerBuilderFactory processHandlerBuilderFactory,
            PowerShellDebugSettings settings) {
        myConfiguration = configuration;
        mySession = session;
        myProcessHandlerBuilderFactory = processHandlerBuilderFactory;
        mySettings = settings;
    }

    static @Nullable Path findStartupScript(String location) {
        if (location == null || location.isBlank()) {
            return null;
        }
        Path root;
        try {
            root = Platform.current().fs().getPath(location).toAbsolutePath();
        }
        catch (InvalidPathException error) {
            return null;
        }
        if (Files.isRegularFile(root)
                && root.getFileName().toString().equalsIgnoreCase("Start-EditorServices.ps1")) {
            return root;
        }
        for (String suffix :
                new String[] {
                    "Start-EditorServices.ps1",
                    "PowerShellEditorServices/Start-EditorServices.ps1",
                    "modules/PowerShellEditorServices/Start-EditorServices.ps1"
                }) {
            Path candidate = root.resolve(suffix);
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    DAP connect(DAPFactory factory) throws Exception {
        Path startup = findStartupScript(mySettings.getState().myEditorServicesPath);
        if (startup == null) {
            throw new IOException(
                    PowerShellDebuggerLocalize.uiStartEditorservicesPs1WasNotFound().get());
        }
        Path directory = Files.createTempDirectory("consulo-powershell-debug-");
        mySessionFile = directory.resolve("session.json");
        Platform platform = Platform.current();
        Path moduleDirectory = Objects.requireNonNull(startup.getParent());
        Path bundledModulesDirectory = moduleDirectory.getParent();
        if (bundledModulesDirectory == null) {
            bundledModulesDirectory = moduleDirectory;
        }
        GeneralCommandLine command =
                new GeneralCommandLine()
                        .withPlatform(platform)
                        .withExecutablePath(
                                platform.fs().getPath(myConfiguration.myExecutablePath));
        command.setCharset(StandardCharsets.UTF_8);
        command.addParameters("-NoProfile", "-NonInteractive");
        command.getParametersList().addParametersString(myConfiguration.myCommandOptions);
        command.addParameters(
                "-ExecutionPolicy",
                "RemoteSigned",
                "-File",
                startup.toString(),
                "-HostName",
                "Consulo",
                "-HostProfileId",
                "com.intellij.plugin.powershell",
                "-HostVersion",
                "3.0.0",
                "-BundledModulesPath",
                bundledModulesDirectory.toString(),
                "-DebugServiceOnly",
                "-SplitInOutPipes",
                "-LogLevel",
                "Information",
                "-LogPath",
                directory.resolve("adapter.log").toString(),
                "-SessionDetailsPath",
                mySessionFile.toString());
        GeneralCommandLine runCommand =
                PowerShellScriptCommandLineState.buildCommand(myConfiguration);
        if (runCommand.getWorkDirectory() != null) {
            command.setWorkDirectory(runCommand.getWorkDirectory());
        }
        myConfiguration.myEnvironmentVariables.configureCommandLine(command, true);
        if (myClosed) {
            throw new IOException(PowerShellDebuggerLocalize.uiDebugSessionWasCancelled().get());
        }
        myHandler = myProcessHandlerBuilderFactory.newBuilder(command).colored().killable().build();
        myHandler.addProcessListener(
                new ProcessListener() {

                    public void onTextAvailable(ProcessEvent event, Key outputType) {
                        ConsoleViewContentType type =
                                ProcessOutputType.isStderr(outputType)
                                        ? ConsoleViewContentType.ERROR_OUTPUT
                                        : ConsoleViewContentType.NORMAL_OUTPUT;
                        mySession.getConsoleView().print(event.getText(), type);
                    }

                    public void processTerminated(ProcessEvent event) {
                        if (!myClosed) {
                            mySession.stop();
                        }
                    }
                });
        myHandler.startNotify();
        if (myClosed) {
            close();
            throw new IOException(PowerShellDebuggerLocalize.uiDebugSessionWasCancelled().get());
        }
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(60);
        while (!myClosed && System.nanoTime() < deadline) {
            if (myHandler.isProcessTerminated()) {
                throw new IOException(
                        PowerShellDebuggerLocalize
                                .uiPowershellEditorServicesExitedDuringStartupSeeTheDebugConsoleAnd0(
                                        directory)
                                .get());
            }
            if (Files.isRegularFile(mySessionFile)) {
                JsonObject details = null;
                try {
                    details =
                            JsonParser.parseString(Files.readString(mySessionFile))
                                    .getAsJsonObject();
                }
                catch (JsonParseException | IllegalStateException ignored) {
                    /* The server may still be writing its session file. */
                }
                if (details != null) {
                    String status = text(details, "status");
                    if ("failed".equalsIgnoreCase(status)) {
                        throw new IOException(
                                PowerShellDebuggerLocalize.uiPowershellEditorServicesFailedSee0(
                                                directory.resolve("adapter.log"))
                                        .get());
                    }
                    if (details.has("debugServicePort")) {
                        Socket socket = new Socket();
                        myConnections.add(socket);
                        socket.connect(
                                new InetSocketAddress(
                                        "127.0.0.1", details.get("debugServicePort").getAsInt()),
                                10000);
                        socket.setTcpNoDelay(true);
                        return factory.createStreamDAP(
                                socket.getInputStream(), socket.getOutputStream());
                    }
                    String read = text(details, "debugServiceReadPipeName");
                    String write = text(details, "debugServiceWritePipeName");
                    if (read != null && write != null) {
                        if (Platform.current().os().isWindows()) {
                            RandomAccessFile toServer = new RandomAccessFile(read, "rw");
                            myConnections.add(toServer);
                            RandomAccessFile fromServer = new RandomAccessFile(write, "r");
                            myConnections.add(fromServer);
                            return factory.createStreamDAP(
                                    Channels.newInputStream(fromServer.getChannel()),
                                    Channels.newOutputStream(toServer.getChannel()));
                        }
                        SocketChannel toServer =
                                SocketChannel.open(UnixDomainSocketAddress.of(read));
                        myConnections.add(toServer);
                        SocketChannel fromServer =
                                SocketChannel.open(UnixDomainSocketAddress.of(write));
                        myConnections.add(fromServer);
                        return factory.createStreamDAP(
                                Channels.newInputStream(fromServer),
                                Channels.newOutputStream(toServer));
                    }
                }
            }
            Thread.sleep(100);
        }
        throw new IOException(
                myClosed
                        ? PowerShellDebuggerLocalize.uiDebugSessionWasCancelled().get()
                        : PowerShellDebuggerLocalize.debugAdapterStartupTimeout(
                                        directory.resolve("adapter.log"))
                                .get());
    }

    private static @Nullable String text(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : null;
    }

    public void close() {
        myClosed = true;
        for (AutoCloseable connection : myConnections) {
            try {
                connection.close();
            }
            catch (Exception ignored) {
            }
        }
        myConnections.clear();
        ProcessHandler process = myHandler;
        if (process != null && !process.isProcessTerminated()) {
            process.destroyProcess();
            if (process instanceof KillableProcessHandler killable && killable.canKillProcess()) {
                killable.killProcess();
            }
        }
        if (mySessionFile != null) {
            try {
                Files.deleteIfExists(mySessionFile);
            }
            catch (IOException ignored) {
            }
        }
    }
}
