// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellCallableDeclaration;
import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.impl.PowerShellObjectType;

import consulo.language.ast.ASTNode;

public class PowerShellCallableDeclarationImpl extends PowerShellAbstractComponent
        implements PowerShellCallableDeclaration {

    public PowerShellCallableDeclarationImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getReturnType() {
        return new PowerShellObjectType();
    }
}
