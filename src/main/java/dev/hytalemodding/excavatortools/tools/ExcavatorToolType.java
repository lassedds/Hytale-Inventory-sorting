package dev.hytalemodding.excavatortools.tools;

/**
 * Enum representing the types of excavator tools available.
 */
public enum ExcavatorToolType {
    /**
     * Excavator Hammer - for mining stone, ores, and hard blocks
     */
    HAMMER("excavator_hammer", "Excavator Hammer", "A powerful hammer that mines a 3x3 area of stone and ores"),

    /**
     * Excavator Shovel - for digging dirt, sand, gravel, and soft blocks
     */
    SHOVEL("excavator_shovel", "Excavator Shovel", "A wide shovel that digs a 3x3 area of dirt and sand");

    private final String id;
    private final String displayName;
    private final String description;

    ExcavatorToolType(String id, String displayName, String description) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the unique identifier for this tool type
     */
    public String getId() {
        return id;
    }

    /**
     * Get the display name for this tool type
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description for this tool type
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get an ExcavatorToolType by its ID
     */
    public static ExcavatorToolType fromId(String id) {
        for (ExcavatorToolType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }
}
