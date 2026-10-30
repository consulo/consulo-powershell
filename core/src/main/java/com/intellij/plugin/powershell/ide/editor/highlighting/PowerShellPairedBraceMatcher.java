// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.highlighting;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellTypes;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.BracePair;
import consulo.language.Language;
import consulo.language.PairedBraceMatcher;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiFile;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellPairedBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] PAIRS = {
        new BracePair(PowerShellTypes.LCURLY, PowerShellTypes.RCURLY, true),
        new BracePair(PowerShellTypes.BRACED_VAR_START, PowerShellTypes.RCURLY, false),
        new BracePair(PowerShellTypes.LP, PowerShellTypes.RP, false),
        new BracePair(PowerShellTypes.SQBR_L, PowerShellTypes.SQBR_R, false)
    };

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public BracePair[] getPairs() {
        return PAIRS;
    }

    public boolean isPairedBracesAllowedBeforeType(
            IElementType left, @Nullable IElementType context) {
        return true;
    }

    public int getCodeConstructStart(PsiFile file, int offset) {
        return offset;
    }
}
