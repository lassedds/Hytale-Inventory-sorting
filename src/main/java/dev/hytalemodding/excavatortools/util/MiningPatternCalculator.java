package dev.hytalemodding.excavatortools.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the 3x3 (or NxN) mining pattern based on the block face that was hit.
 *
 * The mining pattern is always perpendicular to the face hit:
 * - Hitting a horizontal face (NORTH/SOUTH/EAST/WEST): mines a vertical plane
 * - Hitting a vertical face (UP/DOWN): mines a horizontal plane
 */
public class MiningPatternCalculator {

    /**
     * Calculate all block positions in the mining pattern.
     *
     * @param center The center block position (the block that was directly mined)
     * @param face   The face of the block that was hit
     * @param radius The radius of the pattern (1 = 3x3, 2 = 5x5, etc.)
     * @return List of all BlockPos in the pattern, including the center
     */
    public static List<BlockPos> calculatePattern(BlockPos center, BlockFace face, int radius) {
        List<BlockPos> positions = new ArrayList<>();

        // Determine the two axes perpendicular to the face
        int[] axis1 = getFirstPerpendicularAxis(face);
        int[] axis2 = getSecondPerpendicularAxis(face);

        // Generate all positions in the NxN pattern
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                int offsetX = axis1[0] * i + axis2[0] * j;
                int offsetY = axis1[1] * i + axis2[1] * j;
                int offsetZ = axis1[2] * i + axis2[2] * j;

                positions.add(center.offset(offsetX, offsetY, offsetZ));
            }
        }

        return positions;
    }

    /**
     * Get the first perpendicular axis based on the face.
     * Returns {x, y, z} modifiers for the axis.
     */
    private static int[] getFirstPerpendicularAxis(BlockFace face) {
        return switch (face) {
            case NORTH, SOUTH -> new int[]{1, 0, 0};  // X axis (horizontal, left-right)
            case EAST, WEST -> new int[]{0, 0, 1};    // Z axis (horizontal, forward-back)
            case UP, DOWN -> new int[]{1, 0, 0};      // X axis (horizontal plane)
        };
    }

    /**
     * Get the second perpendicular axis based on the face.
     * Returns {x, y, z} modifiers for the axis.
     */
    private static int[] getSecondPerpendicularAxis(BlockFace face) {
        return switch (face) {
            case NORTH, SOUTH -> new int[]{0, 1, 0};  // Y axis (vertical)
            case EAST, WEST -> new int[]{0, 1, 0};    // Y axis (vertical)
            case UP, DOWN -> new int[]{0, 0, 1};      // Z axis (horizontal plane)
        };
    }

    /**
     * Calculate the pattern with an optional depth (mining multiple layers).
     *
     * @param center The center block position
     * @param face   The face of the block that was hit
     * @param radius The radius of the pattern
     * @param depth  How many layers deep to mine (1 = single layer)
     * @return List of all BlockPos in the pattern
     */
    public static List<BlockPos> calculatePatternWithDepth(BlockPos center, BlockFace face, int radius, int depth) {
        List<BlockPos> positions = new ArrayList<>();
        BlockFace miningDirection = face.getOppositeFace(); // Mine into the block

        for (int d = 0; d < depth; d++) {
            BlockPos layerCenter = center.offset(miningDirection, d);
            positions.addAll(calculatePattern(layerCenter, face, radius));
        }

        return positions;
    }

    /**
     * Filter positions to only include valid positions (above y=0, below world height, etc.)
     *
     * @param positions List of positions to filter
     * @param minY      Minimum Y coordinate (typically 0 or -64)
     * @param maxY      Maximum Y coordinate (typically 256 or 320)
     * @return Filtered list of valid positions
     */
    public static List<BlockPos> filterValidPositions(List<BlockPos> positions, int minY, int maxY) {
        return positions.stream()
                .filter(pos -> pos.getY() >= minY && pos.getY() <= maxY)
                .toList();
    }

    /**
     * Get the total number of blocks in a pattern of the given radius.
     *
     * @param radius The radius (1 = 3x3, 2 = 5x5, etc.)
     * @return Total number of blocks
     */
    public static int getPatternSize(int radius) {
        int size = (radius * 2) + 1;
        return size * size;
    }
}
