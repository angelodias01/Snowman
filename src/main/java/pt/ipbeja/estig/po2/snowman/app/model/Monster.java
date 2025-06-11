package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * The Monster class represents the movable element controlled by the player in the game.
 * <p>
 * This class extends the abstract class MobileElement and implements the
 * specific movement logic for the monster, including interactions with snowballs
 * and the game board.
 * <p>
 * Key Responsibilities:
 * - Represent the monster controlled by the player in terms of position and movement.
 * - Implement movement functionality based on board constraints and snowball interactions.
 *
 *  @author Ângelo Dias(24288), Edgar Brito(22895)
 */
public class Monster extends MobileElement {

    /**
     * Constructor that initializes the monster's starting position.
     *
     * @param row The initial row position of the monster.
     * @param col The initial column position of the monster.
     */
    public Monster(int row, int col) {
        super(row, col);
    }

    /**
     * Moves the monster in the specified direction if possible.
     * <p>
     * This method first calculates the new position based on the direction.
     * It checks if the move is valid by ensuring the destination is within bounds
     * and not blocked. If a snowball is present at the destination, the method
     * attempts to move the snowball before proceeding with the monster's movement.
     *
     * @param direction The direction of movement (UP, DOWN, LEFT, RIGHT).
     * @param board     The game board model containing the current game state and logic.
     * @return true if the movement was successful; false otherwise.
     */
    @Override
    public boolean move(Direction direction, BoardModel board) {
        // Calculate the next position based on the direction
        int newRow = this.row;
        int newCol = this.col;

        switch (direction) {
            case UP -> newRow--;
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }

        // Validate the new position on the board
        if (!board.validPosition(newRow, newCol)) {
            return false;
        }

        // Check if a snowball is present at the destination
        Snowball snowball = board.snowballInPosition(newRow, newCol);
        if (snowball != null) {
            // Attempt to move the snowball first
            if (!snowball.move(direction, board)) {
                return false; // If snowball cannot be moved, the monster also cannot move
            }
        }

        // Proceed with moving the monster
        // Remove snow from the current position
        board.setPositionContent(this.row, this.col, PositionContent.NO_SNOW);

        // Update the monster's position
        this.row = newRow;
        this.col = newCol;

        // Remove snow from the new position
        board.setPositionContent(this.row, this.col, PositionContent.NO_SNOW);

        return true;
    }

    /**
     * Retrieves the current row position of the monster.
     *
     * @return The monster's current row position.
     */
    @Override
    public int getRow() {
        return super.getRow();
    }

    /**
     * Retrieves the current column position of the monster.
     *
     * @return The monster's current column position.
     */
    @Override
    public int getCol() {
        return super.getCol();
    }
}