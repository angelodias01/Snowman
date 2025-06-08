package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameState class represents a snapshot of the game's state at a specific point in time.
 * <p>
 * This class is utilized for features like undo, redo, and level resetting. It captures and stores
 * critical aspects of the game state, including the board layout, the monster's position, and
 * the states of all snowballs on the board. By creating deep copies of these elements, the
 * GameState class ensures immutability and independence from the current game state.
 * <p>
 * Key Responsibilities:
 * - Preserving a frozen snapshot of the game state for history-related functionalities
 * - Supporting operations such as undo/redo without affecting the current state of the game
 * - Providing access to stored elements (board, monster, and snowballs) through getter methods
 * <p>
 * Key Features:
 * - Deep copying of game board, monster, and snowball data for immutability
 * - Optimized for repeated operations like state saving, retrieval, and comparison
 * <p>
 * Usage Example:
 * <pre>
 *     // Create a snapshot of the game state
 *     GameState snapshot = new GameState(boardModel);
 *
 *     // Access stored game elements
 *     List<List<PositionContent>> savedBoard = snapshot.getBoardState();
 *     Monster savedMonster = snapshot.getMonsterState();
 *     List<Snowball> savedSnowballs = snapshot.getSnowballsState();
 * </pre>
 * <p>
 * Thread-Safety Note:
 * - Instances of GameState are immutable and inherently thread-safe.
 * - However, concurrent access to the BoardModel instance used to create snapshots should be synchronized externally.
 *
 * @author Ângelo Dias, Edgar Brito
 */
public class GameState {

    /**
     * A deep copy of the board grid at the time of snapshot
     */
    private final List<List<PositionContent>> boardState;

    /**
     * A snapshot of the monster's position and state
     */
    private final Monster monsterState;

    /**
     * A deep copy of the list of snowballs' positions and types
     */
    private final List<Snowball> snowballsState;

    /**
     * Constructs a new GameState by taking a snapshot of the current game state.
     * <p>
     * This constructor performs deep copies of the board layout, monster position,
     * and snowballs to ensure the GameState is immutable and independent of any
     * future changes in the game state.
     *
     * @param board The {@link BoardModel} instance representing the current game state
     */
    public GameState(BoardModel board) {
        // Deep copy of the board layout
        this.boardState = new ArrayList<>();
        for (List<PositionContent> row : board.getBoard()) {
            this.boardState.add(new ArrayList<>(row));
        }

        // Deep copy of the monster's position
        this.monsterState = new Monster(board.getMonster().getRow(), board.getMonster().getCol());

        // Deep copy of the snowballs
        this.snowballsState = new ArrayList<>();
        for (Snowball snowball : board.getSnowballs()) {
            this.snowballsState.add(
                    new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType())
            );
        }
    }

    /**
     * Retrieves the saved board layout for this snapshot.
     *
     * @return A deep copy of the board state at the time of this snapshot
     */
    public List<List<PositionContent>> getBoardState() {
        return boardState;
    }

    /**
     * Retrieves the saved position and state of the monster for this snapshot.
     *
     * @return A copy of the {@link Monster} object at the time of this snapshot
     */
    public Monster getMonsterState() {
        return monsterState;
    }

    /**
     * Retrieves the saved list of all snowballs for this snapshot.
     *
     * @return A deep copy of all snowballs at the time of this snapshot
     */
    public List<Snowball> getSnowballsState() {
        return snowballsState;
    }

    /**
     * Implementation Details:
     * - Immutability: All fields are final and initialized with deep copies, ensuring the snapshot is immutable.
     * - Deep Copying: Prevents unexpected mutations by other parts of the code that access the original objects.
     * - Optimized for Undo/Redo: Ensures safe and efficient restoration of previous game states.
     *
     * Known Limitations:
     * - Requires additional memory for deep copies, which could be significant for large boards or history stacks.
     * - Only stores state changes explicitly captured by the BoardModel (e.g., auxiliary state not tied to the board, monster, or snowballs is not saved).
     *
     * Future Enhancements:
     * - Introduce partial state-saving mechanisms for better performance in scenarios with frequent minor changes.
     * - Add support for additional game state attributes, if required, such as level-specific metadata.
     */
}