// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import com.intellij.plugin.powershell.ide.debugger.localize.PowerShellDebuggerLocalize;
import com.intellij.plugin.powershell.ide.run.PowerShellRunConfiguration;
import com.intellij.plugin.powershell.ide.run.PowerShellScriptCommandLineState;

import consulo.execution.debug.XDebugSession;
import consulo.execution.debug.breakpoint.XLineBreakpointType;
import consulo.execution.debug.evaluation.XDebuggerEditorsProvider;
import consulo.execution.debugger.dap.DAPDebugProcess;
import consulo.execution.debugger.dap.protocol.DAP;
import consulo.execution.debugger.dap.protocol.DAPFactory;
import consulo.execution.debugger.dap.protocol.LaunchRequestArguments;
import consulo.execution.util.ProgramParametersUtil;
import consulo.process.ProcessHandlerBuilderFactory;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.cmd.ParametersList;

public final class PowerShellDebugProcess extends DAPDebugProcess {

    private final PowerShellRunConfiguration myConfiguration;

    private final PowerShellDebugHost myHost;

    public PowerShellDebugProcess(
            XDebugSession session,
            PowerShellRunConfiguration configuration,
            ProcessHandlerBuilderFactory processHandlerBuilderFactory,
            PowerShellDebugSettings settings) {
        super(session);
        myConfiguration = configuration;
        myHost =
                new PowerShellDebugHost(
                        configuration, session, processHandlerBuilderFactory, settings);
    }

    protected XLineBreakpointType<?> getLineBreakpointType() {
        return PowerShellBreakpointType.getInstance();
    }

    protected String getAdapterId() {
        return "PowerShell";
    }

    protected DAP createDAP(DAPFactory factory) {
        try {
            return myHost.connect(factory);
        }
        catch (Exception error) {
            myHost.close();
            getSession().reportError(PowerShellDebuggerLocalize.debugStartupFailed(errorMessage(error)).get());
            getSession().stop();
            throw new IllegalStateException(error);
        }
    }

    protected LaunchRequestArguments createLaunchRequestArguments() {
        PowerShellLaunchArguments args = new PowerShellLaunchArguments();
        GeneralCommandLine command = PowerShellScriptCommandLineState.buildCommand(myConfiguration);
        args.script =
                ProgramParametersUtil.expandPath(
                        myConfiguration.myScriptPath, null, myConfiguration.getProject());
        args.cwd =
                command.getWorkDirectory() == null
                        ? null
                        : command.getWorkDirectory().getAbsolutePath();
        ParametersList parameters = new ParametersList();
        parameters.addParametersString(myConfiguration.myScriptParameters);
        args.Args = parameters.getList().toArray(String[]::new);
        args.Env = myConfiguration.myEnvironmentVariables.getEnvs();
        args.noDebug = false;
        args.stopAtEntry = false;
        return args;
    }

    public XDebuggerEditorsProvider getEditorsProvider() {
        return new PowerShellDebuggerEditorsProvider();
    }

    protected void stopImpl() {
        try {
            super.stopImpl();
        }
        finally {
            myHost.close();
        }
    }
}
