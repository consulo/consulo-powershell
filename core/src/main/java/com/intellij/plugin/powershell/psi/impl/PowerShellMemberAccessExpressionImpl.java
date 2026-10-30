// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellEnumLabelDeclaration;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;

public class PowerShellMemberAccessExpressionImpl extends PowerShellQualifiedReferenceExpression {

    public PowerShellMemberAccessExpressionImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        PowerShellComponent target = resolve();
        if (target instanceof PowerShellEnumLabelDeclaration) {
            PowerShellType type = getQualifierType();
            if (type != null) {
                return type;
            }
        }
        return target == null ? PowerShellType.UNKNOWN : inferTypeFromResolved(target);
    }
}
