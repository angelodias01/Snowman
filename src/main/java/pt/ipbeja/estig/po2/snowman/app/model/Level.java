package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Level class represents the configuration and properties of a specific level in the Snowman game.
 * <p>
 * This class encapsulates the initial state of a level, including the board layout, the
 * starting position of the monster, the initial positions of snowballs, and the level's identifier number.
 * Through its methods, Level provides facilities to create a playable model (`BoardModel`)
 * based on its stored configurations.
 * <p>
 * Key Responsibilities:
 * - Store the initial configuration of a level, including board layout, monster, and snowballs.
 * - Provide the ability to create a fresh instance of the game board (`BoardModel`) for this level.
 * <p>
 * Design Considerations:
 * - Deep copies are used to ensure immutability of initial configurations.
 * - Grouping and encapsulating level data simplifies level management and game initialization.
 *
 *  @author Ângelo Dias(24288), Edgar Brito(22895)
 */
public class Level {

    /**
     * A deep copy of the initial board layout for the level.
     */
    private final List<List<PositionContent>> initialBoard;

    /**
     * The starting row index of the monster on the board.
     */
    private final int monsterStartRow;

    /**
     * The starting column index of the monster on the board.
     */
    private final int monsterStartCol;

    /**
     * A list of snowballs present at the beginning of the level.
     */
    private final List<Snowball> initialSnowballs;

    /**
     * The unique number assigned to this level.
     */
    private final int levelNumber;

    /**
     * Constructs a new Level instance with the specified initial configuration parameters.
     * <p>
     * This constructor performs deep copies of the provided board layout and snowballs list to
     * ensure immutability of the initial level state.
     *
     * @param board       A 2D list representing the initial board layout of the level.
     * @param monsterRow  The starting row index of the monster.
     * @param monsterCol  The starting column index of the monster.
     * @param snowballs   A list of initial snowballs' positions and types.
     * @param levelNumber A unique number identifying the level.
     */
    public Level(List<List<PositionContent>> board, int monsterRow, int monsterCol,
                 List<Snowball> snowballs, int levelNumber) {
        // Create a deep copy of the board to prevent direct modifications
        this.initialBoard = new ArrayList<>();
        for (List<PositionContent> row : board) {
            this.initialBoard.add(new ArrayList<>(row));
        }
        this.monsterStartRow = monsterRow;
        this.monsterStartCol = monsterCol;

        // Create a deep copy of the snowballs list for the same reason
        this.initialSnowballs = new ArrayList<>(snowballs);

        this.levelNumber = levelNumber;
    }

    /**
     * Creates a new instance of the `BoardModel` class based on this level's configuration.
     * <p>
     * This method generates a fresh game board with all components (board layout, monster, and snowballs)
     * in their starting positions. Deep copies of the components ensure that the new `BoardModel`
     * is independent of other game states.
     *
     * @return A new `BoardModel` instance initialized with the level's configurations.
     */
    public BoardModel createBoardModel() {
        // Copy the board layout
        List<List<PositionContent>> boardCopy = new ArrayList<>();
        for (List<PositionContent> row : initialBoard) {
            boardCopy.add(new ArrayList<>(row));
        }

        // Create the monster with its starting position
        Monster monster = new Monster(monsterStartRow, monsterStartCol);

        // Create deep copies of the initial snowballs
        List<Snowball> snowballsCopy = new ArrayList<>();
        for (Snowball snowball : initialSnowballs) {
            snowballsCopy.add(new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType()));
        }

        // Return a new BoardModel instance
        return new BoardModel(boardCopy, monster, snowballsCopy);
    }

    /**
     * Returns the unique identifier number for this level.
     *
     * @return The level's unique number.
     */
    public int getLevelNumber() {
        return levelNumber;
    }
}