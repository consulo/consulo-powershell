// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.Application;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;

import jakarta.inject.Singleton;

@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
@State(name = "PowerShellDebugSettings", storages = @Storage("powershell-debug.xml"))
@Singleton
public final class PowerShellDebugSettings
        implements PersistentStateComponent<PowerShellDebugSettings.SettingsState> {

    public static final class SettingsState {

        public String myEditorServicesPath = "";
    }

    private SettingsState myState = new SettingsState();

    public static PowerShellDebugSettings getInstance() {
        return Application.get().getInstance(PowerShellDebugSettings.class);
    }

    public SettingsState getState() {
        return myState;
    }

    public void loadState(SettingsState state) {
        myState = state;
    }
}
