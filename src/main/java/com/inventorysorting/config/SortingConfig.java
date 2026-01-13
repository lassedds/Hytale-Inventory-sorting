package com.inventorysorting.config;

import com.hypixel.hytale.server.plugin.PluginContext;
import com.hypixel.hytale.server.config.JsonConfig;
import com.inventorysorting.services.SortMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration manager for the Inventory Sorting plugin.
 * Handles loading, saving, and accessing configuration options.
 */
public class SortingConfig {

    private final PluginContext context;
    private final Path configFile;

    // Configuration options with defaults
    private SortMode defaultSortMode = SortMode.CATEGORY;
    private boolean autoStackOnPickup = true;
    private boolean autoSortOnClose = false;
    private boolean sortHotbarSeparately = true;
    private String sortKeybind = "R";
    private boolean playSortSound = true;
    private List<String> categoryOrder = new ArrayList<>(List.of(
        "WEAPONS", "TOOLS", "ARMOR", "FOOD", "BLOCKS", "MATERIALS", "MISC"
    ));

    public SortingConfig(PluginContext context) {
        this.context = context;
        this.configFile = context.getDataFolder().resolve("config.json");
    }

    /**
     * Loads configuration from file, or creates default config if not exists.
     */
    public void load() {
        try {
            // Ensure data folder exists
            Files.createDirectories(context.getDataFolder());

            if (Files.exists(configFile)) {
                loadFromFile();
            } else {
                // Create default config file
                save();
            }
        } catch (IOException e) {
            context.getLogger().warn("Failed to load config, using defaults: " + e.getMessage());
        }
    }

    private void loadFromFile() throws IOException {
        JsonConfig config = JsonConfig.load(configFile);

        // Load each setting with fallback to current value
        String modeStr = config.getString("defaultSortMode", defaultSortMode.name());
        try {
            defaultSortMode = SortMode.valueOf(modeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            context.getLogger().warn("Invalid sort mode in config: " + modeStr);
        }

        autoStackOnPickup = config.getBoolean("autoStackOnPickup", autoStackOnPickup);
        autoSortOnClose = config.getBoolean("autoSortOnClose", autoSortOnClose);
        sortHotbarSeparately = config.getBoolean("sortHotbarSeparately", sortHotbarSeparately);
        sortKeybind = config.getString("sortKeybind", sortKeybind);
        playSortSound = config.getBoolean("playSortSound", playSortSound);

        List<String> order = config.getStringList("categoryOrder");
        if (order != null && !order.isEmpty()) {
            categoryOrder = new ArrayList<>(order);
        }

        context.getLogger().info("Configuration loaded successfully");
    }

    /**
     * Saves current configuration to file.
     */
    public void save() {
        try {
            Files.createDirectories(context.getDataFolder());

            JsonConfig config = new JsonConfig();
            config.set("defaultSortMode", defaultSortMode.name());
            config.set("autoStackOnPickup", autoStackOnPickup);
            config.set("autoSortOnClose", autoSortOnClose);
            config.set("sortHotbarSeparately", sortHotbarSeparately);
            config.set("sortKeybind", sortKeybind);
            config.set("playSortSound", playSortSound);
            config.set("categoryOrder", categoryOrder);

            config.save(configFile);
            context.getLogger().info("Configuration saved successfully");
        } catch (IOException e) {
            context.getLogger().error("Failed to save config: " + e.getMessage());
        }
    }

    // Getters and Setters

    public SortMode getDefaultSortMode() {
        return defaultSortMode;
    }

    public void setDefaultSortMode(SortMode defaultSortMode) {
        this.defaultSortMode = defaultSortMode;
        save();
    }

    public boolean isAutoStackOnPickup() {
        return autoStackOnPickup;
    }

    public void setAutoStackOnPickup(boolean autoStackOnPickup) {
        this.autoStackOnPickup = autoStackOnPickup;
        save();
    }

    public boolean isAutoSortOnClose() {
        return autoSortOnClose;
    }

    public void setAutoSortOnClose(boolean autoSortOnClose) {
        this.autoSortOnClose = autoSortOnClose;
        save();
    }

    public boolean isSortHotbarSeparately() {
        return sortHotbarSeparately;
    }

    public void setSortHotbarSeparately(boolean sortHotbarSeparately) {
        this.sortHotbarSeparately = sortHotbarSeparately;
        save();
    }

    public String getSortKeybind() {
        return sortKeybind;
    }

    public void setSortKeybind(String sortKeybind) {
        this.sortKeybind = sortKeybind;
        save();
    }

    public boolean isPlaySortSound() {
        return playSortSound;
    }

    public void setPlaySortSound(boolean playSortSound) {
        this.playSortSound = playSortSound;
        save();
    }

    public List<String> getCategoryOrder() {
        return new ArrayList<>(categoryOrder);
    }

    public void setCategoryOrder(List<String> categoryOrder) {
        this.categoryOrder = new ArrayList<>(categoryOrder);
        save();
    }
}
