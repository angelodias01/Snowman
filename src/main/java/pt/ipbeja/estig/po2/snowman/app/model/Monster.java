package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * Monster class representing the movable element controlled by the player.
 */
public class Monster extends MobileElement {

    public Monster(int row, int col) {
        super(row, col);
    }

    /**
     * Moves the monster in the specified direction (including handling snowball movements).
     *
     * @param direction The direction of the movement.
     * @param board     Reference to the game board model.
     * @return true if the movement was successful; false otherwise.
     */
    @Override
    public boolean move(Direction direction, BoardModel board) {
        int newRow = row;
        int newCol = col;

        // Calculate the new position based on direction
        switch (direction) {
            case UP:
                newRow--;
                break;
            case DOWN:
                newRow++;
                break;
            case LEFT:
                newCol--;
                break;
            case RIGHT:
                newCol++;
                break;
        }

        // Check if the new position is valid
        if (!board.validPosition(newRow, newCol)) {
            return false; // Invalid move
        }

        // Check if there's a snowball in the target position
        Snowball snowball = board.snowballInPosition(newRow, newCol);
        if (snowball != null) {
            // Attempt to move the snowball
            if (!board.moveSnowball(direction, snowball)) {
                return false; // Snowball movement failed; block the monster's movement
            }
        }

        // Update the monster's position
        row = newRow;
        col = newCol;
        return true; // Movement was successful
    }
}