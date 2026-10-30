// SPDX-FileCopyrightText: 2017 Andrey Dernov <https://github.com/ant-druha/>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.psi;

import org.jspecify.annotations.Nullable;

public interface PowerShellMemberDeclaration extends PowerShellComponent {

    @Nullable PowerShellTypeDeclaration getContainingClass();
}
