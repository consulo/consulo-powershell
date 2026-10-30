// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang.parser;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;

import consulo.language.ast.IElementType;

public final class PowerShellElementType extends IElementType {

    public PowerShellElementType(String name) {
        super(name, PowerShellLanguage.INSTANCE);
    }
}
