// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.structure;

import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellPsiElement;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellVariable;
import com.intellij.plugin.powershell.psi.impl.PowerShellFile;

import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.language.editor.structureView.PsiTreeElementBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.localize.LocalizeValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class PowerShellStructureViewElement extends PsiTreeElementBase<PowerShellPsiElement> {

    public PowerShellStructureViewElement(PowerShellPsiElement element) {
        super(element);
    }

    public LocalizeValue getPresentableText() {
        PowerShellPsiElement element = getElement();
        return element == null
                ? LocalizeValue.empty()
                : element.getPresentation().getPresentableText();
    }

    public Collection<StructureViewTreeElement> getChildrenBase() {
        List<StructureViewTreeElement> result = new ArrayList<>();
        PowerShellPsiElement element = getElement();
        if (element instanceof PowerShellTypeDeclaration type) {
            for (PowerShellMemberDeclaration member : type.getMembers()) {
                result.add(new PowerShellStructureViewElement(member));
            }
        }
        else if (element instanceof PowerShellFile) {
            Map<String, PowerShellComponent> collected =
                    new LinkedHashMap<String, PowerShellComponent>();
            PowerShellResolveUtil.processChildDeclarations(
                    element,
                    (child, state) -> {
                        if (child instanceof PowerShellComponent component) {
                            collected.putIfAbsent(
                                    component instanceof PowerShellVariable variable
                                            ? variable.getQualifiedName().toLowerCase(Locale.ROOT)
                                            : component.getTextOffset() + ":" + component.getName(),
                                    component);
                        }
                        return true;
                    },
                    ResolveState.initial(),
                    null,
                    null);
            List<PowerShellComponent> declarations = new ArrayList<>(collected.values());
            declarations.sort(Comparator.comparingInt(PsiElement::getTextOffset));
            for (PowerShellComponent declaration : declarations) {
                result.add(new PowerShellStructureViewElement(declaration));
            }
        }
        return result;
    }
}
