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

Note: the 91 protection flags' names/descriptions themselves
(`menu/ProtectionCategories.java`, ~200 strings) are still Traditional
Chinese only, same as challenge/warp *content* configured by the server
admin — this project's `LocaleService` covers form UI chrome, not
BentoBox-side data content. Translating `ProtectionCategories.java` is a
separate, larger follow-up if wanted.

## Phase 6 — Release v1.0

- [ ] Documentation pass (README, CONTRIBUTING, docs/API.md up to date)
- [ ] Decide whether to translate `menu/ProtectionCategories.java` content
- [ ] Release v1.0

## Testing

- [x] JUnit 5 test infrastructure (`build.gradle.kts`, `src/test/`)
- [x] Unit tests for pure-logic utility classes: `ProgressBarUtil`, `ColorUtil`, `ReflectionAliases`
- [ ] Unit tests for Bukkit-dependent classes (would need MockBukkit — `MenuItem`, hooks, services)
