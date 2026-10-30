<!-- SPDX-License-Identifier: Apache-2.0 -->
# Contributing

Import the root `pom.xml` into Consulo and use JDK 25.

- Build with `mvn package -T 1C -Dmaven.test.skip=true`.
- Use `BUILD_NUMBER=SNAPSHOT` with the build command to create the installable archive in `plugin/target/`; `core` and `debugger` are bundled libraries.
- Declare development plugin dependencies in the Consulo Maven plugin configuration; use the inherited packaging properties.
- Edit the `.bnf` and `.flex` sources rather than generated Java under `target/`.
- Keep the core independent of optional integrations; add integrations in separate modules.
- Keep LSP excluded; Editor Services is used only by the optional DAP integration.
- Put themed icons in `ICON-LIB` and use the generated `PowerShellIconGroup` accessors.
- Tests are converted but disabled by default while the platform test APIs are incomplete.

Keep unused upstream maintenance scripts, migration verification tools, and deferred
API ports in `legacy/`. Runtime code and resources belong in the active modules.
Do not duplicate migrated classes, grammars, resources, or build files there.
