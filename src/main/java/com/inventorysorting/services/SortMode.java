package com.inventorysorting.services;

/**
 * Available sorting modes for inventory organization.
 */
public enum SortMode {
    /**
     * Sort items alphabetically by their display name.
     */
    ALPHABETICAL,

    /**
     * Sort items by their category (weapons, tools, armor, food, blocks, etc.)
     * with customizable category ordering.
     */
    CATEGORY,

    /**
     * Sort items by their internal ID for consistent ordering.
     */
    ID,

    /**
     * Sort items by stack size, largest stacks first.
     */
    QUANTITY,

    /**
     * Sort items by rarity/quality tier.
     */
    RARITY,

    /**
     * Sort items by category first, then alphabetically within each category.
     */
    CATEGORY_ALPHABETICAL
}
