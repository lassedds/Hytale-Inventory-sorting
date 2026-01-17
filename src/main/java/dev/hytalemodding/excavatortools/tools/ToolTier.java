package dev.hytalemodding.excavatortools.tools;

/**
 * Represents the material tier of a tool in Hytale.
 *
 * Hytale progression: Crude → Copper → Iron → Cobalt → Thorium → Adamantite → Mithril
 *
 * Note: Iron can mine ALL ores in Hytale (as of early access).
 * Higher tiers provide better durability and mining speed.
 */
public enum ToolTier {
    CRUDE("Crude", 0, 59, 2.0f),
    COPPER("Copper", 1, 131, 4.0f),
    IRON("Iron", 2, 250, 6.0f),
    COBALT("Cobalt", 3, 500, 7.0f),
    THORIUM("Thorium", 3, 750, 7.5f),
    ADAMANTITE("Adamantite", 4, 1200, 8.5f),
    MITHRIL("Mithril", 5, 1800, 10.0f);

    private final String displayName;
    private final int miningLevel;
    private final int baseDurability;
    private final float baseMiningSpeed;

    ToolTier(String displayName, int miningLevel, int baseDurability, float baseMiningSpeed) {
        this.displayName = displayName;
        this.miningLevel = miningLevel;
        this.baseDurability = baseDurability;
        this.baseMiningSpeed = baseMiningSpeed;
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
     * Note: In Hytale early access, Iron can mine ALL ores.
     */
    public int getMiningLevel() {
        return miningLevel;
    }

    /**
     * Get the base durability for this tier.
     * Excavator tools multiply this by 3 due to 3x3 mining.
     */
    public int getBaseDurability() {
        return baseDurability;
    }

    /**
     * Get the base mining speed for this tier.
     */
    public float getBaseMiningSpeed() {
        return baseMiningSpeed;
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

    /**
     * Get the next tier upgrade, or null if this is the highest tier
     */
    public ToolTier getNextTier() {
        ToolTier[] tiers = values();
        int currentIndex = this.ordinal();
        if (currentIndex < tiers.length - 1) {
            return tiers[currentIndex + 1];
        }
        return null;
    }

    /**
     * Get the previous tier, or null if this is the lowest tier
     */
    public ToolTier getPreviousTier() {
        int currentIndex = this.ordinal();
        if (currentIndex > 0) {
            return values()[currentIndex - 1];
        }
        return null;
    }
}
