// SPDX-FileCopyrightText: 2017-2019 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellCallableReference;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement;
import com.intellij.plugin.powershell.psi.PowerShellReferencePsiElement;
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellVariable;

import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;

import org.jspecify.annotations.Nullable;

public final class PowerShellResolver {

    private PowerShellResolver() {}

    public static PowerShellResolveResult[] resolve(PowerShellReferencePsiElement ref) {
        // PowerShell identifiers are case insensitive. Keep the first definition in a scope,
        // matching the original resolver's variable initialization semantics.
        PowerShellComponent[] result = {null};
        PsiTreeUtil.treeWalkUp(
                (element, state) -> {
                    if (!(element instanceof PowerShellComponent component)) {
                        return true;
                    }
                    boolean match;
                    if (ref instanceof PowerShellCallableReference) {
                        match =
                                equals(component.getName(), ref.getCanonicalText())
                                        && (!(component instanceof PowerShellVariable variable)
                                                || equals(variable.getScopeName(), "function"));
                    }
                    else if (ref instanceof PowerShellReferenceTypeElement) {
                        match =
                                component instanceof PowerShellTypeDeclaration
                                        && equals(component.getName(), ref.getCanonicalText());
                    }
                    else if (ref instanceof PowerShellQualifiedReferenceElement<?>) {
                        return true;
                    }
                    else {
                        match =
                                component instanceof PowerShellVariable variable
                                        ? equals(
                                                variable.getQualifiedName(), ref.getCanonicalText())
                                        : equals(component.getName(), ref.getCanonicalText());
                    }
                    if (match
                            && (result[0] == null
                                    || result[0].getTextOffset() > component.getTextOffset())) {
                        result[0] = component;
                    }
                    return true;
                },
                ref,
                null,
                ResolveState.initial());
        return result[0] == null
                ? PowerShellResolveResult.EMPTY_ARRAY
                : new PowerShellResolveResult[] {new PowerShellResolveResult(result[0])};
    }

    private static boolean equals(@Nullable String first, String second) {
        return first != null && first.equalsIgnoreCase(second);
    }
}
