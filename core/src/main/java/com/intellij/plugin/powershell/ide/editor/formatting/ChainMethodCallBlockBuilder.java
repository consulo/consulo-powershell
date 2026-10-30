// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.COLON2;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DOT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.PARENTHESIZED_ARGUMENT_LIST;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;

import consulo.language.ast.ASTNode;
import consulo.language.codeStyle.Alignment;
import consulo.language.codeStyle.Block;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import consulo.language.codeStyle.Indent;
import consulo.language.codeStyle.Wrap;
import consulo.language.codeStyle.WrappingUtil;
import consulo.language.psi.PsiComment;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class ChainMethodCallBlockBuilder {

    private final @Nullable Alignment myAlignment;

    private final @Nullable Wrap myWrap;

    private final @Nullable Indent myIndent;

    private final CodeStyleSettings mySettings;

    ChainMethodCallBlockBuilder(
            @Nullable Alignment alignment,
            @Nullable Wrap wrap,
            @Nullable Indent indent,
            CodeStyleSettings settings) {
        myAlignment = alignment;
        myWrap = wrap;
        myIndent = indent;
        mySettings = settings;
    }

    PowerShellCodeBlock build(List<ASTNode> nodes) {
        CommonCodeStyleSettings common = mySettings.getCommonSettings(PowerShellLanguage.INSTANCE);
        List<List<ASTNode>> chunks = new ArrayList<>();
        List<ASTNode> current = new ArrayList<>();
        for (ASTNode node : nodes) {
            current.add(node);
            if (node.getElementType() == DOT
                    || node.getElementType() == COLON2
                    || node.getPsi() instanceof PsiComment) {
                chunks.add(current);
                current = new ArrayList<>();
            }
        }
        if (!current.isEmpty()) {
            chunks.add(current);
        }
        List<Block> result = new ArrayList<>();
        Alignment chainAlignment =
                common.ALIGN_MULTILINE_CHAINED_METHODS ? Alignment.createAlignment() : null;
        Wrap chainWrap =
                Wrap.createWrap(WrappingUtil.getWrapType(common.METHOD_CALL_CHAIN_WRAP), false);
        int firstCall = -1;
        for (int i = 0; i < chunks.size(); i++) {
            List<ASTNode> chunk = chunks.get(i);
            boolean call =
                    chunk.size() >= 2
                            && chunk.get(1).getElementType() == PARENTHESIZED_ARGUMENT_LIST;
            if (call && firstCall < 0) {
                firstCall = i;
            }
            boolean subsequent = firstCall >= 0 && i > firstCall;
            boolean wrapChunk =
                    subsequent || common.WRAP_FIRST_METHOD_IN_CALL_CHAIN && i + 1 < chunks.size();
            List<Block> blocks = new ArrayList<>();
            for (ASTNode node : chunk) {
                blocks.add(
                        new PowerShellBlockImpl(
                                node,
                                mySettings,
                                Indent.getContinuationWithoutFirstIndent(),
                                null,
                                null));
            }
            result.add(
                    new SyntheticPowerShellCodeBlock(
                            blocks,
                            i > 0 ? chainAlignment : null,
                            Indent.getContinuationWithoutFirstIndent(),
                            wrapChunk ? chainWrap : null));
        }
        return new SyntheticPowerShellCodeBlock(
                result,
                myAlignment,
                myIndent == null ? Indent.getContinuationIndent() : myIndent,
                myWrap);
    }
}
