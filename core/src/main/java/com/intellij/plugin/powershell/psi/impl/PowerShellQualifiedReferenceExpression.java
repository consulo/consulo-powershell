// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult;
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil;
import com.intellij.plugin.powershell.psi.PowerShellAttribute;
import com.intellij.plugin.powershell.psi.PowerShellAttributesHolder;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellExpression;
import com.intellij.plugin.powershell.psi.PowerShellInvocationExpression;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;

import org.jspecify.annotations.Nullable;

public abstract class PowerShellQualifiedReferenceExpression
        extends PowerShellQualifiedReferenceElementImpl<PowerShellExpression>
        implements PowerShellExpression {

    public PowerShellQualifiedReferenceExpression(ASTNode node) {
        super(node);
    }

    public @Nullable PowerShellExpression getQualifier() {
        return findChildByClass(PowerShellExpression.class);
    }

    protected @Nullable PowerShellType getQualifierType() {
        PowerShellExpression q = getQualifier();
        return q == null ? null : q.getType();
    }

    public PowerShellType getType() {
        PowerShellComponent target = resolve();
        return target == null ? PowerShellType.UNKNOWN : inferTypeFromResolved(target);
    }

    protected PowerShellType inferTypeFromResolved(PowerShellComponent target) {
        if (target instanceof PowerShellAttributesHolder holder) {
            for (PowerShellAttribute attr : holder.getAttributeList()) {
                if (attr.getTypeLiteralExpression() != null) {
                    return attr.getTypeLiteralExpression().getType();
                }
            }
        }
        return PowerShellType.UNKNOWN;
    }

    public PowerShellResolveResult[] multiResolve(boolean incompleteCode) {
        PowerShellType type = getQualifierType();
        return type == null || type == PowerShellType.UNKNOWN
                ? PowerShellResolveResult.EMPTY_ARRAY
                : PowerShellResolveUtil.resolveMembers(
                        type, getReferenceName(), this instanceof PowerShellInvocationExpression);
    }
}
