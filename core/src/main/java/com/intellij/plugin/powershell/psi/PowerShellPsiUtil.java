// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.ast.TokenType;

import org.jspecify.annotations.Nullable;

public final class PowerShellPsiUtil {

    private PowerShellPsiUtil() {}

    public static boolean isLabelContext(ASTNode node) {
        return parentType(node) == PowerShellTypes.LABEL;
    }

    public static boolean isTargetVariableContext(ASTNode node) {
        return parentType(node) == PowerShellTypes.TARGET_VARIABLE_EXPRESSION;
    }

    public static boolean isFunctionDeclarationContext(ASTNode node) {
        return parentType(node) == PowerShellTypes.FUNCTION_STATEMENT;
    }

    public static boolean isPathExpressionContext(ASTNode node) {
        return parentType(node) == PowerShellTypes.PATH_EXPRESSION;
    }

    private static @Nullable IElementType parentType(ASTNode node) {
        return node.getTreeParent() == null ? null : node.getTreeParent().getElementType();
    }

    public static @Nullable ASTNode findSiblingSkipping(
            ASTNode node, IElementType[] skip, boolean forward) {
        ASTNode result = forward ? node.getTreeNext() : node.getTreePrev();
        TokenSet types = TokenSet.create(skip);
        while (result != null && types.contains(result.getElementType())) {
            result = forward ? result.getTreeNext() : result.getTreePrev();
        }
        return result;
    }

    public static @Nullable ASTNode findSiblingSkippingWS(ASTNode node, boolean forward) {
        return findSiblingSkipping(
                node, new IElementType[] {PowerShellTypes.NLS, TokenType.WHITE_SPACE}, forward);
    }
}
