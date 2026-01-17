package dev.hytalemodding.excavatortools;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for the ExcavatorTools plugin.
 *
 * Block IDs are based on Hytale's naming conventions.
 * These may need to be updated once the decompiled server API is available
 * to confirm exact block registry names.
 *
 * Note: Once Hytale's configuration API is available, this should be loaded
 * from a config file (e.g., config.json or config.yml)
 */
public class ExcavatorConfig {

    // Mining radius (1 = 3x3, 2 = 5x5, etc.)
    private int miningRadius = 1;

    // Whether to require the correct tool for the block type
    private boolean requireCorrectTool = true;

    // Whether excavator tools consume extra durability per block
    private boolean extraDurabilityPerBlock = true;

    // Durability cost multiplier (1.0 = normal, 0.5 = half cost per extra block)
    private double durabilityMultiplier = 0.5;

    // Whether to drop items at player location or block location
    private boolean dropAtPlayer = false;

    // Whether to auto-smelt ores (future feature)
    private boolean autoSmelt = false;

    // Block types that the Excavator Hammer can mine (stone-like blocks)
    private final Set<String> hammerBlocks = new HashSet<>();

    // Block types that the Excavator Shovel can mine (dirt-like blocks)
    private final Set<String> shovelBlocks = new HashSet<>();

    public ExcavatorConfig() {
        initializeDefaultBlocks();
    }

    private void initializeDefaultBlocks() {
        // ============================================
        // HAMMER BLOCKS (Stone, Ores, Hard Materials)
        // ============================================

        // Basic stone types
        hammerBlocks.add("stone");
        hammerBlocks.add("cobblestone");
        hammerBlocks.add("rubble");           // Hytale starting material
        hammerBlocks.add("raw_stone");

        // Hytale ores (confirmed from game)
        hammerBlocks.add("copper_ore");
        hammerBlocks.add("iron_ore");
        hammerBlocks.add("gold_ore");
        hammerBlocks.add("silver_ore");
        hammerBlocks.add("cobalt_ore");
        hammerBlocks.add("thorium_ore");
        hammerBlocks.add("adamantite_ore");
        hammerBlocks.add("mithril_ore");
        hammerBlocks.add("coal_ore");

        // Decorative/building stone
        hammerBlocks.add("sandstone");
        hammerBlocks.add("limestone");
        hammerBlocks.add("marble");
        hammerBlocks.add("granite");
        hammerBlocks.add("slate");
        hammerBlocks.add("basalt");

        // Bricks and processed stone
        hammerBlocks.add("stone_brick");
        hammerBlocks.add("stone_bricks");
        hammerBlocks.add("brick");
        hammerBlocks.add("bricks");
        hammerBlocks.add("clay_brick");

        // Zone-specific stones (Hytale has multiple zones)
        hammerBlocks.add("desert_stone");
        hammerBlocks.add("jungle_stone");
        hammerBlocks.add("tundra_stone");
        hammerBlocks.add("cave_stone");
        hammerBlocks.add("underground_stone");

        // Hardened/special blocks
        hammerBlocks.add("obsidian");
        hammerBlocks.add("hardite");
        hammerBlocks.add("bedrock");          // Typically unbreakable, but included for completeness

        // ============================================
        // SHOVEL BLOCKS (Dirt, Sand, Soft Materials)
        // ============================================

        // Basic dirt types
        shovelBlocks.add("dirt");
        shovelBlocks.add("grass");
        shovelBlocks.add("grass_block");
        shovelBlocks.add("mud");
        shovelBlocks.add("peat");
        shovelBlocks.add("topsoil");

        // Sand variants
        shovelBlocks.add("sand");
        shovelBlocks.add("red_sand");
        shovelBlocks.add("desert_sand");
        shovelBlocks.add("beach_sand");
        shovelBlocks.add("fine_sand");

        // Gravel and loose materials
        shovelBlocks.add("gravel");
        shovelBlocks.add("pebbles");
        shovelBlocks.add("loose_rock");

        // Clay types
        shovelBlocks.add("clay");
        shovelBlocks.add("wet_clay");
        shovelBlocks.add("terracotta");

        // Snow and ice (shovel-appropriate)
        shovelBlocks.add("snow");
        shovelBlocks.add("snow_block");
        shovelBlocks.add("packed_snow");
        shovelBlocks.add("powder_snow");

        // Farming/garden blocks
        shovelBlocks.add("farmland");
        shovelBlocks.add("tilled_dirt");
        shovelBlocks.add("garden_soil");
        shovelBlocks.add("compost");
        shovelBlocks.add("mulch");

        // Zone-specific dirt (Hytale zones)
        shovelBlocks.add("jungle_dirt");
        shovelBlocks.add("forest_dirt");
        shovelBlocks.add("swamp_mud");
        shovelBlocks.add("tundra_dirt");
        shovelBlocks.add("permafrost");

        // Ash and volcanic
        shovelBlocks.add("ash");
        shovelBlocks.add("volcanic_ash");
        shovelBlocks.add("soot");
    }

    // Getters and setters

    public int getMiningRadius() {
        return miningRadius;
    }

    public void setMiningRadius(int miningRadius) {
        this.miningRadius = Math.max(1, Math.min(miningRadius, 3)); // Clamp between 1-3
    }

    public boolean isRequireCorrectTool() {
        return requireCorrectTool;
    }

    public void setRequireCorrectTool(boolean requireCorrectTool) {
        this.requireCorrectTool = requireCorrectTool;
    }

    public boolean isExtraDurabilityPerBlock() {
        return extraDurabilityPerBlock;
    }

    public void setExtraDurabilityPerBlock(boolean extraDurabilityPerBlock) {
        this.extraDurabilityPerBlock = extraDurabilityPerBlock;
    }

    public double getDurabilityMultiplier() {
        return durabilityMultiplier;
    }

    public void setDurabilityMultiplier(double durabilityMultiplier) {
        this.durabilityMultiplier = Math.max(0.0, Math.min(durabilityMultiplier, 1.0));
    }

    public boolean isDropAtPlayer() {
        return dropAtPlayer;
    }

    public void setDropAtPlayer(boolean dropAtPlayer) {
        this.dropAtPlayer = dropAtPlayer;
    }

    public boolean isAutoSmelt() {
        return autoSmelt;
    }

    public void setAutoSmelt(boolean autoSmelt) {
        this.autoSmelt = autoSmelt;
    }

    public Set<String> getHammerBlocks() {
        return hammerBlocks;
    }

    public Set<String> getShovelBlocks() {
        return shovelBlocks;
    }

    /**
     * Add a block to the hammer block list
     */
    public void addHammerBlock(String blockId) {
        hammerBlocks.add(blockId.toLowerCase());
    }

    /**
     * Add a block to the shovel block list
     */
    public void addShovelBlock(String blockId) {
        shovelBlocks.add(blockId.toLowerCase());
    }

    /**
     * Remove a block from the hammer block list
     */
    public void removeHammerBlock(String blockId) {
        hammerBlocks.remove(blockId.toLowerCase());
    }

    /**
     * Remove a block from the shovel block list
     */
    public void removeShovelBlock(String blockId) {
        shovelBlocks.remove(blockId.toLowerCase());
    }

    /**
     * Check if a block can be mined by the Excavator Hammer
     */
    public boolean isHammerBlock(String blockId) {
        return hammerBlocks.contains(blockId.toLowerCase());
    }

    /**
     * Check if a block can be mined by the Excavator Shovel
     */
    public boolean isShovelBlock(String blockId) {
        return shovelBlocks.contains(blockId.toLowerCase());
    }

    /**
     * Get the actual size of the mining area (e.g., radius 1 = 3x3)
     */
    public int getMiningSize() {
        return (miningRadius * 2) + 1;
    }
}
