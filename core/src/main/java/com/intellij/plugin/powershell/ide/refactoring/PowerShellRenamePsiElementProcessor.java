// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024-2026 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.refactoring;

import com.intellij.plugin.powershell.psi.PowerShellComponent;

import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.editor.refactoring.rename.RenameDialog;
import consulo.language.editor.refactoring.rename.RenamePsiElementProcessor;
import consulo.language.psi.PsiElement;
import consulo.project.Project;

@ExtensionImpl
public final class PowerShellRenamePsiElementProcessor extends RenamePsiElementProcessor {

    public boolean canProcessElement(PsiElement element) {
        return element instanceof PowerShellComponent;
    }

    public RenameDialog createRenameDialog(
            Project project, PsiElement element, PsiElement context, Editor editor) {
        return new RenameDialog(project, element, context, editor);
    }
}
