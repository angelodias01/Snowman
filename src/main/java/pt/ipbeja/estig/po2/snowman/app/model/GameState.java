package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a snapshot of the game state at a specific moment.
 * This class is used for features like undo/redo and resetting levels,
 * storing the board layout, monster position, and all snowballs' states.
 */
public class GameState {
    private final List<List<PositionContent>> boardState; // Copy of the board grid at this state
    private final Monster monsterState;                   // Copy of the monster's position
    private final List<Snowball> snowballsState;          // List of snowballs' positions and types

    /**
     * Constructs a GameState from the current state of the game.
     * Performs deep copies where needed to ensure the snapshot is immutable and independent.
     *
     * @param board The current game board model to snapshot.
     */
    public GameState(BoardModel board) {
        // Deep copy of the board's current layout (to avoid reference sharing)
        this.boardState = new ArrayList<>();
        for (List<PositionContent> row : board.getBoard()) {
            this.boardState.add(new ArrayList<>(row)); // Copy each row individually
        }

        // Create a copy of the monster's current position
        this.monsterState = new Monster(board.getMonster().getRow(), board.getMonster().getCol());

        // Deep copy of the current list of snowballs
        this.snowballsState = new ArrayList<>();
        for (Snowball snowball : board.getSnowballs()) {
            this.snowballsState.add(
                    new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType())
            );
        }
    }

    /**
     * Returns the stored board layout for this state.
     *
     * @return A deep copy of the board state.
     */
    public List<List<PositionContent>> getBoardState() {
        return boardState;
    }

    /**
     * Returns the stored position of the monster for this state.
     *
     * @return A copy of the monster object at the time of snapshot.
     */
    public Monster getMonsterState() {
        return monsterState;
    }

    /**
     * Returns the stored list of snowballs for this state.
     *
     * @return A deep copy of all snowballs at the time of snapshot.
     */
    public List<Snowball> getSnowballsState() {
        return snowballsState;
    }
}