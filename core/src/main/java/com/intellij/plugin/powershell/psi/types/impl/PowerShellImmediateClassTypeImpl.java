// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types.impl;

import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.types.PowerShellImmediateClassType;
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public final class PowerShellImmediateClassTypeImpl implements PowerShellImmediateClassType {

    private final PowerShellTypeDeclaration myClass;

    public PowerShellImmediateClassTypeImpl(PowerShellTypeDeclaration value) {
        myClass = value;
    }

    public <T> @Nullable T accept(PowerShellTypeVisitor<T> visitor) {
        return visitor.visitClassType(this);
    }

    public @Nullable PowerShellComponent resolve() {
        return myClass;
    }

    public String getName() {
        return Objects.toString(myClass.getName(), "<Unnamed>");
    }
}
