// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.debugger;

import consulo.execution.debugger.dap.protocol.LaunchRequestArguments;

import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public final class PowerShellLaunchArguments extends LaunchRequestArguments {

    public @Nullable String script;

    public String terminal = "none";

    public String __sessionId = UUID.randomUUID().toString();

    // PowerShell Editor Services uses these capitalized adapter-specific fields.
    public String @Nullable [] Args;

    public @Nullable Map<String, String> Env;
}
