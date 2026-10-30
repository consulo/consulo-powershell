// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang.resolve;

import com.intellij.plugin.powershell.psi.PowerShellExpression;
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement;
import com.intellij.plugin.powershell.psi.PowerShellTypeLiteralExpression;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

public final class PowerShellTypeUtil {

    private PowerShellTypeUtil() {}

    public static PowerShellType inferExpressionType(PowerShellExpression expression) {
        if (expression instanceof PowerShellTypeLiteralExpression literal
                && literal.getTypeElement() instanceof PowerShellReferenceTypeElement type) {
            return type.getType();
        }
        return PowerShellType.UNKNOWN;
    }
}
