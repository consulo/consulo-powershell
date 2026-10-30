// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2025 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.Configurable;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CustomCodeStyleSettings;
import consulo.language.codeStyle.setting.CodeStyleSettingsProvider;
import consulo.language.codeStyle.ui.setting.CodeStyleAbstractConfigurable;
import consulo.language.codeStyle.ui.setting.CodeStyleAbstractPanel;
import consulo.language.codeStyle.ui.setting.TabbedLanguageCodeStylePanel;
import consulo.localize.LocalizeValue;

@ExtensionImpl
public final class PowerShellCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
        return new PowerShellCodeStyleSettings(settings);
    }

    public LocalizeValue getConfigurableDisplayName() {
        return PowerShellLocalize.uiPowershell();
    }

    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings original) {
        return new CodeStyleAbstractConfigurable(
                settings, original, PowerShellLocalize.uiPowershell()) {

            protected CodeStyleAbstractPanel createPanel(CodeStyleSettings current) {
                return new TabbedLanguageCodeStylePanel(
                        PowerShellLanguage.INSTANCE, current, original) {};
            }
        };
    }
}
