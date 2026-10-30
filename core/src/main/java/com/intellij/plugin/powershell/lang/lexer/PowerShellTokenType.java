// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang.lexer;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;

import consulo.language.ast.IElementType;

public final class PowerShellTokenType extends IElementType {

    public PowerShellTokenType(String name) {
        super(name, PowerShellLanguage.INSTANCE);
    }
}
