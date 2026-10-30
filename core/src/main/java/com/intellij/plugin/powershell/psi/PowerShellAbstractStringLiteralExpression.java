// SPDX-FileCopyrightText: 2018 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import consulo.document.util.TextRange;
import consulo.language.psi.PsiLanguageInjectionHost;

public interface PowerShellAbstractStringLiteralExpression
        extends PowerShellExpression, PsiLanguageInjectionHost {

    boolean hasSubstitutions();

    boolean isHereString();

    boolean isExpandable();

    char getQuoteEscapeChar();

    char getQuoteChar();

    TextRange getContentRange();

    String getStringContent();
}
