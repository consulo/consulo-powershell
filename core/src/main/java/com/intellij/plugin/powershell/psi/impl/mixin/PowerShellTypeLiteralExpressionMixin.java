// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl.mixin;

import com.intellij.plugin.powershell.psi.PowerShellTypeLiteralExpression;
import com.intellij.plugin.powershell.psi.impl.PowerShellExpressionImplGen;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;

public abstract class PowerShellTypeLiteralExpressionMixin extends PowerShellExpressionImplGen
        implements PowerShellTypeLiteralExpression {

    public PowerShellTypeLiteralExpressionMixin(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        return getTypeElement().getType();
    }
}
