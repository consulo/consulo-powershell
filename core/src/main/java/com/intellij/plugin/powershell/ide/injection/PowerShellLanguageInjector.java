// SPDX-FileCopyrightText: 2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2025 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.injection;

import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression;
import com.intellij.plugin.powershell.psi.PowerShellTypes;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.TokenSet;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.inject.MultiHostInjector;
import consulo.language.inject.MultiHostRegistrar;
import consulo.language.inject.advanced.BaseInjection;
import consulo.language.inject.advanced.InjectedLanguage;
import consulo.language.inject.advanced.InjectorUtils;
import consulo.language.inject.advanced.LanguageInjectionSupport;
import consulo.language.inject.advanced.TemporaryPlacesRegistry;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.util.lang.Trinity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ExtensionImpl(id = "powershell", order = "first, before temporaryPlaces")
public final class PowerShellLanguageInjector implements MultiHostInjector {

    public Class<? extends PsiElement> getElementClass() {
        return PowerShellStringLiteralExpression.class;
    }

    public void injectLanguages(MultiHostRegistrar registrar, PsiElement context) {
        if (!(context instanceof PowerShellStringLiteralExpression literal)) {
            return;
        }
        PsiFile file = context.getContainingFile();
        InjectedLanguage temporary =
                TemporaryPlacesRegistry.getInstance(context.getProject())
                        .getLanguageFor(literal, file);
        if (temporary == null) {
            return;
        }
        LanguageInjectionSupport support = InjectorUtils.findInjectionSupport("PowerShell");
        if (support == null) {
            return;
        }
        BaseInjection injection = new BaseInjection("PowerShell");
        injection.setInjectedLanguageId(temporary.getID());
        injection.setPrefix(temporary.getPrefix());
        injection.setSuffix(temporary.getSuffix());
        if (!literal.hasSubstitutions()) {
            InjectorUtils.registerInjectionSimple(literal, injection, support, registrar);
            return;
        }
        Language language = InjectorUtils.getLanguageByString(injection.getInjectedLanguageId());
        if (language == null) {
            return;
        }
        TokenSet strings =
                TokenSet.create(
                        PowerShellTypes.EXPANDABLE_STRING_PART,
                        PowerShellTypes.EXPANDABLE_HERE_STRING_PART);
        TokenSet substitutions =
                TokenSet.create(
                        PowerShellTypes.SUB_EXPRESSION, PowerShellTypes.TARGET_VARIABLE_EXPRESSION);
        ASTNode[] children = literal.getNode().getChildren(TokenSet.orSet(strings, substitutions));
        List<Trinity<PsiLanguageInjectionHost, InjectedLanguage, TextRange>> ranges =
                new ArrayList<>();
        String prefix = injection.getPrefix();
        for (int i = 0; i < children.length; i++) {
            ASTNode child = children[i];
            int start = child.getStartOffset() - literal.getTextRange().getStartOffset();
            boolean expression = substitutions.contains(child.getElementType());
            if (expression) {
                if (!prefix.isEmpty() || i == 0) {
                    addRange(ranges, literal, injection, TextRange.from(start, 0), prefix, "");
                }
                prefix = "missingValue";
            }
            else {
                while (i + 1 < children.length
                        && strings.contains(children[i + 1].getElementType())) {
                    i++;
                }
                int end =
                        children[i].getStartOffset()
                                - literal.getTextRange().getStartOffset()
                                + children[i].getTextLength();
                addRange(
                        ranges,
                        literal,
                        injection,
                        new TextRange(start, end),
                        prefix,
                        i == children.length - 1 ? injection.getSuffix() : "");
                prefix = "";
            }
        }
        if (children.length > 0 && !prefix.isEmpty()) {
            ASTNode last = children[children.length - 1];
            int end =
                    last.getStartOffset()
                            - literal.getTextRange().getStartOffset()
                            + last.getTextLength();
            addRange(
                    ranges,
                    literal,
                    injection,
                    TextRange.from(end, 0),
                    prefix,
                    injection.getSuffix());
        }
        InjectorUtils.registerInjection(language, ranges, file, registrar);
        InjectorUtils.registerSupport(support, false, literal, language);
        InjectorUtils.putInjectedFileUserData(
                literal, language, InjectedLanguageManager.FRANKENSTEIN_INJECTION, true);
    }

    private static void addRange(
            List<Trinity<PsiLanguageInjectionHost, InjectedLanguage, TextRange>> ranges,
            PowerShellStringLiteralExpression literal,
            BaseInjection injection,
            TextRange range,
            String prefix,
            String suffix) {
        ranges.add(
                Trinity.create(
                        literal,
                        Objects.requireNonNull(
                                InjectedLanguage.create(
                                        injection.getInjectedLanguageId(), prefix, suffix, true)),
                        range));
    }
}
