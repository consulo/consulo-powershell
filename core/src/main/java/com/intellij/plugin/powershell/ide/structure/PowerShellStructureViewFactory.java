// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.structure;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellPsiElement;
import com.intellij.plugin.powershell.psi.impl.PowerShellFile;

import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.TreeBasedStructureViewBuilder;
import consulo.fileEditor.structureView.tree.Sorter;
import consulo.language.Language;
import consulo.language.editor.structureView.PsiStructureViewFactory;
import consulo.language.editor.structureView.StructureViewModelBase;
import consulo.language.psi.PsiFile;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellStructureViewFactory implements PsiStructureViewFactory {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public @Nullable StructureViewBuilder getStructureViewBuilder(PsiFile file) {
        if (!(file instanceof PowerShellFile)) {
            return null;
        }
        return new TreeBasedStructureViewBuilder() {

            public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
                return new StructureViewModelBase(
                                file,
                                editor,
                                new PowerShellStructureViewElement((PowerShellPsiElement) file))
                        .withSuitableClasses(PowerShellComponent.class, PowerShellFile.class)
                        .withSorters(Sorter.ALPHA_SORTER);
            }
        };
    }
}
