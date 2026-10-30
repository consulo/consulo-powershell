// SPDX-FileCopyrightText: 2020-2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellTypes;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.Document;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.editor.folding.CustomFoldingBuilder;
import consulo.language.editor.folding.FoldingDescriptor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiWhiteSpace;

import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtensionImpl
public final class PowerShellFoldingBuilder extends CustomFoldingBuilder {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    protected boolean isRegionCollapsedByDefault(ASTNode node) {
        return false;
    }

    protected String getLanguagePlaceholderText(ASTNode node, TextRange range) {
        if (node.getElementType() == PowerShellTypes.SCRIPT_BLOCK_EXPRESSION) {
            return "{...}";
        }
        if (node.getElementType() == PowerShellTypes.ARRAY_EXPRESSION) {
            return "@(...)";
        }
        return "...";
    }

    protected void buildLanguageFoldRegions(
            List<FoldingDescriptor> descriptors,
            PsiElement root,
            Document document,
            boolean quick) {
        collect(root, descriptors, new HashSet<>());
    }

    private void collect(
            PsiElement element, List<FoldingDescriptor> descriptors, Set<PsiElement> used) {
        ASTNode node = element.getNode();
        IElementType type = node.getElementType();
        if ((type == PowerShellTypes.BLOCK_BODY
                        || type == PowerShellTypes.SCRIPT_BLOCK_EXPRESSION
                        || type == PowerShellTypes.ARRAY_EXPRESSION)
                && (node.textContains('\n') || node.textContains('\r'))) {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }
        if (type == PowerShellTypes.COMMENT
                && used.add(element)
                && !isCustomRegionElement(element)) {
            PsiElement end = null;
            PsiElement next = nextNonWhitespace(element);
            while (next != null && next.getNode().getElementType() == PowerShellTypes.COMMENT) {
                used.add(next);
                if (isCustomRegionElement(next)) {
                    break;
                }
                end = next;
                next = nextNonWhitespace(next);
            }
            if (end != null) {
                descriptors.add(
                        new FoldingDescriptor(
                                node,
                                new TextRange(
                                        element.getTextRange().getStartOffset(),
                                        end.getTextRange().getEndOffset())));
            }
        }
        Set<PsiElement> childUsed = new HashSet<>();
        for (PsiElement child : element.getChildren()) {
            collect(child, descriptors, childUsed);
        }
    }

    private static @Nullable PsiElement nextNonWhitespace(PsiElement element) {
        PsiElement next = element.getNextSibling();
        while (next != null
                && (next instanceof PsiWhiteSpace
                        || next.getNode().getElementType() == PowerShellTypes.NLS)) {
            next = next.getNextSibling();
        }
        return next;
    }
}
