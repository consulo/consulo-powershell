// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public final class MessagesBundle {

    private MessagesBundle() {}

    public static String message(String key, Object... parameters) {
        String text = ResourceBundle.getBundle("messages.MessagesBundle").getString(key);
        return parameters.length == 0 ? text : MessageFormat.format(text, parameters);
    }

    public static Supplier<String> messagePointer(String key, Object... parameters) {
        return () -> message(key, parameters);
    }
}
