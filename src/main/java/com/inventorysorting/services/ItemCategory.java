package com.inventorysorting.services;

/**
 * Categories for organizing items during sorting.
 */
public enum ItemCategory {
    WEAPONS(0, "Weapons"),
    TOOLS(1, "Tools"),
    ARMOR(2, "Armor"),
    FOOD(3, "Food"),
    BLOCKS(4, "Blocks"),
    MATERIALS(5, "Materials"),
    MISC(6, "Miscellaneous");

    private final int defaultOrder;
    private final String displayName;

    ItemCategory(int defaultOrder, String displayName) {
        this.defaultOrder = defaultOrder;
        this.displayName = displayName;
    }

    public int getDefaultOrder() {
        return defaultOrder;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Attempts to parse a category from a string name (case-insensitive).
     */
    public static ItemCategory fromString(String name) {
        for (ItemCategory category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return MISC;
    }
}
