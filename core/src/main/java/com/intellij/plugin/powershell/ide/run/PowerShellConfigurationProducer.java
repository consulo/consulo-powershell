// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.run;

import com.intellij.plugin.powershell.psi.impl.PowerShellFile;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.action.ConfigurationContext;
import consulo.execution.action.RunConfigurationProducer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.util.lang.ref.SimpleReference;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ExtensionImpl
public final class PowerShellConfigurationProducer
        extends RunConfigurationProducer<PowerShellRunConfiguration> {

    public PowerShellConfigurationProducer() {
        super(PowerShellConfigurationType.getInstance());
    }

    protected boolean setupConfigurationFromContext(
            PowerShellRunConfiguration configuration,
            ConfigurationContext context,
            SimpleReference<PsiElement> source) {
        PsiFile file = scriptFile(context);
        if (file == null) {
            return false;
        }
        configuration.myScriptPath = Objects.requireNonNull(file.getVirtualFile()).getPath();
        configuration.setGeneratedName();
        source.set(file);
        return true;
    }

    public boolean isConfigurationFromContext(
            PowerShellRunConfiguration configuration, ConfigurationContext context) {
        PsiFile file = scriptFile(context);
        return file != null
                && configuration.myScriptPath.equals(
                        Objects.requireNonNull(file.getVirtualFile()).getPath());
    }

    private static @Nullable PsiFile scriptFile(ConfigurationContext context) {
        PsiElement element = context.getPsiLocation();
        PsiFile file = element == null ? null : element.getContainingFile();
        return file instanceof PowerShellFile
                        && file.getVirtualFile() != null
                        && "ps1".equalsIgnoreCase(file.getVirtualFile().getExtension())
                ? file
                : null;
    }
}
