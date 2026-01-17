package dev.hytalemodding.excavatortools;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.hytalemodding.excavatortools.events.BlockBreakHandler;
import dev.hytalemodding.excavatortools.tools.ExcavatorToolRegistry;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * ExcavatorTools Plugin - Adds 3x3 mining tools to Hytale
 *
 * Features:
 * - Excavator Hammer: 3x3 mining for stone, ores, and hard blocks
 * - Excavator Shovel: 3x3 digging for dirt, sand, gravel, and soft blocks
 * - Face-based mining: The 3x3 area is perpendicular to the block face you hit
 */
public class ExcavatorPlugin extends JavaPlugin {

    private static ExcavatorPlugin instance;
    private static final Logger LOGGER = Logger.getLogger("ExcavatorTools");

    private ExcavatorToolRegistry toolRegistry;
    private ExcavatorConfig config;

    public ExcavatorPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        LOGGER.info("Initializing ExcavatorTools plugin...");

        // Load configuration
        this.config = new ExcavatorConfig();

        // Initialize tool registry
        this.toolRegistry = new ExcavatorToolRegistry();
        this.toolRegistry.registerTools();

        // Register block break event handler
        // Note: The actual event class name may vary based on Hytale's API
        // Common patterns: BlockBreakEvent, PlayerBreakBlockEvent, etc.
        BlockBreakHandler.register(this);

        LOGGER.info("ExcavatorTools plugin loaded successfully!");
        LOGGER.info("Tools registered: Excavator Hammer, Excavator Shovel");
    }

    /**
     * Get the plugin instance
     */
    public static ExcavatorPlugin getInstance() {
        return instance;
    }

    /**
     * Get the tool registry
     */
    public ExcavatorToolRegistry getToolRegistry() {
        return toolRegistry;
    }

    /**
     * Get the plugin configuration
     */
    public ExcavatorConfig getConfig() {
        return config;
    }

    /**
     * Get the plugin logger
     */
    public static Logger getPluginLogger() {
        return LOGGER;
    }
}
