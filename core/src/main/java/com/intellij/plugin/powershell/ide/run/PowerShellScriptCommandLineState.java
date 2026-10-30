// SPDX-FileCopyrightText: 2017-2022 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2021-2026 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.run;

import consulo.execution.configuration.CommandLineState;
import consulo.execution.process.ProcessTerminatedListener;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.util.ProgramParametersUtil;
import consulo.platform.Platform;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilderFactory;
import consulo.process.cmd.GeneralCommandLine;

import java.nio.charset.StandardCharsets;

public final class PowerShellScriptCommandLineState extends CommandLineState {

    private final PowerShellRunConfiguration myConfiguration;
    private final ProcessHandlerBuilderFactory myProcessHandlerBuilderFactory;

    public PowerShellScriptCommandLineState(
            PowerShellRunConfiguration configuration,
            ExecutionEnvironment environment,
            ProcessHandlerBuilderFactory processHandlerBuilderFactory) {
        super(environment);
        myConfiguration = configuration;
        myProcessHandlerBuilderFactory = processHandlerBuilderFactory;
    }

    public static GeneralCommandLine buildCommand(PowerShellRunConfiguration configuration) {
        Platform platform = Platform.current();
        GeneralCommandLine command =
                new GeneralCommandLine()
                        .withPlatform(platform)
                        .withExecutablePath(platform.fs().getPath(configuration.myExecutablePath));
        command.getParametersList().addParametersString(configuration.myCommandOptions);
        command.addParameter("-File");
        String script =
                ProgramParametersUtil.expandPath(
                        configuration.myScriptPath, null, configuration.getProject());
        command.addParameter(script);
        command.getParametersList().addParametersString(configuration.myScriptParameters);
        String workingDirectory = configuration.myCustomWorkingDirectory;
        if (workingDirectory == null || workingDirectory.isBlank()) {
            workingDirectory =
                    PSExecutionUtil.getDefaultWorkingDirectory(
                                    platform.fs().getPath(script).toAbsolutePath())
                            .toString();
        }
        command.withWorkingDirectory(
                platform.fs()
                        .getPath(
                                ProgramParametersUtil.expandPath(
                                        workingDirectory, null, configuration.getProject())));
        command.setCharset(StandardCharsets.UTF_8);
        configuration.myEnvironmentVariables.configureCommandLine(command, true);
        return command;
    }

    protected ProcessHandler startProcess() throws ExecutionException {
        ProcessHandler process =
                myProcessHandlerBuilderFactory
                        .newBuilder(buildCommand(myConfiguration))
                        .colored()
                        .killable()
                        .build();
        ProcessTerminatedListener.attach(process);
        return process;
    }
}
