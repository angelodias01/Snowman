package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * Direction enum represents cardinal directions for movement in a 2D grid-based game system.
 * 
 * This enumeration provides a type-safe way to represent and handle directional movement
 * by associating each direction with its corresponding grid coordinate changes.
 * It encapsulates the logic for translating cardinal directions into coordinate modifications.
 *
 * Features:
 * - Four cardinal directions (UP, DOWN, LEFT, RIGHT)
 * - Coordinate delta values for each direction
 * - Immutable design
 * - Grid-based movement support
 *
 * Usage example:
 * <pre>
 *     Direction dir = Direction.UP;
 *     int newRow = currentRow + dir.getDeltaRow();
 *     int newCol = currentCol + dir.getDeltaCol();
 * </pre>
 *
 * @since 1.0
 * @version 1.0
 */
public enum Direction {
    /**
     * Represents upward movement (negative row delta)
     * Delta: (-1, 0)
     */
    UP(-1, 0),

    /**
     * Represents downward movement (positive row delta)
     * Delta: (1, 0)
     */
    DOWN(1, 0),

    /**
     * Represents leftward movement (negative column delta)
     * Delta: (0, -1)
     */
    LEFT(0, -1),

    /**
     * Represents rightward movement (positive column delta)
     * Delta: (0, 1)
     */
    RIGHT(0, 1);

    /** The change in row index when moving in this direction */
    private final int deltaRow;

    /** The change in column index when moving in this direction */
    private final int deltaCol;

    /**
     * Constructs a Direction with specified coordinate deltas.
     * 
     * @param deltaRow The change in row coordinate (-1 for up, 1 for down, 0 for no change)
     * @param deltaCol The change in column coordinate (-1 for left, 1 for right, 0 for no change)
     */
    Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     * Returns the row delta value for this direction.
     * 
     * @return An integer representing the change in row index:
     *         -1 for upward movement
     *         1 for downward movement
     *         0 for horizontal movement
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     * Returns the column delta value for this direction.
     * 
     * @return An integer representing the change in column index:
     *         -1 for leftward movement
     *         1 for rightward movement
     *         0 for vertical movement
     */
    public int getDeltaCol() {
        return deltaCol;
    }

    /**
     * Technical Notes:
     * - Thread-safe due to enum implementation
     * - Immutable design prevents state-related bugs
     * - Memory efficient with only four instances
     * - Coordinate system assumes top-left origin (0,0)
     * - Positive Y-axis points downward (standard computer graphics convention)
     * 
     * Limitations:
     * - Only supports cardinal directions (no diagonals)
     * - Fixed step size of 1 unit
     */
}