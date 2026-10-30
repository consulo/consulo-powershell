// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;

import java.util.ArrayList;
import java.util.List;

public class PowerShellMemberScopeProcessor implements PsiScopeProcessor {

    protected final List<PowerShellResolveResult> myResult = new ArrayList<>();

    public boolean execute(PsiElement element, ResolveState state) {
        return !(element instanceof PowerShellMemberDeclaration declaration)
                || doExecute(declaration, state);
    }

    public boolean doExecute(PowerShellMemberDeclaration declaration, ResolveState state) {
        myResult.add(new PowerShellResolveResult(declaration));
        return true;
    }

    public List<PowerShellResolveResult> getResult() {
        return List.copyOf(myResult);
    }
}
