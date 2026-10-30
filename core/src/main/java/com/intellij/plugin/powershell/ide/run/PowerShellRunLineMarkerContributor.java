// SPDX-FileCopyrightText: 2026 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.run;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;
import com.intellij.plugin.powershell.psi.impl.PowerShellFile;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.execution.icon.ExecutionIconGroup;
import consulo.execution.lineMarker.ExecutorAction;
import consulo.execution.lineMarker.RunLineMarkerContributor;
import consulo.execution.lineMarker.RunLineMarkerContributor.Info;
import consulo.language.Language;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellRunLineMarkerContributor extends RunLineMarkerContributor
        implements DumbAware {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public @Nullable Info getInfo(PsiElement element) {
        if (element.getTextOffset() != 0) {
            return null;
        }
        PsiFile file = element.getContainingFile();
        if (!(file instanceof PowerShellFile)
                || file.getVirtualFile() == null
                || !"ps1".equalsIgnoreCase(file.getVirtualFile().getExtension())
                || element != file.findElementAt(0)) {
            return null;
        }
        return new Info(
                ExecutionIconGroup.gutterRun(),
                ignored -> PowerShellLocalize.uiRunPowershellScript().get(),
                ExecutorAction.getActions(0));
    }
}
