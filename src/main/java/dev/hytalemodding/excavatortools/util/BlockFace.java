package dev.hytalemodding.excavatortools.util;

/**
 * Represents the face of a block that was interacted with.
 * This is used to determine the orientation of the 3x3 mining area.
 */
public enum BlockFace {
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    EAST(1, 0, 0),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0);

    private final int modX;
    private final int modY;
    private final int modZ;

    BlockFace(int modX, int modY, int modZ) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    /**
     * Get the X modifier for this face direction
     */
    public int getModX() {
        return modX;
    }

    /**
     * Get the Y modifier for this face direction
     */
    public int getModY() {
        return modY;
    }

    /**
     * Get the Z modifier for this face direction
     */
    public int getModZ() {
        return modZ;
    }

    /**
     * Get the opposite face
     */
    public BlockFace getOppositeFace() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
            case UP -> DOWN;
            case DOWN -> UP;
        };
    }

    /**
     * Check if this is a horizontal face (NORTH, SOUTH, EAST, WEST)
     */
    public boolean isHorizontal() {
        return this == NORTH || this == SOUTH || this == EAST || this == WEST;
    }

    /**
     * Check if this is a vertical face (UP, DOWN)
     */
    public boolean isVertical() {
        return this == UP || this == DOWN;
    }

    /**
     * Get a BlockFace from direction modifiers
     */
    public static BlockFace fromModifiers(int modX, int modY, int modZ) {
        for (BlockFace face : values()) {
            if (face.modX == modX && face.modY == modY && face.modZ == modZ) {
                return face;
            }
        }
        return NORTH; // Default
    }
}
