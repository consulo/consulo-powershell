// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang.lexer;

import com.intellij.plugin.powershell.lang._PowerShellLexer;

import consulo.language.lexer.FlexAdapter;

public final class PowerShellLexerAdapter extends FlexAdapter {

    public PowerShellLexerAdapter() {
        super(new _PowerShellLexer());
    }
}
