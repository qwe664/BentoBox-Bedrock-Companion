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
- Team Management (delegates to BentoBox `/is team`)
- Island Flags (91 protection flags, categorized menu)
- Admin Tools (island lookup/teleport, config reload)
- LuckPerms integration (displays player's primary group)
- Developer/Debug tools (environment info, reflection explorer)

### Planned

- Warps
- Challenges
- Visit Menu
- PlaceholderAPI support
- Vault integration
- Configuration file
- Multi-language support

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
| Java | 21+ |
| Paper / Purpur | Latest |
| BentoBox | 3.20.0+ |
| Floodgate | Required |
| Geyser | Required |
| LuckPerms | Optional |

---

## Installation

1. Install BentoBox.
2. Install Geyser.
3. Install Floodgate.
4. (Optional) Install LuckPerms for player group display.
5. Place the BentoBox Bedrock Companion plugin into the `plugins` folder.
6. Restart the server.

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

### v0.5

- [ ] Warps

### v0.6

- [ ] Challenges

### v0.7

- [ ] Visit Menu

### v0.8 ✅

- [x] Admin Forms

### v0.9

- [ ] PlaceholderAPI support
- [x] LuckPerms integration
- [ ] Vault integration

### v1.0

- [ ] Complete Bedrock Companion support
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

🚧 Active Development — core island management features complete, Warps/Challenges/Visit menu pending.

---

## License

To be decided.
