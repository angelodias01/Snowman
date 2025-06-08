package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * Enum that represents the four cardinal directions a character or object can move on a 2D grid.
 * Each direction is associated with a change in row and column indexes.
 */
public enum Direction {
    // Move one row up (decrement row index)
    UP(-1, 0),

    // Move one row down (increment row index)
    DOWN(1, 0),

    // Move one column to the left (decrement column index)
    LEFT(0, -1),

    // Move one column to the right (increment column index)
    RIGHT(0, 1);

    // Change in row index for the given direction
    private final int deltaRow;

    // Change in column index for the given direction
    private final int deltaCol;

    /**
     * Constructs a Direction enum with specified row and column deltas.
     *
     * @param deltaRow The change in row when moving in this direction.
     * @param deltaCol The change in column when moving in this direction.
     */
    Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     * Gets the row delta associated with the direction.
     *
     * @return The change in row index.
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     * Gets the column delta associated with the direction.
     *
     * @return The change in column index.
     */
    public int getDeltaCol() {
        return deltaCol;
    }
}
