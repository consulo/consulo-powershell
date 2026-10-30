// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMENT;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.DELIMITED_COMMENT;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.CodeDocumentationAwareCommenter;
import consulo.language.Language;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiComment;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellCommenter implements CodeDocumentationAwareCommenter {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public IElementType getLineCommentTokenType() {
        return COMMENT;
    }

    public IElementType getBlockCommentTokenType() {
        return DELIMITED_COMMENT;
    }

    public @Nullable IElementType getDocumentationCommentTokenType() {
        return null;
    }

    public @Nullable String getDocumentationCommentPrefix() {
        return null;
    }

    public @Nullable String getDocumentationCommentLinePrefix() {
        return null;
    }

    public @Nullable String getDocumentationCommentSuffix() {
        return null;
    }

    public boolean isDocumentationComment(PsiComment element) {
        return false;
    }

    public String getLineCommentPrefix() {
        return "#";
    }

    public String getBlockCommentPrefix() {
        return "<#";
    }

    public String getBlockCommentSuffix() {
        return "#>";
    }

    public @Nullable String getCommentedBlockCommentPrefix() {
        return null;
    }

    public @Nullable String getCommentedBlockCommentSuffix() {
        return null;
    }
}
