// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.highlighting;

import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;

import consulo.codeEditor.DefaultLanguageHighlighterColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.ast.IElementType;
import consulo.language.editor.highlight.SyntaxHighlighterBase;
import consulo.language.lexer.Lexer;

import java.util.HashMap;
import java.util.Map;

public final class PowerShellSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey COMMENT =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_STRING", DefaultLanguageHighlighterColors.STRING);

    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

    public static final TextAttributesKey COMMAND_NAME =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_COMMAND_NAME", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);

    public static final TextAttributesKey COMMAND_PARAMETER =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_COMMAND_PARAMETER",
                    DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);

    public static final TextAttributesKey TYPE_NAME =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);

    public static final TextAttributesKey TYPE_REFERENCE =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_TYPE_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);

    public static final TextAttributesKey VARIABLE_NAME =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

    public static final TextAttributesKey PROPERTY_REFERENCE =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_PROPERTY_REF_NAME", DefaultLanguageHighlighterColors.IDENTIFIER);

    public static final TextAttributesKey METHOD_CALL =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_METHOD_CALL_NAME", DefaultLanguageHighlighterColors.FUNCTION_CALL);

    public static final TextAttributesKey METHOD_DECLARATION =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_METHOD_DECLARATION_NAME",
                    DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);

    public static final TextAttributesKey LABEL =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_LABEL_NAME", DefaultLanguageHighlighterColors.LABEL);

    public static final TextAttributesKey COMMAND_NAME_ATTR =
            TextAttributesKey.createTextAttributesKey(
                    "POWER_SHELL_COMMAND_NAME", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);

    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    static {
        fillMap(ATTRIBUTES, PowerShellTokenTypeSets.KEYWORDS, KEYWORD);
        fillMap(ATTRIBUTES, PowerShellTokenTypeSets.COMMENTS, COMMENT);
        fillMap(ATTRIBUTES, PowerShellTokenTypeSets.STRINGS, STRING);
        fillMap(ATTRIBUTES, PowerShellTokenTypeSets.NUMBERS, NUMBER);
    }

    public TextAttributesKey[] getTokenHighlights(IElementType type) {
        return pack(ATTRIBUTES.get(type));
    }

    public Lexer getHighlightingLexer() {
        return new PowerShellLexerAdapter();
    }
}
