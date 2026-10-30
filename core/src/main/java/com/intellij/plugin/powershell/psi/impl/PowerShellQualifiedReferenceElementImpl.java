// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellPsiElement;
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement;
import com.intellij.plugin.powershell.psi.PowerShellReferenceIdentifier;
import com.intellij.plugin.powershell.psi.PowerShellTypes;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;

import org.jspecify.annotations.Nullable;

public abstract class PowerShellQualifiedReferenceElementImpl<Q extends PowerShellPsiElement>
        extends PowerShellReferencePsiElementImpl
        implements PowerShellQualifiedReferenceElement<Q> {

    public PowerShellQualifiedReferenceElementImpl(ASTNode node) {
        super(node);
    }

    public boolean isTypeMemberAccess() {
        return findChildByType(PowerShellTypes.COLON2) != null;
    }

    public PsiElement getNavigationElement() {
        PsiElement name = getNameElement();
        return name == null ? super.getNavigationElement() : name;
    }

    public @Nullable String getReferenceName() {
        PsiElement name = getNameElement();
        return name == null ? null : name.getText();
    }

    public @Nullable PsiElement getNameElement() {
        return findChildByClass(PowerShellReferenceIdentifier.class);
    }

    public TextRange getRangeInElement() {
        PsiElement name = getNameElement();
        return name == null
                ? super.getRangeInElement()
                : name.getTextRange().shiftRight(-getTextRange().getStartOffset());
    }

    public int getTextOffset() {
        PsiElement name = getNameElement();
        return name == null ? super.getTextOffset() : name.getTextOffset();
    }
}
