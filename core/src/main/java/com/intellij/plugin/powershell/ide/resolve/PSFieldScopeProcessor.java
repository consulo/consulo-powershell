// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellEnumLabelDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellMethodDeclarationStatement;
import com.intellij.plugin.powershell.psi.PowerShellPropertyDeclarationStatement;

import consulo.language.psi.resolve.ResolveState;

public final class PSFieldScopeProcessor extends PowerShellMemberScopeProcessor {

    private final String myName;

    public PSFieldScopeProcessor(String name) {
        myName = name;
    }

    public boolean doExecute(PowerShellMemberDeclaration member, ResolveState state) {
        if ((member instanceof PowerShellPropertyDeclarationStatement
                        || member instanceof PowerShellEnumLabelDeclaration
                        || member instanceof PowerShellMethodDeclarationStatement)
                && myName.equalsIgnoreCase(member.getName())) {
            myResult.add(new PowerShellResolveResult(member));
        }
        return true;
    }
}
