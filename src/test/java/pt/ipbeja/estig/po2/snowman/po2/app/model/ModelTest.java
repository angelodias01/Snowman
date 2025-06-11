package pt.ipbeja.estig.po2.snowman.po2.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po2.snowman.app.model.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BoardModel, Monster, and Snowball game logic.
 *
 * This test suite verifies the behavior of the monster's movement on the board,
 * interactions with snowballs (including growth and positioning),
 * and the formation of a complete snowman by stacking snowballs.
 *
 * It uses a 5x5 grid with various PositionContent states to simulate the game environment.
 *
 *  * @authors Ângelo Dias(24288), Edgar Brito(22895)
 */
public class ModelTest {
    private BoardModel board;
    private Monster monster;
    List<List<PositionContent>> content = new ArrayList<>();
    List<Snowball> snowballs = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        snowballs.clear();
        List<List<PositionContent>> grid = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(PositionContent.NO_SNOW); // Initializes everything without snow
            }
            grid.add(row);
        }

        // Set the snow to position (0, 1)
        grid.get(0).set(1, PositionContent.SNOW);

        content = grid;

        // Monster starts at (0,3)
        monster = new Monster(0, 3);

        // Small ball at (0,2)
        snowballs.add(new Snowball(0, 2, SnowballType.SMALL));

        board = new BoardModel(content, monster, snowballs);
    }

    @Test
    @DisplayName("Move the monster to the left (invalid, should stay in place)")
    void testMonsterToTheLeft() {
        // Block position to the left of the monster
        content.get(0).set(2, PositionContent.BLOCK);

        System.out.println("Before move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        board.moveMonster(Direction.LEFT);

        System.out.println("After move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        // Expect monster to remain at (0, 3)
        assertEquals(0, monster.getRow());
        assertEquals(3, monster.getCol());
    }

    @Test
    @DisplayName("Create average (MID) snowball after pushing once to the left")
    void testCreateAverageSnowballLeftPush() {
        // Snowball starts at (0, 2)
        Snowball snowball = board.snowballInPosition(0, 2);
        System.out.println("Initial snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Initial snowball type: " + snowball.getType());

        // Make sure there is snow at the position where the ball will grow, here (0, 1)
        content.get(0).set(1, PositionContent.SNOW);
        System.out.println("Set (0,1) to SNOW");

        // Move the monster to the left to push the snowball to (0,1)
        board.moveMonster(Direction.LEFT);
        System.out.println("After move LEFT - monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Snowball type after move: " + snowball.getType());

        // Checks that the snowball has grown to MID and is in the correct position
        assertEquals(SnowballType.MID, snowball.getType());
        assertEquals(0, snowball.getRow());
        assertEquals(1, snowball.getCol());
    }

    @Test
    public void testCompleteSnowman() {
        board.getSnowballs().clear();
        board.getSnowballs().add(new Snowball(1, 3, SnowballType.BIG));
        board.getSnowballs().add(new Snowball(2, 3, SnowballType.MID));
        board.getSnowballs().add(new Snowball(3, 3, SnowballType.SMALL));

        // Place the monster in row 4, column 3
        board.getMonster().setRow(4);
        board.getMonster().setCol(3);

        System.out.println("Initial monster position: (" + board.getMonster().getRow() + ", " + board.getMonster().getCol() + ")");
        System.out.println("Snowballs on board:");
        for (Snowball sb : board.getSnowballs()) {
            System.out.println(" - Snowball at (" + sb.getRow() + ", " + sb.getCol() + ") of type " + sb.getType());
        }

        // The monster climbs 2 times to pass the 3 balls in order: (3,3), (2,3), (1,3)
        board.moveMonster(Direction.UP); // go to (3,3) - small ball
        System.out.println("Monster moved UP to: (" + board.getMonster().getRow() + ", " + board.getMonster().getCol() + ")");

        board.moveMonster(Direction.UP); // go to (2,3) - medium ball
        System.out.println("Monster moved UP to: (" + board.getMonster().getRow() + ", " + board.getMonster().getCol() + ")");

        // Now it checks if the state at that position or along the column has changed to SNOWMAN
        PositionContent content = board.getPositionContent(1, 3);
        System.out.println("Content at (1,3): " + content);

        assertEquals(PositionContent.SNOWMAN, content, "Deve formar SNOWMAN");
    }
}