// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.lang.resolve.PowerShellTypeUtil;
import com.intellij.plugin.powershell.psi.PowerShellExpression;
import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement;

import consulo.language.ast.ASTNode;

public class PowerShellExpressionImpl extends PowerShellPsiElementImpl
        implements PowerShellExpression, PowerShellTypedElement {

    public PowerShellExpressionImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        return PowerShellTypeUtil.inferExpressionType(this);
    }
}
