package pt.ipbeja.estig.po2.snowman.po2.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po2.snowman.app.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    private BoardModel board;
    private Monster monster;

    @BeforeEach
    public void setUp() {
        List<List<PositionContent>> grid = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(PositionContent.NO_SNOW);
            }
            grid.add(row);
        }
        grid.get(2).set(2, PositionContent.SNOW);

        monster = new Monster(1, 1);
        List<Snowball> snowballs = new ArrayList<>();
        snowballs.add(new Snowball(1, 2, SnowballType.SMALL));

        board = new BoardModel(List.of(grid), monster, snowballs);
    }

    @Test
    public void testMonsterToTheLeft() {
        assertTrue(board.moveMonster(Direction.LEFT));
        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());
    }

    @Test
    public void testCreateAverageSnowball() {
        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.DOWN);

        Snowball snowball = board.snowballInPosition(2, 2);
        assertNotNull(snowball);
        assertEquals(SnowballType.MID, snowball.getType());
    }

    @Test
    public void testCompleteSnowman() {
        board.getSnowballs().clear();
        board.getSnowballs().add(new Snowball(1, 2, SnowballType.BIG));
        board.getSnowballs().add(new Snowball(2, 2, SnowballType.MID));
        board.getSnowballs().add(new Snowball(3, 2, SnowballType.SMALL));

        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.DOWN);
        board.getSnowballs().forEach(snowball -> {
            assertEquals(PositionContent.SNOWMAN, board.getPositionContent(2, 2));
        });
    }
}