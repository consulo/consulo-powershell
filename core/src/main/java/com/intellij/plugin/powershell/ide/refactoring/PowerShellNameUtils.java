// SPDX-FileCopyrightText: 2017-2021 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.refactoring;

import com.intellij.plugin.powershell.lang._PowerShellLexer;
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter;
import com.intellij.plugin.powershell.psi.PowerShellFunctionStatement;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;
import com.intellij.plugin.powershell.psi.PowerShellVariable;

import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;

import org.jspecify.annotations.Nullable;

public final class PowerShellNameUtils {

    private PowerShellNameUtils() {}

    public static @Nullable IElementType tokenType(String name, int state) {
        PowerShellLexerAdapter lexer = new PowerShellLexerAdapter();
        lexer.start(name, 0, name.length(), state);
        IElementType type = lexer.getTokenType();
        lexer.advance();
        return lexer.getTokenType() == null ? type : null;
    }

    public static boolean isValidName(String name, PsiElement element) {
        if (name.isEmpty()) {
            return false;
        }
        if (element instanceof PowerShellVariable variable) {
            return variable.isBracedVariable()
                    ? PowerShellTokenTypeSets.BRACED_VARIABLE_IDENTIFIERS.contains(
                            tokenType(name, _PowerShellLexer.VAR_BRACED))
                    : PowerShellTokenTypeSets.SIMPLE_VARIABLE_IDENTIFIERS.contains(
                            tokenType(name, _PowerShellLexer.VAR_SIMPLE));
        }
        if (element instanceof PowerShellFunctionStatement) {
            return PowerShellTokenTypeSets.FUNCTION_IDENTIFIERS.contains(
                            tokenType(name, _PowerShellLexer.FUNCTION_ID))
                    || PowerShellPsiElementFactory.createIdentifierFromText(
                                    element.getProject(), name, true)
                            != null;
        }
        return (element instanceof PowerShellMemberDeclaration
                        ? PowerShellTokenTypeSets.MEMBER_IDENTIFIERS
                        : PowerShellTokenTypeSets.IDENTIFIERS)
                .contains(tokenType(name, 0));
    }
}
