package dev.hytalemodding.excavatortools.tools;

import dev.hytalemodding.excavatortools.ExcavatorConfig;
import dev.hytalemodding.excavatortools.ExcavatorPlugin;

/**
 * Represents an Excavator Tool with its properties.
 *
 * Note: This class is a data model. The actual item registration
 * will depend on Hytale's item API once it's available.
 */
public class ExcavatorTool {

    private final ExcavatorToolType type;
    private final ToolTier tier;
    private int durability;
    private final int maxDurability;
    private final float miningSpeed;
    private final int attackDamage;

    public ExcavatorTool(ExcavatorToolType type, ToolTier tier) {
        this.type = type;
        this.tier = tier;
        this.maxDurability = calculateDurability(tier);
        this.durability = maxDurability;
        this.miningSpeed = calculateMiningSpeed(tier);
        this.attackDamage = calculateAttackDamage(tier, type);
    }

    private int calculateDurability(ToolTier tier) {
        // Excavator tools have higher durability due to 3x3 mining
        return switch (tier) {
            case WOOD -> 177;      // 59 * 3
            case STONE -> 393;     // 131 * 3
            case IRON -> 750;      // 250 * 3
            case GOLD -> 96;       // 32 * 3
            case DIAMOND -> 4683;  // 1561 * 3
            case NETHERITE -> 6093; // 2031 * 3
        };
    }

    private float calculateMiningSpeed(ToolTier tier) {
        return switch (tier) {
            case WOOD -> 2.0f;
            case STONE -> 4.0f;
            case IRON -> 6.0f;
            case GOLD -> 12.0f;    // Fast but fragile
            case DIAMOND -> 8.0f;
            case NETHERITE -> 9.0f;
        };
    }

    private int calculateAttackDamage(ToolTier tier, ExcavatorToolType type) {
        int baseDamage = switch (tier) {
            case WOOD -> 1;
            case STONE -> 2;
            case IRON -> 3;
            case GOLD -> 1;
            case DIAMOND -> 4;
            case NETHERITE -> 5;
        };

        // Hammers do more damage than shovels
        if (type == ExcavatorToolType.HAMMER) {
            baseDamage += 2;
        }

        return baseDamage;
    }

    /**
     * Check if this tool can mine the given block
     */
    public boolean canMine(String blockId) {
        ExcavatorConfig config = ExcavatorPlugin.getInstance().getConfig();
        return switch (type) {
            case HAMMER -> config.isHammerBlock(blockId);
            case SHOVEL -> config.isShovelBlock(blockId);
        };
    }

    /**
     * Use durability on the tool
     *
     * @param amount Amount of durability to consume
     * @return true if the tool is still usable, false if it broke
     */
    public boolean useDurability(int amount) {
        durability -= amount;
        return durability > 0;
    }

    /**
     * Calculate durability cost for mining multiple blocks
     */
    public int calculateDurabilityCost(int blocksMinned) {
        ExcavatorConfig config = ExcavatorPlugin.getInstance().getConfig();
        if (!config.isExtraDurabilityPerBlock()) {
            return 1; // Only cost 1 durability regardless of blocks mined
        }
        // First block costs 1, additional blocks cost multiplier
        int additionalBlocks = blocksMinned - 1;
        double additionalCost = additionalBlocks * config.getDurabilityMultiplier();
        return 1 + (int) Math.ceil(additionalCost);
    }

    // Getters

    public ExcavatorToolType getType() {
        return type;
    }

    public ToolTier getTier() {
        return tier;
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public float getMiningSpeed() {
        return miningSpeed;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public boolean isBroken() {
        return durability <= 0;
    }

    /**
     * Get the full item ID for this tool (e.g., "diamond_excavator_hammer")
     */
    public String getItemId() {
        return tier.name().toLowerCase() + "_" + type.getId();
    }

    /**
     * Get the display name for this tool (e.g., "Diamond Excavator Hammer")
     */
    public String getDisplayName() {
        return tier.getDisplayName() + " " + type.getDisplayName();
    }
}
