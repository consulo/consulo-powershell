// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult;
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolver;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellIdentifier;
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory;
import com.intellij.plugin.powershell.psi.PowerShellReferencePsiElement;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;
import consulo.language.psi.PsiReference;

import org.jspecify.annotations.Nullable;

public abstract class PowerShellReferencePsiElementImpl extends PowerShellPsiElementImpl
        implements PowerShellReferencePsiElement, PsiPolyVariantReference {

    public PowerShellReferencePsiElementImpl(ASTNode node) {
        super(node);
    }

    public PowerShellResolveResult[] multiResolve(boolean incompleteCode) {
        return PowerShellResolver.resolve(this);
    }

    public @Nullable PsiElement getNameElement() {
        return findChildByClass(PowerShellIdentifier.class);
    }

    public PsiElement getElement() {
        return this;
    }

    public PsiReference getReference() {
        return this;
    }

    public @Nullable PowerShellComponent resolve() {
        PowerShellResolveResult[] results = multiResolve(false);
        return results.length == 0 ? null : results[0].getElement();
    }

    public Object[] getVariants() {
        return new Object[0];
    }

    public TextRange getRangeInElement() {
        return TextRange.from(0, getTextLength());
    }

    public String getCanonicalText() {
        return getNode().getText();
    }

    public PsiElement handleElementRename(String name) {
        PsiElement old = getNameElement();
        PowerShellIdentifier replacement =
                PowerShellPsiElementFactory.createIdentifierFromText(getProject(), name, true);
        if (old != null && replacement != null) {
            old.replace(replacement);
        }
        return this;
    }

    public PsiElement bindToElement(PsiElement element) {
        return this;
    }

    public boolean isSoft() {
        return false;
    }

    public boolean isReferenceTo(PsiElement element) {
        return resolve() == element;
    }
}
