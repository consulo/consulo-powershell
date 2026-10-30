// SPDX-License-Identifier: Apache-2.0

import org.jspecify.annotations.NullMarked;

@NullMarked
module consulo.powershell.debugger {
    requires consulo.platform.api;
    requires consulo.powershell.core;
    requires consulo.execution.debugger.dap;
    requires com.google.gson;
    requires consulo.application.api;
    requires consulo.component.api;
    requires consulo.configurable.api;
    requires consulo.disposer.api;
    requires consulo.document.api;
    requires consulo.execution.api;
    requires consulo.execution.debug.api;
    requires consulo.file.editor.api;
    requires consulo.language.api;
    requires consulo.language.editor.api;
    requires consulo.localize.api;
    requires consulo.logging.api;
    requires consulo.process.api;
    requires consulo.project.api;
    requires consulo.ui.api;
    requires consulo.ui.ex.api;
    requires consulo.util.concurrent;
    requires consulo.util.dataholder;
    requires consulo.util.lang;
    requires consulo.virtual.file.system.api;

    opens com.intellij.plugin.powershell.ide.debugger to
            com.google.gson,
            consulo.util.xml.serializer;
}
