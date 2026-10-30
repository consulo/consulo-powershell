// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.generate;

import com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.impl.PowerShellFile;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.language.editor.template.context.BaseTemplateContextType;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;

@ExtensionImpl
public final class PowerShellDeclarationContext extends BaseTemplateContextType {

    public PowerShellDeclarationContext() {
        super(
                "powershell.context.declaration",
                PowerShellLocalize.uiDeclaration(),
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
        return context != null
                && (context.getContext() instanceof PowerShellComponent
                        || context.getContext() instanceof PowerShellFile);
    }

    public SyntaxHighlighter createHighlighter() {
        return new PowerShellSyntaxHighlighter();
    }
}
