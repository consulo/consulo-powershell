// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.search;

import com.intellij.plugin.powershell.icon.PowerShellIconGroup;
import com.intellij.plugin.powershell.psi.PowerShellCallableDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellClassDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellEnumDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellEnumLabelDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellPropertyDeclarationStatement;
import com.intellij.plugin.powershell.psi.PowerShellVariable;

import consulo.application.AllIcons;
import consulo.language.psi.PsiElement;
import consulo.ui.image.Image;

public final class PowerShellComponentType {

    private PowerShellComponentType() {}

    public static Image iconFor(PsiElement element) {
        if (element instanceof PowerShellClassDeclaration) {
            return AllIcons.Nodes.Class;
        }
        if (element instanceof PowerShellEnumDeclaration) {
            return AllIcons.Nodes.Enum;
        }
        if (element instanceof PowerShellCallableDeclaration) {
            return AllIcons.Nodes.Method;
        }
        if (element instanceof PowerShellPropertyDeclarationStatement
                || element instanceof PowerShellEnumLabelDeclaration) {
            return AllIcons.Nodes.Field;
        }
        if (element instanceof PowerShellVariable) {
            return AllIcons.Nodes.Variable;
        }
        return PowerShellIconGroup.file();
    }
}
