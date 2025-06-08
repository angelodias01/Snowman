package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * The PositionContent enum represents the different types of content 
 * that can occupy a position on the game board.
 *
 * This enum is used to define the state of each cell on the board grid, 
 * enabling the game to identify and interact with the content at each position.
 *
 * Enum Constants:
 * - NO_SNOW: Represents an empty cell with no snow.
 * - SNOW: Represents a cell covered with snow that can be cleared during gameplay.
 * - BLOCK: Represents an immovable obstacle on the board.
 * - SNOWMAN: Represents a completed snowman occupying the position.
 *
 * Key Use Cases:
 * - Defines the content for board cells during initialization or gameplay.
 * - Helps in rendering and updating the board dynamically.
 * - Simplifies logic for interactions between game entities and board cells.
 *
 * @version 1.0
 * @since 2025-06-08
 */
public enum PositionContent {
    /** Represents an empty cell with no snow. */
    NO_SNOW,

    /** Represents a cell covered with snow that players can clear. */
    SNOW,

    /** Represents an immovable obstacle blocking the cell. */
    BLOCK,

    /** Represents a completed snowman standing on the cell. */
    SNOWMAN
}