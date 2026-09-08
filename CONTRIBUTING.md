# Contributing

Thank you for your interest in contributing to BentoBox Bedrock Companion!

## Development Environment

- Java 25
- Purpur 26.2
- BentoBox 3.22.2+
- Geyser
- Floodgate
- Optional integration test addons: AOneBlock, ChunkBlock, Level, Warps,
  Challenges, Visit, and Bank

Gradle downloads dependencies from the configured Maven repositories on the
first build. Build and run the test suite with:

```bash
./gradlew clean build --no-daemon
```

The current suite contains pure-logic permission and amount tests plus a class
isolation test that verifies the plugin's non-Vault startup path. Changes to
Bukkit, Floodgate, BentoBox forms, or economy transactions still require a
separate test server check.

For game-mode and addon lifecycle changes, verify the server log after a full
restart. In particular, ChunkBlock requires Level, and a missing hard
dependency prevents ChunkBlock from being registered as a game mode. Also
verify the Bedrock forms from a neutral world and from each enabled game-mode
world.

For optional-integration changes, test both the complete installation and the
relevant missing dependency. Warps, Challenges, Visit, and Bank controls must
disappear when their addon is unavailable. Without Vault, economy controls
must remain hidden; without PlaceholderAPI, `%bbc_*%` values are unavailable;
without LuckPerms, the group label must use its safe fallback. None of these
cases may prevent the plugin's unrelated features from starting.

## Project Goals

- Improve the Bedrock Edition experience.
- Keep the Java Edition experience unchanged.
- Do not modify BentoBox source code.
- Prefer Bedrock Forms over Chest GUI.
- Integrate with existing plugins whenever possible.

## Code Style

- Use meaningful class and method names.
- Keep code clean and easy to read.
- Add comments only when necessary.

## Commit Messages

Please follow these prefixes:

- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation
- `refactor:` Code refactoring
- `test:` Testing
- `build:` Build system
- `chore:` Maintenance

Example:

```text
feat: add first Bedrock form
fix: detect Floodgate players correctly
docs: update README
```

## Pull Requests

- Keep each pull request focused on a single feature or fix.
- Update documentation when needed.
- Ensure the project builds successfully before submitting.
- For a version change, inspect `plugin.yml` inside the built JAR and confirm it
  matches `gradle.properties`.
