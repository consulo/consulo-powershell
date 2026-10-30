// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import com.intellij.plugin.powershell.ide.debugger.localize.PowerShellDebuggerLocalize;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.ConfigurationException;
import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.ui.Component;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.LabeledLayout;

import jakarta.inject.Inject;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ExtensionImpl
public final class PowerShellDebugConfigurable implements ApplicationConfigurable {

    private @Nullable TextBox myPath;

    private final PowerShellDebugSettings mySettings;

    @Inject
    public PowerShellDebugConfigurable(PowerShellDebugSettings settings) {
        mySettings = settings;
    }

    public String getId() {
        return "powershell.debugger";
    }

    public String getParentId() {
        return "execution";
    }

    public LocalizeValue getDisplayName() {
        return PowerShellDebuggerLocalize.uiPowershellDebugger();
    }

    @RequiredUIAccess
    public Component createUIComponent(Disposable disposable) {
        myPath = TextBox.create();
        return LabeledLayout.create(
                PowerShellDebuggerLocalize
                        .uiPowershellEditorServicesDirectoryOrStartEditorservicesPs1(),
                myPath);
    }

    @RequiredUIAccess
    public boolean isModified() {
        return myPath != null
                && !Objects.equals(myPath.getValue(), mySettings.getState().myEditorServicesPath);
    }

    @RequiredUIAccess
    public void apply() throws ConfigurationException {
        TextBox path = myPath;
        if (path == null) {
            return;
        }
        String value = Objects.toString(path.getValue(), "").trim();
        if (!value.isEmpty() && PowerShellDebugHost.findStartupScript(value) == null) {
            throw new ConfigurationException(
                    PowerShellDebuggerLocalize
                            .uiCannotFindStartEditorservicesPs1InTheSelectedLocation()
                            .get());
        }
        mySettings.getState().myEditorServicesPath = value;
    }

    @RequiredUIAccess
    public void reset() {
        if (myPath != null) {
            myPath.setValue(mySettings.getState().myEditorServicesPath);
        }
    }

    @RequiredUIAccess
    public void disposeUIResources() {
        myPath = null;
    }
}
