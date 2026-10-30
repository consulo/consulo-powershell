// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2021-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.ATTRIBUTE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.BLOCK_BODY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CAST_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CATCH_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COLON;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COLON2;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMA;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMAND_ARGUMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMAND_CALL_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ELSEIF_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ELSE_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FINALLY_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FOREACH;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.IDENTIFIER;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.IF;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LCURLY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.NLS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARAMETER_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARENTHESIZED_ARGUMENT_LIST;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.RCURLY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.RP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SEMI;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SIMPLE_ID;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SQBR_L;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SQBR_R;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.TYPE_LITERAL_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.WHILE;

import static consulo.language.codeStyle.CommonCodeStyleSettings.DO_NOT_WRAP;
import static consulo.language.codeStyle.CommonCodeStyleSettings.END_OF_LINE;
import static consulo.language.codeStyle.CommonCodeStyleSettings.NEXT_LINE;
import static consulo.language.codeStyle.CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED;
import static consulo.language.codeStyle.CommonCodeStyleSettings.NEXT_LINE_SHIFTED;
import static consulo.language.codeStyle.CommonCodeStyleSettings.NEXT_LINE_SHIFTED2;

import com.intellij.plugin.powershell.psi.PowerShellAttribute;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import consulo.language.codeStyle.Spacing;
import consulo.language.psi.PsiElement;

import org.jspecify.annotations.Nullable;

final class PowerShellSpacingProcessor {

    private final CommonCodeStyleSettings myCommon;

    private final PowerShellCodeStyleSettings myCustom;

    PowerShellSpacingProcessor(CommonCodeStyleSettings common, PowerShellCodeStyleSettings custom) {
        myCommon = common;
        myCustom = custom;
    }

    @Nullable Spacing getSpacing(@Nullable PowerShellCodeBlock first, PowerShellCodeBlock second) {
        ASTNode b = second.getNode();
        if (b == null) {
            return null;
        }
        ASTNode a = PSFormattingUtils.sibling(b, false);
        while (a == null && b.getTreeParent() != null) {
            b = b.getTreeParent();
            a = PSFormattingUtils.sibling(b, false);
        }
        if (a == null) {
            return null;
        }
        IElementType x = PSFormattingUtils.type(a);
        IElementType y = PSFormattingUtils.type(b);
        if (a.getTreeParent() == b.getTreeParent()) {
            IElementType parent = PSFormattingUtils.parentType(a);
            if (parent == COMMAND_ARGUMENT
                    || parent == COMMAND_CALL_EXPRESSION
                            && a.getTextRange().getEndOffset()
                                    == b.getTextRange().getStartOffset()) {
                return null;
            }
        }
        if (x == LCURLY || y == RCURLY) {
            ASTNode brace = x == LCURLY ? a : b;
            ASTNode body = PSFormattingUtils.sibling(brace, brace == a);
            IElementType bodyType = PSFormattingUtils.type(body);
            if (bodyType == IDENTIFIER || bodyType == SIMPLE_ID) {
                return null;
            }
            boolean simple =
                    bodyType == BLOCK_BODY && !PSFormattingUtils.containsNewLines(body)
                            || bodyType == LCURLY
                            || bodyType == RCURLY;
            boolean keep =
                    simple
                            && (PSFormattingUtils.isCallableDeclarationContext(brace)
                                    ? myCommon.KEEP_SIMPLE_METHODS_IN_ONE_LINE
                                    : PSFormattingUtils.isClassDeclarationContext(brace)
                                                    || PSFormattingUtils.isEnumDeclarationContext(
                                                            brace)
                                            ? myCommon.KEEP_SIMPLE_CLASSES_IN_ONE_LINE
                                            : PSFormattingUtils.isScriptBlockExpressionContext(
                                                            brace)
                                                    ? myCommon.KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE
                                                    : PSFormattingUtils.isHashLiteralContext(brace)
                                                            ? myCustom.KEEP_SIMPLE_HASH_LITERAL_IN_ONE_LINE
                                                            : myCommon.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE);
            return spacing(myCommon.SPACE_WITHIN_BRACES, !keep);
        }
        if (x == SQBR_L || y == SQBR_R) {
            return simple(
                    PSFormattingUtils.isCastExpressionSuperContext(a)
                                    || PSFormattingUtils.isCastExpressionSuperContext(b)
                            ? myCommon.SPACE_WITHIN_CAST_PARENTHESES
                            : myCommon.SPACE_WITHIN_BRACKETS);
        }
        if (x == TYPE_LITERAL_EXPRESSION && PSFormattingUtils.parentType(a) == CAST_EXPRESSION) {
            return simple(myCommon.SPACE_AFTER_TYPE_CAST);
        }
        if (x == LP || y == RP) {
            ASTNode paren = x == LP ? a : b;
            boolean opening = x == LP;
            boolean empty =
                    PSFormattingUtils.type(PSFormattingUtils.sibling(paren, opening))
                            == (opening ? RP : LP);
            if (PSFormattingUtils.isArgumentListContext(paren)) {
                boolean newline =
                        myCommon.CALL_PARAMETERS_WRAP != DO_NOT_WRAP
                                && (opening
                                        ? myCommon.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE
                                                || empty
                                                        && myCommon.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE
                                        : myCommon.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE);
                return spacing(
                        empty
                                ? myCommon.SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES
                                : myCommon.SPACE_WITHIN_METHOD_CALL_PARENTHESES,
                        newline);
            }
            if (PSFormattingUtils.isParameterClauseContext(paren)
                    || PSFormattingUtils.isBlockParameterClauseContext(paren)) {
                boolean method = PSFormattingUtils.isParameterClauseContext(paren);
                int wrap =
                        method
                                ? myCommon.METHOD_PARAMETERS_WRAP
                                : myCustom.BLOCK_PARAMETER_CLAUSE_WRAP;
                boolean left =
                        method
                                ? myCommon.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE
                                : myCustom.BLOCK_PARAMETERS_LPAREN_ON_NEXT_LINE;
                boolean right =
                        method
                                ? myCommon.METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE
                                : myCustom.BLOCK_PARAMETERS_RPAREN_ON_NEXT_LINE;
                boolean newline = wrap != DO_NOT_WRAP && (opening ? left || empty && right : right);
                return spacing(
                        empty
                                ? myCommon.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES
                                : myCommon.SPACE_WITHIN_METHOD_PARENTHESES,
                        newline);
            }
            if (PSFormattingUtils.isForClauseContext(paren)) {
                return spacing(
                        myCommon.SPACE_WITHIN_FOR_PARENTHESES,
                        opening
                                ? myCommon.FOR_STATEMENT_LPAREN_ON_NEXT_LINE
                                : myCommon.FOR_STATEMENT_RPAREN_ON_NEXT_LINE);
            }
            if (PSFormattingUtils.isIfStatementContext(paren)) {
                return simple(myCommon.SPACE_WITHIN_IF_PARENTHESES);
            }
            if (PSFormattingUtils.isForEachStatementContext(paren)) {
                return simple(myCommon.SPACE_WITHIN_FOR_PARENTHESES);
            }
            if (PSFormattingUtils.isWhileStatementContext(paren)) {
                return simple(myCommon.SPACE_WITHIN_WHILE_PARENTHESES);
            }
            if (PSFormattingUtils.isSwitchStatementContext(paren)) {
                return simple(myCommon.SPACE_WITHIN_SWITCH_PARENTHESES);
            }
            if (PSFormattingUtils.isParenthesizedExpressionContext(paren)) {
                return simple(myCommon.SPACE_WITHIN_PARENTHESES);
            }
            if (PSFormattingUtils.isSubExpressionContext(paren)) {
                return simple(myCustom.SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES);
            }
            if (PSFormattingUtils.parentType(paren) == ATTRIBUTE) {
                return simple(myCommon.SPACE_WITHIN_ANNOTATION_PARENTHESES);
            }
        }
        if (y == LP) {
            if (x == IF) {
                return simple(myCommon.SPACE_BEFORE_IF_PARENTHESES);
            }
            if (x == FOR || x == FOREACH) {
                return simple(myCommon.SPACE_BEFORE_FOR_PARENTHESES);
            }
            if (x == WHILE) {
                return simple(myCommon.SPACE_BEFORE_WHILE_PARENTHESES);
            }
            if (PSFormattingUtils.isSwitchStatementContext(a)) {
                return simple(myCommon.SPACE_BEFORE_SWITCH_PARENTHESES);
            }
            if (a.getPsi() instanceof PsiElement psi
                    && psi.getContext() instanceof PowerShellAttribute) {
                return simple(myCommon.SPACE_BEFORE_ANOTATION_PARAMETER_LIST);
            }
        }
        if (y == PARAMETER_CLAUSE) {
            return simple(myCommon.SPACE_BEFORE_METHOD_PARENTHESES);
        }
        if (y == PARENTHESIZED_ARGUMENT_LIST) {
            return simple(myCommon.SPACE_BEFORE_METHOD_CALL_PARENTHESES);
        }
        if (PSFormattingUtils.isAssignmentOperator(x)
                || PSFormattingUtils.isAssignmentOperator(y)) {
            return simple(myCommon.SPACE_AROUND_ASSIGNMENT_OPERATORS);
        }
        if (PSFormattingUtils.isComparisonOperator(x)
                || PSFormattingUtils.isComparisonOperator(y)) {
            return simple(myCommon.SPACE_AROUND_RELATIONAL_OPERATORS);
        }
        if (PSFormattingUtils.isAdditiveOperator(a) || PSFormattingUtils.isAdditiveOperator(b)) {
            return simple(myCommon.SPACE_AROUND_ADDITIVE_OPERATORS);
        }
        if (PSFormattingUtils.isLogicalOperator(x) || PSFormattingUtils.isLogicalOperator(y)) {
            return simple(myCommon.SPACE_AROUND_LOGICAL_OPERATORS);
        }
        if (PSFormattingUtils.isBitwiseOperator(x) || PSFormattingUtils.isBitwiseOperator(y)) {
            return simple(myCommon.SPACE_AROUND_BITWISE_OPERATORS);
        }
        if ((PSFormattingUtils.isMultiplicativeOperator(x)
                        || PSFormattingUtils.isMultiplicativeOperator(y))
                && !PSFormattingUtils.isIdentifier(a.getTreeParent())
                && !PSFormattingUtils.isIdentifier(b.getTreeParent())) {
            return simple(myCommon.SPACE_AROUND_MULTIPLICATIVE_OPERATORS);
        }
        if (PSFormattingUtils.isWhitespaceRequiringUnaryOperator(a)) {
            return spacing(true, false);
        }
        if (PSFormattingUtils.isWhitespaceFlexibleUnaryOperator(a)
                || PSFormattingUtils.isWhitespaceFlexibleUnaryOperator(b)) {
            return simple(myCommon.SPACE_AROUND_UNARY_OPERATOR);
        }
        if (x == COLON2 || y == COLON2) {
            return simple(myCommon.SPACE_AROUND_METHOD_REF_DBL_COLON);
        }
        if (y == LCURLY) {
            int style = myCommon.BRACE_STYLE;
            boolean space = true;
            if (PSFormattingUtils.isClassDeclarationContext(b)
                    || PSFormattingUtils.isEnumDeclarationContext(b)) {
                style = myCommon.CLASS_BRACE_STYLE;
                space = myCommon.SPACE_BEFORE_CLASS_LBRACE;
            }
            else if (PSFormattingUtils.isCallableDeclarationContext(b)) {
                style = myCommon.METHOD_BRACE_STYLE;
                space = myCommon.SPACE_BEFORE_METHOD_LBRACE;
            }
            else if (PSFormattingUtils.isIfStatementContext(b)) {
                space = myCommon.SPACE_BEFORE_IF_LBRACE;
            }
            else if (PSFormattingUtils.isForClauseContext(b)
                    || PSFormattingUtils.isForEachStatementContext(b)) {
                space = myCommon.SPACE_BEFORE_FOR_LBRACE;
            }
            else if (PSFormattingUtils.isWhileStatementContext(b)) {
                space = myCommon.SPACE_BEFORE_WHILE_LBRACE;
            }
            else if (PSFormattingUtils.isDoStatementContext(b)) {
                space = myCommon.SPACE_BEFORE_DO_LBRACE;
            }
            else if (PSFormattingUtils.isSwitchStatementContext(b)) {
                space = myCommon.SPACE_BEFORE_SWITCH_LBRACE;
            }
            else if (PSFormattingUtils.isTryStatementBlockContext(b)) {
                space = myCommon.SPACE_BEFORE_TRY_LBRACE;
            }
            else if (PSFormattingUtils.isElseClauseContext(b)) {
                space = myCommon.SPACE_BEFORE_ELSE_LBRACE;
            }
            else if (PSFormattingUtils.isCatchClauseContext(b)) {
                space = myCommon.SPACE_BEFORE_CATCH_LBRACE;
            }
            else if (PSFormattingUtils.isFinallyClauseContext(b)) {
                space = myCommon.SPACE_BEFORE_FINALLY_LBRACE;
            }
            else if (PSFormattingUtils.isDataStatementContext(b)) {
                space = myCustom.SPACE_BEFORE_DATA_LBRACE;
            }
            else if (PSFormattingUtils.isTrapStatementContext(b)) {
                space = myCustom.SPACE_BEFORE_TRAP_LBRACE;
            }
            else if (PSFormattingUtils.isScriptBlockExpressionContext(b)) {
                return braceSpacing(0, Integer.MAX_VALUE, style, null);
            }
            PsiElement declaration = b.getPsi() == null ? null : b.getPsi().getContext();
            int start =
                    declaration == null
                            ? a.getTextRange().getStartOffset()
                            : declaration.getTextRange().getStartOffset();
            if (declaration instanceof PowerShellComponent component
                    && component.getNameIdentifier() != null) {
                start = component.getNameIdentifier().getTextRange().getStartOffset();
            }
            return braceSpacing(
                    space ? 1 : 0,
                    space ? 1 : 0,
                    style,
                    new TextRange(start, b.getTextRange().getEndOffset()));
        }
        if (y == ELSE_CLAUSE) {
            return spacing(myCommon.SPACE_BEFORE_ELSE_KEYWORD, myCommon.ELSE_ON_NEW_LINE);
        }
        if (y == ELSEIF_CLAUSE) {
            return spacing(myCommon.SPACE_BEFORE_ELSE_KEYWORD, myCommon.SPECIAL_ELSE_IF_TREATMENT);
        }
        if (y == CATCH_CLAUSE) {
            return spacing(myCommon.SPACE_BEFORE_CATCH_KEYWORD, myCommon.CATCH_ON_NEW_LINE);
        }
        if (y == FINALLY_CLAUSE) {
            return spacing(myCommon.SPACE_BEFORE_FINALLY_KEYWORD, myCommon.FINALLY_ON_NEW_LINE);
        }
        if (y == WHILE && PSFormattingUtils.isDoStatementContext(b)) {
            return spacing(myCommon.SPACE_BEFORE_WHILE_KEYWORD, myCommon.WHILE_ON_NEW_LINE);
        }
        if (x == COMMA) {
            return simple(myCommon.SPACE_AFTER_COMMA);
        }
        if (y == COMMA) {
            return simple(myCommon.SPACE_BEFORE_COMMA);
        }
        if (x == DS) {
            return Spacing.createSpacing(0, 0, 0, true, 0);
        }
        if (x == COLON) {
            return PSFormattingUtils.canAddSpaceBefore(b)
                    ? simple(myCustom.SPACE_AFTER_COLON)
                    : null;
        }
        if (y == COLON) {
            return PSFormattingUtils.canAddSpaceBefore(b)
                    ? simple(myCustom.SPACE_BEFORE_COLON)
                    : null;
        }
        if (x == SEMI) {
            return simple(myCommon.SPACE_AFTER_SEMICOLON);
        }
        if (y == SEMI) {
            return simple(myCommon.SPACE_BEFORE_SEMICOLON);
        }
        if (PowerShellTokenTypeSets.KEYWORDS.contains(x)
                && !PSFormattingUtils.isIdentifier(a.getTreeParent())
                && y != NLS) {
            return Spacing.createSpacing(1, 1, 0, true, 0);
        }
        if (PSFormattingUtils.isRedirectionOperator(x)) {
            return simple(myCommon.SPACE_AROUND_RELATIONAL_OPERATORS);
        }
        return null;
    }

    private Spacing spacing(boolean space, boolean newline) {
        int n = space ? 1 : 0;
        return Spacing.createSpacing(
                n,
                n,
                newline ? 1 : 0,
                myCommon.KEEP_LINE_BREAKS,
                myCommon.KEEP_BLANK_LINES_IN_CODE);
    }

    private Spacing simple(boolean space) {
        return spacing(space, false);
    }

    private Spacing braceSpacing(int min, int max, int style, @Nullable TextRange dependency) {
        return switch (style) {
            case END_OF_LINE -> Spacing.createSpacing(min, max, 0, false, 0);
            case NEXT_LINE, NEXT_LINE_SHIFTED, NEXT_LINE_SHIFTED2 ->
                    Spacing.createSpacing(min, max, 1, false, 0);
            case NEXT_LINE_IF_WRAPPED ->
                    dependency == null
                            ? Spacing.createSpacing(
                                    min,
                                    max,
                                    0,
                                    myCommon.KEEP_LINE_BREAKS,
                                    myCommon.KEEP_BLANK_LINES_IN_CODE)
                            : Spacing.createDependentLFSpacing(
                                    min, max, dependency, false, myCommon.KEEP_BLANK_LINES_IN_CODE);
            default ->
                    Spacing.createSpacing(
                            min,
                            max,
                            0,
                            myCommon.KEEP_LINE_BREAKS,
                            myCommon.KEEP_BLANK_LINES_IN_CODE);
        };
    }
}
