// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.resolve;

import com.intellij.plugin.powershell.psi.PowerShellAssignmentExpression;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellConstructorDeclarationStatement;
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellMethodDeclarationStatement;
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil;
import com.intellij.plugin.powershell.psi.types.PowerShellArrayClassType;
import com.intellij.plugin.powershell.psi.types.PowerShellClassType;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class PowerShellResolveUtil {

    private PowerShellResolveUtil() {}

    public static boolean processChildDeclarations(
            PsiElement context,
            PsiScopeProcessor processor,
            ResolveState state,
            @Nullable PsiElement lastParent,
            @Nullable PsiElement place) {
        Set<PowerShellComponent> declarations = new LinkedHashSet<>();
        collectChildrenDeclarations(context, declarations, lastParent);
        for (PowerShellComponent declaration : declarations) {
            if ((place == null || place.getTextOffset() > declaration.getTextOffset())
                    && !processor.execute(declaration, state)) {
                return false;
            }
        }
        return true;
    }

    private static void collectChildrenDeclarations(
            PsiElement context, Set<PowerShellComponent> result, @Nullable PsiElement lastParent) {
        for (PsiElement child : context.getChildren()) {
            if (child == lastParent) {
                continue;
            }
            if (child instanceof PowerShellComponent component) {
                result.add(component);
            }
            else if (child instanceof PowerShellAssignmentExpression assignment) {
                result.addAll(PowerShellPsiImplUtil.getTargetVariables(assignment));
            }
            else {
                collectChildrenDeclarations(child, result, null);
            }
        }
    }

    public static boolean hasDefaultConstructor(PowerShellTypeDeclaration type) {
        return hasDefaultConstructor(type, new HashSet<>());
    }

    private static boolean hasDefaultConstructor(
            PowerShellTypeDeclaration type, Set<PowerShellTypeDeclaration> visited) {
        if (!visited.add(type)) {
            return false;
        }
        for (PowerShellMemberDeclaration member : type.getMembers()) {
            if (member instanceof PowerShellConstructorDeclarationStatement) {
                return false;
            }
        }
        PowerShellReferenceTypeElement base = type.getBaseClass();
        return base == null
                || !(base.resolve() instanceof PowerShellTypeDeclaration declaration)
                || hasDefaultConstructor(declaration, visited);
    }

    public static PowerShellResolveResult[] resolveMembers(
            PowerShellType type, @Nullable String name, boolean method) {
        if (type instanceof PowerShellArrayClassType array) {
            type = array.getComponentType();
        }
        if (!(type instanceof PowerShellClassType clazz)
                || !(clazz.resolve() instanceof PowerShellTypeDeclaration declaration)) {
            return PowerShellResolveResult.EMPTY_ARRAY;
        }
        List<PowerShellResolveResult> result = new ArrayList<>();
        collectMembers(declaration, name, method, result, new HashSet<>());
        return result.toArray(PowerShellResolveResult[]::new);
    }

    private static void collectMembers(
            PowerShellTypeDeclaration type,
            @Nullable String name,
            boolean method,
            List<PowerShellResolveResult> result,
            Set<PowerShellTypeDeclaration> visited) {
        if (name == null || !visited.add(type)) {
            return;
        }
        for (PowerShellMemberDeclaration member : type.getMembers()) {
            boolean constructor =
                    method
                            && "new".equalsIgnoreCase(name)
                            && member instanceof PowerShellConstructorDeclarationStatement;
            boolean match =
                    name.equalsIgnoreCase(member.getName())
                            && (!method || member instanceof PowerShellMethodDeclarationStatement);
            if (constructor || match) {
                result.add(new PowerShellResolveResult(member));
                if (method) {
                    return;
                }
            }
        }
        PowerShellReferenceTypeElement base = type.getBaseClass();
        if (base != null && base.resolve() instanceof PowerShellTypeDeclaration declaration) {
            collectMembers(declaration, name, method, result, visited);
        }
    }
}
