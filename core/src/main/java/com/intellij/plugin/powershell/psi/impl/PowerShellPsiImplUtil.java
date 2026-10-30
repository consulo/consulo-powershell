// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellAssignmentExpression;
import com.intellij.plugin.powershell.psi.PowerShellCastExpression;
import com.intellij.plugin.powershell.psi.PowerShellClassDeclarationStatement;
import com.intellij.plugin.powershell.psi.PowerShellEnumDeclarationStatement;
import com.intellij.plugin.powershell.psi.PowerShellPsiElement;
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression;
import com.intellij.plugin.powershell.psi.PowerShellTypeLiteralExpression;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;
import consulo.language.ast.TokenType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PowerShellPsiImplUtil {

    private PowerShellPsiImplUtil() {}

    public static List<PowerShellTargetVariableExpression> getTargetVariables(
            PowerShellAssignmentExpression expression) {
        return new ArrayList<>(
                PsiTreeUtil.findChildrenOfType(
                        expression, PowerShellTargetVariableExpression.class));
    }

    public static @Nullable PowerShellPsiElement getRhsElement(
            PowerShellAssignmentExpression expression) {
        ASTNode child = expression.getNode().getFirstChildNode();
        boolean after = false;
        for (; child != null; child = child.getTreeNext()) {
            if (child.getElementType() == PowerShellTypes.EQ) {
                after = true;
            }
            else if (after && child.getPsi() instanceof PowerShellPsiElement element) {
                return element;
            }
        }
        return null;
    }

    public static PowerShellType getCastType(PowerShellCastExpression expression) {
        PowerShellTypeLiteralExpression type =
                PsiTreeUtil.getChildOfType(expression, PowerShellTypeLiteralExpression.class);
        return type == null ? PowerShellType.UNKNOWN : type.getTypeElement().getType();
    }

    public static boolean isTypeDeclarationContext(PsiElement element) {
        return element.getContext() instanceof PowerShellClassDeclarationStatement
                || element.getContext() instanceof PowerShellEnumDeclarationStatement;
    }

    public static boolean isWhiteSpaceOrNls(ASTNode node) {
        return node.getElementType() == TokenType.WHITE_SPACE
                || node.getElementType() == PowerShellTypes.NLS
                || node.getElementType() == PowerShellTypes.LF;
    }
}
