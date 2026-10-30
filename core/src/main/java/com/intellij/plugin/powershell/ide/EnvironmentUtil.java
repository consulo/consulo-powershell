/*
 * SPDX-FileCopyrightText: 2026 intellij-powershell contributors <https://github.com/intellij-powershell/intellij-powershell>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.intellij.plugin.powershell.ide;

import consulo.platform.Platform;

import org.jspecify.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.regex.Pattern;

public final class EnvironmentUtil {

    private EnvironmentUtil() {}

    public static @Nullable Path findExecutableInPath(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        Platform platform = Platform.current();
        Path path;
        try {
            path = platform.fs().getPath(name);
        }
        catch (InvalidPathException ignored) {
            return null;
        }
        if (path.isAbsolute()) {
            return Files.isRegularFile(path) ? path : null;
        }
        boolean windows = platform.os().isWindows();
        String extensions =
                windows
                        ? platform.os()
                                .environmentVariables()
                                .getOrDefault("PATHEXT", ".COM;.EXE;.BAT;.CMD")
                        : "";
        for (String directory :
                platform.os()
                        .environmentVariables()
                        .getOrDefault("PATH", "")
                        .split(Pattern.quote(windows ? ";" : ":"))) {
            Path candidate = platform.fs().getPath(directory).resolve(name);
            if (Files.isRegularFile(candidate) && (windows || Files.isExecutable(candidate))) {
                return candidate;
            }
            for (String extension : extensions.split(";")) {
                Path withExtension =
                        platform.fs()
                                .getPath(directory)
                                .resolve(name + extension.toLowerCase(Locale.ROOT));
                if (Files.isRegularFile(withExtension)) {
                    return withExtension;
                }
            }
        }
        return null;
    }
}
