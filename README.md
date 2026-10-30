# PowerShell for Consulo

PowerShell language support, script execution, and debugging for Consulo.

## Features

- Syntax highlighting, brace matching, comments, and code folding.
- Structure view, navigation, find usages, and rename refactoring.
- Code formatting, file templates, and language injection.
- Run configurations with executable, script path, working directory, interpreter options, and script arguments.
- Optional debugging with breakpoints, stepping, stack frames, variables, watches, and expression evaluation.

## Requirements

Install [PowerShell](https://github.com/PowerShell/PowerShell) separately. Run
configurations use `pwsh` by default; select another executable when needed,
including `powershell.exe` on Windows.

For debugging, also install the Consulo DAP plugin (`consulo.dap`) and
[PowerShell Editor Services](https://github.com/PowerShell/PowerShellEditorServices).
Editing and script execution work without DAP.

## Run a script

Open a `.ps1` file and use its run gutter icon, or create a PowerShell run
configuration. Set the PowerShell executable and script path, then choose **Run**.
Working directory, interpreter options, and script arguments are optional.

## Debug a script

In **Settings | Execution | PowerShell Debugger**, select the Editor Services
directory or its `Start-EditorServices.ps1`. Set a breakpoint in the script and
choose **Debug** for a PowerShell run configuration.

## Build

Use JDK 25 and Maven 3.9 or newer:

```sh
mvn package -T 1C -Dmaven.test.skip=true
```

To produce the installable plugin archive:

```sh
BUILD_NUMBER=SNAPSHOT mvn package -T 1C -Dmaven.test.skip=true
```

Install `plugin/target/consulo.powershell.consulo-plugin`.

## License

[Apache License 2.0](LICENSE). See [NOTICE.txt](NOTICE.txt) for attribution.
