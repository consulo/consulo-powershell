// SPDX-FileCopyrightText: 2017-2020 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.search;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.TargetElementUtilExtender;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiReference;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellTargetElementEvaluator implements TargetElementUtilExtender {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public @Nullable PsiElement getElementByReference(PsiReference reference, int flags) {
        return reference.resolve();
    }

    public boolean includeSelfInGotoImplementation(PsiElement element) {
        return true;
    }

    public boolean isIdentifierPart(PsiFile file, CharSequence text, int offset) {
        PsiElement element = file.findElementAt(offset);
        return element != null
                && PowerShellTokenTypeSets.IDENTIFIERS.contains(element.getNode().getElementType());
    }
}
