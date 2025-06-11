package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The LevelManager class is responsible for managing and providing access to different Levels in the Snowman game.
 * <p>
 * It encapsulates the initialization of game levels, tracks the current level being played,
 * and facilitates progression to subsequent levels. This class stores all the predefined
 * game levels and allows retrieval of level configurations through `BoardModel` instances.
 * <p>
 * Key Responsibilities:
 * - Initialize game levels with specific board layouts and starting configurations.
 * - Manage the progression between levels.
 * - Provide current level configurations in a structured manner.
 * <p>
 * Design Considerations:
 * - The levels are managed as a sequential list (`List<Level>`), where each Level is indexed starting at 0.
 * - The levels are predefined in the `initializeLevels` method.
 *
 *  @author Ângelo Dias(24288), Edgar Brito(22895)
 */
public class LevelManager {
    /**
     * List containing all predefined levels in the game.
     */
    private final List<Level> levels;

    /**
     * The index of the currently active level in the list of levels.
     */
    private int currentLevelIndex;

    /**
     * Constructs a new LevelManager instance and initializes all predefined levels.
     * <p>
     * By default, the first level (`index 0`) is set as the active level upon instantiation.
     */
    public LevelManager() {
        this.levels = new ArrayList<>();
        this.currentLevelIndex = 0;
        initializeLevels(); // Populate levels list with predefined configurations.
    }

    /**
     * Creates the initial board configuration for Level 1.
     * <p>
     * The method generates a 5x5 grid filled with snow (`PositionContent.SNOW`).
     *
     * @return A two-dimensional list representing the board for Level 1.
     */
    private List<List<PositionContent>> createLevel1Board() {
        List<List<PositionContent>> board = new ArrayList<>();
        int rows = 5;
        int cols = 5;

        // Create a 5x5 grid and fill with snow
        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.SNOW);
            }
            board.add(row);
        }

        return board;
    }

    /**
     * Creates the initial board configuration for Level 2.
     * <p>
     * The method generates a 6x6 grid filled with snow (`PositionContent.SNOW`) and places
     * blocks (`PositionContent.BLOCK`) in a cross-shaped pattern in the center.
     *
     * @return A two-dimensional list representing the board for Level 2.
     */
    private List<List<PositionContent>> createLevel2Board() {
        List<List<PositionContent>> board = new ArrayList<>();
        int rows = 6;
        int cols = 6;

        // Create a 6x6 grid filled with snow
        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.SNOW);
            }
            board.add(row);
        }

        // Add blocks in a cross shape in the center of the board
        board.get(2).set(2, PositionContent.BLOCK);
        board.get(2).set(3, PositionContent.BLOCK);
        board.get(3).set(2, PositionContent.BLOCK);
        board.get(3).set(3, PositionContent.BLOCK);

        return board;
    }
    private List<List<PositionContent>> createLevel3Board() {
        List<List<PositionContent>> board = new ArrayList<>();
        int rows = 6;
        int cols = 6;

        // Create a 6x6 grid filled with snow
        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.SNOW);
            }
            board.add(row);
        }

        board.get(0).set(4, PositionContent.BLOCK);  // Block 1
        board.get(3).set(1, PositionContent.BLOCK);  // Block 2
        board.get(2).set(0, PositionContent.BLOCK);  // Block 3
        board.get(4).set(4, PositionContent.BLOCK);  // Bl

        return board;
    }
    private List<List<PositionContent>> createLevel4Board() {
        List<List<PositionContent>> board = new ArrayList<>();
        int rows = 6;
        int cols = 6;

        // Create a 6x6 grid filled with snow
        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.SNOW);
            }
            board.add(row);
        }

        board.get(0).set(3, PositionContent.BLOCK);  // Block 1
        board.get(2).set(2, PositionContent.BLOCK);  // Block 2
        board.get(3).set(3, PositionContent.BLOCK);  // Block 3
        board.get(4).set(0, PositionContent.BLOCK);  // Block 4


        return board;
    }
    private List<List<PositionContent>> createLevel5Board() {
        List<List<PositionContent>> board = new ArrayList<>();
        int rows = 6;
        int cols = 6;

        // Create a 6x6 grid filled with snow
        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.SNOW);
            }
            board.add(row);
        }

        board.get(2).set(2, PositionContent.BLOCK);
        board.get(2).set(3, PositionContent.BLOCK);
        board.get(3).set(2, PositionContent.BLOCK);
        board.get(3).set(3, PositionContent.BLOCK);


        return board;
    }

    /**
     * Initializes all predefined levels in the game.
     * <p>
     * Each level is configured with its specific board layout, snowballs, monster starting position,
     * and a unique identifier. The levels are added to the `levels` list in sequential order.
     */
    private void initializeLevels() {
        // Level 1 configuration (5x5 grid with snowballs placed in specific positions)
        List<List<PositionContent>> board1 = createLevel1Board();
        List<Snowball> snowballs1 = Arrays.asList(
                new Snowball(2, 3, SnowballType.SMALL),
                new Snowball(1, 2, SnowballType.SMALL),
                new Snowball(3, 1, SnowballType.SMALL)
        );
        levels.add(new Level(board1, 0, 0, snowballs1, 1)); // Set Level 1 with ID 1

        // Level 2 configuration (6x6 grid with added blocks and snowballs)
        List<List<PositionContent>> board2 = createLevel2Board();
        List<Snowball> snowballs2 = Arrays.asList(
                new Snowball(1, 1, SnowballType.SMALL),
                new Snowball(1, 4, SnowballType.SMALL),
                new Snowball(4, 1, SnowballType.SMALL)
        );
        levels.add(new Level(board2, 0, 0, snowballs2, 2)); // Set Level 2 with ID 2
        // Level 2 configuration (6x6 grid with added blocks and snowballs)
        List<List<PositionContent>> board3 = createLevel3Board();
        List<Snowball> snowballs3 = Arrays.asList(
                new Snowball(2, 3, SnowballType.SMALL),
                new Snowball(4, 0, SnowballType.SMALL),
                new Snowball(1, 2, SnowballType.SMALL)
        );
        levels.add(new Level(board3, 0, 0, snowballs3, 3)); // Set Level 2 with ID 2
        // Level 2 configuration (6x6 grid with added blocks and snowballs)
        List<List<PositionContent>> board4 = createLevel4Board();
        List<Snowball> snowballs4 = Arrays.asList(
                new Snowball(1, 1, SnowballType.SMALL),
                new Snowball(2, 4, SnowballType.SMALL),
                new Snowball(4, 2, SnowballType.SMALL)

        );
        levels.add(new Level(board4, 0, 0, snowballs4, 4)); // Set Level 2 with ID 2
        // Level 2 configuration (6x6 grid with added blocks and snowballs)
        List<List<PositionContent>> board5 = createLevel5Board();
        List<Snowball> snowballs5 = Arrays.asList(
                new Snowball(0, 1, SnowballType.SMALL),
                new Snowball(1, 3, SnowballType.SMALL),
                new Snowball(4, 0, SnowballType.SMALL)
        );
        levels.add(new Level(board5, 0, 0, snowballs5, 5)); // Set Level 2 with ID 2
    }

    /**
     * Retrieves the current level's configuration as a `BoardModel` instance.
     *
     * @return A `BoardModel` representing the current level.
     */
    public BoardModel getCurrentLevel() {
        return levels.get(currentLevelIndex).createBoardModel();
    }

    /**
     * Retrieves the index of the currently active level in the levels list.
     *
     * @return The index of the current level.
     */
    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    /**
     * Checks whether there are more levels available after the current one.
     *
     * @return `true` if there are more levels available; `false` otherwise.
     */
    public boolean hasNextLevel() {
        return currentLevelIndex < levels.size() - 1;
    }

    /**
     * Advances to the next level if available and retrieves its configuration.
     * <p>
     * This method increments the `currentLevelIndex` if there is a subsequent level
     * and returns the configuration (`BoardModel`) of the next level.
     *
     * @return A `BoardModel` for the next level or `null` if no additional levels are available.
     */
    public BoardModel loadNextLevel() {
        if (hasNextLevel()) {
            currentLevelIndex++;
            return getCurrentLevel();
        }
        return null;
    }
}