// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell;

import com.intellij.plugin.powershell.icon.PowerShellIconGroup;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;

public final class PowerShellFileType extends LanguageFileType {

    public static final PowerShellFileType INSTANCE = new PowerShellFileType();

    private PowerShellFileType() {
        super(PowerShellLanguage.INSTANCE);
    }

    public String getId() {
        return "PowerShell";
    }

    public LocalizeValue getDescription() {
        return PowerShellLocalize.uiPowershellScript();
    }

    public String getDefaultExtension() {
        return "ps1";
    }

    public Image getIcon() {
        return PowerShellIconGroup.file();
    }
}
