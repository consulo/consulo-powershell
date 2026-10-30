// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang.parser;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter;
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.plugin.powershell.psi.impl.PowerShellFile;
import com.intellij.plugin.powershell.psi.impl.PowerShellTypesFactory;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.ast.TokenType;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.ParserDefinition.SpaceRequirements;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;

@ExtensionImpl
public final class PowerShellParserDefinition implements ParserDefinition {

    private static final IFileElementType FILE = new IFileElementType(PowerShellLanguage.INSTANCE);

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public PsiFile createFile(FileViewProvider provider) {
        return new PowerShellFile(provider);
    }

    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    public TokenSet getStringLiteralElements(LanguageVersion version) {
        return TokenSet.create(PowerShellTypes.STRING_LITERAL_EXPRESSION);
    }

    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public TokenSet getWhitespaceTokens(LanguageVersion version) {
        return TokenSet.create(TokenType.WHITE_SPACE);
    }

    public Lexer createLexer(LanguageVersion version) {
        return new PowerShellLexerAdapter();
    }

    public PsiElement createElement(ASTNode node) {
        return PowerShellTypesFactory.createElement(node);
    }

    public TokenSet getCommentTokens(LanguageVersion version) {
        return PowerShellTokenTypeSets.COMMENTS;
    }

    public PsiParser createParser(LanguageVersion version) {
        return new PowerShellParser();
    }
}
