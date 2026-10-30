// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024-2026 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.codeStyle.Alignment;
import consulo.language.codeStyle.Block;
import consulo.language.codeStyle.ChildAttributes;
import consulo.language.codeStyle.Indent;
import consulo.language.codeStyle.Spacing;
import consulo.language.codeStyle.Wrap;
import consulo.language.codeStyle.template.BlockWithParent;

import org.jspecify.annotations.Nullable;

import java.util.List;

final class SyntheticPowerShellCodeBlock implements PowerShellCodeBlock {

    private final List<Block> myChildren;

    private final @Nullable Alignment myAlignment;

    private final @Nullable Indent myIndent;

    private final @Nullable Wrap myWrap;

    private @Nullable BlockWithParent myParent;

    SyntheticPowerShellCodeBlock(
            List<Block> children,
            @Nullable Alignment alignment,
            @Nullable Indent indent,
            @Nullable Wrap wrap) {
        myChildren = children;
        myAlignment = alignment;
        myIndent = indent;
        myWrap = wrap;
        for (Block child : children) {
            if (child instanceof BlockWithParent block) {
                block.setParent(this);
            }
        }
    }

    public @Nullable ASTNode getNode() {
        return null;
    }

    public @Nullable BlockWithParent getParent() {
        return myParent;
    }

    public void setParent(@Nullable BlockWithParent parent) {
        myParent = parent;
    }

    public boolean isLeaf() {
        return false;
    }

    public @Nullable Alignment getAlignment() {
        return myAlignment;
    }

    public @Nullable Indent getIndent() {
        return myIndent;
    }

    public @Nullable Wrap getWrap() {
        return myWrap;
    }

    public @Nullable Spacing getSpacing(@Nullable Block left, Block right) {
        return null;
    }

    public TextRange getTextRange() {
        return new TextRange(
                myChildren.get(0).getTextRange().getStartOffset(),
                myChildren.get(myChildren.size() - 1).getTextRange().getEndOffset());
    }

    public List<Block> getSubBlocks() {
        return myChildren;
    }

    public boolean isIncomplete() {
        return !myChildren.isEmpty() && myChildren.get(myChildren.size() - 1).isIncomplete();
    }

    public ChildAttributes getChildAttributes(int index) {
        Alignment childAlignment =
                index < myChildren.size() ? myChildren.get(index).getAlignment() : null;
        if (index == myChildren.size() && myChildren.size() > 1) {
            childAlignment = myChildren.get(1).getAlignment();
        }
        return new ChildAttributes(myIndent, childAlignment);
    }
}
