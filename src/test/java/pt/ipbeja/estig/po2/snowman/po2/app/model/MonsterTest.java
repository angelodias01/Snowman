package pt.ipbeja.estig.po2.snowman.po2.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po2.snowman.app.model.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for the Monster class and its interactions with the BoardModel.
 *
 * This test suite verifies the monster's movement behavior within the board,
 * including valid moves (left, up), and invalid moves (attempting to move
 * beyond the board boundaries or into blocked positions).
 *
 * The tests also print the monster's position before and after moves to
 * aid in debugging and understanding the monster's state transitions.
 *
 *  @author Ângelo Dias(24288), Edgar Brito(22895)
 */
public class MonsterTest {
    Monster monster;
    List<List<PositionContent>> content = new ArrayList<>();
    List<Snowball> snowballs = new ArrayList<>();
    BoardModel board;

    int rows = 3;
    int cols = 3;

    @BeforeEach
    public void setUp() {
        monster = new Monster(2, 2);

        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    row.add(PositionContent.SNOW);
                } else {
                    row.add(PositionContent.NO_SNOW);
                }
            }
            content.add(row);
        }


        board = new BoardModel(content, monster, snowballs);

    }

    @Test
    @DisplayName("Move the monster to the left")
    void testMonsterToTheLeft() {
        System.out.println("Before move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        monster.move(Direction.LEFT, board);

        System.out.println("After move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        assertEquals(2, monster.getRow());
        assertEquals(1, monster.getCol());
    }

    @Test
    @DisplayName("Move the monster up")
    void testMonsterToUp() {
        System.out.println("Before move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        monster.move(Direction.UP, board);

        System.out.println("After move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        assertEquals(1, monster.getRow());
        assertEquals(2, monster.getCol());
    }

    @Test
    @DisplayName("Test invalid monster move")
    void testMonsterInvalidMove() {
        System.out.println("Before move:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");

        boolean validMove = board.moveMonster(Direction.RIGHT);

        System.out.println("After move attempt:");
        System.out.println("Monster position: (" + monster.getRow() + ", " + monster.getCol() + ")");
        System.out.println("Was move valid? " + validMove);

        assertEquals(2, monster.getRow());
        assertEquals(2, monster.getCol());

        assertFalse(validMove);
    }
}