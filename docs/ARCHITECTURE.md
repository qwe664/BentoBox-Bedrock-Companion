# BentoBox Bedrock Companion Architecture

## Objective

Provide Bedrock Edition players with native Bedrock Forms while keeping the Java Edition experience unchanged.

## Player Flow

```mermaid
flowchart TD
    Java[Java player] --> Native[BentoBox original commands and GUI]
    Bedrock[Bedrock player] --> Floodgate[FloodgateHook detection]
    Floodgate --> Forms[FormManager and Bedrock forms]
    Forms --> Services[BBC services and guarded hooks]
    Services --> BentoBox[BentoBox API and optional addons]
```

## Modules

### Core
Plugin startup and dependency management.

### FloodgateHook
Detect Bedrock players.

### FormManager
Create and manage Bedrock Forms.

### LocaleService
Resolve `zh-TW` or `en-US` from each player's BentoBox language preference and
serve the same translations to forms, commands, placeholders, and rank
fallbacks. Server-console commands use `zh-TW`. Existing locale files receive
new bundled keys without losing administrator customizations.

### BentoBoxService
Resolve game modes, worlds, islands, and BentoBox data. Cross-game-mode form
requests carry the selected world instead of inferring it from player location.

### SettingsAccess
Mirror BentoBox's command, per-flag, `CHANGE_SETTINGS`, OP, and admin permission
rules. Forms re-check these rules and the island by ID at submission time, then
validate the whole batch before changing any flags.

### BentoBoxReadyListener
Run the final dependency health check from BentoBox's `BentoBoxReadyEvent`,
after `AddonsManager` has finished enabling addons. It verifies the main plugin,
logs every addon's version and state, reports the enabled count, and checks that
Bank has produced a usable `BankManager`.

### BentoBoxAddonListener
Track `AddonEnableEvent` and `AddonDisableEvent` after startup. BentoBox remains
responsible for loading its addons; BBC only logs lifecycle changes and its
hooks query the current enabled state before exposing addon-backed features.

### Optional hooks
Resolve Warps, Challenges, Visit, Bank, LuckPerms, PlaceholderAPI, and economy
features only when their providers are available. `EconomyHook` prevents core
classes from linking directly to Vault; `VaultHook` is loaded reflectively and
`UnavailableEconomyHook` supplies the disabled behavior.

Forms query hook availability when they are built, so controls backed by a
missing or disabled addon disappear without affecting unrelated controls.
Placeholder and permission-group integrations also use safe unavailable
behavior when PlaceholderAPI or LuckPerms is absent.

### Commands
Plugin commands.

### Listeners
Inventory and player events.

## Dependencies

- Paper
- BentoBox
- Floodgate
- Geyser
- Cumulus
- PlaceholderAPI (Optional)
- LuckPerms (Optional)
- Vault (Optional)
- Vault-compatible economy provider (Optional; needed for wallet features)
- BentoBox Warps, Challenges, Visit, and Bank addons (Optional)

## Principles

- No BentoBox source modifications.
- Java players keep the original experience.
- Bedrock players use native Forms.
- Prefer official APIs over reflection.
- Treat form input as stale by submission time and revalidate mutable state.
- Validate both sides of multi-system money transfers and compensate partial failures.
- Use the owning platform's ready event for final integration checks.
- Keep optional integrations isolated and verify their missing-dependency paths.
