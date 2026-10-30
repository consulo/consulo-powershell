// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellPsiElement;

import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;

import org.jspecify.annotations.Nullable;

public class PowerShellPsiElementImpl extends ASTWrapperPsiElement implements PowerShellPsiElement {

    public PowerShellPsiElementImpl(ASTNode node) {
        super(node);
    }

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {

            public LocalizeValue getPresentableText() {
                return LocalizeValue.of(getText());
            }

            public @Nullable String getLocationString() {
                return null;
            }

            public @Nullable Image getIcon() {
                return null;
            }
        };
    }

    public boolean processDeclarations(
            PsiScopeProcessor processor,
            ResolveState state,
            @Nullable PsiElement lastParent,
            PsiElement place) {
        for (PsiElement child : getChildren()) {
            if (child == lastParent) {
                continue;
            }
            if (child instanceof PowerShellComponent) {
                if (place.getTextOffset() > child.getTextOffset()
                        && !processor.execute(child, state)) {
                    return false;
                }
            }
            else if (!child.processDeclarations(processor, state, lastParent, place)) {
                return false;
            }
        }
        return true;
    }
}
