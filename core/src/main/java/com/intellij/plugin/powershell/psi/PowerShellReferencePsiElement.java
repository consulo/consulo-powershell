// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;

import org.jspecify.annotations.Nullable;

public interface PowerShellReferencePsiElement extends PowerShellPsiElement, PsiReference {

    @Nullable PowerShellComponent resolve();

    @Nullable PsiElement getNameElement();
}
