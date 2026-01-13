package com.inventorysorting.listeners;

import com.hypixel.hytale.server.event.EventHandler;
import com.hypixel.hytale.server.event.Listener;
import com.hypixel.hytale.server.event.player.PlayerItemPickupEvent;
import com.hypixel.hytale.server.event.player.PlayerKeyPressEvent;
import com.hypixel.hytale.server.entity.player.Player;
import com.hypixel.hytale.server.item.ItemStack;
import com.inventorysorting.config.SortingConfig;
import com.inventorysorting.services.SortingService;

/**
 * Listener for player inventory-related events.
 * Handles auto-stacking on pickup and keybind sorting.
 */
public class InventoryListener implements Listener {

    private final SortingService sortingService;
    private final SortingConfig config;

    public InventoryListener(SortingService sortingService, SortingConfig config) {
        this.sortingService = sortingService;
        this.config = config;
    }

    /**
     * Handles item pickup to auto-stack items into existing stacks.
     */
    @EventHandler
    public void onItemPickup(PlayerItemPickupEvent event) {
        if (!config.isAutoStackOnPickup()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Try to auto-stack the item
        ItemStack remaining = sortingService.autoStack(player.getInventory(), item);

        if (remaining == null) {
            // Item was fully stacked, cancel the default pickup behavior
            event.setCancelled(true);
            // The item entity should be removed since we handled it
            event.getItemEntity().remove();
        } else if (remaining.getAmount() < item.getAmount()) {
            // Partial stack, update the remaining amount
            event.setItem(remaining);
        }
    }

    /**
     * Handles keybind press for quick sorting.
     */
    @EventHandler
    public void onKeyPress(PlayerKeyPressEvent event) {
        String keybind = config.getSortKeybind();

        if (keybind == null || keybind.isEmpty()) {
            return;
        }

        if (event.getKey().equalsIgnoreCase(keybind)) {
            Player player = event.getPlayer();

            // Check if player has a container open
            if (player.getOpenContainer() != null) {
                // Sort the container
                sortingService.sortContainer(player, player.getOpenContainer());
            } else if (player.isInventoryOpen()) {
                // Sort player inventory
                sortingService.sortPlayerInventory(player);
            }
        }
    }
}
