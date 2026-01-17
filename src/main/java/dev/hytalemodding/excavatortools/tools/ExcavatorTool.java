package dev.hytalemodding.excavatortools.tools;

import dev.hytalemodding.excavatortools.ExcavatorConfig;
import dev.hytalemodding.excavatortools.ExcavatorPlugin;

/**
 * Represents an Excavator Tool with its properties.
 *
 * Hytale tool progression: Crude → Copper → Iron → Cobalt → Thorium → Adamantite → Mithril
 *
 * Note: This class is a data model. The actual item registration
 * will depend on Hytale's item API once it's available.
 */
public class ExcavatorTool {

    // Excavator tools have 3x durability multiplier due to 3x3 mining
    private static final int DURABILITY_MULTIPLIER = 3;

    private final ExcavatorToolType type;
    private final ToolTier tier;
    private int durability;
    private final int maxDurability;
    private final float miningSpeed;
    private final int attackDamage;

    public ExcavatorTool(ExcavatorToolType type, ToolTier tier) {
        this.type = type;
        this.tier = tier;
        this.maxDurability = tier.getBaseDurability() * DURABILITY_MULTIPLIER;
        this.durability = maxDurability;
        this.miningSpeed = tier.getBaseMiningSpeed();
        this.attackDamage = calculateAttackDamage(tier, type);
    }

    private int calculateAttackDamage(ToolTier tier, ExcavatorToolType type) {
        // Base damage scales with tier
        int baseDamage = switch (tier) {
            case CRUDE -> 2;
            case COPPER -> 3;
            case IRON -> 4;
            case COBALT -> 5;
            case THORIUM -> 5;
            case ADAMANTITE -> 6;
            case MITHRIL -> 7;
        };

        // Hammers do more damage than shovels (they're heavier)
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
     * Check if this tool's tier can mine blocks at the given mining level
     */
    public boolean canMineLevel(int requiredLevel) {
        return tier.canMine(requiredLevel);
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
    public int calculateDurabilityCost(int blocksMined) {
        ExcavatorConfig config = ExcavatorPlugin.getInstance().getConfig();
        if (!config.isExtraDurabilityPerBlock()) {
            return 1; // Only cost 1 durability regardless of blocks mined
        }
        // First block costs 1, additional blocks cost multiplier
        int additionalBlocks = blocksMined - 1;
        double additionalCost = additionalBlocks * config.getDurabilityMultiplier();
        return 1 + (int) Math.ceil(additionalCost);
    }

    /**
     * Repair the tool by a percentage of max durability
     *
     * @param percentage Percentage to repair (0.0 to 1.0)
     */
    public void repair(double percentage) {
        int repairAmount = (int) (maxDurability * Math.min(1.0, Math.max(0.0, percentage)));
        durability = Math.min(maxDurability, durability + repairAmount);
    }

    /**
     * Fully repair the tool
     */
    public void fullRepair() {
        durability = maxDurability;
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
     * Get the durability as a percentage (0.0 to 1.0)
     */
    public double getDurabilityPercent() {
        return (double) durability / maxDurability;
    }

    /**
     * Get the full item ID for this tool (e.g., "iron_excavator_hammer")
     */
    public String getItemId() {
        return tier.name().toLowerCase() + "_" + type.getId();
    }

    /**
     * Get the display name for this tool (e.g., "Iron Excavator Hammer")
     */
    public String getDisplayName() {
        return tier.getDisplayName() + " " + type.getDisplayName();
    }

    @Override
    public String toString() {
        return String.format("%s [%d/%d durability, %.1f speed, %d damage]",
                getDisplayName(), durability, maxDurability, miningSpeed, attackDamage);
    }
}
