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

        monster = new Monster(2, 0); // Start monster at bottom

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
        board.moveMonster(Direction.LEFT);

        assertEquals(2, monster.getRow());
        assertEquals(0, monster.getCol()); // Should not move
    }

    @Test
    @DisplayName("Move the monster up")
    void testMonsterToUp() {
        board.moveMonster(Direction.UP); // (2,0) -> (1,0)

        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());
    }

    @Test
    @DisplayName("Test invalid monster move beyond board")
    void testMonsterInvalidMove() {
        board.moveMonster(Direction.UP); // (2,0) -> (1,0)
        board.moveMonster(Direction.UP); // (1,0) -> (0,0)
        boolean validMove = board.moveMonster(Direction.UP); // Invalid: out of bounds

        assertFalse(validMove);
        assertEquals(0, monster.getRow());
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

        content.get(0).set(0, PositionContent.SNOW); // Ensure snow for growth

        board.moveMonster(Direction.UP); // (2,0) -> (1,0)
        board.moveMonster(Direction.UP); // Push snowball to (0,0)

        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());

        assertEquals(1, monster.getRow());  // ✅ If monster STOPS at (1,0)
        // assertEquals(0, monster.getRow()); // <-- Uncomment if monster follows snowball
    }

    @Test
    @DisplayName("Create average (MID) snowball after pushing once")
    void testCreateAverageSnowball() {
        Snowball snowball = board.snowballInPosition(1, 0);
        assertEquals(SnowballType.SMALL, snowball.getType());

        content.get(0).set(0, PositionContent.SNOW); // ✅ Ensure snow is present for upgrade

        board.moveMonster(Direction.UP);  // (2,0) -> (1,0)
        board.moveMonster(Direction.UP);  // Push to (0,0), grows if snow present

        assertEquals(SnowballType.MID, snowball.getType());
        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());
    }

    @Test
    @DisplayName("Test invalid snowball move (can't push out of bounds)")
    void testSnowballInvalidMove() {
        Snowball snowball = board.snowballInPosition(1, 0);
        assertEquals(1, snowball.getRow());

        content.get(0).set(0, PositionContent.SNOW); // For growth

        board.moveMonster(Direction.UP);  // to (1,0)
        boolean pushed = board.moveMonster(Direction.UP); // pushes to (0,0)

        assertTrue(pushed);
        assertEquals(0, snowball.getRow());

        boolean invalidPush = board.moveMonster(Direction.UP); // Try push beyond board
        assertFalse(invalidPush);
        assertEquals(0, snowball.getRow()); // Should remain at top
    }
}