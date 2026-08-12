# Changelog

All notable changes to this project will be documented in this file.

This project has not cut a stable `1.0.0` release yet; versions below are the
`-Beta` tags used during active `develop` work (see `gradle.properties` for
the current version).

## [Unreleased] — working toward v1.0

### Planned
- Documentation pass.
- Decide whether to translate `menu/ProtectionCategories.java`'s 91 flag names/descriptions (currently Traditional Chinese only, same status as challenge/warp content).
- Stable `v1.0.0` release.

## [0.12.0-Beta] — Multi-language support complete

- `feat:` add unit tests (JUnit 5) for `ProgressBarUtil`, `ColorUtil`, `ReflectionAliases` — the project's first automated tests.
- `feat:` localize the remaining 6 text-bearing forms: Admin Island Teleport, Debug Menu, Game Mode Picker, Protection Category, Settings Menu, Visit Browse. (`Base Form` is an abstract base class with no UI text.)
- All form UI text is now localized across `en-US` / `zh-TW`.

## [0.11.x-Beta] — Multi-language support rollout

- `fix:` correct challenge level progress bar calculation.
- `fix:` translate & color codes in challenge names via `ColorUtil`.
- `feat:` localize Challenges series + admin challenges import (`v0.11.8-Beta`).
- `feat:` localize Team Member Action, Team Invite, Warps; fix kick UX (`v0.11.6-Beta`).
- `feat:` localize Protection Menu, Island Info, Wallet Bank (`v0.11.4-Beta`).
- `feat:` localize Team Menu; `LocaleService` auto-merge fix (`v0.11.3-Beta`).
- `feat:` localize Main Menu, Island Menu — first `en-US`/`zh-TW` locale files (`v0.11.2-Beta`).

## [0.11.0-Beta] — Vault integration

- `feat:` Vault integration — personal wallet display, wallet ↔ island bank transfer.

## [0.10.0-Beta] — Configuration file

- `feat:` configuration file support (customizable messages & feature toggles, reloadable in-game).

## [0.9.x-Beta] — PlaceholderAPI & Visit Menu

- `feat:` Visit Menu integration (`v0.9.1-Beta`).
- `feat:` PlaceholderAPI integration — 7 `%bbc_*%` placeholders (`v0.8.1`–`v0.8.2-Beta`).

## [0.8.0-Beta] — Admin & debug tooling

- `feat:` debug info commands (`plugins`, `api`, `island`, `flags`, `version`).
- Admin Forms (island lookup/teleport, config reload).

## [0.7.0-Beta] — Team Management

- `feat:` native `TeamMenuForm` for island team management (invite/kick/promote/transfer ownership).

## [0.6.x-Beta] — Challenges

- `feat:` Challenges support (`v0.6.0-Beta`).
- `fix:` AOneBlock/ChunkBlock command alias dynamic lookup; add "back to lobby" and current gamemode display (`v0.6.1-Beta`).

## [0.5.x-Beta] — Warps

- `feat:` Island Warp support; fix `WarpsHook` / LuckPerms primary group detection.
- Menu item binding (right-click compass opens the main menu, no third-party item plugin required).

## [0.4.0-Beta] — LuckPerms & Reflection tooling

- `feat:` Reflection explorer button; start LuckPerms integration; remove dead code.

## [0.2.0–0.3.0-Beta] — Island Settings & Flags

- `feat:` island protection panel — 91 rank-based flags across 9 categories.
- `feat:` per-flag descriptions and usage hints; 5 additional rank-based flags (harvest, crop planting, sign editing, bell ringing, candles).
- `feat:` island info form (owner, members, island stats).
- `feat:` expand island settings form to 11 SETTING-type flags.
- Team button, full `/bbc help` command list, Java-edition `/is` command forwarding.

## [0.1.0-Beta] — Project foundation

- Initial Gradle project setup.
- Floodgate detection.
- Native Bedrock Form framework.
- First test form.
