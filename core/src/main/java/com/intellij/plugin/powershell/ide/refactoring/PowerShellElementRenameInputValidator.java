// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.refactoring;

import com.intellij.plugin.powershell.psi.PowerShellComponent;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.refactoring.rename.RenameInputValidator;
import consulo.language.pattern.ElementPattern;
import consulo.language.pattern.PlatformPatterns;
import consulo.language.psi.PsiElement;
import consulo.language.util.ProcessingContext;

@ExtensionImpl
public final class PowerShellElementRenameInputValidator implements RenameInputValidator {

    public boolean isInputValid(String name, PsiElement element, ProcessingContext context) {
        return PowerShellNameUtils.isValidName(name, element);
    }

    public ElementPattern<? extends PsiElement> getPattern() {
        return PlatformPatterns.psiElement(PowerShellComponent.class);
    }
}
