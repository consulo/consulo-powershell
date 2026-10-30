// SPDX-FileCopyrightText: 2018 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.injection;

import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.inject.advanced.AbstractLanguageInjectionSupport;
import consulo.language.inject.advanced.TemporaryPlacesRegistry;
import consulo.language.psi.PsiLanguageInjectionHost;

@ExtensionImpl
public final class PowerShellLanguageInjectionSupport extends AbstractLanguageInjectionSupport {

    public String getId() {
        return "PowerShell";
    }

    public boolean useDefaultInjector(PsiLanguageInjectionHost host) {
        return false;
    }

    public boolean isApplicableTo(PsiLanguageInjectionHost host) {
        return host instanceof PowerShellStringLiteralExpression;
    }

    public boolean removeInjectionInPlace(PsiLanguageInjectionHost host) {
        if (!(host instanceof PowerShellStringLiteralExpression)) {
            return false;
        }
        TemporaryPlacesRegistry.getInstance(host.getProject())
                .removeHostWithUndo(host.getProject(), host);
        return true;
    }

    public Class<?>[] getPatternClasses() {
        return new Class<?>[] {PSPatterns.class};
    }
}
