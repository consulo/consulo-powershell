// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi.impl;

import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult;
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolver;
import com.intellij.plugin.powershell.psi.PowerShellAssignmentExpression;
import com.intellij.plugin.powershell.psi.PowerShellBlockBody;
import com.intellij.plugin.powershell.psi.PowerShellCastExpression;
import com.intellij.plugin.powershell.psi.PowerShellComponent;
import com.intellij.plugin.powershell.psi.PowerShellExpression;
import com.intellij.plugin.powershell.psi.PowerShellIdentifier;
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory;
import com.intellij.plugin.powershell.psi.PowerShellReferencePsiElement;
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.plugin.powershell.psi.PowerShellVariable;
import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.impl.PowerShellImmediateClassTypeImpl;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class PowerShellTargetVariableImpl extends PowerShellAbstractComponent
        implements PowerShellVariable, PsiPolyVariantReference {

    public PowerShellTargetVariableImpl(ASTNode node) {
        super(node);
    }

    public PowerShellType getType() {
        PowerShellComponent resolved = resolve();
        if ("this".equalsIgnoreCase(getName())
                && resolved instanceof PowerShellTypeDeclaration type) {
            return new PowerShellImmediateClassTypeImpl(type);
        }
        PowerShellVariable variable = resolved instanceof PowerShellVariable v ? v : this;
        if (variable.getContext() instanceof PowerShellCastExpression cast) {
            return PowerShellPsiImplUtil.getCastType(cast);
        }
        if (variable != this) {
            PowerShellAssignmentExpression assignment =
                    PsiTreeUtil.getParentOfType(
                            variable,
                            PowerShellAssignmentExpression.class,
                            true,
                            PowerShellBlockBody.class);
            if (assignment != null
                    && PowerShellPsiImplUtil.getRhsElement(assignment)
                            instanceof PowerShellExpression expression
                    && expression != this) {
                return expression.getType();
            }
        }
        return PowerShellType.UNKNOWN;
    }

    public @Nullable PsiElement getNameElement() {
        return getNameIdentifier();
    }

    public String getQualifiedName() {
        return Objects.toString(getScopeName(), "Variable") + ":" + getName();
    }

    public PowerShellResolveResult[] multiResolve(boolean incompleteCode) {
        return PowerShellResolver.resolve(this);
    }

    public PsiElement getElement() {
        return this;
    }

    public @Nullable PowerShellComponent resolve() {
        if ("this".equalsIgnoreCase(getName())) {
            return PsiTreeUtil.getParentOfType(this, PowerShellTypeDeclaration.class);
        }
        PowerShellResolveResult[] results = multiResolve(false);
        return results.length == 0 ? this : results[0].getElement();
    }

    public Object[] getVariants() {
        return new Object[0];
    }

    public TextRange getRangeInElement() {
        PowerShellIdentifier id = getNameIdentifier();
        return id == null
                ? TextRange.from(0, getTextLength())
                : id.getTextRange().shiftRight(-getTextRange().getStartOffset());
    }

    public String getCanonicalText() {
        return getQualifiedName();
    }

    public PsiElement handleElementRename(String name) {
        return setName(name);
    }

    public PsiElement setName(String name) {
        PowerShellIdentifier id = getNameIdentifier();
        PowerShellVariable variable =
                PowerShellPsiElementFactory.createVariableFromText(
                        getProject(), name, isBracedVariable());
        if (id != null && variable != null && variable.getNameElement() != null) {
            getNode().replaceChild(id.getNode(), variable.getNameElement().getNode());
        }
        return this;
    }

    public boolean isBracedVariable() {
        return getFirstChild() != null
                && getFirstChild().getNode().getElementType() == PowerShellTypes.BRACED_VAR_START;
    }

    public PsiElement bindToElement(PsiElement element) {
        return this;
    }

    public boolean isSoft() {
        return false;
    }

    public boolean isReferenceTo(PsiElement element) {
        return resolve() == element;
    }

    public @Nullable String getScopeName() {
        PsiElement scope = getScope();
        return scope == null ? null : scope.getText();
    }

    public @Nullable PsiElement getScope() {
        return findChildByType(PowerShellTypes.SIMPLE_ID);
    }

    public String getPrefix() {
        ASTNode prefix =
                getNode()
                        .findChildByType(
                                TokenSet.create(
                                        PowerShellTypes.DS,
                                        PowerShellTypes.AT,
                                        PowerShellTypes.BRACED_VAR_START));
        return prefix == null ? "" : prefix.getText();
    }

    public @Nullable String getSuffix() {
        ASTNode suffix = getNode().findChildByType(PowerShellTypes.RCURLY);
        return suffix == null ? null : suffix.getText();
    }

    public PowerShellReferencePsiElement getReference() {
        if ("function".equalsIgnoreCase(getScopeName())
                && !(getContext() instanceof PowerShellAssignmentExpression
                        && getContext().getFirstChild() == this)) {
            return new PowerShellCommandCallExpressionImpl(getNode()) {

                public @Nullable PsiElement getNameElement() {
                    return PowerShellTargetVariableImpl.this.getNameElement();
                }

                public TextRange getRangeInElement() {
                    return PowerShellTargetVariableImpl.this.getRangeInElement();
                }
            };
        }
        return this;
    }

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {

            public LocalizeValue getPresentableText() {
                return LocalizeValue.of(
                        getPrefix()
                                + (getScopeName() == null ? "" : getScopeName() + ":")
                                + Objects.toString(getName(), "")
                                + Objects.toString(getSuffix(), ""));
            }

            public @Nullable String getLocationString() {
                return getScopeName();
            }

            public Image getIcon() {
                return PowerShellTargetVariableImpl.this.getIcon(0);
            }
        };
    }
}
