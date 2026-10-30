// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023-2026 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import consulo.language.codeStyle.setting.CodeStyleSettingsCustomizable;
import consulo.language.codeStyle.setting.LanguageCodeStyleSettingsProvider;
import consulo.language.codeStyle.setting.LanguageCodeStyleSettingsProvider.SettingsType;

@ExtensionImpl
public final class PowerShellLanguageCodeStyleSettingsProvider
        extends LanguageCodeStyleSettingsProvider {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public String getCodeSample(SettingsType type) {
        return "function Get-Value($name) {\n"
                   + "    $value = 1 + 2\n"
                   + "    Write-Output $name, $value\n"
                   + "}\n";
    }

    public CommonCodeStyleSettings getDefaultCommonSettings() {
        CommonCodeStyleSettings settings = new CommonCodeStyleSettings(getLanguage());
        settings.initIndentOptions().INDENT_SIZE = 4;
        settings.LINE_COMMENT_AT_FIRST_COLUMN = false;
        settings.KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE = true;
        settings.RIGHT_MARGIN = 115;
        settings.SPACE_WITHIN_BRACES = true;
        settings.SPACE_BEFORE_ARRAY_INITIALIZER_LBRACE = true;
        settings.SPACE_AFTER_TYPE_CAST = false;
        settings.CATCH_ON_NEW_LINE = true;
        settings.FINALLY_ON_NEW_LINE = true;
        settings.ELSE_ON_NEW_LINE = true;
        settings.SPECIAL_ELSE_IF_TREATMENT = true;
        settings.BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE;
        settings.CLASS_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE;
        settings.METHOD_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE;
        settings.FIELD_ANNOTATION_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP;
        return settings;
    }

    public void customizeSettings(CodeStyleSettingsCustomizable consumer, SettingsType type) {
        consumer.showAllStandardOptions();
        if (type == SettingsType.SPACING_SETTINGS) {
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES",
                    PowerShellLocalize.uiSubExpressionParentheses().get(),
                    PowerShellLocalize.uiWithin().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "SPACE_BEFORE_DATA_LBRACE",
                    PowerShellLocalize.uiDataLeftBrace().get(),
                    PowerShellLocalize.uiBeforeLeftBrace().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "SPACE_BEFORE_TRAP_LBRACE",
                    PowerShellLocalize.uiTrapLeftBrace().get(),
                    PowerShellLocalize.uiBeforeLeftBrace().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "SPACE_BEFORE_COLON",
                    PowerShellLocalize.uiBeforeColon().get(),
                    PowerShellLocalize.uiOther().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "SPACE_AFTER_COLON",
                    PowerShellLocalize.uiAfterColon().get(),
                    PowerShellLocalize.uiOther().get());
        }
        if (type == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "KEEP_SIMPLE_HASH_LITERAL_IN_ONE_LINE",
                    PowerShellLocalize.uiSimpleHashLiteralsInOneLine().get(),
                    PowerShellLocalize.uiKeepWhenReformatting().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "CATCH_TYPE_LIST_WRAP",
                    PowerShellLocalize.uiCatchTypeList().get(),
                    PowerShellLocalize.uiCatchTypeList().get(),
                    new String[] {
                        PowerShellLocalize.uiDoNotWrap().get(),
                        PowerShellLocalize.uiWrapIfLong().get(),
                        PowerShellLocalize.uiWrapAlways().get(),
                        PowerShellLocalize.uiChopDownIfLong().get()
                    },
                    new int[] {
                        CommonCodeStyleSettings.DO_NOT_WRAP,
                        CommonCodeStyleSettings.WRAP_AS_NEEDED,
                        CommonCodeStyleSettings.WRAP_ALWAYS,
                        CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
                    });
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "ALIGN_MULTILINE_CATCH_TYPE_LIST",
                    PowerShellLocalize.uiAlignWhenMultiline().get(),
                    PowerShellLocalize.uiCatchTypeList().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "ATTRIBUTE_ARGUMENT_WRAP",
                    PowerShellLocalize.uiAttributeArguments().get(),
                    PowerShellLocalize.uiAttributeArguments().get(),
                    new String[] {
                        PowerShellLocalize.uiDoNotWrap().get(),
                        PowerShellLocalize.uiWrapIfLong().get(),
                        PowerShellLocalize.uiWrapAlways().get(),
                        PowerShellLocalize.uiChopDownIfLong().get()
                    },
                    new int[] {
                        CommonCodeStyleSettings.DO_NOT_WRAP,
                        CommonCodeStyleSettings.WRAP_AS_NEEDED,
                        CommonCodeStyleSettings.WRAP_ALWAYS,
                        CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
                    });
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "ALIGN_MULTILINE_ATTRIBUTE_ARGUMENT",
                    PowerShellLocalize.uiAlignWhenMultiline().get(),
                    PowerShellLocalize.uiAttributeArguments().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "PIPELINE_TAIL_WRAP",
                    PowerShellLocalize.uiPipeline().get(),
                    PowerShellLocalize.uiPipeline().get(),
                    new String[] {
                        PowerShellLocalize.uiDoNotWrap().get(),
                        PowerShellLocalize.uiWrapIfLong().get(),
                        PowerShellLocalize.uiWrapAlways().get(),
                        PowerShellLocalize.uiChopDownIfLong().get()
                    },
                    new int[] {
                        CommonCodeStyleSettings.DO_NOT_WRAP,
                        CommonCodeStyleSettings.WRAP_AS_NEEDED,
                        CommonCodeStyleSettings.WRAP_ALWAYS,
                        CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
                    });
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "ALIGN_MULTILINE_PIPELINE_STATEMENT",
                    PowerShellLocalize.uiAlignWhenMultiline().get(),
                    PowerShellLocalize.uiPipeline().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "BLOCK_PARAMETER_CLAUSE_WRAP",
                    PowerShellLocalize.uiBlockParameters().get(),
                    PowerShellLocalize.uiBlockParameters().get(),
                    new String[] {
                        PowerShellLocalize.uiDoNotWrap().get(),
                        PowerShellLocalize.uiWrapIfLong().get(),
                        PowerShellLocalize.uiWrapAlways().get(),
                        PowerShellLocalize.uiChopDownIfLong().get()
                    },
                    new int[] {
                        CommonCodeStyleSettings.DO_NOT_WRAP,
                        CommonCodeStyleSettings.WRAP_AS_NEEDED,
                        CommonCodeStyleSettings.WRAP_ALWAYS,
                        CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
                    });
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "ALIGN_MULTILINE_BLOCK_PARAMETERS",
                    PowerShellLocalize.uiAlignWhenMultiline().get(),
                    PowerShellLocalize.uiBlockParameters().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "BLOCK_PARAMETERS_LPAREN_ON_NEXT_LINE",
                    PowerShellLocalize.uiNewLineAfter().get(),
                    PowerShellLocalize.uiBlockParameters().get());
            consumer.showCustomOption(
                    PowerShellCodeStyleSettings.class,
                    "BLOCK_PARAMETERS_RPAREN_ON_NEXT_LINE",
                    PowerShellLocalize.uiPlaceOnNewLine().get(),
                    PowerShellLocalize.uiBlockParameters().get());
        }
    }
}
