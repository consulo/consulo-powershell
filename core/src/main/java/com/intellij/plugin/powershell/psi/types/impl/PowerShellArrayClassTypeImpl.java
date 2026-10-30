// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types.impl;

import com.intellij.plugin.powershell.psi.PowerShellArrayTypeElement;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.types.PowerShellArrayClassType;
import com.intellij.plugin.powershell.psi.types.PowerShellClassType;
import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public final class PowerShellArrayClassTypeImpl implements PowerShellArrayClassType {

    private final PowerShellArrayTypeElement myPsiElement;

    public PowerShellArrayClassTypeImpl(PowerShellArrayTypeElement value) {
        myPsiElement = value;
    }

    public <T> @Nullable T accept(PowerShellTypeVisitor<T> visitor) {
        return visitor.visitArrayClassType(this);
    }

    public PowerShellType getComponentType() {
        return myPsiElement.getReferenceTypeElement().getType();
    }

    public @Nullable PowerShellComponent resolve() {
        return getComponentType() instanceof PowerShellClassType type ? type.resolve() : null;
    }

    public String getName() {
        return Objects.toString(
                myPsiElement.getReferenceTypeElement().getReferenceName(), "<Unnamed>");
    }

    public @Nullable String getReferenceName() {
        return "[" + getName() + "]";
    }
}
