// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.ide.search.PowerShellComponentType;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellFunctionStatement;
import com.intellij.plugin.powershell.psi.PowerShellIdentifier;
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory;

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public abstract class PowerShellAbstractComponent extends PowerShellPsiElementImpl
        implements PowerShellComponent {

    public PowerShellAbstractComponent(ASTNode node) {
        super(node);
    }

    public @Nullable String getName() {
        PowerShellIdentifier id = getNameIdentifier();
        return id == null ? null : id.getText();
    }

    public int getTextOffset() {
        PowerShellIdentifier id = getNameIdentifier();
        return id == null ? super.getTextOffset() : id.getTextOffset();
    }

    public @Nullable PowerShellIdentifier getNameIdentifier() {
        return findChildByClass(PowerShellIdentifier.class);
    }

    public PsiElement setName(String name) {
        PowerShellIdentifier id = getNameIdentifier();
        PowerShellIdentifier replacement =
                PowerShellPsiElementFactory.createIdentifierFromText(
                        getProject(), name, this instanceof PowerShellFunctionStatement);
        if (id != null && replacement != null) {
            getNode().replaceChild(id.getNode(), replacement.getNode());
        }
        return this;
    }

    public Image getIcon(int flags) {
        return PowerShellComponentType.iconFor(this);
    }

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {

            public LocalizeValue getPresentableText() {
                return LocalizeValue.of(Objects.toString(getName(), ""));
            }

            public @Nullable String getLocationString() {
                return null;
            }

            public Image getIcon() {
                return PowerShellAbstractComponent.this.getIcon(0);
            }
        };
    }
}
