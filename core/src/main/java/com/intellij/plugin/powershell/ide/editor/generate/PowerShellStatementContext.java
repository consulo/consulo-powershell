// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.generate;

import com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;
import com.intellij.plugin.powershell.psi.PowerShellBlockBody;
import com.intellij.plugin.powershell.psi.PowerShellCommandName;
import com.intellij.plugin.powershell.psi.impl.PowerShellFile;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.language.editor.template.context.BaseTemplateContextType;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;

@ExtensionImpl
public final class PowerShellStatementContext extends BaseTemplateContextType {

    public PowerShellStatementContext() {
        super(
                "powershell.context.statement",
                PowerShellLocalize.uiStatement(),
                PowerShellLanguageContext.class);
    }

    public boolean isInContext(PsiFile file, int offset) {
        if (!(file instanceof PowerShellFile)) {
            return false;
        }
        PsiElement place = file.findElementAt(offset);
        if (place == null || place instanceof PsiWhiteSpace || place instanceof PsiComment) {
            return false;
        }
        PsiElement context = place.getContext();
        if (context == null) {
            return false;
        }
        PsiElement owner = context.getContext();
        return owner instanceof PowerShellCommandName
                || owner instanceof PowerShellBlockBody
                || owner instanceof PowerShellFile;
    }

    public SyntaxHighlighter createHighlighter() {
        return new PowerShellSyntaxHighlighter();
    }
}
