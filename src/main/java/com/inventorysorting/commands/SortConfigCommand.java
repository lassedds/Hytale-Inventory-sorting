package com.inventorysorting.commands;

import com.hypixel.hytale.server.command.Command;
import com.hypixel.hytale.server.command.CommandContext;
import com.hypixel.hytale.server.command.CommandResult;
import com.hypixel.hytale.server.command.annotation.CommandInfo;
import com.hypixel.hytale.server.command.annotation.CommandPermission;
import com.hypixel.hytale.server.entity.player.Player;
import com.inventorysorting.config.SortingConfig;
import com.inventorysorting.services.SortMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Command to configure sorting preferences.
 * Usage: /sortconfig <option> [value]
 */
@CommandInfo(
    name = "sortconfig",
    aliases = {"sortcfg", "sortc"},
    description = "Configure inventory sorting preferences",
    usage = "/sortconfig <option> [value]"
)
@CommandPermission("inventorysorting.config")
public class SortConfigCommand implements Command {

    private final SortingConfig config;

    private static final List<String> OPTIONS = List.of(
        "mode", "autostack", "autosort", "hotbar", "sound", "reload", "show"
    );

    public SortConfigCommand(SortingConfig config) {
        this.config = config;
    }

    @Override
    public CommandResult execute(CommandContext context) {
        if (!(context.getSender() instanceof Player player)) {
            return CommandResult.failure("This command can only be used by players.");
        }

        String[] args = context.getArgs();

        if (args.length == 0) {
            showHelp(player);
            return CommandResult.success();
        }

        String option = args[0].toLowerCase();

        return switch (option) {
            case "mode" -> handleMode(player, args);
            case "autostack" -> handleAutoStack(player, args);
            case "autosort" -> handleAutoSort(player, args);
            case "hotbar" -> handleHotbar(player, args);
            case "sound" -> handleSound(player, args);
            case "reload" -> handleReload(player);
            case "show" -> handleShow(player);
            default -> {
                player.sendMessage("Unknown option: " + option);
                showHelp(player);
                yield CommandResult.failure("Unknown option");
            }
        };
    }

    private void showHelp(Player player) {
        player.sendMessage("=== Inventory Sorting Config ===");
        player.sendMessage("/sortconfig mode <mode> - Set default sort mode");
        player.sendMessage("/sortconfig autostack <on/off> - Toggle auto-stacking on pickup");
        player.sendMessage("/sortconfig autosort <on/off> - Toggle auto-sort on container close");
        player.sendMessage("/sortconfig hotbar <on/off> - Toggle sorting hotbar separately");
        player.sendMessage("/sortconfig sound <on/off> - Toggle sort sound effect");
        player.sendMessage("/sortconfig reload - Reload configuration");
        player.sendMessage("/sortconfig show - Show current settings");
    }

    private CommandResult handleMode(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Current default mode: " + config.getDefaultSortMode().name());
            player.sendMessage("Available modes: " + String.join(", ",
                Arrays.stream(SortMode.values()).map(Enum::name).toList()));
            return CommandResult.success();
        }

        try {
            SortMode mode = SortMode.valueOf(args[1].toUpperCase());
            config.setDefaultSortMode(mode);
            player.sendMessage("Default sort mode set to: " + mode.name());
            return CommandResult.success();
        } catch (IllegalArgumentException e) {
            return CommandResult.failure("Invalid sort mode: " + args[1]);
        }
    }

    private CommandResult handleAutoStack(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Auto-stack on pickup: " + (config.isAutoStackOnPickup() ? "ON" : "OFF"));
            return CommandResult.success();
        }

        boolean value = parseBoolean(args[1]);
        config.setAutoStackOnPickup(value);
        player.sendMessage("Auto-stack on pickup: " + (value ? "ON" : "OFF"));
        return CommandResult.success();
    }

    private CommandResult handleAutoSort(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Auto-sort on close: " + (config.isAutoSortOnClose() ? "ON" : "OFF"));
            return CommandResult.success();
        }

        boolean value = parseBoolean(args[1]);
        config.setAutoSortOnClose(value);
        player.sendMessage("Auto-sort on container close: " + (value ? "ON" : "OFF"));
        return CommandResult.success();
    }

    private CommandResult handleHotbar(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Sort hotbar separately: " + (config.isSortHotbarSeparately() ? "ON" : "OFF"));
            return CommandResult.success();
        }

        boolean value = parseBoolean(args[1]);
        config.setSortHotbarSeparately(value);
        player.sendMessage("Sort hotbar separately: " + (value ? "ON" : "OFF"));
        return CommandResult.success();
    }

    private CommandResult handleSound(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Sort sound: " + (config.isPlaySortSound() ? "ON" : "OFF"));
            return CommandResult.success();
        }

        boolean value = parseBoolean(args[1]);
        config.setPlaySortSound(value);
        player.sendMessage("Sort sound: " + (value ? "ON" : "OFF"));
        return CommandResult.success();
    }

    private CommandResult handleReload(Player player) {
        config.load();
        player.sendMessage("Configuration reloaded!");
        return CommandResult.success();
    }

    private CommandResult handleShow(Player player) {
        player.sendMessage("=== Current Settings ===");
        player.sendMessage("Default mode: " + config.getDefaultSortMode().name());
        player.sendMessage("Auto-stack on pickup: " + (config.isAutoStackOnPickup() ? "ON" : "OFF"));
        player.sendMessage("Auto-sort on close: " + (config.isAutoSortOnClose() ? "ON" : "OFF"));
        player.sendMessage("Sort hotbar separately: " + (config.isSortHotbarSeparately() ? "ON" : "OFF"));
        player.sendMessage("Sort sound: " + (config.isPlaySortSound() ? "ON" : "OFF"));
        player.sendMessage("Sort keybind: " + config.getSortKeybind());
        return CommandResult.success();
    }

    private boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("on") ||
               value.equalsIgnoreCase("true") ||
               value.equalsIgnoreCase("yes") ||
               value.equalsIgnoreCase("1");
    }

    @Override
    public List<String> tabComplete(CommandContext context) {
        String[] args = context.getArgs();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return OPTIONS.stream()
                .filter(opt -> opt.startsWith(partial))
                .toList();
        }

        if (args.length == 2) {
            String option = args[0].toLowerCase();
            String partial = args[1].toLowerCase();

            return switch (option) {
                case "mode" -> Arrays.stream(SortMode.values())
                    .map(Enum::name)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .map(String::toLowerCase)
                    .toList();
                case "autostack", "autosort", "hotbar", "sound" ->
                    List.of("on", "off").stream()
                        .filter(v -> v.startsWith(partial))
                        .toList();
                default -> Collections.emptyList();
            };
        }

        return Collections.emptyList();
    }
}
