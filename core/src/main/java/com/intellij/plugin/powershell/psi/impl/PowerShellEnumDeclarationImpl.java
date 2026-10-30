// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellBlockBody;
import com.intellij.plugin.powershell.psi.PowerShellEnumDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement;

import consulo.language.ast.ASTNode;
import consulo.language.psi.util.PsiTreeUtil;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PowerShellEnumDeclarationImpl extends PowerShellAbstractComponent
        implements PowerShellEnumDeclaration {

    public PowerShellEnumDeclarationImpl(ASTNode node) {
        super(node);
    }

    public @Nullable PowerShellReferenceTypeElement getBaseClass() {
        return findChildByClass(PowerShellReferenceTypeElement.class);
    }

    public List<PowerShellMemberDeclaration> getMembers() {
        PowerShellBlockBody body = findChildByClass(PowerShellBlockBody.class);
        return body == null
                ? List.of()
                : new ArrayList<>(
                        PsiTreeUtil.findChildrenOfType(body, PowerShellMemberDeclaration.class));
    }
}
