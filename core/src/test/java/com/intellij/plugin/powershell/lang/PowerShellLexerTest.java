// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang;

import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PowerShellLexerTest {

    private static void check(String input, String expected) {
        PowerShellLexerAdapter lexer = new PowerShellLexerAdapter();
        lexer.start(input);
        StringBuilder actual = new StringBuilder();
        int end = 0;
        while (lexer.getTokenType() != null) {
            Assertions.assertEquals(end, lexer.getTokenStart(), "Tokens must cover the input");
            Assertions.assertTrue(
                    lexer.getTokenEnd() > lexer.getTokenStart(), "Lexer must advance");
            if (!actual.isEmpty()) {
                actual.append('\n');
            }
            actual.append(lexer.getTokenType())
                    .append(" ('")
                    .append(
                            input.substring(lexer.getTokenStart(), lexer.getTokenEnd())
                                    .replace("\n", "\\n")
                                    .replace("\r", "\\r")
                                    .replace("\t", "\\t"))
                    .append("')");
            end = lexer.getTokenEnd();
            lexer.advance();
        }
        Assertions.assertEquals(input.length(), end);
        Assertions.assertEquals(expected, actual.toString(), input);
    }

    @Test
    public void case0() {
        check("hello#world", "SIMPLE_ID ('hello#world')");
    }

    @Test
    public void case1() {
        check("hello #world", "SIMPLE_ID ('hello')\nWHITE_SPACE (' ')\nCOMMENT ('#world')");
    }

    @Test
    public void case2() {
        check(" ", "WHITE_SPACE (' ')");
    }

    @Test
    public void case3() {
        check("`\n", "WHITE_SPACE ('`\\n')");
    }

    @Test
    public void case4() {
        check("Get-Power", "GENERIC_ID_PART ('Get-Power')");
    }

    @Test
    public void case5() {
        check("Get-Power2", "GENERIC_ID_PART ('Get-Power')\nDEC_INTEGER ('2')");
    }

    @Test
    public void case6() {
        check("Get-Power!2", "GENERIC_ID_PART ('Get-Power')\n! ('!')\nDEC_INTEGER ('2')");
    }

    @Test
    public void case7() {
        check(
                "echoargs.exe --% \"%path%\"",
                "SIMPLE_ID ('echoargs')\n"
                    + ". ('.')\n"
                    + "SIMPLE_ID ('exe')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "--% ('--%')\n"
                    + "VERBATIM_ARG_INPUT (' \"%path%\"')");
    }

    @Test
    public void case8() {
        check("123", "DEC_INTEGER ('123')");
    }

    @Test
    public void case9() {
        check("200000000000", "DEC_INTEGER ('200000000000')");
    }

    @Test
    public void case10() {
        check("123l", "DEC_INTEGER ('123l')");
    }

    @Test
    public void case11() {
        check("0x200000000000", "HEX_INTEGER ('0x200000000000')");
    }

    @Test
    public void case12() {
        check("123d", "DEC_INTEGER ('123d')");
    }

    @Test
    public void case13() {
        check("0x2001l", "HEX_INTEGER ('0x2001l')");
    }

    @Test
    public void case14() {
        check("123L", "DEC_INTEGER ('123L')");
    }

    @Test
    public void case15() {
        check("0x2001l", "HEX_INTEGER ('0x2001l')");
    }

    @Test
    public void case16() {
        check("123D", "DEC_INTEGER ('123D')");
    }

    @Test
    public void case17() {
        check("0x2002L", "HEX_INTEGER ('0x2002L')");
    }

    @Test
    public void case18() {
        check("0x2003Lkb", "HEX_INTEGER ('0x2003Lkb')");
    }

    @Test
    public void case19() {
        check("0x2003kb", "HEX_INTEGER ('0x2003kb')");
    }

    @Test
    public void case20() {
        check("0x2003Kb", "HEX_INTEGER ('0x2003Kb')");
    }

    @Test
    public void case21() {
        check("0x2003Gb", "HEX_INTEGER ('0x2003Gb')");
    }

    @Test
    public void case22() {
        check("0x2003pB", "HEX_INTEGER ('0x2003pB')");
    }

    @Test
    public void case23() {
        check("0xfff81", "HEX_INTEGER ('0xfff81')");
    }

    @Test
    public void case24() {
        check("0xFFf81", "HEX_INTEGER ('0xFFf81')");
    }

    @Test
    public void case25() {
        check("+123", "+ ('+')\nDEC_INTEGER ('123')");
    }

    @Test
    public void case26() {
        check("-123", "DASH ('-')\nDEC_INTEGER ('123')");
    }

    @Test
    public void case27() {
        check("1.", "REAL_NUM ('1.')");
    }

    @Test
    public void case28() {
        check("1.23", "REAL_NUM ('1.23')");
    }

    @Test
    public void case29() {
        check(".45e35", "REAL_NUM ('.45e35')");
    }

    @Test
    public void case30() {
        check("32.e+12", "REAL_NUM ('32.e+12')");
    }

    @Test
    public void case31() {
        check("1.2345e-3d", "REAL_NUM ('1.2345e-3d')");
    }

    @Test
    public void case32() {
        check("123.456E-231", "REAL_NUM ('123.456E-231')");
    }

    @Test
    public void case33() {
        check("1.20d", "REAL_NUM ('1.20d')");
    }

    @Test
    public void case34() {
        check("1.23450e1d", "REAL_NUM ('1.23450e1d')");
    }

    @Test
    public void case35() {
        check("$totalCost", "$ ('$')\nSIMPLE_ID ('totalCost')");
    }

    @Test
    public void case36() {
        check("$итог", "$ ('$')\nSIMPLE_ID ('итог')");
    }

    @Test
    public void case37() {
        check("$総計", "$ ('$')\nSIMPLE_ID ('総計')");
    }

    @Test
    public void case38() {
        check("$values[2]", "$ ('$')\nSIMPLE_ID ('values')\n[ ('[')\nDEC_INTEGER ('2')\n] (']')");
    }

    @Test
    public void case39() {
        check(
                "$function: F",
                "$ ('$')\nSIMPLE_ID ('function')\n: (':')\nWHITE_SPACE (' ')\nSIMPLE_ID ('F')");
    }

    @Test
    public void case40() {
        check(
                "$Function: F",
                "$ ('$')\nSIMPLE_ID ('Function')\n: (':')\nWHITE_SPACE (' ')\nSIMPLE_ID ('F')");
    }

    @Test
    public void case41() {
        check(
                "$global: values",
                "$ ('$')\nSIMPLE_ID ('global')\n: (':')\nWHITE_SPACE (' ')\nSIMPLE_ID ('values')");
    }

    @Test
    public void case42() {
        check(
                "$using: values",
                "$ ('$')\nSIMPLE_ID ('using')\n: (':')\nWHITE_SPACE (' ')\nSIMPLE_ID ('values')");
    }

    @Test
    public void case43() {
        check("$Variable:Count", "$ ('$')\nSIMPLE_ID ('Variable')\n: (':')\nSIMPLE_ID ('Count')");
    }

    @Test
    public void case44() {
        check(
                "$Env: Path",
                "$ ('$')\nSIMPLE_ID ('Env')\n: (':')\nWHITE_SPACE (' ')\nSIMPLE_ID ('Path')");
    }

    @Test
    public void case45() {
        check(
                "${Name with`twhite space and `{punctuation`}}",
                "${ ('${')\nBRACED_ID ('Name with`twhite space and `{punctuation`}')\n} ('}')");
    }

    @Test
    public void case46() {
        check(
                "${E:\\File.txt}",
                "${ ('${')\nSIMPLE_ID ('E')\n: (':')\nBRACED_ID ('\\File.txt')\n} ('}')");
    }

    @Test
    public void case47() {
        check(
                "$function: var_name",
                "$ ('$')\n"
                    + "SIMPLE_ID ('function')\n"
                    + ": (':')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "SIMPLE_ID ('var_name')");
    }

    @Test
    public void case48() {
        check(
                "$local: var_name",
                "$ ('$')\nSIMPLE_ID ('local')\n: (':')\nWHITE_SPACE (' ')\nSIMPLE_ID ('var_name')");
    }

    @Test
    public void case49() {
        check(
                "$my_scope:var_name",
                "$ ('$')\nSIMPLE_ID ('my_scope')\n: (':')\nSIMPLE_ID ('var_name')");
    }

    @Test
    public void case50() {
        check(
                "${my_scope: var_/'name}",
                "${ ('${')\nSIMPLE_ID ('my_scope')\n: (':')\nBRACED_ID (' var_/'name')\n} ('}')");
    }

    @Test
    public void case51() {
        check(
                "${E: \\File.txt}",
                "${ ('${')\nSIMPLE_ID ('E')\n: (':')\nBRACED_ID (' \\File.txt')\n} ('}')");
    }

    @Test
    public void case52() {
        check(
                "${my_scope: File.txt}",
                "${ ('${')\nSIMPLE_ID ('my_scope')\n: (':')\nBRACED_ID (' File.txt')\n} ('}')");
    }

    @Test
    public void case53() {
        check(
                "${not a_scope: File.txt}",
                "${ ('${')\nBRACED_ID ('not a_scope: File.txt')\n} ('}')");
    }

    @Test
    public void case54() {
        check(
                "${not_a_scope : File.txt}",
                "${ ('${')\nBRACED_ID ('not_a_scope : File.txt')\n} ('}')");
    }

    @Test
    public void case55() {
        check(
                "${a_scope:File.txt}",
                "${ ('${')\nSIMPLE_ID ('a_scope')\n: (':')\nBRACED_ID ('File.txt')\n} ('}')");
    }

    @Test
    public void case56() {
        check(
                "\"Hello World!\"",
                "DQ_OPEN ('\"')\nEXPANDABLE_STRING_PART ('Hello World!')\nDQ_CLOSE ('\"')");
    }

    @Test
    public void case57() {
        check(
                "“Hello World!\"",
                "DQ_OPEN ('“')\nEXPANDABLE_STRING_PART ('Hello World!')\nDQ_CLOSE ('\"')");
    }

    @Test
    public void case58() {
        check("\"\"", "DQ_OPEN ('\"')\nDQ_CLOSE ('\"')");
    }

    @Test
    public void case59() {
        check(
                "\"Hello World!”",
                "DQ_OPEN ('\"')\nEXPANDABLE_STRING_PART ('Hello World!')\nDQ_CLOSE ('”')");
    }

    @Test
    public void case60() {
        check(
                "„Hello World!„",
                "DQ_OPEN ('„')\nEXPANDABLE_STRING_PART ('Hello World!')\nDQ_CLOSE ('„')");
    }

    @Test
    public void case61() {
        check(
                "\"I said, \"\"Hello\"\".\"",
                "DQ_OPEN ('\"')\n"
                    + "EXPANDABLE_STRING_PART ('I said, \"\"Hello\"\".')\n"
                    + "DQ_CLOSE ('\"')");
    }

    @Test
    public void case62() {
        check(
                "\"I said, „„Hello„„.\"",
                "DQ_OPEN ('\"')\nEXPANDABLE_STRING_PART ('I said, „„Hello„„.')\nDQ_CLOSE ('\"')");
    }

    @Test
    public void case63() {
        check(
                "\"I said, 'Hello'\"",
                "DQ_OPEN ('\"')\nEXPANDABLE_STRING_PART ('I said, 'Hello'')\nDQ_CLOSE ('\"')");
    }

    @Test
    public void case64() {
        check(
                "\"column1`tcolumn2`nsecond line, `\"Hello`\", ```Q`5`!\"",
                "DQ_OPEN ('\"')\n"
                    + "EXPANDABLE_STRING_PART ('column1`tcolumn2`nsecond line, `\"Hello`\","
                    + " ```Q`5`!')\n"
                    + "DQ_CLOSE ('\"')");
    }

    @Test
    public void case65() {
        check(
                "\">$a.Length<\"",
                "DQ_OPEN ('\"')\n"
                    + "EXPANDABLE_STRING_PART ('>')\n"
                    + "$ ('$')\n"
                    + "SIMPLE_ID ('a')\n"
                    + "EXPANDABLE_STRING_PART ('.Length<')\n"
                    + "DQ_CLOSE ('\"')");
    }

    @Test
    public void case66() {
        check(
                "\"${Lengt}\"",
                "DQ_OPEN ('\"')\n${ ('${')\nBRACED_ID ('Lengt')\n} ('}')\nDQ_CLOSE ('\"')");
    }

    @Test
    public void case67() {
        check(
                "\"lonely dollar $ is here$\"",
                "DQ_OPEN ('\"')\n"
                    + "EXPANDABLE_STRING_PART ('lonely dollar ')\n"
                    + "EXPANDABLE_STRING_PART ('$')\n"
                    + "EXPANDABLE_STRING_PART (' is here')\n"
                    + "EXPANDABLE_STRING_PART ('$')\n"
                    + "DQ_CLOSE ('\"')");
    }

    @Test
    public void case68() {
        check("'Hello World!'", "VERBATIM_STRING (''Hello World!'')");
    }

    @Test
    public void case69() {
        check("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')");
    }

    @Test
    public void case70() {
        check("'What‘'s the time?'", "VERBATIM_STRING (''What‘'s the time?'')");
    }

    @Test
    public void case71() {
        check("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')");
    }

    @Test
    public void case72() {
        check("‚Hello, World‚", "VERBATIM_STRING ('‚Hello, World‚')");
    }

    @Test
    public void case73() {
        check("‛Hello, World‛", "VERBATIM_STRING ('‛Hello, World‛')");
    }

    @Test
    public void case74() {
        check("'What''s the time?'", "VERBATIM_STRING (''What''s the time?'')");
    }

    @Test
    public void case75() {
        check(
                "@\"\n\"@",
                "EXPANDABLE_HERE_STRING_START ('@\"\\n')\nEXPANDABLE_HERE_STRING_END ('\"@')");
    }

    @Test
    public void case76() {
        check(
                "@„\n„@",
                "EXPANDABLE_HERE_STRING_START ('@„\\n')\nEXPANDABLE_HERE_STRING_END ('„@')");
    }

    @Test
    public void case77() {
        check(
                "@\"\nline 1\n\"@",
                "EXPANDABLE_HERE_STRING_START ('@\"\\n"
                    + "')\n"
                    + "EXPANDABLE_STRING_PART ('line 1\\n"
                    + "')\n"
                    + "EXPANDABLE_HERE_STRING_END ('\"@')");
    }

    @Test
    public void case78() {
        check("@'\n'@", "VERBATIM_HERE_STRING ('@'\\n'@')");
    }

    @Test
    public void case79() {
        check("@‚\n‚@", "VERBATIM_HERE_STRING ('@‚\\n‚@')");
    }

    @Test
    public void case80() {
        check("@‘\n'@", "VERBATIM_HERE_STRING ('@‘\\n'@')");
    }

    @Test
    public void case81() {
        check("@‛\n‛@", "VERBATIM_HERE_STRING ('@‛\\n‛@')");
    }

    @Test
    public void case82() {
        check("@'\nline 1\n'@", "VERBATIM_HERE_STRING ('@'\\nline 1\\n'@')");
    }

    @Test
    public void case83() {
        check(
                "-3\n–3\n—3\n―3",
                "DASH ('-')\n"
                    + "DEC_INTEGER ('3')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "DASH ('–')\n"
                    + "DEC_INTEGER ('3')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "DASH ('—')\n"
                    + "DEC_INTEGER ('3')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "DASH ('―')\n"
                    + "DEC_INTEGER ('3')");
    }

    @Test
    public void case84() {
        check(
                "$h1[$true]",
                "$ ('$')\nSIMPLE_ID ('h1')\n[ ('[')\n$ ('$')\nSIMPLE_ID ('true')\n] (']')");
    }

    @Test
    public void case85() {
        check("$h1[20.5]", "$ ('$')\nSIMPLE_ID ('h1')\n[ ('[')\nREAL_NUM ('20.5')\n] (']')");
    }

    @Test
    public void case86() {
        check(
                "$h1[$my_var]",
                "$ ('$')\nSIMPLE_ID ('h1')\n[ ('[')\n$ ('$')\nSIMPLE_ID ('my_var')\n] (']')");
    }

    @Test
    public void case87() {
        check("$j-le100", "$ ('$')\nSIMPLE_ID ('j')\nOP_C ('-le')\nDEC_INTEGER ('100')");
    }

    @Test
    public void case88() {
        check("$j-gt100", "$ ('$')\nSIMPLE_ID ('j')\nOP_C ('-gt')\nDEC_INTEGER ('100')");
    }

    @Test
    public void case89() {
        check(
                "$j-ccontains100",
                "$ ('$')\nSIMPLE_ID ('j')\nOP_C ('-ccontains')\nDEC_INTEGER ('100')");
    }

    @Test
    public void case90() {
        check("$_-eq$null", "$ ('$')\nSIMPLE_ID ('_')\nOP_C ('-eq')\n$ ('$')\nSIMPLE_ID ('null')");
    }

    @Test
    public void case91() {
        check(
                "$i = 1",
                "$ ('$')\n"
                    + "SIMPLE_ID ('i')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "= ('=')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "DEC_INTEGER ('1')");
    }

    @Test
    public void case92() {
        check(
                "$i += 1",
                "$ ('$')\n"
                    + "SIMPLE_ID ('i')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "EQ_PLUS ('+=')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "DEC_INTEGER ('1')");
    }

    @Test
    public void case93() {
        check(
                "$i -= 1",
                "$ ('$')\n"
                    + "SIMPLE_ID ('i')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "EQ_DASH ('-=')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "DEC_INTEGER ('1')");
    }

    @Test
    public void case94() {
        check(
                "$i *= 1",
                "$ ('$')\n"
                    + "SIMPLE_ID ('i')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "EQ_STAR ('*=')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "DEC_INTEGER ('1')");
    }

    @Test
    public void case95() {
        check(
                "$i /= 1",
                "$ ('$')\n"
                    + "SIMPLE_ID ('i')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "EQ_DIV ('/=')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "DEC_INTEGER ('1')");
    }

    @Test
    public void case96() {
        check(
                "$i %= 1",
                "$ ('$')\n"
                    + "SIMPLE_ID ('i')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "EQ_PERS ('%=')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "DEC_INTEGER ('1')");
    }

    @Test
    public void case97() {
        check(
                "function Single-Line-Definition\n{\n}\n",
                "function ('function')\n"
                    + "WHITE_SPACE (' ')\n"
                    + "GENERIC_ID_PART ('Single-Line-Definition')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "{ ('{')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "} ('}')\n"
                    + "NLS ('\\n"
                    + "')");
    }

    @Test
    public void case98() {
        check(
                "function\nMulti-Line-Definition\n{\n}\n",
                "function ('function')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "GENERIC_ID_PART ('Multi-Line-Definition')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "{ ('{')\n"
                    + "NLS ('\\n"
                    + "')\n"
                    + "} ('}')\n"
                    + "NLS ('\\n"
                    + "')");
    }
}
