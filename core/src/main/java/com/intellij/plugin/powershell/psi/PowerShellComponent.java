// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.psi.PsiNamedElement;

import org.jspecify.annotations.Nullable;

public interface PowerShellComponent
        extends PowerShellPsiElement, PsiNamedElement, PsiNameIdentifierOwner {

    @Nullable String getName();

    @Nullable PowerShellIdentifier getNameIdentifier();
}
