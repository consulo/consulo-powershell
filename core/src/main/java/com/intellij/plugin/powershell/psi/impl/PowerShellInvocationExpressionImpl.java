// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult;
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.types.PowerShellClassType;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;

public class PowerShellInvocationExpressionImpl extends PowerShellQualifiedReferenceExpression {

    public PowerShellInvocationExpressionImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        if ("new".equalsIgnoreCase(getReferenceName())) {
            PowerShellType type = getQualifierType();
            if (type != null) {
                return type;
            }
        }
        return super.getType();
    }

    public PowerShellResolveResult[] multiResolve(boolean incompleteCode) {
        if ("new".equalsIgnoreCase(getReferenceName())
                && getQualifierType() instanceof PowerShellClassType type
                && type.resolve() instanceof PowerShellTypeDeclaration clazz
                && PowerShellResolveUtil.hasDefaultConstructor(clazz)) {
            return new PowerShellResolveResult[] {new PowerShellResolveResult(clazz)};
        }
        return super.multiResolve(incompleteCode);
    }
}
