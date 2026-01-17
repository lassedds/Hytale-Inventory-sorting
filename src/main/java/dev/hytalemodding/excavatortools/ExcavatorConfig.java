package dev.hytalemodding.excavatortools;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for the ExcavatorTools plugin.
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
        // Stone-like blocks for the Excavator Hammer
        // Note: Block IDs should match Hytale's block registry
        hammerBlocks.add("stone");
        hammerBlocks.add("cobblestone");
        hammerBlocks.add("granite");
        hammerBlocks.add("diorite");
        hammerBlocks.add("andesite");
        hammerBlocks.add("deepslate");
        hammerBlocks.add("tuff");
        hammerBlocks.add("calcite");
        hammerBlocks.add("coal_ore");
        hammerBlocks.add("iron_ore");
        hammerBlocks.add("gold_ore");
        hammerBlocks.add("diamond_ore");
        hammerBlocks.add("emerald_ore");
        hammerBlocks.add("lapis_ore");
        hammerBlocks.add("redstone_ore");
        hammerBlocks.add("copper_ore");
        hammerBlocks.add("netherrack");
        hammerBlocks.add("basalt");
        hammerBlocks.add("blackstone");
        hammerBlocks.add("end_stone");
        hammerBlocks.add("obsidian");
        hammerBlocks.add("sandstone");
        hammerBlocks.add("red_sandstone");
        hammerBlocks.add("prismarine");
        hammerBlocks.add("terracotta");
        hammerBlocks.add("brick");
        hammerBlocks.add("nether_brick");

        // Dirt-like blocks for the Excavator Shovel
        shovelBlocks.add("dirt");
        shovelBlocks.add("grass_block");
        shovelBlocks.add("coarse_dirt");
        shovelBlocks.add("podzol");
        shovelBlocks.add("mycelium");
        shovelBlocks.add("rooted_dirt");
        shovelBlocks.add("mud");
        shovelBlocks.add("sand");
        shovelBlocks.add("red_sand");
        shovelBlocks.add("gravel");
        shovelBlocks.add("clay");
        shovelBlocks.add("soul_sand");
        shovelBlocks.add("soul_soil");
        shovelBlocks.add("snow");
        shovelBlocks.add("snow_block");
        shovelBlocks.add("powder_snow");
        shovelBlocks.add("farmland");
        shovelBlocks.add("dirt_path");
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
