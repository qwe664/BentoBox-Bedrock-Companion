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
- Island permissions (96 rank-based protection flags across 9 categories)

Settings forms follow the same authorization layers as BentoBox 3.22.2:

- Opening settings requires `<game-mode-prefix>island.settings`.
- Editing a flag requires `<game-mode-prefix>settings.<flag-id>` or
  `<game-mode-prefix>settings.*`.
- Regular players must also pass the island's live `CHANGE_SETTINGS` rank.
- OPs and `<game-mode-prefix>admin.settings` holders receive BentoBox's
  administrative exception.

`SettingsAccess` centralizes these checks. Both settings forms re-resolve the
island by ID and repeat authorization and original-value checks at submission
time before making any changes. Intercepted cross-game-mode settings commands
carry the selected `World` through `GameModeChoice` so island lookup does not
fall back to the player's current world.

## BentoBox readiness

`listener/BentoBoxReadyListener.java` listens for BentoBox 3.22.2's official
`BentoBoxReadyEvent`. At that point `AddonsManager#allLoaded()` has run, so BBC:

- confirms that the BentoBox main plugin is enabled;
- logs every registered addon's name, version, and `Addon.State`;
- compares the number of `ENABLED` addons with the total; and
- performs the separate `BankManager` readiness check needed by Bank features.

Any addon that has not reached `ENABLED` is logged as a warning. This check uses
the BentoBox lifecycle event because Bukkit's `ServerLoadEvent` occurs before
BentoBox finishes enabling addons on the tested server.

`listener/BentoBoxAddonListener.java` also listens for `AddonEnableEvent` and
`AddonDisableEvent` so reload-time lifecycle changes are visible after the
initial readiness check. BBC does not load or enable addons itself.

## BentoBox Addons (optional, managed by BentoBox)

Each addon is optional and guarded with an `isAvailable()` check before use,
since the server may not have it installed or enabled. They are discovered via
`AddonsManager` and are not declared as Bukkit soft dependencies:

- **Warps** (`hook/WarpsHook.java`) — browse & manage island warp points
- **Challenges** (`hook/ChallengesHook.java`) — challenge menus & admin import
- **Bank** (`hook/BankHook.java`) — island balance display and transfers; the
  hook remains unavailable until `BankManager` has initialized
- **Visit** (`hook/VisitHook.java`) — browse & visit other players' islands

Note: the Bukkit plugin name for these addons (e.g. `BentoBox-Bank`) differs
from the BentoBox Addon system name (e.g. `Bank`) — the hooks always resolve
through `getAddonByName(...)`, not `Bukkit.getPluginManager().getPlugin(...)`.

---

# Vault

Optional (soft-depend). Purpose:

- Personal wallet display and wallet ↔ island bank transfer
- Requires an economy plugin registered with Vault (e.g. EssentialsX Economy)
  on the server — Vault itself is just the interface.
- `EconomyHook` is the API-free boundary used by forms and placeholders.
  `VaultHook` is loaded reflectively only after Vault is enabled;
  `UnavailableEconomyHook` keeps all other BBC features usable without Vault.
- Every debit, credit, and refund result is checked. Partial failures trigger a
  compensating transaction where possible and an error log if both operations
  fail.

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

- Exposes 8 `%bbc_*%` placeholders (`placeholder/BBCExpansion.java`)

---

# Cumulus

Bundled with Floodgate. Purpose:

- Builds and sends native Bedrock Forms (`manager/FormManager.java`, `form/`)
