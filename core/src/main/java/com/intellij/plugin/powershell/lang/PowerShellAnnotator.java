// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2026 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang;

import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.COMMAND_NAME;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.COMMAND_PARAMETER;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.LABEL;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.METHOD_CALL;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.METHOD_DECLARATION;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.PROPERTY_REFERENCE;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.TYPE_NAME;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.TYPE_REFERENCE;
import static com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter.VARIABLE_NAME;

import com.intellij.plugin.powershell.localize.PowerShellLocalize;
import com.intellij.plugin.powershell.psi.PowerShellCallableDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellCommandName;
import com.intellij.plugin.powershell.psi.PowerShellCommandParameter;
import com.intellij.plugin.powershell.psi.PowerShellIdentifier;
import com.intellij.plugin.powershell.psi.PowerShellIncompleteDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellInvocationExpression;
import com.intellij.plugin.powershell.psi.PowerShellLabel;
import com.intellij.plugin.powershell.psi.PowerShellLabelReferenceExpression;
import com.intellij.plugin.powershell.psi.PowerShellMemberAccessExpression;
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement;
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil;

import consulo.colorScheme.TextAttributesKey;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.annotation.HighlightSeverity;
import consulo.language.psi.PsiElement;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

@NullMarked
public final class PowerShellAnnotator implements Annotator {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public void annotate(PsiElement element, AnnotationHolder holder) {
        if (element instanceof PowerShellCommandName) {
            String text = element.getText();
            if (text.toLowerCase(Locale.ROOT).endsWith(".exe")
                    || text.contains("-")
                    || !text.contains(".")) {
                highlight(holder, element, COMMAND_NAME);
            }
        }
        else if (element instanceof PowerShellIdentifier
                && PowerShellPsiImplUtil.isTypeDeclarationContext(element)) {
            highlight(holder, element, TYPE_NAME);
        }
        else if (element instanceof PowerShellReferenceTypeElement) {
            highlight(holder, element, TYPE_REFERENCE);
        }
        else if (element instanceof PowerShellTargetVariableExpression variable) {
            highlight(holder, variable.getScope(), VARIABLE_NAME);
            ASTNode colon = variable.getNode().findChildByType(PowerShellTypes.COLON);
            if (colon != null) {
                highlight(holder, colon.getPsi(), VARIABLE_NAME);
            }
            PowerShellIdentifier id = variable.getIdentifier();
            if (id.getFirstChild() == null
                    || !PowerShellTokenTypeSets.KEYWORDS.contains(
                            id.getFirstChild().getNode().getElementType())) {
                highlight(holder, id, VARIABLE_NAME);
            }
        }
        else if (element instanceof PowerShellMemberAccessExpression member) {
            highlight(holder, member.getReferenceIdentifier(), PROPERTY_REFERENCE);
        }
        else if (element instanceof PowerShellInvocationExpression call) {
            highlight(holder, call.getReferenceIdentifier(), METHOD_CALL);
        }
        else if (element instanceof PowerShellCallableDeclaration callable) {
            highlight(holder, callable.getNameIdentifier(), METHOD_DECLARATION);
        }
        else if (element instanceof PowerShellLabel label) {
            highlight(holder, label.getIdentifier(), LABEL);
        }
        else if (element instanceof PowerShellLabelReferenceExpression label) {
            highlight(holder, label.getIdentifier(), LABEL);
        }
        else if (element instanceof PowerShellIncompleteDeclaration) {
            holder.newAnnotation(
                            HighlightSeverity.ERROR,
                            PowerShellLocalize.uiIncompleteDeclaration().get())
                    .range(element)
                    .create();
        }
        else if (element instanceof PowerShellCommandParameter
                || element.getNode().getElementType() == PowerShellTypes.CMD_PARAMETER) {
            highlight(holder, element, COMMAND_PARAMETER);
        }
    }

    private static void highlight(
            AnnotationHolder holder, @Nullable PsiElement element, TextAttributesKey key) {
        if (element != null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element)
                    .textAttributes(key)
                    .create();
        }
    }
}
