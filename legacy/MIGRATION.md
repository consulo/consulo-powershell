<!-- SPDX-License-Identifier: Apache-2.0 -->
# Deferred code and migration tools

This directory is excluded from the Maven reactor and plugin package.

- `src/main/`: deferred LSP integration, inspections, remote editing, completion,
  documentation, interactive console, and supporting lifecycle/logging code.
- `src/test/`: tests and harnesses requiring unavailable test APIs or deferred
  integrations. Converted lexer/parser tests and fixtures live in `core/src/test/`.
- `src/scripts/`: unused upstream maintenance scripts for distribution handling,
  publishing, dependency updates, and copyright reporting. These retain their
  original build/service assumptions and are archived, not part of the build.
- `tools/`: private desktop launch/capture tools and the standalone DAP smoke check
  used during migration.
- `examples/debug-smoke.ps1`: the script used by the debugger smoke check.

Run the standalone Unix adapter check with Python 3:

```sh
python legacy/tools/test-debug-adapter.py \
    --pwsh /path/to/pwsh \
    --editor-services /path/to/PowerShellEditorServices
```

The private desktop tools require Xvfb, Openbox, ImageMagick, and a downloaded
Consulo build. `POWERSHELL_UI_PLATFORM` selects the directory containing
`Consulo/platform/`. Launch with `legacy/tools/run-private-desktop.sh` and capture
with `legacy/tools/capture-private-desktop.sh`. Runtime settings remain isolated
in `plugin/sandbox/`.

Active formatting, injection, and language support live in `core/`. The optional
Java debugger library lives in `debugger/` and uses `consulo.dap`.
