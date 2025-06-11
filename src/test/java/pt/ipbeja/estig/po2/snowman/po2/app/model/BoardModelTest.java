package pt.ipbeja.estig.po2.snowman.po2.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po2.snowman.app.model.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BoardModelTest {
    Monster monster;
    List<List<PositionContent>> content = new ArrayList<>();
    List<Snowball> snowballs = new ArrayList<>();
    BoardModel board;

    int rows = 3;
    int cols = 1;

    @BeforeEach
    public void setUp() {
        content.clear();
        snowballs.clear();

        monster = new Monster(2, 0); // Starts monster at bottom

        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    row.add(PositionContent.SNOW); // Top row has snow
                } else {
                    row.add(PositionContent.NO_SNOW);
                }
            }
            content.add(row);
        }

        Snowball snowball = new Snowball(1, 0, SnowballType.SMALL); // Snowball in the middle
        snowballs.add(snowball);

        board = new BoardModel(content, monster, snowballs);
    }

    @Test
    @DisplayName("Move the monster to the left (invalid, should stay in place)")
    void testMonsterToTheLeft() {
        System.out.println("Before move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        board.moveMonster(Direction.LEFT);

        System.out.println("After move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        assertEquals(2, monster.getRow());
        assertEquals(0, monster.getCol()); // Should not move
    }

    @Test
    @DisplayName("Move the monster up")
    void testMonsterToUp() {
        System.out.println("Before move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        board.moveMonster(Direction.UP); // (2,0) -> (1,0)

        System.out.println("After move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());
    }


    @Test
    @DisplayName("Test invalid monster move beyond board")
    void testMonsterInvalidMove() {
        System.out.println("Initial position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        board.moveMonster(Direction.UP); // (2,0) -> (1,0)
        System.out.println("After 1st UP move: (" + monster.getRow() + ", " + monster.getCol() + ")");

        board.moveMonster(Direction.UP); // (1,0) -> (0,0)
        System.out.println("After 2nd UP move: (" + monster.getRow() + ", " + monster.getCol() + ")");

        boolean validMove = board.moveMonster(Direction.UP); // Invalid: out of bounds
        System.out.println("After 3rd UP move (should be invalid): " + validMove);
        System.out.println("Final position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        assertFalse(validMove);
        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());
    }

    @Test
    @DisplayName("Check board position contents")
    void testPositionContent() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                PositionContent element = board.getPositionContent(i, j);
                System.out.print(" | " + element);
            }
            System.out.println(" |");
        }
    }

    @Test
    @DisplayName("Move snowball up")
    void testMoveSnowballUpward() {
        Snowball snowball = board.snowballInPosition(1, 0);
        assertNotNull(snowball);

        System.out.println("Initial snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Initial monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        content.get(0).set(0, PositionContent.SNOW); // Ensure snow for growth
        System.out.println("Top cell (0,0) set to SNOW");

        board.moveMonster(Direction.UP); // (2,0) -> (1,0)
        System.out.println("After 1st UP move - monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        board.moveMonster(Direction.UP); // Push snowball to (0,0)
        System.out.println("After 2nd UP move - monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());

        assertEquals(1, monster.getRow());  // Check if monster STOPS at (1,0)
    }

    @Test
    @DisplayName("Create average (MID) snowball after pushing once")
    void testCreateAverageSnowball() {
        Snowball snowball = board.snowballInPosition(1, 0);
        System.out.println("Initial snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Initial snowball type: " + snowball.getType());

        // Ensure snow is present at (0,0) for growth
        content.get(0).set(0, PositionContent.SNOW);
        System.out.println("Set (0,0) to SNOW");

        // Move monster up to (1,0), where the snowball is
        board.moveMonster(Direction.UP);
        System.out.println("After 1st UP move - monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        // Push snowball up to (0,0), it should grow if snow is present
        board.moveMonster(Direction.UP);
        System.out.println("After 2nd UP move - monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        System.out.println("Snowball type after move: " + snowball.getType());

        // Final assertions
        assertEquals(SnowballType.MID, snowball.getType());
        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());
    }

    @Test
    @DisplayName("Test invalid snowball move (can't push out of bounds)")
    void testSnowballInvalidMove() {
        Snowball snowball = board.snowballInPosition(1, 0);
        System.out.println("Initial snowball position: (" + snowball.getRow() + ", " + snowball.getCol() + ")");
        assertEquals(1, snowball.getRow());

        content.get(0).set(0, PositionContent.SNOW); // For growth
        System.out.println("Set (0,0) to SNOW");

        board.moveMonster(Direction.UP);  // to (1,0)
        System.out.println("After 1st UP move - monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        boolean pushed = board.moveMonster(Direction.UP); // pushes to (0,0)
        System.out.println("After 2nd UP move - pushed: " + pushed);
        System.out.println("Monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        assertFalse(pushed);
        assertEquals(0, snowball.getRow());

        boolean invalidPush = board.moveMonster(Direction.UP); // Try push beyond board
        System.out.println("Attempted invalid push beyond board - result: " + invalidPush);
        System.out.println("Monster at: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Snowball at: (" + snowball.getRow() + ", " + snowball.getCol() + ")");

        assertFalse(invalidPush);
        assertEquals(0, snowball.getRow()); // Should remain at top
    }

    //TODO A function here needs verification for the end snowball not sure if last or before last..
}