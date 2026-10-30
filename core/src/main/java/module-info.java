// SPDX-License-Identifier: Apache-2.0

import org.jspecify.annotations.NullMarked;

@NullMarked
module consulo.powershell.core {
    requires consulo.platform.api;
    requires consulo.application.api;
    requires consulo.code.editor.api;
    requires consulo.color.scheme.api;
    requires consulo.component.api;
    requires consulo.configurable.api;
    requires consulo.document.api;
    requires consulo.language.api;
    requires consulo.language.inject.advanced.api;
    requires consulo.language.impl;
    requires consulo.language.editor.api;
    requires consulo.language.code.style.api;
    requires consulo.language.code.style.ui.api;
    requires consulo.localize.api;
    requires consulo.logging.api;
    requires consulo.navigation.api;
    requires consulo.project.api;
    requires consulo.ui.api;
    requires consulo.ui.ex.api;
    requires consulo.ui.ex.awt.api;
    requires consulo.util.collection;
    requires consulo.util.dataholder;
    requires consulo.util.lang;
    requires consulo.util.io;
    requires consulo.virtual.file.system.api;
    requires consulo.file.template.api;
    requires consulo.execution.api;
    requires consulo.process.api;
    requires consulo.util.xml.serializer;
    requires consulo.language.editor.refactoring.api;
    requires consulo.ide.api;

    exports com.intellij.plugin.powershell;
    exports com.intellij.plugin.powershell.ide;
    exports com.intellij.plugin.powershell.ide.actions;
    exports com.intellij.plugin.powershell.ide.editor;
    exports com.intellij.plugin.powershell.ide.editor.formatting;
    exports com.intellij.plugin.powershell.ide.editor.generate;
    exports com.intellij.plugin.powershell.ide.editor.highlighting;
    exports com.intellij.plugin.powershell.ide.refactoring;
    exports com.intellij.plugin.powershell.ide.resolve;
    exports com.intellij.plugin.powershell.ide.run;
    exports com.intellij.plugin.powershell.ide.search;
    exports com.intellij.plugin.powershell.ide.structure;
    exports com.intellij.plugin.powershell.lang;
    exports com.intellij.plugin.powershell.lang.lexer;
    exports com.intellij.plugin.powershell.lang.parser;
    exports com.intellij.plugin.powershell.lang.resolve;
    exports com.intellij.plugin.powershell.lang.util;
    exports com.intellij.plugin.powershell.psi;
    exports com.intellij.plugin.powershell.psi.impl;
    exports com.intellij.plugin.powershell.psi.impl.mixin;
    exports com.intellij.plugin.powershell.psi.types;
    exports com.intellij.plugin.powershell.psi.types.impl;

    opens com.intellij.plugin.powershell.ide.editor.formatting to
            consulo.util.xml.serializer;
    opens com.intellij.plugin.powershell.ide.injection to
            consulo.language.inject.advanced.api,
            consulo.language.inject.advanced.impl;
}
