// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types.impl;

import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement;
import com.intellij.plugin.powershell.psi.types.PowerShellReferenceClassType;
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public final class PowerShellReferenceClassTypeImpl implements PowerShellReferenceClassType {

    private final PowerShellReferenceTypeElement myReference;

    public PowerShellReferenceClassTypeImpl(PowerShellReferenceTypeElement value) {
        myReference = value;
    }

    public <T> @Nullable T accept(PowerShellTypeVisitor<T> visitor) {
        return visitor.visitClassType(this);
    }

    public @Nullable PowerShellComponent resolve() {
        return myReference.resolve();
    }

    public @Nullable String getReferenceName() {
        return myReference.getReferenceName();
    }

    public String getName() {
        return Objects.toString(getReferenceName(), "<Unnamed>");
    }
}
