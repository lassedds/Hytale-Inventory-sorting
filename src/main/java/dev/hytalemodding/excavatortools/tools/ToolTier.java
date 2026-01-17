package dev.hytalemodding.excavatortools.tools;

/**
 * Represents the material tier of a tool.
 *
 * Note: Hytale may have different tiers/materials. Adjust as needed
 * once the actual game API is available.
 */
public enum ToolTier {
    WOOD("Wood", 0),
    STONE("Stone", 1),
    IRON("Iron", 2),
    GOLD("Gold", 0),  // Gold has low mining level but high speed
    DIAMOND("Diamond", 3),
    NETHERITE("Netherite", 4);

    private final String displayName;
    private final int miningLevel;

    ToolTier(String displayName, int miningLevel) {
        this.displayName = displayName;
        this.miningLevel = miningLevel;
    }

    /**
     * Get the display name for this tier
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the mining level for this tier.
     * Higher levels can mine harder blocks.
     */
    public int getMiningLevel() {
        return miningLevel;
    }

    /**
     * Check if this tier can mine blocks requiring the given mining level
     */
    public boolean canMine(int requiredLevel) {
        return miningLevel >= requiredLevel;
    }

    /**
     * Get a ToolTier from its name (case-insensitive)
     */
    public static ToolTier fromName(String name) {
        for (ToolTier tier : values()) {
            if (tier.name().equalsIgnoreCase(name) || tier.displayName.equalsIgnoreCase(name)) {
                return tier;
            }
        }
        return null;
    }
}
