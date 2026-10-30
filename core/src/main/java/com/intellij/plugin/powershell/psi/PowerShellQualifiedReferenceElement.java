// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import consulo.language.psi.PsiQualifiedReference;

import org.jspecify.annotations.Nullable;

public interface PowerShellQualifiedReferenceElement<Q extends PowerShellPsiElement>
        extends PowerShellReferencePsiElement, PsiQualifiedReference {

    @Nullable Q getQualifier();

    @Nullable String getReferenceName();

    boolean isTypeMemberAccess();
}
