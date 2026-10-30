// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellComponent;

import consulo.language.psi.ResolveResult;

public final class PowerShellResolveResult implements ResolveResult {

    public static final PowerShellResolveResult[] EMPTY_ARRAY = new PowerShellResolveResult[0];

    private final PowerShellComponent myElement;

    public PowerShellResolveResult(PowerShellComponent element) {
        myElement = element;
    }

    public PowerShellComponent getElement() {
        return myElement;
    }

    public boolean isValidResult() {
        return true;
    }
}
