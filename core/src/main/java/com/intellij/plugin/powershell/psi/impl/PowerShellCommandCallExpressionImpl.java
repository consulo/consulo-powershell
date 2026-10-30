// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.lang.resolve.PowerShellTypeUtil;
import com.intellij.plugin.powershell.psi.PowerShellCallableReference;
import com.intellij.plugin.powershell.psi.PowerShellCommandName;
import com.intellij.plugin.powershell.psi.PowerShellExpression;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;

import org.jspecify.annotations.Nullable;

public class PowerShellCommandCallExpressionImpl extends PowerShellReferencePsiElementImpl
        implements PowerShellCallableReference, PowerShellExpression {

    public PowerShellCommandCallExpressionImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        return PowerShellTypeUtil.inferExpressionType(this);
    }

    public @Nullable PsiElement getNameElement() {
        PowerShellCommandName name = findChildByClass(PowerShellCommandName.class);
        return name == null ? null : name.getIdentifier();
    }

    public TextRange getRangeInElement() {
        PsiElement name = getNameElement();
        return name == null
                ? super.getRangeInElement()
                : name.getTextRange().shiftRight(-getTextRange().getStartOffset());
    }

    public String getCanonicalText() {
        PsiElement name = getNameElement();
        return name == null ? super.getCanonicalText() : name.getText();
    }
}
