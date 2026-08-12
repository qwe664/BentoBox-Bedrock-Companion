# API Reference

This document records the APIs and plugin integrations used by BentoBox
Bedrock Companion, and where each one is implemented.

---

# Paper API

Required. Purpose:

- Plugin lifecycle
- Commands (`/bbc`)
- Events (`listener/`)
- Scheduler

---

# Floodgate API

Required. Purpose:

- Detect Bedrock Edition players (`hook/FloodgateHook.java`)
- Retrieve Floodgate player information
- Send native Bedrock Forms (via Cumulus, bundled with Floodgate)

---

# Geyser

Required (runs alongside Floodgate). Purpose:

- Bridges Bedrock clients to the Java server; no direct API calls from this
  plugin beyond what Floodgate exposes.

---

# BentoBox API

Required. Purpose:

- Island information, protection flags, and settings (`service/BentoBoxService.java`)
- Team management (invite/kick/promote/transfer ownership)
- Island permissions (91 rank-based protection flags)

## BentoBox Addons (optional, soft-depend)

Each addon is optional and guarded with an `isAvailable()` check before use,
since the server may not have it installed:

- **Warps** (`hook/WarpsHook.java`) — browse & manage island warp points
- **Challenges** (`hook/ChallengesHook.java`) — challenge menus & admin import
- **Bank** (`hook/BankHook.java`) — island balance display
- **Visit** (`hook/VisitHook.java`) — browse & visit other players' islands

Note: the Bukkit plugin name for these addons (e.g. `BentoBox-Bank`) differs
from the BentoBox Addon system name (e.g. `Bank`) — the hooks always resolve
through `getAddonByName(...)`, not `Bukkit.getPluginManager().getPlugin(...)`.

---

# Vault

Optional (soft-depend). Purpose:

- Personal wallet display and wallet ↔ island bank transfer (`hook/VaultHook.java`)
- Requires an economy plugin registered with Vault (e.g. EssentialsX Economy)
  on the server — Vault itself is just the interface.

---

# LuckPerms API

Optional (soft-depend). Purpose:

- Displays the player's primary permission group (`service/LuckPermsService.java`)
- Permission checks themselves go through Bukkit's `Player#hasPermission`,
  which already reflects LuckPerms grants — this integration is only for
  reading the primary group label.

---

# PlaceholderAPI

Optional (soft-depend). Purpose:

- Exposes 7 `%bbc_*%` placeholders (`placeholder/BBCExpansion.java`)

---

# Cumulus

Bundled with Floodgate. Purpose:

- Builds and sends native Bedrock Forms (`manager/FormManager.java`, `form/`)
