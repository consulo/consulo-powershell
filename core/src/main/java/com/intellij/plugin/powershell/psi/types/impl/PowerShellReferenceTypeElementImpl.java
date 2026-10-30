// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types.impl;

import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement;
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.plugin.powershell.psi.impl.PowerShellReferencePsiElementImpl;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class PowerShellReferenceTypeElementImpl extends PowerShellReferencePsiElementImpl
        implements PowerShellReferenceTypeElement,
                PowerShellQualifiedReferenceElement<PowerShellReferenceTypeElement> {

    public PowerShellReferenceTypeElementImpl(ASTNode node) {
        super(node);
    }

    public boolean isTypeMemberAccess() {
        return false;
    }

    public @Nullable String getReferenceName() {
        PsiElement name = getNameElement();
        return name == null ? null : name.getText();
    }

    public String getCanonicalText() {
        return Objects.toString(getReferenceName(), "");
    }

    public @Nullable PowerShellReferenceTypeElement getQualifier() {
        return findChildByClass(PowerShellReferenceTypeElement.class);
    }

    public PowerShellType getType() {
        return new PowerShellReferenceClassTypeImpl(this);
    }

    public @Nullable PsiElement getNameElement() {
        PsiElement name = findLastChildByType(PowerShellTypes.SIMPLE_ID);
        return name == null ? getLastChild() : name;
    }
}
