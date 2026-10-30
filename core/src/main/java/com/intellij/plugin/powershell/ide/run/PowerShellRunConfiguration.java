// SPDX-FileCopyrightText: 2017-2022 Andrey Dernov <https://github.com/ant-druha/>
// SPDX-FileCopyrightText: 2021-2024 intellij-powershell contributors
// <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.run;

import com.intellij.plugin.powershell.localize.PowerShellLocalize;

import consulo.execution.RuntimeConfigurationException;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.EnvironmentVariablesData;
import consulo.execution.configuration.LocatableConfigurationBase;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.util.ProgramParametersUtil;
import consulo.platform.Platform;
import consulo.process.ProcessHandlerBuilderFactory;
import consulo.project.Project;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.WriteExternalException;

import org.jdom.Element;

import java.io.File;
import java.util.List;
import java.nio.file.Files;

public final class PowerShellRunConfiguration extends LocatableConfigurationBase {

    private final ProcessHandlerBuilderFactory myProcessHandlerBuilderFactory;

    public String myScriptPath = "";

    public String myCustomWorkingDirectory = "";

    public String myScriptParameters = "";

    public String myCommandOptions = "";

    public String myExecutablePath = "pwsh";

    public EnvironmentVariablesData myEnvironmentVariables = EnvironmentVariablesData.DEFAULT;

    public PowerShellRunConfiguration(
            Project project,
            ConfigurationFactory factory,
            String name,
            ProcessHandlerBuilderFactory processHandlerBuilderFactory) {
        super(project, factory, name);
        myProcessHandlerBuilderFactory = processHandlerBuilderFactory;
    }

    @Override
    public List<RunConfigurationFragment<?>> createFragments() {
        return List.of(new PowerShellRunConfigurationFragment());
    }

    public RunProfileState getState(Executor executor, ExecutionEnvironment environment) {
        return new PowerShellScriptCommandLineState(
                this, environment, myProcessHandlerBuilderFactory);
    }

    public String suggestedName() {
        return myScriptPath.isBlank() ? "PowerShell" : new File(myScriptPath).getName();
    }

    public void checkConfiguration() throws RuntimeConfigurationException {
        if (myScriptPath.isBlank()) {
            throw new RuntimeConfigurationError(
                    PowerShellLocalize.uiSelectAPowershellScript().get());
        }
        if (!Files.isRegularFile(
                Platform.current()
                        .fs()
                        .getPath(
                                ProgramParametersUtil.expandPath(
                                        myScriptPath, null, getProject())))) {
            throw new RuntimeConfigurationError(
                    PowerShellLocalize.uiScriptDoesNotExist0(myScriptPath).get());
        }
        if (myExecutablePath.isBlank()) {
            throw new RuntimeConfigurationError(
                    PowerShellLocalize.uiSpecifyThePowershellExecutable().get());
        }
    }

    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        myScriptPath = element.getAttributeValue("scriptUrl", "");
        myScriptParameters = element.getAttributeValue("scriptParameters", "");
        myCommandOptions = element.getAttributeValue("commandOptions", "");
        myCustomWorkingDirectory = element.getAttributeValue("workingDirectory", "");
        myExecutablePath = element.getAttributeValue("executablePath", "pwsh");
        myEnvironmentVariables = EnvironmentVariablesData.readExternal(element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        element.setAttribute("scriptUrl", myScriptPath);
        element.setAttribute("scriptParameters", myScriptParameters);
        element.setAttribute("commandOptions", myCommandOptions);
        element.setAttribute("workingDirectory", myCustomWorkingDirectory);
        element.setAttribute("executablePath", myExecutablePath);
        myEnvironmentVariables.writeExternal(element);
    }
}
