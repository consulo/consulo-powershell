// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.BEGIN;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.BRACED_ID;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.BREAK;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CATCH;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CLASS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CONFIGURATION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CONTINUE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DASH;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DATA;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DEC_INTEGER;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DEFINE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DIV;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DO;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DOT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DQ_CLOSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DQ_OPEN;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DYNAMICPARAM;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ELSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ELSEIF;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.END;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ENUM;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EXIT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EXPANDABLE_HERE_STRING_END;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EXPANDABLE_HERE_STRING_PART;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EXPANDABLE_HERE_STRING_START;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EXPANDABLE_STRING_PART;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FILTER;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FINALLY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FOREACH;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FROM;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FUNCTION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.GENERIC_ID_PART;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.HAT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.HEX_INTEGER;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.HIDDEN;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.IF;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.IN;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.INLINESCRIPT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_AND;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_BAND;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_BOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_BXOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_C;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_OR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_XOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARALLEL;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARAM;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PERS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PLUS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PROCESS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.QMARK;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.REAL_NUM;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.REQUIRES_COMMENT_START;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.RETURN;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.RP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SIMPLE_ID;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.STAR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.STATIC;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SWITCH;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.THIS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.THROW;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.TRAP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.TRY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.UNTIL;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.USING;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.VAR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.VAR_ID;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.VERBATIM_HERE_STRING;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.VERBATIM_STRING;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.WHILE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.WORKFLOW;

import consulo.language.ast.TokenSet;

public final class PowerShellTokenTypeSets {

    public static final TokenSet KEYWORDS =
            TokenSet.create(
                    BEGIN,
                    BREAK,
                    CATCH,
                    CLASS,
                    CONTINUE,
                    DATA,
                    DEFINE,
                    DO,
                    DYNAMICPARAM,
                    ELSE,
                    ELSEIF,
                    END,
                    EXIT,
                    FILTER,
                    FINALLY,
                    FOR,
                    FOREACH,
                    FROM,
                    FUNCTION,
                    IF,
                    IN,
                    INLINESCRIPT,
                    PARALLEL,
                    PARAM,
                    PROCESS,
                    RETURN,
                    SWITCH,
                    THROW,
                    TRAP,
                    TRY,
                    UNTIL,
                    USING,
                    VAR,
                    WHILE,
                    WORKFLOW,
                    CONFIGURATION,
                    THIS,
                    HIDDEN,
                    STATIC,
                    ENUM);

    public static final TokenSet COMMENTS = TokenSet.create(COMMENT, REQUIRES_COMMENT_START);

    public static final TokenSet STRINGS =
            TokenSet.create(
                    EXPANDABLE_STRING_PART,
                    DQ_OPEN,
                    DQ_CLOSE,
                    VERBATIM_STRING,
                    EXPANDABLE_HERE_STRING_START,
                    EXPANDABLE_HERE_STRING_END,
                    EXPANDABLE_HERE_STRING_PART,
                    VERBATIM_HERE_STRING);

    public static final TokenSet NUMBERS = TokenSet.create(DEC_INTEGER, HEX_INTEGER, REAL_NUM);

    public static final TokenSet IDENTIFIERS =
            TokenSet.create(SIMPLE_ID, BRACED_ID, VAR_ID, GENERIC_ID_PART, DS, QMARK, HAT);

    private static final TokenSet IDENTIFIERS_ALLOWED_AS_KEYWORD =
            TokenSet.create(
                    PARAM,
                    UNTIL,
                    WORKFLOW,
                    END,
                    DEFINE,
                    FINALLY,
                    PARALLEL,
                    CONTINUE,
                    BEGIN,
                    DYNAMICPARAM,
                    IN,
                    PROCESS,
                    BREAK,
                    ELSE,
                    INLINESCRIPT,
                    CATCH,
                    THIS,
                    HIDDEN,
                    FOREACH,
                    STATIC);

    public static final TokenSet MEMBER_IDENTIFIERS =
            TokenSet.orSet(TokenSet.create(SIMPLE_ID), IDENTIFIERS_ALLOWED_AS_KEYWORD);

    public static final TokenSet BRACED_VARIABLE_IDENTIFIERS = TokenSet.create(BRACED_ID);

    public static final TokenSet SIMPLE_VARIABLE_IDENTIFIERS =
            TokenSet.create(SIMPLE_ID, VAR_ID, THIS, QMARK, HAT, DS);

    public static final TokenSet FUNCTION_IDENTIFIERS =
            TokenSet.orSet(
                    TokenSet.create(SIMPLE_ID, GENERIC_ID_PART, THIS, QMARK, HAT, DS, DOT),
                    IDENTIFIERS_ALLOWED_AS_KEYWORD);

    public static final TokenSet OPERATORS =
            TokenSet.create(
                    OP_C, OP_AND, OP_OR, OP_XOR, OP_BAND, OP_BOR, OP_BXOR, PLUS, DASH, STAR, DIV,
                    PERS);

    public static final TokenSet PROCESS_AS_WORD_TOKENS =
            TokenSet.create(QMARK, DS, HAT, LP, RP, DOT);
}
