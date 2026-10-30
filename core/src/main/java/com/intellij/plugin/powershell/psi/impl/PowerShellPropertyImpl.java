// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellAssignmentExpression;
import com.intellij.plugin.powershell.psi.PowerShellAttribute;
import com.intellij.plugin.powershell.psi.PowerShellAttributesHolder;
import com.intellij.plugin.powershell.psi.PowerShellBlockBody;
import com.intellij.plugin.powershell.psi.PowerShellIdentifier;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellVariable;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.ast.ASTNode;

import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class PowerShellPropertyImpl extends PowerShellTargetVariableImpl
        implements PowerShellMemberDeclaration, PowerShellAttributesHolder {

    public PowerShellPropertyImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        for (PowerShellAttribute attribute : getAttributeList()) {
            if (attribute.getTypeLiteralExpression() != null) {
                return attribute.getTypeLiteralExpression().getTypeElement().getType();
            }
        }
        return super.getType();
    }

    public @Nullable PowerShellTypeDeclaration getContainingClass() {
        return getContext() instanceof PowerShellBlockBody body
                        && body.getContext() instanceof PowerShellTypeDeclaration type
                ? type
                : null;
    }

    private @Nullable PowerShellVariable getVariable() {
        PowerShellTargetVariableExpression variable =
                findChildByClass(PowerShellTargetVariableExpression.class);
        if (variable != null) {
            return variable;
        }
        PowerShellAssignmentExpression assignment =
                findChildByClass(PowerShellAssignmentExpression.class);
        if (assignment == null) {
            return null;
        }
        List<PowerShellTargetVariableExpression> targets =
                PowerShellPsiImplUtil.getTargetVariables(assignment);
        return targets.isEmpty() ? null : targets.get(0);
    }

    public @Nullable PowerShellIdentifier getNameIdentifier() {
        PowerShellVariable variable = getVariable();
        return variable == null ? null : variable.getNameIdentifier();
    }

    public String getPrefix() {
        PowerShellVariable variable = getVariable();
        return variable == null ? "" : variable.getPrefix();
    }

    public @Nullable String getSuffix() {
        PowerShellVariable variable = getVariable();
        return variable == null ? null : variable.getSuffix();
    }

    public @Nullable String getScopeName() {
        PowerShellVariable variable = getVariable();
        return variable == null ? null : variable.getScopeName();
    }
}
