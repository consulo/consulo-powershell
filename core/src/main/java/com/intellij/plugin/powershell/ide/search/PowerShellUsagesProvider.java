// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.search;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;
import com.intellij.plugin.powershell.psi.PowerShellCallableDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellReferencePsiElement;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellVariable;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.cacheBuilder.DefaultWordsScanner;
import consulo.language.cacheBuilder.WordsScanner;
import consulo.language.findUsage.FindUsagesProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNamedElement;
import consulo.language.psi.PsiReference;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ExtensionImpl
public final class PowerShellUsagesProvider implements FindUsagesProvider {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(
                new PowerShellLexerAdapter(),
                PowerShellTokenTypeSets.IDENTIFIERS,
                PowerShellTokenTypeSets.COMMENTS,
                PowerShellTokenTypeSets.STRINGS,
                PowerShellTokenTypeSets.PROCESS_AS_WORD_TOKENS);
    }

    public boolean canFindUsagesFor(PsiElement element) {
        return element instanceof PowerShellReferencePsiElement
                || element instanceof PowerShellComponent;
    }

    public @Nullable String getHelpId(PsiElement element) {
        return null;
    }

    public String getType(PsiElement element) {
        if (element instanceof PowerShellTypeDeclaration) {
            return PowerShellLocalize.usageTypeType().get();
        }
        if (element instanceof PowerShellVariable) {
            return PowerShellLocalize.usageTypeVariable().get();
        }
        if (element instanceof PowerShellCallableDeclaration) {
            return PowerShellLocalize.usageTypeFunction().get();
        }
        return PowerShellLocalize.usageTypeReference().get();
    }

    public String getDescriptiveName(PsiElement element) {
        return element instanceof PsiNamedElement named
                ? Objects.toString(named.getName(), "")
                : element instanceof PsiReference ref ? ref.getCanonicalText() : element.getText();
    }

    public String getNodeText(PsiElement element, boolean full) {
        return getDescriptiveName(element);
    }
}
