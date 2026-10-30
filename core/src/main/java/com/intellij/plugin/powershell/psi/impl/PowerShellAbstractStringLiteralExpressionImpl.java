// SPDX-FileCopyrightText: 2018 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.lang.util.PowerShellStringUtil;
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression;
import com.intellij.plugin.powershell.psi.PowerShellSubExpression;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.plugin.powershell.psi.PowerShellVariable;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.ElementManipulators;
import consulo.language.psi.LiteralTextEscaper;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiLanguageInjectionHost;

import org.jspecify.annotations.Nullable;

public abstract class PowerShellAbstractStringLiteralExpressionImpl extends PowerShellExpressionImpl
        implements PsiLanguageInjectionHost, PowerShellStringLiteralExpression {

    public PowerShellAbstractStringLiteralExpressionImpl(ASTNode node) {
        super(node);
    }

    public boolean hasSubstitutions() {
        if (isExpandable()) {
            for (PsiElement child : getChildren()) {
                if (child instanceof PowerShellVariable || child instanceof PowerShellSubExpression)
                    return true;
            }
        }
        return false;
    }

    public char getQuoteEscapeChar() {
        return !isExpandable() ? '\'' : getStringContent().contains("\"\"") ? '"' : '`';
    }

    public char getQuoteChar() {
        return isExpandable() ? '"' : '\'';
    }

    public String getStringContent() {
        return getContentRange().substring(getText());
    }

    public TextRange getContentRange() {
        String text = getText();
        int start =
                Math.min(
                        text.length(),
                        isHereString()
                                ? (text.startsWith("@\"\r\n") || text.startsWith("@'\r\n") ? 4 : 3)
                                : 1);
        int end = text.length();
        if (isHereString()) {
            int closing = text.lastIndexOf("" + getQuoteChar() + '@');
            if (closing >= start) {
                end = closing;
                if (end > start && text.charAt(end - 1) == '\n') {
                    end--;
                }
                if (end > start && text.charAt(end - 1) == '\r') {
                    end--;
                }
            }
        }
        else if (end > start && text.charAt(end - 1) == getQuoteChar()) {
            end--;
        }
        return new TextRange(start, Math.max(start, end));
    }

    public boolean isHereString() {
        PsiElement child = getFirstChild();
        if (child == null) {
            return false;
        }
        IElementType type = child.getNode().getElementType();
        return type == PowerShellTypes.EXPANDABLE_HERE_STRING_START
                || type == PowerShellTypes.VERBATIM_HERE_STRING;
    }

    public boolean isExpandable() {
        PsiElement child = getFirstChild();
        if (child == null) {
            return false;
        }
        IElementType type = child.getNode().getElementType();
        return type == PowerShellTypes.DQ_OPEN
                || type == PowerShellTypes.EXPANDABLE_HERE_STRING_START;
    }

    public boolean isValidHost() {
        return true;
    }

    public PsiLanguageInjectionHost updateText(String text) {
        return ElementManipulators.handleContentChange(this, text);
    }

    public LiteralTextEscaper<PowerShellStringLiteralExpression> createLiteralTextEscaper() {
        return new StringLiteralEscaper(this);
    }

    public static final class StringLiteralEscaper
            extends LiteralTextEscaper<PowerShellStringLiteralExpression> {

        private int @Nullable [] mySourceOffsets;

        public StringLiteralEscaper(PowerShellStringLiteralExpression host) {
            super(host);
        }

        public TextRange getRelevantTextRange() {
            return myHost.getContentRange();
        }

        public boolean decode(TextRange range, StringBuilder out) {
            String text = range.substring(myHost.getText());
            mySourceOffsets = new int[text.length() + 1];
            if (myHost.isExpandable()) {
                return PowerShellStringUtil.parseExpandableString(
                        text,
                        out,
                        mySourceOffsets,
                        myHost.isHereString() ? null : myHost.getQuoteChar());
            }
            int decoded = 0;
            for (int i = 0; i < text.length(); i++) {
                mySourceOffsets[decoded++] = i;
                char c = text.charAt(i);
                out.append(c);
                if (!myHost.isHereString()
                        && c == '\''
                        && i + 1 < text.length()
                        && text.charAt(i + 1) == '\'') {
                    i++;
                }
            }
            mySourceOffsets[decoded] = text.length();
            return true;
        }

        public int getOffsetInHost(int offset, TextRange range) {
            return mySourceOffsets == null || offset < 0 || offset >= mySourceOffsets.length
                    ? -1
                    : Math.min(mySourceOffsets[offset], range.getLength()) + range.getStartOffset();
        }

        public boolean isOneLine() {
            return false;
        }
    }
}
