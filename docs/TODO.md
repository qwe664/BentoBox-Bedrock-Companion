## Phase 1 ✅

- [x] Create Gradle project
- [x] Load plugin
- [x] Floodgate detection
- [x] Test Form

## Phase 2 ✅

- [x] Read BentoBox settings
- [x] Open Bedrock Form from /is
- [x] Intercept /is settings

## Phase 3 ✅

- [x] Island Information
- [x] Island Settings
- [x] Member Management
- [x] Island Flags

## Phase 4 ✅

- [x] Island Warp
- [x] Challenges
- [x] Admin Forms
- [x] Island Visit
- [x] Configuration file
- [x] PlaceholderAPI support
- [x] Vault integration

## Phase 5 — Multi-language support ✅

- [x] `LocaleService` (`en-US` / `zh-TW`, auto-merge fallback)
- [x] Main Menu, Island Menu, Team Menu, Team Member Action, Team Invite Picker
- [x] Warp Menu / Browse / Manage
- [x] Protection Menu, Island Info, Wallet/Bank
- [x] Challenges Menu / Detail / Level, Admin Challenges Import
- [x] Admin Island Teleport Form
- [x] Base Form (no user-facing text — nothing to localize)
- [x] Debug Menu Form
- [x] Game Mode Picker Form
- [x] Protection Category Form (form UI chrome only — see note below)
- [x] Settings Menu Form
- [x] Visit Browse Form

## Phase 5.1 — v0.13.0 hardening ✅

- [x] Enforce command, per-flag, and live `CHANGE_SETTINGS` authorization
- [x] Revalidate island identity, permissions, cooldowns, and original values on submit
- [x] Preselect current protection ranks and preserve custom unmapped ranks as read-only
- [x] Add a dedicated form entry for the setting-management rank
- [x] Check economy transaction/refund results and compensate partial failures
- [x] Keep Vault optional and handle an unavailable Bank manager
- [x] Reject non-finite money amounts
- [x] Use the selected game-mode world for intercepted settings commands

## Phase 5.2 — v0.13.3 startup health check ✅

- [x] Wait for `BentoBoxReadyEvent` before the final integration check
- [x] Verify the BentoBox main plugin and all registered addon states
- [x] Log addon names, versions, enabled totals, and failed states
- [x] Confirm Bank addon and `BankManager` readiness separately

## Phase 5.3 — v0.13.4 addon lifecycle integration ✅

- [x] Let BentoBox manage optional addon loading
- [x] Remove BentoBox addons from Bukkit `softdepend`
- [x] Track addon enable/disable events at runtime
- [x] Treat disabled addons as unavailable in integration hooks

## Phase 6 — Release v1.0

- [x] Final beta documentation pass (README, CONTRIBUTING, docs/API.md up to date)
- [ ] Release v1.0

## Testing

- [x] JUnit 5 test infrastructure (`build.gradle.kts`, `src/test/`)
- [x] 27 tests covering `ProgressBarUtil`, `ColorUtil`, `ReflectionAliases`,
      `SettingsPermissionPolicy`, `MoneyAmountValidator`, and optional Vault class isolation
- [ ] Unit tests for Bukkit-dependent classes (would need MockBukkit — `MenuItem`, hooks, services)
- [ ] Automated integration tests for Floodgate forms and Bank/Vault failure compensation
