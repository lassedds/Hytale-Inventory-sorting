package dev.hytalemodding.excavatortools.tools;

import dev.hytalemodding.excavatortools.ExcavatorPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Registry for all Excavator Tools.
 *
 * This class handles the registration of excavator tools with the game's item system.
 *
 * Note: The actual item registration will depend on Hytale's item registration API.
 * This class provides the structure; implementation details should be updated
 * once the API is available.
 */
public class ExcavatorToolRegistry {

    private static final Logger LOGGER = ExcavatorPlugin.getPluginLogger();

    // Map of item ID to tool type
    private final Map<String, ExcavatorToolType> registeredTools = new HashMap<>();

    /**
     * Register all excavator tools with the game
     */
    public void registerTools() {
        LOGGER.info("Registering Excavator Tools...");

        // Register all tier variants of each tool type
        for (ToolTier tier : ToolTier.values()) {
            for (ExcavatorToolType type : ExcavatorToolType.values()) {
                registerTool(tier, type);
            }
        }

        LOGGER.info("Registered " + registeredTools.size() + " excavator tools");
    }

    /**
     * Register a single excavator tool
     */
    private void registerTool(ToolTier tier, ExcavatorToolType type) {
        ExcavatorTool tool = new ExcavatorTool(type, tier);
        String itemId = tool.getItemId();

        // Store in our registry
        registeredTools.put(itemId, type);

        /*
         * TODO: Register with Hytale's item system
         *
         * Once Hytale's API is available, this would look something like:
         *
         * ItemDefinition definition = ItemDefinition.builder()
         *     .id(itemId)
         *     .displayName(tool.getDisplayName())
         *     .maxStackSize(1)
         *     .durability(tool.getMaxDurability())
         *     .toolType(type == ExcavatorToolType.HAMMER ? "pickaxe" : "shovel")
         *     .miningSpeed(tool.getMiningSpeed())
         *     .attackDamage(tool.getAttackDamage())
         *     .build();
         *
         * ItemRegistry.register(definition);
         */

        LOGGER.fine("Registered tool: " + tool.getDisplayName() + " (" + itemId + ")");
    }

    /**
     * Check if an item ID is a registered excavator tool
     */
    public boolean isExcavatorTool(String itemId) {
        return registeredTools.containsKey(itemId);
    }

    /**
     * Get the tool type for an item ID
     */
    public ExcavatorToolType getToolType(String itemId) {
        return registeredTools.get(itemId);
    }

    /**
     * Get all registered tool IDs
     */
    public Set<String> getAllToolIds() {
        return registeredTools.keySet();
    }

    /**
     * Check if an item ID is an excavator hammer
     */
    public boolean isExcavatorHammer(String itemId) {
        ExcavatorToolType type = registeredTools.get(itemId);
        return type == ExcavatorToolType.HAMMER;
    }

    /**
     * Check if an item ID is an excavator shovel
     */
    public boolean isExcavatorShovel(String itemId) {
        ExcavatorToolType type = registeredTools.get(itemId);
        return type == ExcavatorToolType.SHOVEL;
    }

    /**
     * Create an ExcavatorTool instance from an item ID
     */
    public ExcavatorTool createToolFromItemId(String itemId) {
        if (!isExcavatorTool(itemId)) {
            return null;
        }

        // Parse the item ID to get tier and type
        // Format: "{tier}_excavator_{hammer|shovel}"
        String[] parts = itemId.split("_excavator_");
        if (parts.length != 2) {
            return null;
        }

        ToolTier tier = ToolTier.fromName(parts[0]);
        ExcavatorToolType type = registeredTools.get(itemId);

        if (tier == null || type == null) {
            return null;
        }

        return new ExcavatorTool(type, tier);
    }
}
