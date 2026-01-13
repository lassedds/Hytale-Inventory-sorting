package com.inventorysorting;

import com.hypixel.hytale.server.plugin.Plugin;
import com.hypixel.hytale.server.plugin.PluginContext;
import com.inventorysorting.commands.SortCommand;
import com.inventorysorting.commands.SortChestCommand;
import com.inventorysorting.commands.SortConfigCommand;
import com.inventorysorting.config.SortingConfig;
import com.inventorysorting.listeners.ContainerListener;
import com.inventorysorting.listeners.InventoryListener;
import com.inventorysorting.services.SortingService;

/**
 * Inventory Sorting Plugin for Hytale
 *
 * Provides automatic stacking and sorting of items in player inventories
 * and containers (chests, barrels, etc.), inspired by popular Minecraft
 * sorting mods like Inventory Tweaks and Mouse Tweaks.
 */
public class InventorySortingPlugin implements Plugin {

    private static InventorySortingPlugin instance;

    private PluginContext context;
    private SortingConfig config;
    private SortingService sortingService;

    @Override
    public void onLoad(PluginContext context) {
        instance = this;
        this.context = context;

        context.getLogger().info("Loading Inventory Sorting Plugin...");

        // Load configuration
        this.config = new SortingConfig(context);
        this.config.load();

        // Initialize sorting service
        this.sortingService = new SortingService(config);

        context.getLogger().info("Inventory Sorting Plugin loaded successfully!");
    }

    @Override
    public void onEnable() {
        context.getLogger().info("Enabling Inventory Sorting Plugin...");

        // Register commands
        registerCommands();

        // Register event listeners
        registerListeners();

        context.getLogger().info("Inventory Sorting Plugin enabled!");
        context.getLogger().info("Use /sort to sort your inventory, /sortchest to sort opened container");
    }

    @Override
    public void onDisable() {
        context.getLogger().info("Disabling Inventory Sorting Plugin...");

        // Save configuration
        config.save();

        context.getLogger().info("Inventory Sorting Plugin disabled!");
    }

    private void registerCommands() {
        var commandManager = context.getCommandManager();

        commandManager.register(new SortCommand(sortingService, config));
        commandManager.register(new SortChestCommand(sortingService, config));
        commandManager.register(new SortConfigCommand(config));

        context.getLogger().info("Registered sorting commands");
    }

    private void registerListeners() {
        var eventManager = context.getEventManager();

        eventManager.register(new InventoryListener(sortingService, config));
        eventManager.register(new ContainerListener(sortingService, config));

        context.getLogger().info("Registered event listeners");
    }

    public static InventorySortingPlugin getInstance() {
        return instance;
    }

    public PluginContext getContext() {
        return context;
    }

    public SortingConfig getConfig() {
        return config;
    }

    public SortingService getSortingService() {
        return sortingService;
    }
}
