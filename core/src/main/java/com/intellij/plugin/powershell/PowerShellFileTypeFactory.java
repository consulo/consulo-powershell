// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;

@ExtensionImpl
public final class PowerShellFileTypeFactory extends FileTypeFactory {

    public void createFileTypes(FileTypeConsumer consumer) {
        consumer.consume(PowerShellFileType.INSTANCE, "ps1;psm1;psd1");
    }
}
