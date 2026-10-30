// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl.mixin;

import com.intellij.plugin.powershell.psi.PowerShellExpression;
import com.intellij.plugin.powershell.psi.PowerShellParenthesizedExpression;
import com.intellij.plugin.powershell.psi.impl.PowerShellExpressionImplGen;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;
import consulo.language.psi.util.PsiTreeUtil;

public abstract class PowerShellParenthesizedExpressionMixin extends PowerShellExpressionImplGen
        implements PowerShellParenthesizedExpression {

    public PowerShellParenthesizedExpressionMixin(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        PowerShellExpression child = PsiTreeUtil.getChildOfType(this, PowerShellExpression.class);
        return child == null ? PowerShellType.UNKNOWN : child.getType();
    }
}
