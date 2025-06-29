package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * The Snowball class represents a movable snowball on the game board.
 * <p>
 * This class extends the MobileElement abstract class and incorporates additional features
 * specific to snowballs, such as their type (size) and the ability to grow or combine
 * with other snowballs.
 * <p>
 * Key Responsibilities:
 * - Represent a snowball in terms of size, position, and movement on the board.
 * - Define behaviors for growing in size and combining with other snowballs.
 * - Implement movement logic specific to snowballs, including interactions
 * with the board and other elements.
 *
 *  @author Ângelo Dias(24288), Edgar Brito(22895)
 */
public class Snowball extends MobileElement {
    /**
     * The type/size of the snowball (e.g., SMALL, MID, BIG).
     */
    private SnowballType type;

    /**
     * Constructs a new Snowball with the specified position and type.
     *
     * @param row  The initial row position of the snowball.
     * @param col  The initial column position of the snowball.
     * @param type The initial type/size of the snowball.
     */
    public Snowball(int row, int col, SnowballType type) {
        super(row, col);
        this.type = type;
    }

    /**
     * Retrieves the type/size of the snowball.
     *
     * @return The current type of the snowball.
     */
    public SnowballType getType() {
        return type;
    }

    /**
     * Sets the type/size of the snowball.
     *
     * @param type The new type of the snowball.
     */
    public void setType(SnowballType type) {
        this.type = type;
    }

    /**
     * Increases the size of the snowball when it collects snow from the ground.
     * <p>
     * The snowball grows from SMALL to MID, and from MID to BIG. If it is already
     * BIG, no further growth occurs.
     */
    public void increaseSnowballType() {
        switch (type) {
            case SMALL -> setType(SnowballType.MID);
            case MID -> setType(SnowballType.BIG);
        }
    }

    /**
     * Implements the movement behavior of the snowball.
     * <p>
     * The method calculates the next position based on the given direction and checks:
     * 1. If the position is valid and within the board's bounds.
     * 2. If another snowball is present, attempts to combine them.
     * 3. If snow is present at the destination, the snowball collects it and grows.
     * Finally, updates the snowball's position upon successful movement.
     *
     * @param direction The direction of the movement.
     * @param board     The game board model containing the current game state.
     * @return true if the movement was successful; false otherwise.
     */
    @Override
    public boolean move(Direction direction, BoardModel board) {
        // Prevent movement if the snowball is combined or complete
        switch (type) {
            case MID_SMALL, BIG_SMALL, BIG_MID, COMPLETE -> {
                return false;
            }
            default -> {
                // Your existing move logic for SMALL, MID, BIG
                int newRow = this.getRow() + direction.getDeltaRow();
                int newCol = this.getCol() + direction.getDeltaCol();

                if (!board.validPosition(newRow, newCol)) {
                    return false;
                }

                Snowball otherSnowball = board.snowballInPosition(newRow, newCol);
                if (otherSnowball != null) {
                    return tryToCombineSnowballs(otherSnowball, board);
                }

                PositionContent destination = board.getPositionContent(newRow, newCol);
                if (destination == PositionContent.SNOW) {
                    increaseSnowballType();
                    board.setPositionContent(newRow, newCol, PositionContent.NO_SNOW);
                }

                this.setRow(newRow);
                this.setCol(newCol);
                return true;
            }
        }
    }

    /**
     * Attempts to combine the current snowball with another snowball.
     * <p>
     * If the two snowballs can combine, they are removed from the board
     * and replaced by a combined snowball or a complete snowman,
     * depending on their types.
     *
     * @param other The other snowball to combine with.
     * @param board The game board.
     * @return true if the combination was successful; false otherwise.
     */
    private boolean tryToCombineSnowballs(Snowball other, BoardModel board) {
        SnowballType newType = calculateCombinedType(this.type, other.type);
        if (newType != null) {
            // Remove the original snowballs
            board.getSnowballs().remove(other);
            board.getSnowballs().remove(this);

            if (newType == SnowballType.COMPLETE) {
                // Create a complete snowman at the position
                board.setPositionContent(other.getRow(), other.getCol(), PositionContent.SNOWMAN);
            } else {
                // Create a new combined snowball
                Snowball combinedSnowball = new Snowball(other.getRow(), other.getCol(), newType);
                board.getSnowballs().add(combinedSnowball);
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the resulting type of two combined snowballs.
     * <p>
     * This method determines whether two snowballs of the specified types
     * can combine into a larger snowball or a complete snowman.
     *
     * @param type1 The type of the first snowball.
     * @param type2 The type of the second snowball.
     * @return The resulting type, or null if the snowballs cannot combine.
     */
    private SnowballType calculateCombinedType(SnowballType type1, SnowballType type2) {
        if ((type1 == SnowballType.SMALL && type2 == SnowballType.MID) ||
                (type2 == SnowballType.SMALL && type1 == SnowballType.MID)) {
            return SnowballType.MID_SMALL;
        }
        if ((type1 == SnowballType.SMALL && type2 == SnowballType.BIG) ||
                (type2 == SnowballType.SMALL && type1 == SnowballType.BIG)) {
            return SnowballType.BIG_SMALL;
        }
        if ((type1 == SnowballType.MID && type2 == SnowballType.BIG) ||
                (type2 == SnowballType.MID && type1 == SnowballType.BIG)) {
            return SnowballType.BIG_MID;
        }
        if ((type1 == SnowballType.SMALL && type2 == SnowballType.BIG_MID) ||
                (type2 == SnowballType.SMALL && type1 == SnowballType.BIG_MID)) {
            return SnowballType.COMPLETE;
        }
        return null;
    }

}