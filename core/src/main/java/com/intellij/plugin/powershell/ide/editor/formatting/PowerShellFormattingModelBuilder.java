// SPDX-FileCopyrightText: 2017-2021 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.formatting;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenType;
import consulo.language.codeStyle.FormatterUtil;
import consulo.language.codeStyle.FormattingContext;
import consulo.language.codeStyle.FormattingDocumentModel;
import consulo.language.codeStyle.FormattingModel;
import consulo.language.codeStyle.FormattingModelBuilder;
import consulo.language.codeStyle.Indent;
import consulo.language.codeStyle.PsiBasedFormattingModel;
import consulo.language.impl.ast.TreeUtil;
import consulo.language.psi.PsiFile;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellFormattingModelBuilder implements FormattingModelBuilder {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public FormattingModel createModel(FormattingContext context) {
        PsiFile file = context.getContainingFile();
        PowerShellBlockImpl root =
                new PowerShellBlockImpl(
                        file.getNode(),
                        context.getCodeStyleSettings(),
                        Indent.getNoneIndent(),
                        null,
                        null);
        return new PsiBasedFormattingModel(file, root, FormattingDocumentModel.create(file)) {

            protected @Nullable String replaceWithPsiInLeaf(
                    TextRange range, String whitespace, ASTNode leaf) {
                // Newlines are significant lexer tokens in PowerShell. Preserve their token
                // type when replacing whitespace, as in the upstream formatting model.
                if (!myCanModifyAllWhiteSpaces
                        && leaf != null
                        && PowerShellPsiImplUtil.isWhiteSpaceOrNls(leaf)) {
                    return null;
                }
                IElementType type = TokenType.WHITE_SPACE;
                ASTNode previous = TreeUtil.prevLeaf(leaf);
                if (previous != null && PowerShellPsiImplUtil.isWhiteSpaceOrNls(previous)) {
                    type = previous.getElementType();
                }
                FormatterUtil.replaceWhiteSpace(whitespace, leaf, type, range);
                return whitespace;
            }
        };
    }
}
