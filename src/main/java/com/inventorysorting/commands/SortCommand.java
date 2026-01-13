package com.inventorysorting.commands;

import com.hypixel.hytale.server.command.Command;
import com.hypixel.hytale.server.command.CommandContext;
import com.hypixel.hytale.server.command.CommandResult;
import com.hypixel.hytale.server.command.annotation.CommandInfo;
import com.hypixel.hytale.server.command.annotation.CommandPermission;
import com.hypixel.hytale.server.command.annotation.Argument;
import com.hypixel.hytale.server.entity.player.Player;
import com.inventorysorting.config.SortingConfig;
import com.inventorysorting.services.SortMode;
import com.inventorysorting.services.SortingService;

/**
 * Command to sort the player's inventory.
 * Usage: /sort [mode]
 */
@CommandInfo(
    name = "sort",
    description = "Sort your inventory",
    usage = "/sort [mode]"
)
@CommandPermission("inventorysorting.sort")
public class SortCommand implements Command {

    private final SortingService sortingService;
    private final SortingConfig config;

    public SortCommand(SortingService sortingService, SortingConfig config) {
        this.sortingService = sortingService;
        this.config = config;
    }

    @Override
    public CommandResult execute(CommandContext context) {
        if (!(context.getSender() instanceof Player player)) {
            return CommandResult.failure("This command can only be used by players.");
        }

        // Parse optional sort mode argument
        SortMode mode = config.getDefaultSortMode();
        String[] args = context.getArgs();

        if (args.length > 0) {
            try {
                mode = SortMode.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                return CommandResult.failure("Invalid sort mode: " + args[0] +
                    ". Available modes: ALPHABETICAL, CATEGORY, ID, QUANTITY, RARITY, CATEGORY_ALPHABETICAL");
            }
        }

        // Perform the sort
        sortingService.sortPlayerInventory(player, mode);

        player.sendMessage("Inventory sorted using " + mode.name().toLowerCase() + " mode.");
        return CommandResult.success();
    }

    @Override
    public java.util.List<String> tabComplete(CommandContext context) {
        String[] args = context.getArgs();

        if (args.length == 1) {
            String partial = args[0].toUpperCase();
            return java.util.Arrays.stream(SortMode.values())
                .map(Enum::name)
                .filter(name -> name.startsWith(partial))
                .map(String::toLowerCase)
                .toList();
        }

        return java.util.Collections.emptyList();
    }
}
