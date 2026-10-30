// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types.impl;

import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor;

import org.jspecify.annotations.Nullable;

public final class PowerShellObjectType implements PowerShellType {

    public <T> @Nullable T accept(PowerShellTypeVisitor<T> visitor) {
        return visitor.visitType(this);
    }

    public String getName() {
        return "Object";
    }
}
