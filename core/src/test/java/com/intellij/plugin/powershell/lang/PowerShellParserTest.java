// SPDX-FileCopyrightText: 2023-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang;

import com.intellij.plugin.powershell.PowerShellFileType;

import consulo.language.file.LanguageFileType;
import consulo.test.junit.impl.language.SimpleParsingTest;
import consulo.test.junit.impl.language.SimpleParsingTest.Context;

import org.junit.jupiter.api.Test;

public class PowerShellParserTest extends SimpleParsingTest<Void> {

    public PowerShellParserTest() {
        super("/testData/parser", "ps1");
    }

    protected LanguageFileType getFileType(Context context, Void unused) {
        return PowerShellFileType.INSTANCE;
    }

    protected boolean includeRanges() {
        return true;
    }

    protected boolean skipSpaces() {
        return false;
    }

    @Test
    public void testCallInvocationOperator(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testVerbatimCommandArgument(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testPathExpression(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testCommandCallExpression(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testStdCmdlets(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testMemberElementAccess(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testTypeMemberAccess(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testOperators(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testNullCoalesceOperator(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testLabeledStatement(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testStatements(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testFunctionMultilineDefinition(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testDesiredSateConfiguration(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testArrayStatement(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testAttributes(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testClosures(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testModules(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testHashTables(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testExpandableString(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testVerbatimString(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testTypeLiteral(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testClassDeclaration(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testEnumDeclaration(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testParamBlock(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testComment(Context context) throws Exception {
        doTest(context, null);
    }
}
