package pt.ipbeja.estig.po2.snowman.po2.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po2.snowman.app.model.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Snowball class and its interaction with the BoardModel.
 *
 * This test suite verifies:
 * - Snowball movement in different directions (left, up).
 * - Snowball growth from SMALL to MID type when pushed onto snow.
 * - Handling of invalid moves where the snowball cannot move beyond the board edges.
 *
 * The tests use a 3x3 board with snow placed only at position (0,1), and
 * a single snowball initially located at position (1,1).
 *
 * Console output is included in the tests for easier step-by-step tracing of
 * snowball positions and move results during execution.
 *
 *  @author Ângelo Dias(24288), Edgar Brito(22895)
 */
public class SnowballTest {
    Monster monster;
    List<List<PositionContent>> content = new ArrayList<>();
    List<Snowball> snowballs = new ArrayList<>();
    BoardModel board;

    int rows = 3;
    int cols = 3;

    @BeforeEach
    public void setUp() {
        monster = new Monster(2, 0);

        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                if (i == 0 && j == 1) {
                    row.add(PositionContent.SNOW);
                } else {
                    row.add(PositionContent.NO_SNOW);
                }
            }
            content.add(row);
        }

        Snowball snowball = new Snowball(1, 1, SnowballType.SMALL);
        snowballs.add(snowball);
        board = new BoardModel(content, monster, snowballs);

    }

    @Test
    @DisplayName("Move snowball to the left")
    void testMoveSnowballToTheLeft() {
        Snowball snowball = board.snowballInPosition(1, 1);

        System.out.println("Before move:");
        System.out.println("Snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        snowball.move(Direction.LEFT, board);

        System.out.println("After move:");
        System.out.println("Snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        assertEquals(1, snowball.getRow());
        assertEquals(0, snowball.getCol());
    }

    @Test
    @DisplayName("Move the snowball up")
    void testMoveSnowballToUp() {
        Snowball snowball = board.snowballInPosition(1, 1);

        System.out.println("Before move:");
        System.out.println("Snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        snowball.move(Direction.UP, board);

        System.out.println("After move:");
        System.out.println("Snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        assertEquals(0, snowball.getRow());
        assertEquals(1, snowball.getCol());
    }


    @Test
    @DisplayName("Create average snowball")
    void testCreateAverageSnowball() {
        Snowball snowball = board.snowballInPosition(1, 1);

        System.out.println("Before move:");
        System.out.println("Snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Snowball type: " + snowball.getType());

        snowball.move(Direction.UP, board);

        System.out.println("After move:");
        System.out.println("Snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Snowball type: " + snowball.getType());

        assertEquals(0, snowball.getRow());
        assertEquals(1, snowball.getCol());
        assertEquals(SnowballType.MID, snowball.getType());
    }

    @Test
    @DisplayName("Test invalid snowball move")
    void testSnowballInvalidMove() {
        Snowball snowball = board.snowballInPosition(1, 1);

        System.out.println("Initial position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        boolean status = snowball.move(Direction.UP, board);
        System.out.println("After 1st move UP:");
        System.out.println("Position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Move successful? " + status);

        assertEquals(0, snowball.getRow());
        assertEquals(1, snowball.getCol());
        assertTrue(status);

        status = snowball.move(Direction.UP, board);
        System.out.println("After 2nd move UP (expected invalid):");
        System.out.println("Position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Move successful? " + status);

        assertEquals(0, snowball.getRow());
        assertEquals(1, snowball.getCol());
        assertFalse(status);
    }
}