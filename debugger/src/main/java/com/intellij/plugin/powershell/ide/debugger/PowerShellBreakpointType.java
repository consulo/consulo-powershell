// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import com.intellij.plugin.powershell.PowerShellFileType;
import com.intellij.plugin.powershell.ide.debugger.localize.PowerShellDebuggerLocalize;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.execution.debug.breakpoint.XBreakpointProperties;
import consulo.execution.debug.breakpoint.XBreakpointType;
import consulo.execution.debug.breakpoint.XLineBreakpoint;
import consulo.execution.debug.breakpoint.XLineBreakpointType;
import consulo.execution.debug.evaluation.XDebuggerEditorsProvider;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import org.jspecify.annotations.Nullable;

@ExtensionImpl
public final class PowerShellBreakpointType extends XLineBreakpointType<XBreakpointProperties> {

    public PowerShellBreakpointType() {
        super("powershell", PowerShellDebuggerLocalize.uiPowershellLineBreakpoints().get());
    }

    public static PowerShellBreakpointType getInstance() {
        return Application.get()
                .getExtensionPoint(XBreakpointType.class)
                .findExtensionOrFail(PowerShellBreakpointType.class);
    }

    public boolean canPutAt(VirtualFile file, int line, Project project) {
        return file.getFileType() == PowerShellFileType.INSTANCE;
    }

    public @Nullable XBreakpointProperties createBreakpointProperties(VirtualFile file, int line) {
        return null;
    }

    public XDebuggerEditorsProvider getEditorsProvider(
            XLineBreakpoint<XBreakpointProperties> breakpoint, Project project) {
        return new PowerShellDebuggerEditorsProvider();
    }
}
