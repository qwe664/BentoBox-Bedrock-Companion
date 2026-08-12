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
- [ ] Not covered: the 91 protection flags' names/descriptions in
      `menu/ProtectionCategories.java` are still Traditional Chinese only —
      treated as BentoBox-side content data, same as challenge/warp names,
      out of scope for form-UI localization

## v1.0.0 — Stable Release

### Goals

- Native Bedrock Forms for every BentoBox interaction Bedrock players need
- No changes to the Java Edition experience
- Multi-language support finished across all forms
- Documentation complete

### Remaining

- [ ] Documentation pass
- [ ] Decide whether to translate `menu/ProtectionCategories.java` content
- [ ] Stable release
