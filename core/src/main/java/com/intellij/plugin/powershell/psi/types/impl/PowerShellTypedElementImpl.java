// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types.impl;

import com.intellij.plugin.powershell.psi.impl.PowerShellPsiElementImpl;
import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement;

import consulo.language.ast.ASTNode;

public class PowerShellTypedElementImpl extends PowerShellPsiElementImpl
        implements PowerShellTypedElement {

    public PowerShellTypedElementImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        return new PowerShellObjectType();
    }
}
