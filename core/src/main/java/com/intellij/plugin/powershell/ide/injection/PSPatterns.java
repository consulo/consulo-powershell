// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.injection;

import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression;

import consulo.language.pattern.PlatformPatterns;
import consulo.language.pattern.PsiElementPattern;
import consulo.language.pattern.StandardPatterns;

public final class PSPatterns extends PlatformPatterns {

    public static PsiElementPattern.Capture<PowerShellStringLiteralExpression> sqlCapture() {
        return psiElement(PowerShellStringLiteralExpression.class)
                .withText(StandardPatterns.string().contains("script type=\"text/javascript\""));
    }
}
