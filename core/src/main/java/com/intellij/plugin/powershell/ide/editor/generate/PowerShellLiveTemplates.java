// SPDX-FileCopyrightText: 2017-2018 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2023 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.editor.generate;

import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.LiveTemplateContributor;
import consulo.language.editor.template.LiveTemplateContributor.Factory;
import consulo.localize.LocalizeValue;

@ExtensionImpl
public final class PowerShellLiveTemplates implements LiveTemplateContributor {

    public String groupId() {
        return "powershell";
    }

    public LocalizeValue groupName() {
        return PowerShellLocalize.uiPowershell();
    }

    public void contribute(Factory factory) {
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.sout",
                        "sout",
                        "Write-Output $ARG$",
                        PowerShellLocalize.uiWriteObjectValueToStandardOutput())) {
            builder.withReformat();
            builder.withVariable("ARG", "", "", true);
            builder.withContext(PowerShellStatementContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.wo",
                        "wo",
                        "Write-Output $ARG$",
                        PowerShellLocalize.uiWriteObjectValueToStandardOutput())) {
            builder.withReformat();
            builder.withVariable("ARG", "", "", true);
            builder.withContext(PowerShellStatementContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.fun0",
                        "fun0",
                        "function $NAME$ {\n    $END$\n}",
                        PowerShellLocalize.uiFunctionWithNoParameters())) {
            builder.withReformat();
            builder.withVariable("NAME", "", "", true);
            builder.withContext(PowerShellStatementContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.fun",
                        "fun",
                        "function $NAME$($PARAMS$) {\n    $END$\n}",
                        PowerShellLocalize.uiFunctionWithParameters())) {
            builder.withReformat();
            builder.withVariable("NAME", "", "", true);
            builder.withVariable("PARAMS", "", "", true);
            builder.withContext(PowerShellStatementContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.for",
                        "for",
                        "for ($$$INDEX$ = 0; $$$INDEX$ -lt $$$ARRAY$.Count; $$$INDEX$++) {\n"
                            + "    $END$\n"
                            + "}",
                        PowerShellLocalize.uiForLoopSnippet())) {
            builder.withReformat();
            builder.withVariable("INDEX", "", "\"i\"", true);
            builder.withVariable("ARRAY", "", "\"array\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.foreach",
                        "foreach",
                        "foreach ($$$ITEM$ in $$$COLLECTION$) {\n    $END$\n}",
                        PowerShellLocalize.uiForeachLoopSnippet())) {
            builder.withReformat();
            builder.withVariable("ITEM", "", "\"item\"", true);
            builder.withVariable("COLLECTION", "complete()", "\"collection\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.if",
                        "if",
                        "if ($CONDITION$) {\n    $END$\n}",
                        PowerShellLocalize.uiIfStatementSnippet())) {
            builder.withReformat();
            builder.withVariable("CONDITION", "", "\"condition\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.else",
                        "else",
                        "else {\n    $END$\n}",
                        PowerShellLocalize.uiElseStatementSnippet())) {
            builder.withReformat();
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.while",
                        "while",
                        "while ($CONDITION$) {\n    $END$\n}",
                        PowerShellLocalize.uiWhileLoopSnippet())) {
            builder.withReformat();
            builder.withVariable("CONDITION", "", "\"condition\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.class",
                        "class",
                        "class $NAME$ {\n    $END$\n}",
                        PowerShellLocalize.uiClassDefinitionSnippet())) {
            builder.withReformat();
            builder.withVariable("NAME", "", "\"ClassName\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.cmdlet",
                        "cmdlet",
                        "function $NAME$ {\n"
                            + "    [CmdletBinding()]\n"
                            + "    param (\n"
                            + "        $END$\n"
                            + "    )\n"
                            + "    \n"
                            + "    begin {\n"
                            + "    }\n"
                            + "    \n"
                            + "    process {\n"
                            + "    }\n"
                            + "    \n"
                            + "    end {\n"
                            + "    }\n"
                            + "}",
                        PowerShellLocalize.uiScriptCmdletDefinitionSnippet())) {
            builder.withReformat();
            builder.withVariable("NAME", "", "\"Verb-Noun\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.ctor",
                        "ctor",
                        "$ClassName$($OptionalParameters$) {\n    $END$\n}",
                        PowerShellLocalize.uiClassConstructorDefinitionSnippet())) {
            builder.withReformat();
            builder.withVariable("ClassName", "", "\"ClassName\"", true);
            builder.withVariable("OptionalParameters", "", "\"OptionalParameters\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.do until",
                        "do until",
                        "do {\n    $END$\n} until ($condition$)",
                        PowerShellLocalize.uiDoUntilLoopSnippet())) {
            builder.withReformat();
            builder.withVariable("condition", "", "\"condition\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.do while",
                        "do while",
                        "do {\n    $END$\n} while ($condition$)",
                        PowerShellLocalize.uiDoWhileLoopSnippet())) {
            builder.withReformat();
            builder.withVariable("condition", "", "\"condition\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.DSC Ensure enum",
                        "DSC Ensure enum",
                        "enum Ensure {\n    Absent\n    Present\n}",
                        PowerShellLocalize.uiDscEnsureEnumDefinitionSnippet())) {
            builder.withReformat();
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.DSC resource provider (class-based)",
                        "DSC resource provider (class-based)",
                        "[DscResource()]\n"
                            + "class NameOfResource {\n"
                            + "    [DscProperty(Key)]\n"
                            + "    [string] $KeyName\n"
                            + "    \n"
                            + "    # Gets the resource's current state.\n"
                            + "    [NameOfResource] Get() {\n"
                            + "        $END$\n"
                            + "        return $this\n"
                            + "    }\n"
                            + "    \n"
                            + "    # Sets the desired state of the resource.\n"
                            + "    [void] Set() {\n"
                            + "        \n"
                            + "    }\n"
                            + "    \n"
                            + "    # Tests if the resource is in the desired state.\n"
                            + "    [bool] Test() {\n"
                            + "        \n"
                            + "    }\n"
                            + "}",
                        PowerShellLocalize.uiClassBasedDscResourceProviderSnippet())) {
            builder.withReformat();
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.DSC resource provider (function-based)",
                        "DSC resource provider (function-based)",
                        "function Get-TargetResource {\n"
                            + "    param (\n"
                            + "    )\n"
                            + "    $END$\n"
                            + "}\n"
                            + "function Set-TargetResource {\n"
                            + "    param (\n"
                            + "    )\n"
                            + "    \n"
                            + "}\n"
                            + "function Test-TargetResource {\n"
                            + "    param (\n"
                            + "    )\n"
                            + "    \n"
                            + "}",
                        PowerShellLocalize.uiFunctionBasedDscResourceProviderSnippet())) {
            builder.withReformat();
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.elseif",
                        "elseif",
                        "elseif ($condition$) {\n    $END$\n}",
                        PowerShellLocalize.uiElseifStatementSnippet())) {
            builder.withReformat();
            builder.withVariable("condition", "", "\"condition\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.ex advanced workflow",
                        "ex advanced workflow",
                        "<#\n"
                            + ".Synopsis\n"
                            + "    Short description\n"
                            + ".DESCRIPTION\n"
                            + "    Long description\n"
                            + ".EXAMPLE\n"
                            + "    Example of how to use this workflow\n"
                            + ".EXAMPLE\n"
                            + "    Another example of how to use this workflow\n"
                            + ".INPUTS\n"
                            + "    Inputs to this workflow (if any)\n"
                            + ".OUTPUTS\n"
                            + "    Output from this workflow (if any)\n"
                            + ".NOTES\n"
                            + "    General notes\n"
                            + ".FUNCTIONALITY\n"
                            + "    The functionality that best describes this workflow\n"
                            + "#>\n"
                            + "workflow Verb-Noun {$END$\n"
                            + "    [CmdletBinding(DefaultParameterSetName='Parameter Set 1',\n"
                            + "                   HelpUri = 'http://www.microsoft.com/',\n"
                            + "                   ConfirmImpact='Medium')]\n"
                            + "    [Alias()]\n"
                            + "    [OutputType([String])]\n"
                            + "    param (\n"
                            + "        # Param1 help description\n"
                            + "        [Parameter(Mandatory=$true, \n"
                            + "                   Position=0,\n"
                            + "                   ParameterSetName='Parameter Set 1')]\n"
                            + "        [ValidateNotNull()]\n"
                            + "        [Alias(\"p1\")] \n"
                            + "        $Param1,\n\n"
                            + "        # Param2 help description\n"
                            + "        [int]\n"
                            + "        $Param2\n"
                            + "    )\n\n"
                            + "    # Saves (persists) the current workflow state and output\n"
                            + "    # Checkpoint-Workflow\n"
                            + "    # Suspends the workflow\n"
                            + "    # Suspend-Workflow\n\n"
                            + "    # Workflow common parameters are available as variables such"
                            + " as:\n"
                            + "    $PSPersist \n"
                            + "    $PSComputerName\n"
                            + "    $PSCredential\n"
                            + "    $PSUseSsl\n"
                            + "    $PSAuthentication\n\n"
                            + "    # Workflow runtime information can be accessed by using the"
                            + " following variables:\n"
                            + "    $Input\n"
                            + "    $PSSenderInfo\n"
                            + "    $PSWorkflowRoot\n"
                            + "    $JobCommandName\n"
                            + "    $ParentCommandName\n"
                            + "    $JobId\n"
                            + "    $ParentJobId\n"
                            + "    $WorkflowInstanceId\n"
                            + "    $JobInstanceId\n"
                            + "    $ParentJobInstanceId\n"
                            + "    $JobName\n"
                            + "    $ParentJobName\n\n"
                            + "    # Set the progress message ParentActivityId\n"
                            + "    $PSParentActivityId\n\n"
                            + "    # Preference variables that control runtime behavior\n"
                            + "    $PSRunInProcessPreference\n"
                            + "    $PSPersistPreference\n"
                            + "}",
                        PowerShellLocalize.uiExampleAdvancedWorkflowSnippet())) {
            builder.withReformat();
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.ex class",
                        "ex class",
                        "class $MyClass$ {\n"
                            + "    # Property: Holds name\n"
                            + "    [String] $Name\n\n"
                            + "    # Constructor: Creates a new MyClass object, with the specified"
                            + " name\n"
                            + "    $MyClass$([String] $NewName) {\n"
                            + "        # Set name for $MyClass$\n"
                            + "        $this.Name = $NewName\n"
                            + "    }\n\n"
                            + "    # Method: Method that changes $Name to the default name\n"
                            + "    [void] ChangeNameToDefault() {\n"
                            + "        $this.Name = \"DefaultName\"\n"
                            + "    }\n"
                            + "}",
                        PowerShellLocalize
                                .uiExampleClassSnippetWithAConstructorPropertyAndAMethod())) {
            builder.withReformat();
            builder.withVariable("MyClass", "", "\"MyClass\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.parameter",
                        "parameter",
                        "# $Parameter_help_description$\n"
                            + "[Parameter($AttributeValues$)]\n"
                            + "[$ParameterType$]\n"
                            + "$$$ParameterName$",
                        PowerShellLocalize.uiParameterDeclarationSnippet())) {
            builder.withReformat();
            builder.withVariable(
                    "Parameter_help_description", "", "\"Parameter help description\"", true);
            builder.withVariable("AttributeValues", "", "\"AttributeValues\"", true);
            builder.withVariable("ParameterType", "", "\"ParameterType\"", true);
            builder.withVariable("ParameterName", "", "\"ParameterName\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.manifest",
                        "manifest",
                        "@{\n"
                            + "    # If authoring a script module, the RootModule is the name of"
                            + " your .psm1 file\n"
                            + "    RootModule = 'MyModule.psm1'\n\n"
                            + "    Author = 'Cool Person <coolperson@email.local>'\n\n"
                            + "    CompanyName = 'Contoso Inc.'\n\n"
                            + "    ModuleVersion = '0.1'\n\n"
                            + "    # Use the New-Guid command to generate a GUID, and copy/paste"
                            + " into the next line\n"
                            + "    GUID = '<pasteNewGUIDhere>'\n\n"
                            + "    Copyright = '2017 Copyright Holder'\n\n"
                            + "    Description = 'What does this module do?'\n\n"
                            + "    # Minimum PowerShell version supported by this module (optional,"
                            + " recommended)\n"
                            + "    # PowerShellVersion = ''\n\n"
                            + "    # Which PowerShell Editions does this module work with? (Core,"
                            + " Desktop)\n"
                            + "    CompatiblePSEditions = @('Desktop', 'Core')\n\n"
                            + "    # Which PowerShell functions are exported from your module? (eg."
                            + " Get-CoolObject)\n"
                            + "    FunctionsToExport = @('')\n\n"
                            + "    # Which PowerShell aliases are exported from your module? (eg."
                            + " gco)\n"
                            + "    AliasesToExport = @('')\n\n"
                            + "    # Which PowerShell variables are exported from your module? (eg."
                            + " Fruits, Vegetables)\n"
                            + "    VariablesToExport = @('')\n\n"
                            + "    # PowerShell Gallery: Define your module's metadata\n"
                            + "    PrivateData = @{\n"
                            + "        PSData = @{\n"
                            + "            # What keywords represent your PowerShell module? (eg."
                            + " cloud, tools, framework, vendor)\n"
                            + "            Tags = @('cooltag1', 'cooltag2')\n\n"
                            + "            # What software license is your code being released"
                            + " under? (see https://opensource.org/licenses)\n"
                            + "            LicenseUri = ''\n\n"
                            + "            # What is the URL to your project's website?\n"
                            + "            ProjectUri = ''\n\n"
                            + "            # What is the URI to a custom icon file for your"
                            + " project? (optional)\n"
                            + "            IconUri = ''\n\n"
                            + "            # What new features, bug fixes, or deprecated features,"
                            + " are part of this release?\n"
                            + "            ReleaseNotes = @'\n"
                            + "'@\n"
                            + "        }\n"
                            + "    }\n\n"
                            + "    # If your module supports updateable help, what is the URI to"
                            + " the help archive? (optional)\n"
                            + "    # HelpInfoURI = ''\n"
                            + "}",
                        PowerShellLocalize.uiBasicSkeletonForAPowershellModuleManifest())) {
            builder.withReformat();
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.prop",
                        "prop",
                        "[$PopertyType$] $$$PropertyName$",
                        PowerShellLocalize.uiClassPropertyDefinitionSnippet())) {
            builder.withReformat();
            builder.withVariable("PopertyType", "", "\"string\"", true);
            builder.withVariable("PropertyName", "", "\"PropertyName\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.switch",
                        "switch",
                        "switch ($$$x$) {\n    condition {$END$ }\n    Default {}\n}",
                        PowerShellLocalize.uiSwitchStatementSnippet())) {
            builder.withReformat();
            builder.withVariable("x", "", "\"x\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.try",
                        "try",
                        "try {\n    $END$\n}\ncatch {\n    \n}",
                        PowerShellLocalize.uiTryCatchSnippet())) {
            builder.withReformat();
            builder.withContext(PowerShellLanguageContext.class);
        }
        try (LiveTemplateContributor.Builder builder =
                factory.newBuilder(
                        "powershell.workflow foreach-parallel",
                        "workflow foreach-parallel",
                        "foreach -parallel ($$$item$ in $$$collection$) {\n    $END$\n}",
                        PowerShellLocalize.uiForeachParallelSnippetForUseInsideAWorkflow())) {
            builder.withReformat();
            builder.withVariable("item", "", "\"item\"", true);
            builder.withVariable("collection", "", "\"collection\"", true);
            builder.withContext(PowerShellLanguageContext.class);
        }
    }
}
