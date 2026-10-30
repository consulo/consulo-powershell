// SPDX-FileCopyrightText: 2017-2020 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.actions;

import com.intellij.plugin.powershell.icon.PowerShellIconGroup;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.annotation.component.ActionImpl;
import consulo.annotation.component.ActionParentRef;
import consulo.annotation.component.ActionRef;
import consulo.application.dumb.DumbAware;
import consulo.ide.action.CreateFileFromTemplateAction;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.language.psi.PsiDirectory;
import consulo.localize.LocalizeValue;
import consulo.project.Project;

@ActionImpl(id = "NewPowerShellFile", parents = @ActionParentRef(@ActionRef(id = "NewGroup")))
public final class NewPowerShellFileAction extends CreateFileFromTemplateAction
        implements DumbAware {

    public NewPowerShellFileAction() {
        super(
                PowerShellLocalize.uiPowershellScript(),
                PowerShellLocalize.uiCreateAPowershellScript(),
                PowerShellIconGroup.file());
    }

    protected LocalizeValue getActionName(PsiDirectory directory, String name, String template) {
        return PowerShellLocalize.uiNewPowershellScript0(name);
    }

    protected void buildDialog(
            Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle(PowerShellLocalize.uiNewPowershellFile())
                .addKind(
                        PowerShellLocalize.uiPowershellFile(),
                        PowerShellIconGroup.file(),
                        "PowerShell File.ps1");
    }
}
