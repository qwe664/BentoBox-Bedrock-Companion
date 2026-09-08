# Roadmap

This roadmap tracks the actual development progress of BentoBox Bedrock
Companion. It mirrors the "Roadmap" section in the project [README](../README.md);
the README is the canonical, most up-to-date source — this file adds a bit
more detail per milestone.

---

## v0.1 ✅ Project Foundation

- [x] Gradle project setup
- [x] Floodgate detection
- [x] Native Bedrock Form framework
- [x] First test form

## v0.2–v0.3 ✅ Island Settings & Flags

- [x] Island protection panel (91 rank-based flags, 9 categories)
- [x] Island Settings (11 SETTING-type flags via native forms)
- [x] Island Information (owner, members, size, creation date)

## v0.4 ✅ Island Flags foundation

- [x] Reflection explorer / developer tools
- [x] LuckPerms integration started

## v0.5 ✅ Warps

- [x] Warps (browse & manage island warp points)
- [x] Menu item binding (right-click compass opens the main menu)

## v0.6 ✅ Challenges

- [x] Challenges support

## v0.7 ✅ Team Management

- [x] Native form: invite / kick / promote / transfer ownership

## v0.8 ✅ Admin Forms

- [x] Admin Tools (island lookup/teleport, config reload)
- [x] Debug info commands (`plugins`, `api`, `island`, `flags`, `version`)

## v0.9 ✅ PlaceholderAPI & Visit Menu

- [x] PlaceholderAPI support (`%bbc_*%` variables)
- [x] Visit Menu (browse & visit other players' islands)

## v0.10 ✅ Configuration file

- [x] Config file with customizable messages & feature toggles

## v0.11 ✅ Vault integration

- [x] Personal wallet display, wallet ↔ island bank transfer

## v0.12.0 ✅ Multi-language support complete

- [x] `LocaleService` with `en-US` / `zh-TW` locale files, auto-merge fallback
- [x] All 22 text-bearing forms localized (Main Menu, Island Menu, Team Menu,
      Team Member Action, Team Invite Picker, Warp Menu/Browse/Manage,
      Protection Menu, Island Info, Wallet/Bank, Challenges Menu/Detail/Level,
      Admin Menu, Admin Challenges Import, Admin Island Teleport, Debug Menu,
      Game Mode Picker, Protection Category, Settings Menu, Visit Browse) —
      `Base Form` is an abstract base class with no UI text, so it needed no
      localization
- [x] All 96 protection flag names/descriptions and 9 category titles localized
      through `protection_flags.*` and `protection_categories.*` keys

## v0.13.0 ✅ Settings and economy hardening

- [x] Match BentoBox's settings command, per-flag, `CHANGE_SETTINGS`, OP, and
      admin permission rules
- [x] Revalidate the island and all proposed changes at form submission; make
      unauthorized entries read-only and reject the whole batch on conflict
- [x] Preselect protection ranks and add a dedicated setting-management rank entry
- [x] Check Vault transaction results and compensate partial transfer failures
- [x] Support startup without Vault and an installed Bank addon whose manager is
      not ready
- [x] Reject non-finite money amounts
- [x] Route intercepted settings commands to the selected game-mode world

## v0.13.3 ✅ BentoBox startup health check

- [x] Move the final Bank check to `BentoBoxReadyEvent`
- [x] Verify the BentoBox main plugin after addon initialization
- [x] Log every registered addon's name, version, and final state
- [x] Report enabled/total counts and warn for non-`ENABLED` addons
- [x] Confirm `BankManager` readiness separately from the Bank addon state

## v0.13.4 ✅ BentoBox addon lifecycle integration

- [x] Let BentoBox manage optional addon loading
- [x] Remove BentoBox addons from Bukkit `softdepend`
- [x] Track addon enable/disable events at runtime
- [x] Treat disabled addons as unavailable in integration hooks

## v0.13.5 ✅ Final beta before v1.0

- [x] Complete the documentation pass
- [x] Document game-mode addon dependencies and installation layout
- [x] Verify release metadata for the final beta

## v1.0.0 — Stable Release

### Goals

- Native Bedrock Forms for every BentoBox interaction Bedrock players need
- No changes to the Java Edition experience
- Multi-language support finished across all forms
- Documentation complete

### Remaining

- [ ] Stable release
