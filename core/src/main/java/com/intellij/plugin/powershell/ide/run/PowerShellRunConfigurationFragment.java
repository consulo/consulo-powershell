// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.run;

import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.disposer.Disposable;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;

import java.util.Objects;

final class PowerShellRunConfigurationFragment implements RunConfigurationFragment<PowerShellRunConfiguration> {
    @Override
    public String getId() {
        return "powershell.script";
    }

    @Override
    public LocalizeValue getName() {
        return PowerShellLocalize.uiPowershell();
    }

    @Override
    public boolean isRemovable() {
        return false;
    }

    @Override
    @RequiredUIAccess
    public RunConfigurationFragmentEditor<PowerShellRunConfiguration> createEditor(Project project, Disposable parent) {
        TextBox executable = TextBox.create();
        TextBox script = TextBox.create();
        TextBox directory = TextBox.create();
        TextBox options = TextBox.create();
        TextBox arguments = TextBox.create();
        return new RunConfigurationFragmentEditor<>() {
            @Override
            @RequiredUIAccess
            public void addRows(RunConfigurationFragmentLayout layout) {
                layout.addLabeled(PowerShellLocalize.uiPowershellExecutable(), executable);
                layout.addLabeled(PowerShellLocalize.uiScriptPath(), script);
                layout.addLabeled(PowerShellLocalize.uiWorkingDirectory(), directory);
                layout.addLabeled(PowerShellLocalize.uiPowershellOptions(), options);
                layout.addLabeled(PowerShellLocalize.uiScriptArguments(), arguments);
            }

            @Override
            @RequiredUIAccess
            public void reset(PowerShellRunConfiguration target) {
                executable.setValue(target.myExecutablePath);
                script.setValue(target.myScriptPath);
                directory.setValue(target.myCustomWorkingDirectory);
                options.setValue(target.myCommandOptions);
                arguments.setValue(target.myScriptParameters);
            }

            @Override
            @RequiredUIAccess
            public void apply(PowerShellRunConfiguration target) {
                target.myExecutablePath = Objects.toString(executable.getValue(), "");
                target.myScriptPath = Objects.toString(script.getValue(), "");
                target.myCustomWorkingDirectory = Objects.toString(directory.getValue(), "");
                target.myCommandOptions = Objects.toString(options.getValue(), "");
                target.myScriptParameters = Objects.toString(arguments.getValue(), "");
            }
        };
    }
}
