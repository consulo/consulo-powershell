/*
 * SPDX-FileCopyrightText: 2023-2026 intellij-powershell contributors <https://github.com/intellij-powershell/intellij-powershell>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.intellij.plugin.powershell.ide.run;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.RunProfile;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.execution.runner.DefaultProgramRunner;

@ExtensionImpl
public final class PowerShellProgramRunner extends DefaultProgramRunner {

    public String getRunnerId() {
        return "PowerShellRunner";
    }

    public boolean canRun(String executorId, RunProfile profile) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId)
                && profile instanceof PowerShellRunConfiguration;
    }
}
