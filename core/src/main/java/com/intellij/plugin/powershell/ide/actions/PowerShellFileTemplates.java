// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.actions;

import consulo.annotation.component.ExtensionImpl;
import consulo.fileTemplate.FileTemplateContributor;
import consulo.fileTemplate.FileTemplateRegistrator;

@ExtensionImpl
public final class PowerShellFileTemplates implements FileTemplateContributor {

    public void register(FileTemplateRegistrator registrator) {
        registrator.registerInternalTemplate("PowerShell File.ps1");
    }
}
