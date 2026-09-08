# Development History

This document summarizes the complete Git history of BentoBox Bedrock
Companion from project initialization through the first stable release.

## Overview

- First commit: July 28, 2026 at 16:36:31 (Asia/Taipei)
- First commit subject: `Initial commit`
- First stable release: `v1.0.0`
- Development period covered: July 28 through September 8, 2026
- Active commit days: 17
- Total commits: 245
- Non-merge commits: 217
- Merge commits: 28
- Release tags: 13
- Files in the `v1.0.0` tree: 93
- Growth from the initial commit: approximately 9,099 inserted lines

All 245 commits are reachable from the `main` branch at `v1.0.0`.

## Contributors

Git records contain the following author identities:

| Author identity | Commits |
|---|---:|
| qwe664 (GitHub noreply address) | 188 |
| qwe664 (project email address) | 40 |
| root | 11 |
| Claude | 6 |

The two qwe664 identities represent the same maintainer and account for 228
commits combined.

## Daily Timeline

Dates without commits are omitted. Small experimental, formatting, and
documentation commits from the same day are grouped into their resulting
feature or milestone.

### July 28, 2026 — Project foundation (35 commits)

- Created the repository and initial project structure.
- Added Gradle Kotlin DSL, Java configuration, the Paper, BentoBox, and
  Floodgate dependencies, and the Gradle Wrapper.
- Added `plugin.yml` and the main plugin lifecycle class.
- Created `FloodgateHook`, `FormManager`, and the player join listener.
- Added the first native Bedrock form infrastructure.
- Added the GitHub Actions build workflow.
- Added README, CHANGELOG, API, architecture, roadmap, TODO, contribution, and
  license documentation.

### July 29, 2026 — Core forms and services (79 commits)

- Updated the project toolchain to Java 25.
- Completed and merged the `V0.1.0` milestone.
- Added the `/bbc` command and command registration.
- Built the main menu, island menu, and shared form base.
- Added BentoBox service access and island-related command execution.
- Added dynamic menu registration, menu categories, and permission checks.
- Added administrator and developer menus.
- Added environment diagnostics and the initial debug tools.
- Added delayed command execution and forwarding for island settings.

### August 1, 2026 — Developer tools (21 commits)

- Expanded `/bbc debug` with plugin and API inspection commands.
- Added `ConsoleLogger` and reflection-based method inspection.
- Added reflection aliases and declared-method filtering.
- Added an API explorer for development against external plugins.
- Merged the Developer Tools and Reflection Explorer work.

### August 3, 2026 — BentoBox command integration (14 commits)

- Added the BentoBox API service layer.
- Added `BaseCommand` and `HelpCommand`.
- Added player command interception.
- Added the first island settings form and `/is settings` interception.
- Updated README, Roadmap, and TODO to reflect completed phases.

### August 4, 2026 — Island settings and protection (13 commits)

- Corrected BentoBox Flag API usage, permissions, and CI toolchain settings.
- Expanded the island settings form to 11 setting flags.
- Adopted BentoBox's separate natural and spawner spawn flags.
- Added 96 rank-based protection flags across 9 categories.
- Added flag descriptions, usage hints, and missing crop, sign, bell, candle,
  and harvesting flags.
- Added the island information form.
- Merged and tagged `v0.2.0-Beta`.
- Included several temporary Git credential workflow verification commits.

### August 5, 2026 — Team entry and Java forwarding (2 commits)

- Added the team button and complete `/bbc help` command list.
- Preserved native `/is` forwarding for Java Edition players.
- Updated the version to `0.3.0-Beta`.

### August 6, 2026 — Administration and LuckPerms (5 commits)

- Merged the `0.3.0-Beta` work through PR #10.
- Removed dead code and added a Reflection menu button.
- Began LuckPerms integration.
- Added administrator island teleport and plugin-settings reload actions.
- Advanced development to `0.5.1-Beta` and cleaned up the admin form.

### August 7, 2026 — Menu item binding (3 commits)

- Updated README and TODO progress.
- Added direct compass right-click binding for the main menu.
- Removed the need for a third-party item plugin to open the menu.

### August 8, 2026 — Warps integration (9 commits)

- Replaced local BentoBox libraries with Maven dependencies.
- Added and corrected the Warps addon dependency.
- Implemented island warp browsing and management.
- Fixed LuckPerms primary-group detection.
- Merged Warps through PR #12 and updated its documentation status.

### August 9, 2026 — Challenges and multiple game modes (7 commits)

- Localized the main-menu welcome text and prepared `v0.5.3-Beta`.
- Added Challenges support in `v0.6.0-Beta`.
- Fixed dynamic command alias lookup for AOneBlock and ChunkBlock.
- Added current-game-mode display and return-to-lobby behavior.
- Merged and tagged `v0.6.1-Beta`.

### August 10, 2026 — Team, PlaceholderAPI, and Visit (11 commits)

- Added native team management for invitations, kicks, promotions, and
  ownership transfer.
- Added administrator and debug information commands.
- Added the initial seven `%bbc_*%` PlaceholderAPI placeholders.
- Added the Visit island browser.
- Merged the `v0.7.0-Beta`, `v0.8.0-Beta`, and `v0.9.1-Beta` development work.

### August 11, 2026 — Configuration, economy, and localization (8 commits)

- Added the reloadable configuration file and related documentation.
- Added Vault economy integration.
- Added personal-wallet display and wallet-to-island-bank transfers.
- Began English and Traditional Chinese localization with the main and island
  menus.
- Merged and tagged `v0.10.0-Beta` and `v0.11.0-Beta`.

### August 12, 2026 — Full form localization and tests (18 commits)

- Localized team, invitation, Warps, protection, island information, bank,
  Challenges, and administrator forms.
- Fixed translated challenge color codes and challenge progress calculation.
- Synchronized README, CHANGELOG, Roadmap, TODO, API, and requirement versions.
- Added the JUnit 5 test infrastructure and the first pure-logic unit tests.
- Localized the remaining six text-bearing forms.
- Merged PRs #20 through #25 and advanced through the `v0.11.x-Beta` and
  `v0.12.0-Beta` milestones.

### August 21, 2026 — Protection localization complete (3 commits)

- Localized all 96 protection flag names and descriptions.
- Localized all 9 protection category titles.
- Synchronized the development history and merged PR #26.
- Tagged the completed localization release as `v0.12.1-Beta`.

### September 6, 2026 — Settings authorization fix (1 commit)

- Fixed a Bedrock island-settings authorization bypass.
- Updated the development version to `0.12.2-Beta`.

### September 7, 2026 — Security and transaction hardening (11 commits)

- Changed unauthorized settings forms to an explicit read-only completion
  flow.
- Added a dedicated island settings-access-rank entry.
- Revalidated island identity, live rank, permissions, cooldowns, and original
  values when submitted forms are processed.
- Checked Vault deposit, withdrawal, and refund results and added compensation
  for partial transaction failures.
- Isolated the optional Vault API so the plugin can start without Vault.
- Handled Bank addons whose `BankManager` is not initialized yet.
- Rejected `NaN`, positive infinity, and negative infinity as money amounts.
- Routed intercepted settings commands through the selected game-mode world.
- Consolidated these changes into `0.13.0-Beta`.
- Added the BentoBox startup health check and moved final checks to
  `BentoBoxReadyEvent` for `v0.13.3-beta`.
- Merged `v0.13.3-beta` into `main`.

### September 8, 2026 — Addon lifecycle and stable release (5 commits)

- Deferred optional BentoBox addon detection until BentoBox completed addon
  loading.
- Switched Warps, Challenges, Visit, and Bank discovery to BentoBox's
  `AddonsManager` and final addon state.
- Added runtime addon enable and disable diagnostics.
- Added null and readiness guards for Bank and `BankManager`.
- Completed the final prerelease documentation and merged `v0.13.5-beta`.
- Removed the beta suffix, synchronized all project documents, and prepared
  `1.0.0`.
- Verified the full installation and missing optional dependency scenarios.
- Merged `develop` into `main` through PR #28.
- Tagged and published the first stable release, `v1.0.0`.

## Release Tags

The repository contains the following release tags in chronological order:

1. `V0.1.0`
2. `v0.2.0-Beta`
3. `v0.5.3-Beta`
4. `v0.6.1-Beta`
5. `v0.8.0-Beta`
6. `v0.9.1-Beta`
7. `v0.10.0-Beta`
8. `v0.11.0-Beta`
9. `v0.11.4-Beta`
10. `v0.12.1-Beta`
11. `v0.13.3-beta`
12. `v0.13.5-beta`
13. `v1.0.0`

Some intermediate versions were recorded in commits without receiving a
separate Git tag.

## Development Phases

### Foundation — July 28 through August 3

Established the build system, plugin lifecycle, Floodgate detection, native
form framework, commands, permissions, services, and developer tools.

### Feature implementation — August 4 through August 11

Completed island settings, protection flags, team management, Warps,
Challenges, Visit, PlaceholderAPI, configuration, and Vault economy support.

### Localization and tests — August 12 through August 21

Completed English and Traditional Chinese localization for all forms and
protection flags, synchronized documentation, and introduced automated tests.

### Hardening — September 6 through September 7

Fixed settings authorization, stale form submission, economy transaction
safety, optional Vault linkage, non-finite money input, cross-game-mode world
selection, and Bank readiness handling.

### Stable release — September 8

Completed addon lifecycle integration, optional dependency fallback testing,
documentation, the `develop` to `main` release PR, and the `v1.0.0` release.

## v1.0.0 Verification

- All 27 automated tests passed.
- The test server loaded BentoBox Bedrock Companion `1.0.0` successfully.
- BentoBox reported all 9 registered addons as `ENABLED`.
- Bank and `BankManager` initialized successfully.
- Warps, Challenges, Visit, Bank, Vault, PlaceholderAPI, and LuckPerms were
  each removed and tested independently.
- Missing optional integrations hid or degraded only their related features.
- Island settings permissions, cross-game-mode settings, scoreboard values,
  bank transfers, and Bedrock forms were verified in game.

After `v1.0.0`, the project moved from feature development into compatibility
maintenance, bug fixes, and future feature work.
