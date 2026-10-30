// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellConstructorDeclarationStatement;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellMethodDeclarationStatement;

import consulo.language.psi.resolve.ResolveState;

public final class PSMethodScopeProcessor extends PowerShellMemberScopeProcessor {

    private final String myName;

    public PSMethodScopeProcessor(String name) {
        myName = name;
    }

    public boolean doExecute(PowerShellMemberDeclaration member, ResolveState state) {
        boolean method =
                member instanceof PowerShellMethodDeclarationStatement
                        && myName.equalsIgnoreCase(member.getName());
        boolean constructor =
                PsNames.CONSTRUCTOR_CALL.equalsIgnoreCase(myName)
                        && member instanceof PowerShellConstructorDeclarationStatement;
        if (method || constructor) {
            myResult.add(new PowerShellResolveResult(member));
            return false;
        }
        return true;
    }
}
