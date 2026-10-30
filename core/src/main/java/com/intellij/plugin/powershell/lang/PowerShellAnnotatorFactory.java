// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.lang;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.annotation.AnnotatorFactory;

import org.jspecify.annotations.NullMarked;

@ExtensionImpl
@NullMarked
public final class PowerShellAnnotatorFactory implements AnnotatorFactory {

    public Language getLanguage() {
        return PowerShellLanguage.INSTANCE;
    }

    public Annotator createAnnotator() {
        return new PowerShellAnnotator();
    }
}
