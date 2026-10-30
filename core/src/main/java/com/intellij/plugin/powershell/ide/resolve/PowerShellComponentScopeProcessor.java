// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class PowerShellComponentScopeProcessor implements PsiScopeProcessor {

    private final List<PowerShellComponent> myComponents = new ArrayList<>();

    private final Map<String, PowerShellTargetVariableExpression> myVariables =
            new LinkedHashMap<>();

    public boolean execute(PsiElement element, ResolveState state) {
        if (element instanceof PowerShellTargetVariableExpression variable) {
            String name = variable.getQualifiedName().toLowerCase(Locale.ROOT);
            PowerShellTargetVariableExpression previous = myVariables.get(name);
            if (previous == null || previous.getTextOffset() > variable.getTextOffset()) {
                myVariables.put(name, variable);
            }
            else if (previous.getContainingFile() != variable.getContainingFile()) {
                throw new AssertionError("Elements are defined in different files");
            }
        }
        else if (element instanceof PowerShellComponent component) {
            myComponents.add(component);
        }
        return true;
    }

    public List<PowerShellComponent> getResult() {
        List<PowerShellComponent> result = new ArrayList<>(myComponents);
        result.addAll(myVariables.values());
        return result;
    }
}
