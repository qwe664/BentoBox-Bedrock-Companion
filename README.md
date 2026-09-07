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
- Multi-language support (`en-US`, `zh-TW`) for every form and all 96 protection flag names/descriptions

### Planned

- Documentation
- Stable release

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

1. Install BentoBox.
2. Install Geyser.
3. Install Floodgate.
4. (Optional) Install LuckPerms for player group display.
5. (Optional) Install Bank for island balances. To enable wallet transfers, also install Vault and a Vault-compatible economy provider such as EssentialsX Economy.
6. Place the BentoBox Bedrock Companion plugin into the `plugins` folder.
7. Restart the server.
8. (Optional) Edit `plugins/BentoBoxBedrockCompanion/config.yml` to customize messages and toggle features, then use "Admin Tools → Reload plugin settings" in-game or restart to apply.

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

### v1.0

- [ ] Documentation
- [ ] Stable release

---

## Development Principles

- Keep Java Edition gameplay unchanged.
- Bedrock players should use native Bedrock Forms whenever possible.
- Avoid modifying upstream BentoBox plugins.
- Prefer official BentoBox APIs over internal implementations.
- Keep the project modular and easy to maintain.

---

## Status

🚧 Active Development — v0.13.0-Beta hardens island-setting authorization, multi-game-mode routing, and optional economy integrations. Documentation and stable-release work continue toward v1.0.

---

## License

To be decided.
