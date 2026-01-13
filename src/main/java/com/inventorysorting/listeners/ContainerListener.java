package com.inventorysorting.listeners;

import com.hypixel.hytale.server.event.EventHandler;
import com.hypixel.hytale.server.event.Listener;
import com.hypixel.hytale.server.event.inventory.ContainerOpenEvent;
import com.hypixel.hytale.server.event.inventory.ContainerCloseEvent;
import com.hypixel.hytale.server.event.inventory.InventoryClickEvent;
import com.hypixel.hytale.server.entity.player.Player;
import com.hypixel.hytale.server.inventory.Container;
import com.inventorysorting.config.SortingConfig;
import com.inventorysorting.services.SortingService;

/**
 * Listener for container-related events.
 * Handles auto-sorting on container close and middle-click sorting.
 */
public class ContainerListener implements Listener {

    private final SortingService sortingService;
    private final SortingConfig config;

    public ContainerListener(SortingService sortingService, SortingConfig config) {
        this.sortingService = sortingService;
        this.config = config;
    }

    /**
     * Handles container close to optionally auto-sort.
     */
    @EventHandler
    public void onContainerClose(ContainerCloseEvent event) {
        if (!config.isAutoSortOnClose()) {
            return;
        }

        Player player = event.getPlayer();
        Container container = event.getContainer();

        // Auto-sort the container before it closes
        sortingService.sortContainer(player, container);
    }

    /**
     * Handles middle-click on inventory/container for quick sorting.
     * Middle-clicking empty space in an inventory sorts it.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check for middle-click on empty slot
        if (event.getClickType() != InventoryClickEvent.ClickType.MIDDLE) {
            return;
        }

        // Only trigger if clicking an empty slot
        if (event.getCurrentItem() != null && !event.getCurrentItem().isEmpty()) {
            return;
        }

        Player player = event.getPlayer();

        // Determine which inventory was clicked
        if (event.isTopInventory() && player.getOpenContainer() != null) {
            // Clicked in container area - sort container
            sortingService.sortContainer(player, player.getOpenContainer());
            event.setCancelled(true);
        } else if (event.isBottomInventory()) {
            // Clicked in player inventory area - sort player inventory
            sortingService.sortPlayerInventory(player);
            event.setCancelled(true);
        }
    }

    /**
     * Handles container open - can be used for initialization if needed.
     */
    @EventHandler
    public void onContainerOpen(ContainerOpenEvent event) {
        // Reserved for future features like:
        // - Remembering sort preferences per container type
        // - Auto-applying saved sort configurations
    }
}
