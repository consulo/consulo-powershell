// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.refactoring;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellVariable;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.refactoring.RefactoringSupportProvider;
import consulo.language.psi.PsiElement;

@ExtensionImpl
public final class PowerShellElementRefactoringSupportProvider extends RefactoringSupportProvider {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public boolean isSafeDeleteAvailable(PsiElement element) {
        return element instanceof PowerShellComponent;
    }

    public boolean isInplaceRenameAvailable(PsiElement element, PsiElement context) {
        return false;
    }

    public boolean isMemberInplaceRenameAvailable(PsiElement element, PsiElement context) {
        return element instanceof PowerShellComponent
                && (!(element instanceof PowerShellVariable variable)
                        || !variable.isBracedVariable() && variable.getScopeName() == null);
    }

    public boolean isAvailable(PsiElement context) {
        return context instanceof PowerShellComponent;
    }
}
