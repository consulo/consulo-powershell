// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.PowerShellFileType;
import com.intellij.plugin.powershell.icon.PowerShellIconGroup;
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil;
import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellPsiElement;

import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.fileType.FileType;

import org.jspecify.annotations.Nullable;

public final class PowerShellFile extends PsiFileBase implements PowerShellPsiElement {

    public PowerShellFile(FileViewProvider provider) {
        super(provider, PowerShellLanguage.INSTANCE);
    }

    public FileType getFileType() {
        return PowerShellFileType.INSTANCE;
    }

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {

            public LocalizeValue getPresentableText() {
                return LocalizeValue.of(getName());
            }

            public String getLocationString() {
                return getViewProvider().getVirtualFile().getPath();
            }

            public Image getIcon() {
                return PowerShellIconGroup.file();
            }
        };
    }

    public boolean processDeclarations(
            PsiScopeProcessor processor,
            ResolveState state,
            @Nullable PsiElement lastParent,
            PsiElement place) {
        return PowerShellResolveUtil.processChildDeclarations(
                this, processor, state, lastParent, place);
    }
}
