// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import com.intellij.plugin.powershell.PowerShellFileType;
import com.intellij.plugin.powershell.psi.impl.PowerShellFile;
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil;

import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;

import org.jspecify.annotations.Nullable;

public final class PowerShellPsiElementFactory {

    private PowerShellPsiElementFactory() {}

    public static @Nullable PowerShellIdentifier createIdentifierFromText(
            Project project, String text, boolean function) {
        PowerShellFile file =
                createFile(project, (function ? "function " : "class ") + text + " {}");
        PowerShellComponent component =
                PsiTreeUtil.findChildOfType(file, PowerShellComponent.class);
        return component == null ? null : component.getNameIdentifier();
    }

    public static @Nullable PowerShellCommandName createCommandName(Project project, String text) {
        PowerShellCommandCallExpression call =
                PsiTreeUtil.findChildOfType(
                        createFile(project, text), PowerShellCommandCallExpression.class);
        return call == null ? null : call.getCommandName();
    }

    public static @Nullable PowerShellVariable createVariableFromText(
            Project project, String text, boolean braced) {
        return PsiTreeUtil.findChildOfType(
                createFile(project, braced ? "${" + text + "}" : "$" + text),
                PowerShellTargetVariableExpression.class);
    }

    public static PowerShellFile createFile(Project project, String text) {
        return (PowerShellFile)
                PsiFileFactory.getInstance(project)
                        .createFileFromText("dummy.ps1", PowerShellFileType.INSTANCE, text);
    }

    public static PowerShellExpression createExpression(Project project, String text) {
        PowerShellAssignmentExpression assignment =
                PsiTreeUtil.findChildOfType(
                        createFile(project, "$foo = " + text),
                        PowerShellAssignmentExpression.class);
        if (assignment != null
                && PowerShellPsiImplUtil.getRhsElement(assignment)
                        instanceof PowerShellExpression expression) {
            return expression;
        }
        throw new IllegalArgumentException("Cannot parse expression: " + text);
    }
}
