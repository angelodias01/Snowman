package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * The abstract class MobileElement represents any movable element on the game board.
 *
 * Mobile elements have position coordinates (row and column) on the board
 * and methods to retrieve or update their position. The class also defines
 * an abstract movement method that must be implemented by specific subclasses.
 *
 * Typical subclasses include:
 * - Monster: the monster controlled by the player.
 * - Snowball: the snowball that can be pushed or combined in the game.
 * 
 * @version 1.0
 * @since 2025-06-08
 */
public abstract class MobileElement {
    /** Current row on the board where the element is located. */
    protected int row;

    /** Current column on the board where the element is located. */
    protected int col;

    /**
     * Constructor that initializes the position of the movable element on the board.
     *
     * @param row The initial row of the element on the board.
     * @param col The initial column of the element on the board.
     */
    public MobileElement(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Updates the row (row) of the element to the specified new position.
     *
     * @param row The new row of the element.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Updates the column (col) of the element to the specified new position.
     *
     * @param col The new column of the element.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Returns the current row where the element is located on the board.
     *
     * @return The current row of the element.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the current column where the element is located on the board.
     *
     * @return The current column of the element.
     */
    public int getCol() {
        return col;
    }

    /**
     * Defines the abstract movement behavior of the element.
     *
     * Subclasses must implement this method to provide specific movement logic,
     * such as checking whether the move is valid or interacting with other
     * elements on the board.
     *
     * @param direction The direction of the movement (UP, DOWN, LEFT, RIGHT).
     * @param board The game board model containing the current game logic and state.
     * @return true if the movement was successful; otherwise, false.
     */
    public abstract boolean move(Direction direction, BoardModel board);
}