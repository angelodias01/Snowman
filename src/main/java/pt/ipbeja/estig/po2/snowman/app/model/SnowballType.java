package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * The SnowballType enum represents the various sizes and states that a snowball can achieve
 * in the Snowman game. It is used to define the type or size of a snowball, which determines
 * its interactions with other game elements and its ability to grow or combine.
 * <p>
 * Enum Constants:
 * - SMALL: Represents the smallest size of a snowball.
 * - MID: Represents a medium-sized snowball.
 * - BIG: Represents the largest size of an individual snowball.
 * - MID_SMALL: Represents a combination of a medium and small snowball.
 * - BIG_SMALL: Represents a combination of a large and small snowball.
 * - BIG_MID: Represents a combination of a large and medium snowball.
 * - COMPLETE: Represents a complete snowman formed by combining all necessary parts.
 * <p>
 * Usage:
 * - This enum is used in the game logic to track and manage the state of snowballs.
 * - The combination logic depends on the type of snowballs involved, leading to either
 * larger snowballs or the formation of a complete snowman.
 * <p>
 * Key Features:
 * - Provides a clear and extensible representation of snowball states.
 * - Facilitates logic for snowball growth and combination.
 *
 * @author Ângelo Dias, Edgar Brito
 */
public enum SnowballType {
    /**
     * Represents the smallest size of a snowball.
     */
    SMALL,

    /**
     * Represents a medium-sized snowball.
     */
    MID,

    /**
     * Represents the largest single snowball.
     */
    BIG,

    /**
     * Represents a combined snowball made of medium and small parts.
     */
    MID_SMALL,

    /**
     * Represents a combined snowball made of large and small parts.
     */
    BIG_SMALL,

    /**
     * Represents a combined snowball made of large and medium parts.
     */
    BIG_MID,

    /**
     * Represents a complete snowman formed by all required parts.
     */
    COMPLETE
}