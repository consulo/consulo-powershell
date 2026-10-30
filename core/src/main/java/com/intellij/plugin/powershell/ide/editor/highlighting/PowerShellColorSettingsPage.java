// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.highlighting;

import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.TextAttributesKey;
import consulo.colorScheme.setting.AttributesDescriptor;
import consulo.language.editor.colorScheme.setting.ColorSettingsPage;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.localize.LocalizeValue;

import java.util.HashMap;
import java.util.Map;

@ExtensionImpl
public final class PowerShellColorSettingsPage implements ColorSettingsPage {

    public SyntaxHighlighter getHighlighter() {
        return new PowerShellSyntaxHighlighter();
    }

    public LocalizeValue getDisplayName() {
        return PowerShellLocalize.uiPowershell();
    }

    public AttributesDescriptor[] getAttributeDescriptors() {
        return new AttributesDescriptor[] {
            new AttributesDescriptor(
                    PowerShellLocalize.uiKeyword(), PowerShellSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor(
                    PowerShellLocalize.uiComment(), PowerShellSyntaxHighlighter.COMMENT),
            new AttributesDescriptor(
                    PowerShellLocalize.uiNumber(), PowerShellSyntaxHighlighter.NUMBER),
            new AttributesDescriptor(
                    PowerShellLocalize.uiString(), PowerShellSyntaxHighlighter.STRING),
            new AttributesDescriptor(
                    PowerShellLocalize.uiCommandName(), PowerShellSyntaxHighlighter.COMMAND_NAME),
            new AttributesDescriptor(
                    PowerShellLocalize.uiCommandParameter(),
                    PowerShellSyntaxHighlighter.COMMAND_PARAMETER),
            new AttributesDescriptor(
                    PowerShellLocalize.uiTypeName(), PowerShellSyntaxHighlighter.TYPE_NAME),
            new AttributesDescriptor(
                    PowerShellLocalize.uiTypeReference(),
                    PowerShellSyntaxHighlighter.TYPE_REFERENCE),
            new AttributesDescriptor(
                    PowerShellLocalize.uiVariableName(), PowerShellSyntaxHighlighter.VARIABLE_NAME),
            new AttributesDescriptor(
                    PowerShellLocalize.uiPropertyAccess(),
                    PowerShellSyntaxHighlighter.PROPERTY_REFERENCE),
            new AttributesDescriptor(
                    PowerShellLocalize.uiMethodCall(), PowerShellSyntaxHighlighter.METHOD_CALL),
            new AttributesDescriptor(
                    PowerShellLocalize.uiMethodDeclaration(),
                    PowerShellSyntaxHighlighter.METHOD_DECLARATION),
            new AttributesDescriptor(
                    PowerShellLocalize.uiLabel(), PowerShellSyntaxHighlighter.LABEL)
        };
    }

    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        Map<String, TextAttributesKey> tags = new HashMap<>();
        tags.put("keyword", PowerShellSyntaxHighlighter.KEYWORD);
        tags.put("comment", PowerShellSyntaxHighlighter.COMMENT);
        tags.put("number", PowerShellSyntaxHighlighter.NUMBER);
        tags.put("string", PowerShellSyntaxHighlighter.STRING);
        tags.put("command name", PowerShellSyntaxHighlighter.COMMAND_NAME);
        tags.put("command parameter", PowerShellSyntaxHighlighter.COMMAND_PARAMETER);
        tags.put("type name", PowerShellSyntaxHighlighter.TYPE_NAME);
        tags.put("type ref", PowerShellSyntaxHighlighter.TYPE_REFERENCE);
        tags.put("var name", PowerShellSyntaxHighlighter.VARIABLE_NAME);
        tags.put("property access", PowerShellSyntaxHighlighter.PROPERTY_REFERENCE);
        tags.put("method call", PowerShellSyntaxHighlighter.METHOD_CALL);
        tags.put("method declaration", PowerShellSyntaxHighlighter.METHOD_DECLARATION);
        tags.put("label", PowerShellSyntaxHighlighter.LABEL);
        return tags;
    }

    public String getDemoText() {
        return "foreach ($<var name>i</var name> in <command name>get-childitem</command name> |"
                   + " <command name>sort-object</command name> length)\n"
                   + " {\n"
                   + " $<var name>i</var name>  #highlight\n"
                   + " $<var name>sum</var name> += $<var name>i</var name>.<property"
                   + " access>length</property access>\n"
                   + " }\n"
                   + "\n"
                   + "# 3) variable name token: '$_.length' and 'get-childitem'\n"
                   + ":<label>labelA</label> switch <command parameter>-regex</command parameter>"
                   + " <command parameter>-casesensitive</command parameter> (<command"
                   + " name>get-childitem</command name> | sort length)\n"
                   + "{\n"
                   + " \"^5\" {\"length for $<var name>_</var name> started with 5\" ; continue"
                   + " <label>labelA</label>}\n"
                   + " { $<var name>_</var name>.<property access>length</property access> > 20000"
                   + " } {\"length of $<var name>_</var name> is greater than 20000\"}\n"
                   + " default {\"Didn't match anything else...\"}\n"
                   + "}\n\n"
                   + "class <type name>Person</type name>\n"
                   + "{\n"
                   + "    [<type ref>int</type ref>]$<var name>Age</var name>\n"
                   + "    Person([<type ref>int</type ref>]$<var name>a</var name>)\n"
                   + "    {\n"
                   + "        $this.<property access>Age<property access> = $<var name>a</var"
                   + " name>\n"
                   + "    }\n"
                   + "    <method declaration>DoSomething</method declaration>($<var name>x</var"
                   + " name>)\n"
                   + "    {\n"
                   + "        $this.<method call>DoSomething</method call>($<var name>x</var"
                   + " name>)\n"
                   + "    }\n"
                   + "}\n"
                   + "class <type name>Child</type name>: <type ref>Person</type ref>\n"
                   + "{\n"
                   + "    [<type ref>string</type ref>]$<var name>School</var name>\n"
                   + "\n"
                   + "    <method declaration>Child</method declaration>([<type ref>int</type"
                   + " ref>]$<var name>a</var name>, [<type ref>string</type ref>]$<var name>s</var"
                   + " name>): base($<var name>a</var name>)\n"
                   + "    {\n"
                   + "        $this.<property access>School</property access> = $<var name>s</var"
                   + " name>\n"
                   + "    }\n"
                   + "}\n"
                   + "[<type ref>Child</type ref>]$<var name>littleOne</var name> = [<type"
                   + " ref>Child</type ref>]::<method call>new</method call>(10, \"Silver Fir"
                   + " Elementary School\")\n"
                   + "<command name>Write-Output</command name> \"Child's age is $( $<var"
                   + " name>littleOne</var name>.<property access>Age</property access> )\"";
    }
}
