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
- Island Settings (protection flags via native forms)
- Team Management (native form: invite/kick/promote/transfer ownership)
- Island Flags (91 protection flags, categorized menu)
- Warps (browse & manage island warp points)
- Challenges support
- Visit Menu (browse & visit other players' islands)
- Bank integration (island balance display)
- Vault integration (personal wallet display, wallet ↔ island bank transfer)
- Admin Tools (island lookup/teleport, config reload)
- LuckPerms integration (displays player's primary group)
- PlaceholderAPI support (%bbc_*% variables)
- Configuration file (customizable messages & feature toggles)
- Developer/Debug tools (environment info, reflection explorer)

### In Progress

- Multi-language support (`en-US`, `zh-TW`) — 16 of 23 forms localized so far: Main Menu, Island Menu, Team Menu, Team Member Action, Team Invite Picker, Warp Menu/Browse/Manage, Protection Menu, Island Info, Wallet/Bank, Challenges Menu/Detail/Level, Admin Menu, Admin Challenges Import.
  Remaining: Admin Island Teleport, Base Form, Debug Menu, Game Mode Picker, Protection Category, Settings Menu, Visit Browse.

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
| Paper / Purpur | Latest |
| BentoBox | 3.22.2+ |
| Floodgate | Required |
| Geyser | Required |
| LuckPerms | Optional |
| Warps | Optional |
| Challenges | Optional |
| Visit | Optional |
| Bank | Optional |
| Vault | Optional |
| PlaceholderAPI | Optional |

---

## Installation

1. Install BentoBox.
2. Install Geyser.
3. Install Floodgate.
4. (Optional) Install LuckPerms for player group display.
5. Place the BentoBox Bedrock Companion plugin into the `plugins` folder.
6. Restart the server.
7. (Optional) Edit `plugins/BentoBoxBedrockCompanion/config.yml` to customize messages and toggle features, then use "Admin Tools → Reload plugin settings" in-game or restart to apply.

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

### v0.11.x 🚧 (current: v0.11.10-Beta)

- [ ] Multi-language support (`en-US`, `zh-TW`) — 16/23 forms localized

### v1.0

- [ ] Multi-language support (finish remaining forms)
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

🚧 Active Development — core island management, Warps, Challenges, Visit menu, PlaceholderAPI, configuration file, and Vault integration complete. Multi-language support (`en-US`, `zh-TW`) in progress — 16/23 forms localized as of v0.11.10-Beta.

---

## License

To be decided.
