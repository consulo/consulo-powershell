// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import com.intellij.plugin.powershell.PowerShellFileType;

import consulo.document.Document;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.evaluation.EvaluationMode;
import consulo.execution.debug.evaluation.XDebuggerEditorsProvider;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.project.Project;
import consulo.virtualFileSystem.fileType.FileType;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public final class PowerShellDebuggerEditorsProvider extends XDebuggerEditorsProvider {

    public FileType getFileType() {
        return PowerShellFileType.INSTANCE;
    }

    public Document createDocument(
            Project project, String text, @Nullable XSourcePosition position, EvaluationMode mode) {
        PsiFile file =
                PsiFileFactory.getInstance(project)
                        .createFileFromText("evaluation.ps1", PowerShellFileType.INSTANCE, text, 0, true);
        return Objects.requireNonNull(PsiDocumentManager.getInstance(project).getDocument(file));
    }
}
