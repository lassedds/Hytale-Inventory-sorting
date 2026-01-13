# Inventory Sorting for Hytale

A Hytale plugin that provides automatic stacking and sorting of items in player inventories and containers (chests, barrels, etc.), inspired by popular Minecraft sorting mods like Inventory Tweaks and Mouse Tweaks.

## Features

- **Multiple Sort Modes**
  - `ALPHABETICAL` - Sort items by name
  - `CATEGORY` - Sort by item type (weapons, tools, armor, food, blocks, materials, misc)
  - `ID` - Sort by internal item ID
  - `QUANTITY` - Sort by stack size (largest first)
  - `RARITY` - Sort by item rarity/quality
  - `CATEGORY_ALPHABETICAL` - Sort by category, then alphabetically within each category

- **Auto-Stacking** - Automatically stack picked up items into existing stacks
- **Quick Sort Keybind** - Press a configurable key (default: R) to sort while in inventory
- **Middle-Click Sorting** - Middle-click empty space in any inventory to sort it
- **Container Sorting** - Sort chests, barrels, and other containers
- **Hotbar Protection** - Option to keep hotbar separate from sorting
- **Auto-Sort on Close** - Optionally auto-sort containers when closing them
- **Customizable Category Order** - Configure the order categories appear when sorting

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sort [mode]` | Sort your inventory | `inventorysorting.sort` |
| `/sortchest [mode]` | Sort currently opened container | `inventorysorting.sortchest` |
| `/sortconfig <option> [value]` | Configure sorting preferences | `inventorysorting.config` |

### Sort Config Options

- `mode <mode>` - Set default sort mode
- `autostack <on/off>` - Toggle auto-stacking on pickup
- `autosort <on/off>` - Toggle auto-sort on container close
- `hotbar <on/off>` - Toggle sorting hotbar separately
- `sound <on/off>` - Toggle sort sound effect
- `reload` - Reload configuration from file
- `show` - Display current settings

## Installation

1. Download the latest release JAR from the releases page
2. Place the JAR in your Hytale mods folder:
   - Windows: `%AppData%/Hytale/UserData/Mods/`
   - Linux: `~/.hytale/UserData/Mods/`
   - macOS: `~/Library/Application Support/Hytale/UserData/Mods/`
3. Restart your Hytale server/client

## Configuration

The plugin creates a `config.json` file in the plugin data folder on first run:

```json
{
  "defaultSortMode": "CATEGORY",
  "autoStackOnPickup": true,
  "autoSortOnClose": false,
  "sortHotbarSeparately": true,
  "sortKeybind": "R",
  "playSortSound": true,
  "categoryOrder": [
    "WEAPONS",
    "TOOLS",
    "ARMOR",
    "FOOD",
    "BLOCKS",
    "MATERIALS",
    "MISC"
  ]
}
```

## Building from Source

### Requirements

- Java 25 (Adoptium recommended)
- Gradle 9.x

### Build Steps

```bash
# Clone the repository
git clone https://github.com/lassedds/Hytale-Inventory-sorting.git
cd Hytale-Inventory-sorting

# Build the plugin
./gradlew shadowJar

# The built JAR will be in build/libs/
```

## Development

### Project Structure

```
src/main/java/com/inventorysorting/
├── InventorySortingPlugin.java    # Main plugin class
├── commands/
│   ├── SortCommand.java           # /sort command
│   ├── SortChestCommand.java      # /sortchest command
│   └── SortConfigCommand.java     # /sortconfig command
├── config/
│   └── SortingConfig.java         # Configuration manager
├── listeners/
│   ├── ContainerListener.java     # Container events
│   └── InventoryListener.java     # Inventory events
└── services/
    ├── ItemCategory.java          # Item categories enum
    ├── SortMode.java              # Sort modes enum
    └── SortingService.java        # Core sorting logic
```

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `inventorysorting.sort` | Use /sort command | true |
| `inventorysorting.sortchest` | Use /sortchest command | true |
| `inventorysorting.config` | Use /sortconfig command | op |

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## License

This project is open source. See LICENSE file for details.

## Credits

Inspired by:
- [Inventory Tweaks](https://minecraft.curseforge.com/projects/inventory-tweaks) (Minecraft)
- [Mouse Tweaks](https://minecraft.curseforge.com/projects/mouse-tweaks) (Minecraft)
- [Inventory Sorter](https://modrinth.com/mod/inventory-sorting) (Minecraft)

## Links

- [Hytale Modding Documentation](https://britakee-studios.gitbook.io/hytale-modding-documentation)
- [HytaleModding GitHub](https://github.com/HytaleModding)
- [CurseForge Hytale Mods](https://www.curseforge.com/hytale)
