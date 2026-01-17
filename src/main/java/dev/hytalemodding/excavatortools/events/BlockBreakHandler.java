package dev.hytalemodding.excavatortools.events;

import dev.hytalemodding.excavatortools.ExcavatorConfig;
import dev.hytalemodding.excavatortools.ExcavatorPlugin;
import dev.hytalemodding.excavatortools.tools.ExcavatorTool;
import dev.hytalemodding.excavatortools.tools.ExcavatorToolRegistry;
import dev.hytalemodding.excavatortools.tools.ExcavatorToolType;
import dev.hytalemodding.excavatortools.util.BlockFace;
import dev.hytalemodding.excavatortools.util.BlockPos;
import dev.hytalemodding.excavatortools.util.MiningPatternCalculator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Handles block break events for excavator tools.
 *
 * When a player breaks a block with an excavator tool, this handler
 * breaks the surrounding blocks in a 3x3 pattern perpendicular to
 * the face that was hit.
 *
 * Note: The actual event handling will depend on Hytale's event API.
 * This class provides the logic; event registration should be updated
 * once the API is available.
 */
public class BlockBreakHandler {

    private static final Logger LOGGER = ExcavatorPlugin.getPluginLogger();

    // Set of players currently processing a 3x3 break (to prevent recursion)
    private static final Set<String> processingPlayers = new HashSet<>();

    /**
     * Register the block break event handler with the plugin
     */
    public static void register(ExcavatorPlugin plugin) {
        /*
         * TODO: Register with Hytale's event system
         *
         * Once Hytale's API is available, this would look something like:
         *
         * plugin.getEventRegistry().registerGlobal(
         *     BlockBreakEvent.class,
         *     BlockBreakHandler::onBlockBreak
         * );
         *
         * Or possibly:
         *
         * plugin.getEventRegistry().registerGlobal(
         *     PlayerBreakBlockEvent.class,
         *     BlockBreakHandler::onPlayerBreakBlock
         * );
         */

        LOGGER.info("Block break event handler registered");
    }

    /**
     * Handle a block break event
     *
     * @param playerId    The ID of the player breaking the block
     * @param blockPos    The position of the block being broken
     * @param blockId     The ID/type of the block being broken
     * @param heldItemId  The ID of the item the player is holding
     * @param blockFace   The face of the block that was hit
     * @param worldAccess Interface to access and modify the world
     */
    public static void onBlockBreak(
            String playerId,
            BlockPos blockPos,
            String blockId,
            String heldItemId,
            BlockFace blockFace,
            WorldAccess worldAccess
    ) {
        // Prevent recursive calls when we break additional blocks
        if (processingPlayers.contains(playerId)) {
            return;
        }

        ExcavatorPlugin plugin = ExcavatorPlugin.getInstance();
        ExcavatorToolRegistry toolRegistry = plugin.getToolRegistry();
        ExcavatorConfig config = plugin.getConfig();

        // Check if player is holding an excavator tool
        if (!toolRegistry.isExcavatorTool(heldItemId)) {
            return;
        }

        ExcavatorToolType toolType = toolRegistry.getToolType(heldItemId);
        ExcavatorTool tool = toolRegistry.createToolFromItemId(heldItemId);

        if (tool == null) {
            return;
        }

        // Check if this tool can mine this block type
        if (config.isRequireCorrectTool()) {
            boolean canMine = switch (toolType) {
                case HAMMER -> config.isHammerBlock(blockId);
                case SHOVEL -> config.isShovelBlock(blockId);
            };

            if (!canMine) {
                // Tool doesn't match block type, mine normally
                return;
            }
        }

        // Calculate the 3x3 pattern
        int radius = config.getMiningRadius();
        List<BlockPos> pattern = MiningPatternCalculator.calculatePattern(blockPos, blockFace, radius);

        // Filter to valid positions
        pattern = MiningPatternCalculator.filterValidPositions(pattern, worldAccess.getMinY(), worldAccess.getMaxY());

        // Mark player as processing to prevent recursion
        processingPlayers.add(playerId);

        try {
            int blocksMinned = 0;

            for (BlockPos pos : pattern) {
                // Skip the original block (already being broken)
                if (pos.equals(blockPos)) {
                    blocksMinned++;
                    continue;
                }

                // Get the block at this position
                String targetBlockId = worldAccess.getBlockId(pos);

                // Skip air and unbreakable blocks
                if (worldAccess.isAir(pos) || worldAccess.isUnbreakable(pos)) {
                    continue;
                }

                // Check if this block can be mined by the tool
                if (config.isRequireCorrectTool()) {
                    boolean canMineTarget = switch (toolType) {
                        case HAMMER -> config.isHammerBlock(targetBlockId);
                        case SHOVEL -> config.isShovelBlock(targetBlockId);
                    };

                    if (!canMineTarget) {
                        continue;
                    }
                }

                // Break the block
                worldAccess.breakBlock(pos, playerId, config.isDropAtPlayer());
                blocksMinned++;
            }

            // Apply durability cost
            if (blocksMinned > 0) {
                int durabilityCost = tool.calculateDurabilityCost(blocksMinned);
                worldAccess.damageHeldItem(playerId, durabilityCost);

                LOGGER.fine("Player " + playerId + " mined " + blocksMinned +
                        " blocks with " + tool.getDisplayName() +
                        " (durability cost: " + durabilityCost + ")");
            }

        } finally {
            // Always remove player from processing set
            processingPlayers.remove(playerId);
        }
    }

    /**
     * Interface for world access operations.
     *
     * This abstraction allows the handler to work with Hytale's world API
     * once it's available. Implement this interface with Hytale's actual
     * world/block manipulation methods.
     */
    public interface WorldAccess {
        /**
         * Get the minimum Y coordinate in the world
         */
        int getMinY();

        /**
         * Get the maximum Y coordinate in the world
         */
        int getMaxY();

        /**
         * Get the block ID at the given position
         */
        String getBlockId(BlockPos pos);

        /**
         * Check if the position contains air
         */
        boolean isAir(BlockPos pos);

        /**
         * Check if the block at the position is unbreakable (bedrock, etc.)
         */
        boolean isUnbreakable(BlockPos pos);

        /**
         * Break the block at the given position
         *
         * @param pos          The position to break
         * @param playerId     The player who broke it (for drops/XP)
         * @param dropAtPlayer Whether to drop items at player location
         */
        void breakBlock(BlockPos pos, String playerId, boolean dropAtPlayer);

        /**
         * Damage the item held by the player
         *
         * @param playerId The player whose item to damage
         * @param amount   The amount of durability to remove
         */
        void damageHeldItem(String playerId, int amount);
    }
}
