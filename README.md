# BentoBox Bedrock Companion

A companion plugin that improves the experience of **Minecraft Bedrock Edition** players when using **BentoBox**, while keeping the original Java Edition gameplay unchanged.

This project integrates with the BentoBox API instead of modifying BentoBox itself, allowing Bedrock players to interact with BentoBox through native Bedrock Forms.

---

## Features

### Current

- Floodgate detection
- Geyser compatibility
- Native Bedrock Form framework
- Island Information (owner, members, size, creation date)
- Island Settings (13 setting toggles with BentoBox permission and rank checks)
- Team Management (native form: invite/kick/promote/transfer ownership)
- Island Flags (96 localized protection flags across 9 categorized menus)
- Warps (browse & manage island warp points)
- Challenges support
- Visit Menu (browse & visit other players' islands)
- Bank integration (island balance display; safely disabled while its manager is unavailable)
- Optional Vault economy integration (personal wallet display and checked wallet ↔ island bank transfers)
- Admin Tools (island lookup/teleport, config reload)
- LuckPerms integration (displays player's primary group)
- PlaceholderAPI support (%bbc_*% variables)
- Configuration file (customizable messages & feature toggles)
- Developer/Debug tools (environment info, reflection explorer)
- BentoBox startup health check (main plugin, every addon state, and Bank manager readiness)
- Dynamic game-mode selection for enabled BentoBox game modes (tested with AOneBlock and ChunkBlock)
- Runtime tracking for BentoBox addon enable/disable events
- Multi-language support (`en-US`, `zh-TW`) for forms, commands, placeholders,
  rank fallbacks, and all 96 protection flag names/descriptions

### Planned

- Continued compatibility updates and maintenance

---

## Design Goals

- Improve the Bedrock Edition experience.
- Preserve the original Java Edition experience.
- Never modify BentoBox source code.
- Integrate through the BentoBox API whenever possible.
- Replace inventory/chest GUIs with native Bedrock Forms.
- Maintain compatibility with future BentoBox updates.
- Prefer official APIs over reflection wherever possible.

---

## Requirements

| Component | Version |
|-----------|---------|
| Java | 25+ |
| Paper / Purpur | 26.2 |
| BentoBox | 3.22.2+ (tested against 3.22.2) |
| Floodgate | Required |
| Geyser | Required |
| AOneBlock | Optional game mode |
| ChunkBlock | Optional game mode; requires the Level addon |
| Level | Optional; required by ChunkBlock |
| LuckPerms | Optional |
| Warps | Optional |
| Challenges | Optional |
| Visit | Optional |
| Bank | Optional |
| Vault | Optional |
| Vault economy provider (for example EssentialsX Economy) | Optional; required for wallet features |
| PlaceholderAPI | Optional |

---

## Installation

1. Install BentoBox, then start the server once so BentoBox creates its folders.
2. Install Geyser and Floodgate.
3. (Optional) Install AOneBlock or ChunkBlock in `plugins/BentoBox/addons/`.
   ChunkBlock requires the `Level` addon in the same directory.
4. (Optional) Install Warps, Challenges, Visit, or Bank in
   `plugins/BentoBox/addons/` for their corresponding Bedrock features.
5. (Optional) Install LuckPerms for player group display.
6. (Optional) Install Vault and a Vault-compatible economy provider such as
   EssentialsX Economy. Bank, Vault, and an economy provider are all needed
   for wallet ↔ island bank transfers.
7. Place the BentoBox Bedrock Companion plugin into the `plugins` folder.
8. Restart the server and check the startup log for BentoBox and addon states.
9. (Optional) Edit `plugins/BentoBoxBedrockCompanion/config.yml` to customize
   messages and feature toggles, then use "Admin Tools → Reload plugin
   settings" in-game or restart to apply.

Missing optional addons do not prevent BBC from starting. Their related menu
items are hidden, while the remaining features continue to work. `Level` is
not required by BBC itself; it is required when ChunkBlock is installed.
The optional-integration fallbacks have been verified individually for Warps,
Challenges, Visit, Bank, Vault, PlaceholderAPI, and LuckPerms.

---

## Roadmap

### v0.1 ✅

- [x] Project initialization
- [x] Floodgate detection
- [x] Form framework
- [x] Test Form

### v0.2 ✅

- [x] Island Settings

### v0.3 ✅

- [x] Team Management

### v0.4 ✅

- [x] Island Flags

### v0.5 ✅

- [x] Warps

### v0.6 ✅

- [x] Challenges

### v0.7 ✅

- [x] Visit Menu

### v0.8 ✅

- [x] Admin Forms

### v0.9 ✅

- [x] PlaceholderAPI support
- [x] LuckPerms integration

### v0.10 ✅

- [x] Configuration file

### v0.11 ✅

- [x] Vault integration

### v0.12.0 ✅

- [x] Multi-language support (`en-US`, `zh-TW`) — all forms localized

### v0.13.0 ✅

- [x] Enforce BentoBox setting permissions and the island's `CHANGE_SETTINGS` rank
- [x] Revalidate island identity, permissions, ranks, cooldowns, and original values when a form is submitted
- [x] Preserve current protection ranks in dropdowns and expose the setting-management rank in Admin Tools
- [x] Validate economy amounts and transaction results, including compensating failed transfers
- [x] Keep Vault truly optional and tolerate an unavailable Bank manager
- [x] Open intercepted settings commands for the selected game mode's world

### v0.13.3 ✅

- [x] Wait for BentoBox's official ready event before checking Bank
- [x] Verify the BentoBox main plugin and every registered addon's final state
- [x] Report addon versions, enabled totals, failures, and BankManager readiness

### v0.13.4 ✅

- [x] Let BentoBox manage optional addon loading
- [x] Track addon enable and disable events at runtime
- [x] Hide addon-backed features when their addon is disabled

### v1.0.0 ✅ Stable release

- [x] Complete the documentation pass
- [x] Document game-mode addon dependencies and installation layout
- [x] Verify optional-addon and optional-plugin fallback behavior
- [x] Remove the prerelease suffix and verify the stable build metadata
- [x] Prepare the first stable release

### v1.0.1 🚧 Maintenance

- [x] Localize the remaining player-facing command and placeholder fallbacks
- [x] Use the player's BentoBox locale for command output
- [x] Add automated locale parity and placeholder-token checks

### v1.0.2 🚧 Compatibility maintenance

- [x] Prepare resource processing for Gradle 10 and configuration cache
- [ ] Replace deprecated Bukkit/Paper plugin metadata and item-name APIs
- [ ] Plan migration from legacy `ChatColor` output to Adventure components

---

## Development Principles

- Keep Java Edition gameplay unchanged.
- Bedrock players should use native Bedrock Forms whenever possible.
- Avoid modifying upstream BentoBox plugins.
- Prefer official BentoBox APIs over internal implementations.
- Keep the project modular and easy to maintain.

---

## Status

✅ Stable — v1.0.0 is the current published release. Development has advanced
to v1.0.2.

---

## License

To be decided.
