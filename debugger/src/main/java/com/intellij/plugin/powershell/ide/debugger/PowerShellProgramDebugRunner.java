// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import com.intellij.plugin.powershell.ide.debugger.localize.PowerShellDebuggerLocalize;
import com.intellij.plugin.powershell.ide.run.PowerShellRunConfiguration;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.FileDocumentManager;
import consulo.execution.configuration.RunProfile;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.debug.DefaultDebugExecutor;
import consulo.execution.debug.XDebugSession;
import consulo.execution.debug.XDebuggerManager;
import consulo.execution.runner.DefaultProgramRunner;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.RunContentDescriptor;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandlerBuilderFactory;

import jakarta.inject.Inject;

@ExtensionImpl
public final class PowerShellProgramDebugRunner extends DefaultProgramRunner {

    private final FileDocumentManager myFileDocumentManager;

    private final ProcessHandlerBuilderFactory myProcessHandlerBuilderFactory;

    private final PowerShellDebugSettings mySettings;

    @Inject
    public PowerShellProgramDebugRunner(
            FileDocumentManager fileDocumentManager,
            ProcessHandlerBuilderFactory processHandlerBuilderFactory,
            PowerShellDebugSettings settings) {
        myFileDocumentManager = fileDocumentManager;
        myProcessHandlerBuilderFactory = processHandlerBuilderFactory;
        mySettings = settings;
    }

    public String getRunnerId() {
        return "PowerShellDebugRunner";
    }

    public boolean canRun(String executorId, RunProfile profile) {
        return DefaultDebugExecutor.EXECUTOR_ID.equals(executorId)
                && profile instanceof PowerShellRunConfiguration;
    }

    protected RunContentDescriptor doExecute(
            RunProfileState state, ExecutionEnvironment environment) throws ExecutionException {
        myFileDocumentManager.saveAllDocuments();
        String path = mySettings.getState().myEditorServicesPath;
        if (PowerShellDebugHost.findStartupScript(path) == null) {
            throw new ExecutionException(
                    PowerShellDebuggerLocalize
                            .uiConfigureThePowershellEditorServicesLocationInSettingsExecutionPower()
                            .get());
        }
        PowerShellRunConfiguration configuration =
                (PowerShellRunConfiguration) environment.getRunProfile();
        XDebugSession session =
                XDebuggerManager.getInstance(environment.getProject())
                        .startSession(
                                environment,
                                s ->
                                        new PowerShellDebugProcess(
                                                s,
                                                configuration,
                                                myProcessHandlerBuilderFactory,
                                                mySettings));
        ((PowerShellDebugProcess) session.getDebugProcess()).start();
        return session.getRunContentDescriptor();
    }
}
