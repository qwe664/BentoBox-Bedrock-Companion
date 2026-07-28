# BentoBox Bedrock Companion Architecture

## Objective

Provide Bedrock Edition players with native Bedrock Forms while keeping the Java Edition experience unchanged.

## Player Flow

Java Player
    │
    ├── BentoBox Original GUI
    │
    ▼
 No changes

Bedrock Player
    │
    ├── Floodgate Detection
    │
    ▼
 Bedrock Form UI
    │
    ▼
 BentoBox API

## Modules

### Core
Plugin startup and dependency management.

### FloodgateBridge
Detect Bedrock players.

### FormManager
Create and manage Bedrock Forms.

### BentoBoxBridge
Read and modify BentoBox data.

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

## Principles

- No BentoBox source modifications.
- Java players keep the original experience.
- Bedrock players use native Forms.
- Prefer official APIs over reflection.
