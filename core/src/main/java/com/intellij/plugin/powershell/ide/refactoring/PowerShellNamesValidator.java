// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.refactoring;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.refactoring.NamesValidator;
import consulo.project.Project;

@ExtensionImpl
public final class PowerShellNamesValidator implements NamesValidator {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public boolean isKeyword(String name, Project project) {
        return PowerShellTokenTypeSets.KEYWORDS.contains(PowerShellNameUtils.tokenType(name, 0));
    }

    public boolean isIdentifier(String name, Project project) {
        return PowerShellTokenTypeSets.IDENTIFIERS.contains(PowerShellNameUtils.tokenType(name, 0));
    }
}
