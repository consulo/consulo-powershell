// SPDX-FileCopyrightText: 2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.injection;

import com.intellij.plugin.powershell.lang.util.PowerShellStringUtil;
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory;
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.psi.AbstractElementManipulator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtensionImpl
public final class PowerShellStringManipulator
        extends AbstractElementManipulator<PowerShellStringLiteralExpression> {

    public Class<PowerShellStringLiteralExpression> getElementClass() {
        return PowerShellStringLiteralExpression.class;
    }

    public TextRange getRangeInElement(PowerShellStringLiteralExpression element) {
        return element.getContentRange();
    }

    public PowerShellStringLiteralExpression handleContentChange(
            PowerShellStringLiteralExpression element, TextRange range, String content) {
        String oldText = element.getText();
        String escaped = escape(element, content);
        String text =
                oldText.substring(0, range.getStartOffset())
                        + escaped
                        + oldText.substring(range.getEndOffset());
        return (PowerShellStringLiteralExpression)
                element.replace(
                        PowerShellPsiElementFactory.createExpression(element.getProject(), text));
    }

    private static String escape(PowerShellStringLiteralExpression element, String content) {
        if (!element.isExpandable()) {
            return element.isHereString() ? content : content.replace("'", "''");
        }
        StringBuilder out = new StringBuilder();
        if (element.hasSubstitutions()) {
            Pattern pattern = Pattern.compile("\\$(?:\\([^)]*\\)|[\\p{L}_\\p{Nd}#]+)");
            Matcher matcher = pattern.matcher(content);
            int start = 0;
            while (matcher.find()) {
                out.append(escapeText(element, content.substring(start, matcher.start())));
                String expression = matcher.group();
                int index = element.getText().indexOf(expression);
                out.append(
                        index > 0 && element.getText().charAt(index - 1) != '`'
                                ? expression
                                : escapeText(element, expression));
                start = matcher.end();
            }
            out.append(escapeText(element, content.substring(start)));
            return out.toString();
        }
        return escapeText(element, content);
    }

    private static String escapeText(PowerShellStringLiteralExpression element, String content) {
        if (element.isHereString()) {
            return PowerShellStringUtil.escapeHereStringCharacters(
                            content, "$", false, true, new StringBuilder())
                    .toString();
        }
        return PowerShellStringUtil.escapeStringCharacters(
                        content, "$", false, true, new StringBuilder())
                .toString()
                .replace("\"", "" + element.getQuoteEscapeChar() + '"');
    }
}
