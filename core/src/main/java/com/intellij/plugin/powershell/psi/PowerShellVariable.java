// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement;

import consulo.language.psi.PsiElement;

import org.jspecify.annotations.Nullable;

public interface PowerShellVariable
        extends PowerShellComponent, PowerShellReferencePsiElement, PowerShellTypedElement {

    PowerShellReferencePsiElement getReference();

    @Nullable String getScopeName();

    @Nullable PsiElement getScope();

    String getPrefix();

    @Nullable String getSuffix();

    boolean isBracedVariable();

    /** returns full name of the element, including namespace */
    String getQualifiedName();
}
