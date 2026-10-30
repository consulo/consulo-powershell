// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.refactoring;

import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellFunctionStatement;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellVariable;

import consulo.language.psi.PsiElement;

public final class PowerShellRefactoringUtils {

    private PowerShellRefactoringUtils() {}

    public static boolean isBracedVariable(PsiElement element) {
        return element instanceof PowerShellTargetVariableExpression variable
                && variable.isBracedVariable();
    }

    public static boolean isSimpleVariable(PsiElement element) {
        return element instanceof PowerShellTargetVariableExpression variable
                && !variable.isBracedVariable();
    }

    public static boolean isFunctionStatement(PsiElement element) {
        return element instanceof PowerShellFunctionStatement;
    }

    public static boolean isMember(PsiElement element) {
        return element instanceof PowerShellTypeDeclaration
                || element instanceof PowerShellMemberDeclaration;
    }

    public static boolean isComponent(PsiElement element) {
        return element instanceof PowerShellComponent;
    }

    public static boolean isVariableWithNamespace(PsiElement element) {
        return element instanceof PowerShellVariable variable
                && variable.getScopeName() != null
                && !variable.getScopeName().isEmpty();
    }
}
