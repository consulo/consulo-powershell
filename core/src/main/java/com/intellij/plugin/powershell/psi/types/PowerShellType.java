// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.types;

import org.jspecify.annotations.Nullable;

public interface PowerShellType {

    PowerShellType UNKNOWN =
            new PowerShellType() {

                public <T> @Nullable T accept(PowerShellTypeVisitor<T> visitor) {
                    return visitor.visitType(this);
                }

                public String getName() {
                    return "<Unknown>";
                }
            };

    <T> @Nullable T accept(PowerShellTypeVisitor<T> visitor);

    String getName();
}
