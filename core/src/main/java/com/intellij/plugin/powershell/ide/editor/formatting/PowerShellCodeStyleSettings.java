// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import consulo.language.codeStyle.CustomCodeStyleSettings;

public final class PowerShellCodeStyleSettings extends CustomCodeStyleSettings {

    public PowerShellCodeStyleSettings(CodeStyleSettings settings) {
        super("PowerShellCodeStyleSettings", settings);
    }

    public int CATCH_TYPE_LIST_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP;

    public int ATTRIBUTE_ARGUMENT_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP;

    public int PIPELINE_TAIL_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP;

    public int BLOCK_PARAMETER_CLAUSE_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP;

    public boolean ALIGN_MULTILINE_CATCH_TYPE_LIST = false;

    public boolean ALIGN_MULTILINE_ATTRIBUTE_ARGUMENT = false;

    public boolean ALIGN_MULTILINE_PIPELINE_STATEMENT = false;

    public boolean ALIGN_MULTILINE_BLOCK_PARAMETERS = false;

    public boolean KEEP_SIMPLE_HASH_LITERAL_IN_ONE_LINE = true;

    public boolean BLOCK_PARAMETERS_LPAREN_ON_NEXT_LINE = false;

    public boolean BLOCK_PARAMETERS_RPAREN_ON_NEXT_LINE = true;

    public boolean SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES = true;

    public boolean SPACE_BEFORE_DATA_LBRACE = true;

    public boolean SPACE_BEFORE_TRAP_LBRACE = true;

    public boolean SPACE_BEFORE_COLON = false;

    public boolean SPACE_AFTER_COLON = true;
}
