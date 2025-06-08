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

        board = new BoardModel(grid, monster, snowballs); // Removido List.of()
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
        // Configura manualmente as snowballs no tabuleiro
        board.getSnowballs().clear();
        board.getSnowballs().add(new Snowball(3, 3, SnowballType.BIG));
        board.getSnowballs().add(new Snowball(3, 3, SnowballType.MID));
        board.getSnowballs().add(new Snowball(3, 3, SnowballType.SMALL));

        // Invoca o método de movimentação para ativar a lógica de combinação
        board.moveMonster(Direction.RIGHT); // Monstro avança para perto das snowballs
        board.moveMonster(Direction.DOWN);  // Ativa a combinação no tabuleiro

        // Verifica se o estado da célula mudou para ser um SNOWMAN
        assertEquals(PositionContent.SNOWMAN, board.getPositionContent(3, 3),
                 "O estado do tabuleiro deve ser SNOWMAN após a combinação correta.");
}
}