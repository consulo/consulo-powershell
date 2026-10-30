// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.highlighting;

import com.intellij.plugin.powershell.lang.PowerShellLanguage;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.language.editor.highlight.SyntaxHighlighterFactory;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public SyntaxHighlighter getSyntaxHighlighter(
            @Nullable Project project, @Nullable VirtualFile file) {
        return new PowerShellSyntaxHighlighter();
    }
}
