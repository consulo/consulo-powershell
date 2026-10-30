// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2021-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.ADDITIVE_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ARRAY_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ATTRIBUTE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ATTRIBUTE_ARGUMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.BITWISE_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.BLOCK_BODY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.BLOCK_PARAMETER_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CAST_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CATCH_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CLASS_DECLARATION_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COLON2;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMA;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMAND_CALL_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMPARISON_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.CONSTRUCTOR_DECLARATION_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DASH;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DATA_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DIV;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DOT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DOUBLE_QUESTION_MARK;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DO_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ELSEIF_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ELSE_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.ENUM_DECLARATION_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EQ;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EQ_DASH;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EQ_DIV;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EQ_PERS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EQ_PLUS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EQ_STAR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EXCL_MARK;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FINALLY_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FOREACH_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FOR_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.FUNCTION_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.HASH_LITERAL_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.IDENTIFIER;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.IF_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.INVOCATION_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LABEL;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LOGICAL_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.METHOD_DECLARATION_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.MM;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.MULTIPLICATIVE_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_AND;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_BAND;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_BNOT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_BOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_BXOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_C;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_FR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_MR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_NOT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_OR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.OP_XOR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARAMETER_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARENTHESIZED_ARGUMENT_LIST;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARENTHESIZED_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PATH_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PERS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PIPE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PIPELINE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PLUS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PROPERTY_DECLARATION_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.REFERENCE_IDENTIFIER;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SCRIPT_BLOCK_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SEMI;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.STAR;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SUB_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.SWITCH_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.TARGET_VARIABLE_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.TRAP_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.TRY_STATEMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.TYPE_LITERAL_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.WHILE_STATEMENT;

import com.intellij.plugin.powershell.psi.PowerShellExpression;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;
import com.intellij.plugin.powershell.psi.PowerShellUnaryExpression;
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiElement;

import org.jspecify.annotations.Nullable;

final class PSFormattingUtils {

    private PSFormattingUtils() {}

    static @Nullable IElementType parentType(ASTNode node) {
        return node.getTreeParent() == null ? null : node.getTreeParent().getElementType();
    }

    static @Nullable IElementType type(@Nullable ASTNode node) {
        return node == null ? null : node.getElementType();
    }

    static @Nullable ASTNode sibling(ASTNode node, boolean forward) {
        ASTNode next = forward ? node.getTreeNext() : node.getTreePrev();
        while (next != null && isWhiteSpaceOrNls(next)) {
            next = forward ? next.getTreeNext() : next.getTreePrev();
        }
        return next;
    }

    static boolean isWhiteSpaceOrNls(ASTNode node) {
        return node.getTextLength() == 0 || PowerShellPsiImplUtil.isWhiteSpaceOrNls(node);
    }

    static boolean canAddSpaceBefore(ASTNode node) {
        IElementType type = parentType(node);
        return type != LABEL
                && type != TARGET_VARIABLE_EXPRESSION
                && type != PATH_EXPRESSION
                && type != FUNCTION_STATEMENT;
    }

    static boolean isIdentifier(@Nullable ASTNode node) {
        return node != null && (type(node) == REFERENCE_IDENTIFIER || type(node) == IDENTIFIER);
    }

    static boolean containsNewLines(@Nullable ASTNode node) {
        return node != null && (node.textContains('\n') || node.textContains('\r'));
    }

    static boolean isRedirectionOperator(@Nullable IElementType type) {
        return type == OP_FR || type == OP_MR;
    }

    static boolean isCastExpressionSuperContext(ASTNode node) {
        return node.getTreeParent() != null && parentType(node.getTreeParent()) == CAST_EXPRESSION;
    }

    static boolean isArgumentListContext(ASTNode node) {
        return parentType(node) == PARENTHESIZED_ARGUMENT_LIST;
    }

    static boolean isElseClauseContext(ASTNode node) {
        return parentType(node) == ELSE_CLAUSE || parentType(node) == ELSEIF_CLAUSE;
    }

    static boolean isExpressionInPipelineTail(ASTNode node) {
        return type(node) == COMMAND_CALL_EXPRESSION && type(sibling(node, false)) == PIPE;
    }

    static boolean isCallableDeclaration(ASTNode node) {
        return type(node) == FUNCTION_STATEMENT
                || type(node) == METHOD_DECLARATION_STATEMENT
                || type(node) == CONSTRUCTOR_DECLARATION_STATEMENT;
    }

    static boolean isCallableDeclarationContext(ASTNode node) {
        return node.getTreeParent() != null && isCallableDeclaration(node.getTreeParent());
    }

    static boolean isWhitespaceRequiringUnaryOperator(ASTNode node) {
        return node.getPsi() instanceof PsiElement psi
                && psi.getContext() instanceof PowerShellUnaryExpression
                && (type(node) == OP_NOT || type(node) == OP_BNOT);
    }

    static boolean isWhitespaceFlexibleUnaryOperator(ASTNode node) {
        return node.getPsi() instanceof PsiElement psi
                && psi.getContext() instanceof PowerShellUnaryExpression
                && TokenSet.create(EXCL_MARK, MM, PP, COMMA, PLUS, DASH).contains(type(node));
    }

    static boolean isComparisonOperator(@Nullable IElementType type) {
        return type == OP_C;
    }

    static boolean isMultiplicativeOperator(@Nullable IElementType type) {
        return type == STAR || type == DIV || type == PERS;
    }

    static boolean isBitwiseOperator(@Nullable IElementType type) {
        return type == OP_BAND || type == OP_BOR || type == OP_BXOR;
    }

    static boolean isLogicalOperator(@Nullable IElementType type) {
        return type == OP_AND || type == OP_OR || type == OP_XOR || type == DOUBLE_QUESTION_MARK;
    }

    static boolean isAssignmentOperator(@Nullable IElementType type) {
        return TokenSet.create(EQ, EQ_DASH, EQ_PLUS, EQ_STAR, EQ_DIV, EQ_PERS).contains(type);
    }

    static boolean isAdditiveOperator(ASTNode node) {
        return (type(node) == PLUS || type(node) == DASH)
                && parentType(node) == ADDITIVE_EXPRESSION;
    }

    static boolean isForParameter(ASTNode node) {
        return isForClauseContext(node) && node.getPsi() instanceof PowerShellExpression;
    }

    static boolean isForClauseContext(ASTNode node) {
        return parentType(node) == FOR_CLAUSE;
    }

    static boolean isBinaryExpressionContext(ASTNode node) {
        return node.getTreeParent() != null && isBinaryExpression(node.getTreeParent());
    }

    static boolean isBinaryExpression(ASTNode node) {
        return TokenSet.create(
                        LOGICAL_EXPRESSION,
                        BITWISE_EXPRESSION,
                        COMPARISON_EXPRESSION,
                        ADDITIVE_EXPRESSION,
                        MULTIPLICATIVE_EXPRESSION)
                .contains(type(node));
    }

    static boolean isFollowedByAttribute(ASTNode node) {
        return type(sibling(node, false)) == ATTRIBUTE;
    }

    static boolean isRhsBinaryExpressionContext(ASTNode node) {
        return isBinaryExpressionContext(node)
                && node.getPsi() instanceof PowerShellExpression
                && PowerShellTokenTypeSets.OPERATORS.contains(type(sibling(node, false)));
    }

    static boolean isFirstNodeInForParameter(ASTNode node) {
        return isForClauseContext(node)
                && (type(sibling(node, false)) == SEMI || type(sibling(node, false)) == LP);
    }

    static boolean isFunctionParameter(ASTNode node) {
        return isParameterClauseContext(node) && canBeParameter(node);
    }

    static boolean isBlockParameter(ASTNode node) {
        return isBlockParameterClauseContext(node) && canBeParameter(node);
    }

    static boolean isCallArgument(ASTNode node) {
        return node.getPsi() instanceof PowerShellExpression && isArgumentListContext(node);
    }

    static boolean isArrayElement(ASTNode node) {
        return node.getPsi() instanceof PowerShellExpression
                && parentType(node) == ARRAY_EXPRESSION;
    }

    static boolean isInvocationExpressionQualifier(ASTNode node) {
        IElementType previous = type(sibling(node, false));
        return parentType(node) == INVOCATION_EXPRESSION
                && (previous == DOT || previous == COLON2)
                && (type(node) == REFERENCE_IDENTIFIER
                        || node.getPsi() instanceof PowerShellExpression);
    }

    static boolean isFirstNodeInParameter(ASTNode node, boolean checkFirst) {
        IElementType previous = type(sibling(node, false));
        return previous == COMMA || checkFirst && previous == LP;
    }

    static boolean canBeParameter(ASTNode node) {
        if (type(node) == ATTRIBUTE) {
            if (!isBlockParameterClauseContext(node)) {
                return true;
            }
            for (ASTNode previous = sibling(node, false);
                    previous != null;
                    previous = sibling(previous, false)) {
                if (type(previous) != ATTRIBUTE) {
                    return true;
                }
            }
            return false;
        }
        return type(node) == TARGET_VARIABLE_EXPRESSION
                || type(node) == EQ
                || node.getPsi() instanceof PowerShellExpression;
    }

    static boolean isFinallyClauseContext(ASTNode node) {
        return parentType(node) == FINALLY_CLAUSE;
    }

    static boolean isDataStatementContext(ASTNode node) {
        return parentType(node) == DATA_STATEMENT;
    }

    static boolean isTrapStatementContext(ASTNode node) {
        return parentType(node) == TRAP_STATEMENT;
    }

    static boolean isCatchClauseContext(ASTNode node) {
        return parentType(node) == CATCH_CLAUSE;
    }

    static boolean isTryStatementBlockContext(ASTNode node) {
        return parentType(node) == TRY_STATEMENT;
    }

    static boolean isSwitchStatementContext(ASTNode node) {
        return parentType(node) == SWITCH_STATEMENT;
    }

    static boolean isSubExpressionContext(ASTNode node) {
        return parentType(node) == SUB_EXPRESSION;
    }

    static boolean isDoStatementContext(ASTNode node) {
        return parentType(node) == DO_STATEMENT;
    }

    static boolean isWhileStatementContext(ASTNode node) {
        return parentType(node) == WHILE_STATEMENT;
    }

    static boolean isForEachStatementContext(ASTNode node) {
        return parentType(node) == FOREACH_STATEMENT;
    }

    static boolean isIfStatementContext(ASTNode node) {
        return parentType(node) == IF_STATEMENT;
    }

    static boolean isParameterClauseContext(ASTNode node) {
        return parentType(node) == PARAMETER_CLAUSE;
    }

    static boolean isBlockParameterClauseContext(ASTNode node) {
        return parentType(node) == BLOCK_PARAMETER_CLAUSE;
    }

    static boolean isBlockBodyContext(ASTNode node) {
        return parentType(node) == BLOCK_BODY;
    }

    static boolean isPipelineContext(ASTNode node) {
        return parentType(node) == PIPELINE;
    }

    static boolean isPropertyDeclarationContext(ASTNode node) {
        return parentType(node) == PROPERTY_DECLARATION_STATEMENT;
    }

    static boolean isEnumDeclarationContext(ASTNode node) {
        return parentType(node) == ENUM_DECLARATION_STATEMENT;
    }

    static boolean isClassDeclarationContext(ASTNode node) {
        return parentType(node) == CLASS_DECLARATION_STATEMENT;
    }

    static boolean isScriptBlockExpressionContext(ASTNode node) {
        return parentType(node) == SCRIPT_BLOCK_EXPRESSION;
    }

    static boolean isHashLiteralContext(ASTNode node) {
        return parentType(node) == HASH_LITERAL_EXPRESSION;
    }

    static boolean isParenthesizedExpressionContext(ASTNode node) {
        return parentType(node) == PARENTHESIZED_EXPRESSION;
    }

    static boolean isFunctionDeclaration(ASTNode node) {
        return node.getElementType() == FUNCTION_STATEMENT;
    }

    static boolean isMethodDeclaration(ASTNode node) {
        return node.getElementType() == METHOD_DECLARATION_STATEMENT;
    }

    static boolean isTypeLiteral(ASTNode node) {
        return node.getElementType() == TYPE_LITERAL_EXPRESSION;
    }

    static boolean isAttributeArgument(ASTNode node) {
        return node.getElementType() == ATTRIBUTE_ARGUMENT;
    }

    static boolean isConstructorDeclaration(ASTNode node) {
        return node.getElementType() == CONSTRUCTOR_DECLARATION_STATEMENT;
    }

    static boolean isAttribute(ASTNode node) {
        return node.getElementType() == ATTRIBUTE;
    }
}
