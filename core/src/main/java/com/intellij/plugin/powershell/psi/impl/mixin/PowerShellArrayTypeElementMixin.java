// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl.mixin;

import com.intellij.plugin.powershell.psi.PowerShellArrayTypeElement;
import com.intellij.plugin.powershell.psi.impl.PowerShellTypeElementImplGen;
import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.impl.PowerShellArrayClassTypeImpl;

import consulo.language.ast.ASTNode;

public abstract class PowerShellArrayTypeElementMixin extends PowerShellTypeElementImplGen
        implements PowerShellArrayTypeElement {

    public PowerShellArrayTypeElementMixin(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        return new PowerShellArrayClassTypeImpl(this);
    }
}
