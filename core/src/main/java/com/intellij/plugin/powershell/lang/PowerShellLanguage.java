// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang;

import consulo.language.Language;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PowerShellLanguage extends Language {

    public static final PowerShellLanguage INSTANCE = new PowerShellLanguage();

    private PowerShellLanguage() {
        super("PowerShell");
    }
}
