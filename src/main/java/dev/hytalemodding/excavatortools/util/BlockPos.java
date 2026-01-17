package dev.hytalemodding.excavatortools.util;

import java.util.Objects;

/**
 * Represents a position in the world (block coordinates).
 *
 * Note: Hytale likely has its own BlockPos or Vec3i class.
 * This can be replaced with the native class once the API is available.
 */
public class BlockPos {

    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    /**
     * Get a new BlockPos offset by the given amounts
     */
    public BlockPos offset(int dx, int dy, int dz) {
        return new BlockPos(x + dx, y + dy, z + dz);
    }

    /**
     * Get a new BlockPos offset in the direction of the given face
     */
    public BlockPos offset(BlockFace face) {
        return offset(face.getModX(), face.getModY(), face.getModZ());
    }

    /**
     * Get a new BlockPos offset in the direction of the given face by the given amount
     */
    public BlockPos offset(BlockFace face, int amount) {
        return offset(face.getModX() * amount, face.getModY() * amount, face.getModZ() * amount);
    }

    /**
     * Get the distance to another BlockPos
     */
    public double distanceTo(BlockPos other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Get the squared distance to another BlockPos (faster than distanceTo)
     */
    public double distanceToSquared(BlockPos other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPos blockPos = (BlockPos) o;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "BlockPos{x=" + x + ", y=" + y + ", z=" + z + "}";
    }
}
