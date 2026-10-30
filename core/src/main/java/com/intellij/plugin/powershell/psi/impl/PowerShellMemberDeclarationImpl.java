// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellBlockBody;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;

import consulo.language.ast.ASTNode;

import org.jspecify.annotations.Nullable;

public abstract class PowerShellMemberDeclarationImpl extends PowerShellAbstractComponent
        implements PowerShellMemberDeclaration {

    public PowerShellMemberDeclarationImpl(ASTNode node) {
        super(node);
    }

    public @Nullable PowerShellTypeDeclaration getContainingClass() {
        return getContext() instanceof PowerShellBlockBody body
                        && body.getContext() instanceof PowerShellTypeDeclaration type
                ? type
                : null;
    }
}
