// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types;

import org.jspecify.annotations.Nullable;

public abstract class PowerShellTypeVisitor<T> {

    public @Nullable T visitClassType(PowerShellClassType type) {
        return visitType(type);
    }

    public @Nullable T visitArrayClassType(PowerShellArrayClassType type) {
        return visitType(type.getComponentType());
    }

    public @Nullable T visitType(PowerShellType type) {
        return null;
    }
}
