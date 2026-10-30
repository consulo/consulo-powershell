// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2021-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.BLOCK_BODY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.EXPANDABLE_HERE_STRING_END;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.INVOCATION_EXPRESSION;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LCURLY;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LP;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.NLS;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARAMETER_CLAUSE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARENTHESIZED_ARGUMENT_LIST;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.RCURLY;

import static consulo.language.codeStyle.CommonCodeStyleSettings.DO_NOT_WRAP;
import static consulo.language.codeStyle.CommonCodeStyleSettings.NEXT_LINE_SHIFTED;
import static consulo.language.codeStyle.CommonCodeStyleSettings.NEXT_LINE_SHIFTED2;
import static consulo.language.codeStyle.CommonCodeStyleSettings.WRAP_ALWAYS;

import com.intellij.plugin.powershell.PowerShellFileType;
import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellExpression;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.codeStyle.ASTBlock;
import consulo.language.codeStyle.AbstractBlock;
import consulo.language.codeStyle.Alignment;
import consulo.language.codeStyle.Block;
import consulo.language.codeStyle.ChildAttributes;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import consulo.language.codeStyle.Indent;
import consulo.language.codeStyle.Spacing;
import consulo.language.codeStyle.Wrap;
import consulo.language.codeStyle.WrappingUtil;
import consulo.language.codeStyle.template.BlockWithParent;
import consulo.language.psi.PsiFile;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class PowerShellBlockImpl extends AbstractBlock implements PowerShellCodeBlock {

    private final CodeStyleSettings mySettings;

    private final CommonCodeStyleSettings myCommon;

    private final PowerShellCodeStyleSettings myCustom;

    private final PowerShellSpacingProcessor mySpacing;

    private final @Nullable Indent myIndent;

    private final @Nullable Alignment myBaseAlignment = Alignment.createAlignment();

    private @Nullable BlockWithParent myParent;

    PowerShellBlockImpl(
            ASTNode node,
            CodeStyleSettings settings,
            @Nullable Indent indent,
            @Nullable Wrap wrap,
            @Nullable Alignment alignment) {
        super(node, wrap, alignment);
        mySettings = settings;
        myIndent = indent;
        myCommon = settings.getCommonSettings(PowerShellLanguage.INSTANCE);
        myCustom = settings.getCustomSettings(PowerShellCodeStyleSettings.class);
        mySpacing = new PowerShellSpacingProcessor(myCommon, myCustom);
    }

    public @Nullable BlockWithParent getParent() {
        return myParent;
    }

    public void setParent(@Nullable BlockWithParent parent) {
        myParent = parent;
    }

    public @Nullable Indent getIndent() {
        return myIndent;
    }

    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }

    protected List<Block> buildChildren() {
        List<Block> result = new ArrayList<>();
        for (ASTNode child = myNode.getFirstChildNode();
                child != null;
                child = child.getTreeNext()) {
            if (child.getText().isBlank()) {
                continue;
            }
            PowerShellCodeBlock block;
            if (PSFormattingUtils.type(child) == INVOCATION_EXPRESSION) {
                List<ASTNode> nodes = new ArrayList<>();
                collectCallNodes(child, nodes);
                block =
                        new ChainMethodCallBlockBuilder(
                                        alignment(child), null, indent(child), mySettings)
                                .build(nodes);
            }
            else {
                block =
                        new PowerShellBlockImpl(
                                child, mySettings, indent(child), wrap(child), alignment(child));
            }
            block.setParent(this);
            result.add(block);
        }
        return result;
    }

    private static void collectCallNodes(ASTNode node, List<ASTNode> result) {
        for (ASTNode child = node.getFirstChildNode(); child != null; child = child.getTreeNext()) {
            if (child.getText().isBlank() || PSFormattingUtils.type(child) == NLS) {
                continue;
            }
            if (PSFormattingUtils.type(child) == INVOCATION_EXPRESSION) {
                collectCallNodes(child, result);
            }
            else {
                result.add(child);
            }
        }
    }

    public @Nullable Spacing getSpacing(@Nullable Block first, Block second) {
        return first instanceof PowerShellCodeBlock a && second instanceof PowerShellCodeBlock b
                ? mySpacing.getSpacing(a, b)
                : null;
    }

    public ChildAttributes getChildAttributes(int index) {
        List<Block> children = getSubBlocks();
        if (index < 1 || index >= children.size()) {
            return new ChildAttributes(childIndent(), null);
        }
        Block next = children.get(index);
        Block previous = children.get(index - 1);
        Alignment alignment =
                children.stream()
                        .map(Block::getAlignment)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
        if (next instanceof ASTBlock b && previous instanceof ASTBlock a) {
            if (PSFormattingUtils.type(a.getNode()) == BLOCK_BODY
                    && PSFormattingUtils.type(b.getNode()) == RCURLY) {
                return ChildAttributes.DELEGATE_TO_PREV_CHILD;
            }
            if (PSFormattingUtils.type(a.getNode()) == LCURLY
                    && PSFormattingUtils.type(b.getNode()) == RCURLY) {
                return new ChildAttributes(Indent.getNormalIndent(), alignment);
            }
        }
        return new ChildAttributes(next.getIndent(), alignment);
    }

    private @Nullable Indent childIndent() {
        if (myNode.getPsi() instanceof PsiFile || PSFormattingUtils.type(myNode) == BLOCK_BODY) {
            return Indent.getNoneIndent();
        }
        return myNode.findChildByType(LCURLY) != null ? Indent.getNormalIndent() : null;
    }

    private @Nullable Alignment alignment(ASTNode node) {
        if (PSFormattingUtils.isCallArgument(node) && myCommon.ALIGN_MULTILINE_PARAMETERS_IN_CALLS
                || PSFormattingUtils.isForParameter(node) && myCommon.ALIGN_MULTILINE_FOR
                || PSFormattingUtils.isFunctionParameter(node)
                        && myCommon.ALIGN_MULTILINE_PARAMETERS
                || PSFormattingUtils.isBlockParameter(node)
                        && myCustom.ALIGN_MULTILINE_BLOCK_PARAMETERS
                || PSFormattingUtils.isBinaryExpressionContext(node)
                        && node.getPsi() instanceof PowerShellExpression
                        && myCommon.ALIGN_MULTILINE_BINARY_OPERATION
                || PSFormattingUtils.isTypeLiteral(node)
                        && PSFormattingUtils.isCatchClauseContext(node)
                        && myCustom.ALIGN_MULTILINE_CATCH_TYPE_LIST
                || PSFormattingUtils.isAttributeArgument(node)
                        && myCustom.ALIGN_MULTILINE_ATTRIBUTE_ARGUMENT
                || PSFormattingUtils.isPipelineContext(node)
                        && myCustom.ALIGN_MULTILINE_PIPELINE_STATEMENT) {
            return myBaseAlignment;
        }
        if (PSFormattingUtils.type(myNode) == PARENTHESIZED_ARGUMENT_LIST
                        && myCommon.ALIGN_MULTILINE_PARAMETERS_IN_CALLS
                        && myCommon.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE
                || PSFormattingUtils.type(myNode) == PARAMETER_CLAUSE
                        && myCommon.ALIGN_MULTILINE_PARAMETERS
                        && myCommon.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE) {
            return myBaseAlignment;
        }
        return null;
    }

    private int braceStyle(ASTNode node) {
        if (PSFormattingUtils.isClassDeclarationContext(node)
                || PSFormattingUtils.isEnumDeclarationContext(node)) {
            return myCommon.CLASS_BRACE_STYLE;
        }
        if (PSFormattingUtils.isCallableDeclarationContext(node)) {
            return myCommon.METHOD_BRACE_STYLE;
        }
        return myCommon.BRACE_STYLE;
    }

    private Indent indent(ASTNode node) {
        IElementType type = PSFormattingUtils.type(node);
        int style = braceStyle(node);
        if (type == BLOCK_BODY) {
            return style == NEXT_LINE_SHIFTED2
                    ? Indent.getSpaceIndent(
                            mySettings.getIndentSize(PowerShellFileType.INSTANCE) * 2)
                    : Indent.getNormalIndent();
        }
        if (type == LCURLY || type == RCURLY) {
            return style == NEXT_LINE_SHIFTED || style == NEXT_LINE_SHIFTED2
                    ? Indent.getNormalIndent()
                    : Indent.getNoneIndent();
        }
        if (PSFormattingUtils.isCallArgument(node)
                || PSFormattingUtils.isFunctionParameter(node)
                || PSFormattingUtils.isForParameter(node)
                || PSFormattingUtils.isRhsBinaryExpressionContext(node)
                || PSFormattingUtils.isInvocationExpressionQualifier(node)
                || PSFormattingUtils.isExpressionInPipelineTail(node)
                || PSFormattingUtils.isAttributeArgument(node)) {
            return Indent.getContinuationIndent();
        }
        if (PSFormattingUtils.isBlockParameter(node) || PSFormattingUtils.isArrayElement(node)) {
            return Indent.getNormalIndent();
        }
        if (type == EXPANDABLE_HERE_STRING_END) {
            return Indent.getAbsoluteNoneIndent();
        }
        return Indent.getNoneIndent();
    }

    private static Wrap createWrap(int value, boolean first) {
        return Wrap.createWrap(WrappingUtil.getWrapType(value), first);
    }

    private Wrap wrap(ASTNode node) {
        if ((PSFormattingUtils.isFunctionParameter(node)
                        || PSFormattingUtils.isBlockParameter(node))
                && PSFormattingUtils.type(PSFormattingUtils.sibling(node, false)) != LP) {
            boolean first = PSFormattingUtils.isFirstNodeInParameter(node, true);
            int value =
                    PSFormattingUtils.isFunctionParameter(node)
                            ? myCommon.METHOD_PARAMETERS_WRAP
                            : myCustom.BLOCK_PARAMETER_CLAUSE_WRAP;
            if (first && PSFormattingUtils.isAttribute(node)) {
                Wrap attributeWrap = createWrap(myCommon.PARAMETER_ANNOTATION_WRAP, true);
                return myCommon.PARAMETER_ANNOTATION_WRAP == WRAP_ALWAYS
                        ? attributeWrap
                        : Wrap.createChildWrap(
                                attributeWrap,
                                WrappingUtil.getWrapType(
                                        value | myCommon.PARAMETER_ANNOTATION_WRAP),
                                true);
            }
            if (!first && PSFormattingUtils.isFollowedByAttribute(node)) {
                return createWrap(myCommon.PARAMETER_ANNOTATION_WRAP, true);
            }
            return createWrap(value, first);
        }
        if (PSFormattingUtils.isCallArgument(node)
                && PSFormattingUtils.type(PSFormattingUtils.sibling(node, false)) != LP) {
            return createWrap(
                    myCommon.CALL_PARAMETERS_WRAP,
                    PSFormattingUtils.isFirstNodeInParameter(node, true));
        }
        if (PSFormattingUtils.isForParameter(node)) {
            return createWrap(
                    myCommon.FOR_STATEMENT_WRAP, PSFormattingUtils.isFirstNodeInForParameter(node));
        }
        if (PSFormattingUtils.isRhsBinaryExpressionContext(node)) {
            return createWrap(myCommon.BINARY_OPERATION_WRAP, true);
        }
        if (PSFormattingUtils.isAttribute(node) || PSFormattingUtils.isFollowedByAttribute(node)) {
            if (PSFormattingUtils.isClassDeclarationContext(node)) {
                return createWrap(myCommon.CLASS_ANNOTATION_WRAP, true);
            }
            if (PSFormattingUtils.isPropertyDeclarationContext(node)) {
                return createWrap(myCommon.FIELD_ANNOTATION_WRAP, true);
            }
            if (PSFormattingUtils.isCallableDeclarationContext(node)) {
                return createWrap(myCommon.METHOD_ANNOTATION_WRAP, true);
            }
            if (PSFormattingUtils.isParameterClauseContext(node)
                    || PSFormattingUtils.isBlockBodyContext(node)) {
                return createWrap(myCommon.PARAMETER_ANNOTATION_WRAP, true);
            }
            if (PSFormattingUtils.isBlockParameterClauseContext(node)) {
                return createWrap(myCustom.BLOCK_PARAMETER_CLAUSE_WRAP, true);
            }
        }
        if (PSFormattingUtils.isTypeLiteral(node) && PSFormattingUtils.isCatchClauseContext(node)) {
            return createWrap(myCustom.CATCH_TYPE_LIST_WRAP, true);
        }
        if (PSFormattingUtils.isAttributeArgument(node)
                && PSFormattingUtils.type(PSFormattingUtils.sibling(node, false)) != LP) {
            return createWrap(myCustom.ATTRIBUTE_ARGUMENT_WRAP, true);
        }
        if (PSFormattingUtils.isExpressionInPipelineTail(node)) {
            return createWrap(myCustom.PIPELINE_TAIL_WRAP, true);
        }
        return createWrap(DO_NOT_WRAP, false);
    }
}
