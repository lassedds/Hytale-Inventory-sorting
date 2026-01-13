package com.inventorysorting.services;

import com.hypixel.hytale.server.entity.player.Player;
import com.hypixel.hytale.server.inventory.Container;
import com.hypixel.hytale.server.inventory.Inventory;
import com.hypixel.hytale.server.inventory.PlayerInventory;
import com.hypixel.hytale.server.item.ItemStack;
import com.inventorysorting.config.SortingConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core service for sorting inventories and containers.
 * Handles item stacking, merging, and sorting based on configured sort modes.
 */
public class SortingService {

    private final SortingConfig config;

    public SortingService(SortingConfig config) {
        this.config = config;
    }

    /**
     * Sorts a player's inventory using the default sort mode.
     */
    public void sortPlayerInventory(Player player) {
        sortPlayerInventory(player, config.getDefaultSortMode());
    }

    /**
     * Sorts a player's inventory using the specified sort mode.
     */
    public void sortPlayerInventory(Player player, SortMode mode) {
        PlayerInventory inventory = player.getInventory();

        // Determine the range to sort (exclude hotbar if configured)
        int startSlot = config.isSortHotbarSeparately() ? 9 : 0;
        int endSlot = inventory.getSize();

        // Extract items from the sortable range
        List<ItemStack> items = extractItems(inventory, startSlot, endSlot);

        // Stack and merge items first
        items = stackItems(items);

        // Sort items based on mode
        items = sortItems(items, mode);

        // Place items back into inventory
        placeItems(inventory, items, startSlot, endSlot);

        // Play sort sound if enabled
        if (config.isPlaySortSound()) {
            playSortSound(player);
        }
    }

    /**
     * Sorts a container (chest, barrel, etc.) using the default sort mode.
     */
    public void sortContainer(Player player, Container container) {
        sortContainer(player, container, config.getDefaultSortMode());
    }

    /**
     * Sorts a container using the specified sort mode.
     */
    public void sortContainer(Player player, Container container, SortMode mode) {
        Inventory inventory = container.getInventory();

        // Extract all items from container
        List<ItemStack> items = extractItems(inventory, 0, inventory.getSize());

        // Stack and merge items
        items = stackItems(items);

        // Sort items
        items = sortItems(items, mode);

        // Place items back
        placeItems(inventory, items, 0, inventory.getSize());

        // Play sort sound
        if (config.isPlaySortSound()) {
            playSortSound(player);
        }
    }

    /**
     * Attempts to auto-stack a picked up item into existing stacks.
     * Returns the remaining item if it couldn't be fully stacked.
     */
    public ItemStack autoStack(PlayerInventory inventory, ItemStack item) {
        if (item == null || item.isEmpty()) {
            return null;
        }

        int remaining = item.getAmount();

        // Try to stack with existing items
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot != null && canStack(slot, item)) {
                int maxStack = slot.getMaxStackSize();
                int space = maxStack - slot.getAmount();

                if (space > 0) {
                    int toAdd = Math.min(space, remaining);
                    slot.setAmount(slot.getAmount() + toAdd);
                    remaining -= toAdd;

                    if (remaining <= 0) {
                        return null;
                    }
                }
            }
        }

        if (remaining < item.getAmount()) {
            item.setAmount(remaining);
        }

        return item;
    }

    /**
     * Extracts items from an inventory within a slot range.
     */
    private List<ItemStack> extractItems(Inventory inventory, int startSlot, int endSlot) {
        List<ItemStack> items = new ArrayList<>();

        for (int i = startSlot; i < endSlot; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && !item.isEmpty()) {
                items.add(item.copy());
                inventory.setItem(i, null);
            }
        }

        return items;
    }

    /**
     * Stacks and merges similar items together.
     */
    private List<ItemStack> stackItems(List<ItemStack> items) {
        Map<String, List<ItemStack>> grouped = new HashMap<>();

        // Group items by their stackable identity
        for (ItemStack item : items) {
            String key = getStackKey(item);
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }

        List<ItemStack> result = new ArrayList<>();

        // Merge stacks within each group
        for (List<ItemStack> group : grouped.values()) {
            int totalAmount = 0;
            ItemStack template = null;

            for (ItemStack item : group) {
                totalAmount += item.getAmount();
                if (template == null) {
                    template = item.copy();
                }
            }

            if (template != null) {
                int maxStack = template.getMaxStackSize();

                // Create full stacks
                while (totalAmount > 0) {
                    int stackSize = Math.min(maxStack, totalAmount);
                    ItemStack stack = template.copy();
                    stack.setAmount(stackSize);
                    result.add(stack);
                    totalAmount -= stackSize;
                }
            }
        }

        return result;
    }

    /**
     * Sorts items based on the specified mode.
     */
    private List<ItemStack> sortItems(List<ItemStack> items, SortMode mode) {
        Comparator<ItemStack> comparator = getComparator(mode);
        items.sort(comparator);
        return items;
    }

    /**
     * Places items back into an inventory within a slot range.
     */
    private void placeItems(Inventory inventory, List<ItemStack> items, int startSlot, int endSlot) {
        int slotIndex = startSlot;

        for (ItemStack item : items) {
            if (slotIndex >= endSlot) {
                break;
            }
            inventory.setItem(slotIndex++, item);
        }

        // Clear remaining slots
        for (int i = slotIndex; i < endSlot; i++) {
            inventory.setItem(i, null);
        }
    }

    /**
     * Gets the comparator for the specified sort mode.
     */
    private Comparator<ItemStack> getComparator(SortMode mode) {
        return switch (mode) {
            case ALPHABETICAL -> Comparator.comparing(this::getDisplayName);

            case CATEGORY -> Comparator.comparingInt(item -> getCategoryOrder(getCategory(item)));

            case ID -> Comparator.comparing(item -> item.getType().getId());

            case QUANTITY -> Comparator.comparingInt(ItemStack::getAmount).reversed();

            case RARITY -> Comparator.comparingInt(this::getRarityOrder).reversed();

            case CATEGORY_ALPHABETICAL -> Comparator
                    .comparingInt((ItemStack item) -> getCategoryOrder(getCategory(item)))
                    .thenComparing(this::getDisplayName);
        };
    }

    /**
     * Gets a unique key for stacking purposes.
     * Items with the same key can be stacked together.
     */
    private String getStackKey(ItemStack item) {
        StringBuilder key = new StringBuilder();
        key.append(item.getType().getId());

        // Include metadata/NBT in the key if present
        if (item.hasMetadata()) {
            key.append(":").append(item.getMetadata().hashCode());
        }

        return key.toString();
    }

    /**
     * Checks if two items can be stacked together.
     */
    private boolean canStack(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (!a.getType().equals(b.getType())) return false;
        if (a.getAmount() >= a.getMaxStackSize()) return false;

        // Check metadata equality
        if (a.hasMetadata() != b.hasMetadata()) return false;
        if (a.hasMetadata() && !a.getMetadata().equals(b.getMetadata())) return false;

        return true;
    }

    /**
     * Gets the display name of an item for alphabetical sorting.
     */
    private String getDisplayName(ItemStack item) {
        if (item.hasDisplayName()) {
            return item.getDisplayName().toLowerCase();
        }
        return item.getType().getDefaultName().toLowerCase();
    }

    /**
     * Determines the category of an item based on its type.
     */
    private ItemCategory getCategory(ItemStack item) {
        var type = item.getType();
        String id = type.getId().toLowerCase();

        // Weapons
        if (id.contains("sword") || id.contains("bow") || id.contains("crossbow") ||
            id.contains("axe") || id.contains("mace") || id.contains("spear") ||
            id.contains("dagger") || id.contains("staff")) {
            return ItemCategory.WEAPONS;
        }

        // Tools
        if (id.contains("pickaxe") || id.contains("shovel") || id.contains("hoe") ||
            id.contains("hammer") || id.contains("fishing_rod") || id.contains("shears")) {
            return ItemCategory.TOOLS;
        }

        // Armor
        if (id.contains("helmet") || id.contains("chestplate") || id.contains("leggings") ||
            id.contains("boots") || id.contains("armor") || id.contains("shield")) {
            return ItemCategory.ARMOR;
        }

        // Food
        if (type.isEdible()) {
            return ItemCategory.FOOD;
        }

        // Blocks
        if (type.isBlock()) {
            return ItemCategory.BLOCKS;
        }

        // Materials - common crafting ingredients
        if (id.contains("ingot") || id.contains("nugget") || id.contains("gem") ||
            id.contains("dust") || id.contains("ore") || id.contains("leather") ||
            id.contains("string") || id.contains("feather") || id.contains("bone")) {
            return ItemCategory.MATERIALS;
        }

        return ItemCategory.MISC;
    }

    /**
     * Gets the sort order for a category based on config.
     */
    private int getCategoryOrder(ItemCategory category) {
        List<String> order = config.getCategoryOrder();
        int index = order.indexOf(category.name());
        return index >= 0 ? index : category.getDefaultOrder() + 100;
    }

    /**
     * Gets the rarity order of an item (higher = rarer).
     */
    private int getRarityOrder(ItemStack item) {
        if (item.hasRarity()) {
            return item.getRarity().ordinal();
        }
        return 0;
    }

    /**
     * Plays a sorting sound effect for the player.
     */
    private void playSortSound(Player player) {
        player.playSound("inventorysorting:sort_complete", 1.0f, 1.0f);
    }
}
