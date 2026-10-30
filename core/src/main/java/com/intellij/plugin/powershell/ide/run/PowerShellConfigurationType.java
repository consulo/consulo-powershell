// SPDX-FileCopyrightText: 2017-2021 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.run;

import com.intellij.plugin.powershell.icon.PowerShellIconGroup;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationType;
import consulo.execution.configuration.ConfigurationTypeBase;
import consulo.execution.configuration.RunConfiguration;
import consulo.process.ProcessHandlerBuilderFactory;
import consulo.project.Project;

import jakarta.inject.Inject;

@ExtensionImpl
public final class PowerShellConfigurationType extends ConfigurationTypeBase {

    @Inject
    public PowerShellConfigurationType(ProcessHandlerBuilderFactory processHandlerBuilderFactory) {
        super(
                "PowerShellRunType",
                PowerShellLocalize.uiPowershell(),
                PowerShellLocalize.uiRunAPowershellScript(),
                PowerShellIconGroup.file());
        addFactory(
                new ConfigurationFactory(this) {

                    public RunConfiguration createTemplateConfiguration(Project project) {
                        return new PowerShellRunConfiguration(
                                project, this, "PowerShell", processHandlerBuilderFactory);
                    }
                });
    }

    public static PowerShellConfigurationType getInstance() {
        return Application.get()
                .getExtensionPoint(ConfigurationType.class)
                .findExtensionOrFail(PowerShellConfigurationType.class);
    }
}
