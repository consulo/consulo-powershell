// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellReferencePsiElement;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveCache;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PowerShellComponentResolveProcessor
        implements ResolveCache.AbstractResolver<PowerShellReferencePsiElement, List<PsiElement>> {

    public static final PowerShellComponentResolveProcessor INSTANCE =
            new PowerShellComponentResolveProcessor();

    public @Nullable List<PsiElement> resolve(
            PowerShellReferencePsiElement reference, boolean incomplete) {
        PowerShellComponentScopeProcessor processor = new PowerShellComponentScopeProcessor();
        PsiTreeUtil.treeWalkUp(processor, reference.getElement(), null, ResolveState.initial());
        return new ArrayList<>(processor.getResult());
    }
}
