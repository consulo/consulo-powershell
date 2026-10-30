// SPDX-FileCopyrightText: 2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.run;

import com.intellij.plugin.powershell.ide.EnvironmentUtil;
import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.platform.Platform;
import consulo.process.ExecutionException;

import org.jspecify.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;

public final class PSExecutionUtil {

    private PSExecutionUtil() {}

    public static String findPsExecutable() throws ExecutionException {
        for (String name : new String[] {"pwsh", "powershell"}) {
            Path path = EnvironmentUtil.findExecutableInPath(name);
            if (path != null) {
                return path.toString();
            }
        }
        throw new ExecutionException(
                PowerShellLocalize.uiCannotFindPowershellExecutableInPath().get());
    }

    public static String escapePath(String path) {
        return "&(\"" + path + "\")";
    }

    public static boolean checkExists(String path) {
        return path != null && Files.exists(Platform.current().fs().getPath(path));
    }

    public static String join(String... parts) {
        return Platform.current().fs().getPath("", parts).normalize().toString();
    }

    public static Path getDefaultWorkingDirectory(@Nullable Path script) {
        if (script != null && script.getParent() != null) {
            return script.getParent();
        }
        return Platform.current().user().homePath();
    }
}
