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
        monster = new Monster(2,0);

        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                if(i == 0) {
                    row.add(PositionContent.SNOW);
                }
                else{
                    row.add(PositionContent.NO_SNOW);
                }
            }
            content.add(row);
        }

        Snowball snowball = new Snowball(1,0, SnowballType.SMALL);
        snowballs.add(snowball);
        board = new BoardModel(content, monster, snowballs);

    }

    @Test
    @DisplayName("Move the monster to the left")
    void testMonsterToTheLeft()
    {
        board.moveMonster(Direction.LEFT);

        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());
    }

    @Test
    @DisplayName("Move the monster up")
    void testMonsterToUp(){
        monster = new Monster(2,0); // Mantido posição inicial
        board = new BoardModel(content, monster, snowballs);

        board.moveMonster(Direction.UP);

        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());
    }

    @Test
    @DisplayName("Test invalid monster move")
    void testMonsterInvalidMove(){
        monster = new Monster(2,0);
        board = new BoardModel(content, monster, snowballs);
        
        board.moveMonster(Direction.UP); // Primeiro movimento válido
        
        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());
        
        boolean validMove = board.moveMonster(Direction.UP); // Segundo movimento deve ser inválido
        
        assertFalse(validMove); // Deve retornar falso pois não pode mover
        assertEquals(1, monster.getRow()); // Posição deve permanecer a mesma
        assertEquals(0, monster.getCol());
    }

/*    @Test
    void testPositionContent(){
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                PositionContent element = board.getPositionContent(i,j);
                System.out.print(" | " + element);
            }
            System.out.println(" |");
        }
    }*/

@Test
@DisplayName("Move snowball to the left")
void testMoveSnowballToTheLeft(){
    monster = new Monster(1,1);
    Snowball snowball = new Snowball(1,1, SnowballType.SMALL);
    snowballs.clear();
    snowballs.add(snowball);
    board = new BoardModel(content, monster, snowballs);
    
    board.moveMonster(Direction.LEFT);
    
    assertEquals(1, snowball.getRow());
    assertEquals(0, snowball.getCol());
    assertEquals(1, monster.getRow());
    assertEquals(0, monster.getCol());
}

@Test
@DisplayName("Move the snowball up")
void testMoveSnowballToUp(){
    monster = new Monster(2,0);
    Snowball snowball = new Snowball(1,0, SnowballType.SMALL);
    snowballs.clear();
    snowballs.add(snowball);
    board = new BoardModel(content, monster, snowballs);
    
    board.moveMonster(Direction.UP);
    
    assertEquals(0, snowball.getRow());
    assertEquals(0, snowball.getCol());
    assertEquals(1, monster.getRow());
    assertEquals(0, monster.getCol());
}

    @Test
    @DisplayName("Create average snowball")
    void testCreateAverageSnowball(){
        Snowball snowball = board.snowballInPosition(1, 0);
        assertEquals(1, snowball.getRow());
        assertEquals(0, snowball.getCol());
        assertEquals(SnowballType.SMALL, snowball.getType());

        board.moveMonster(Direction.UP);

        assertEquals(1, monster.getRow());
        assertEquals(0, monster.getCol());

        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());
        assertEquals(SnowballType.MID, snowball.getType());
    }

    @Test
    @DisplayName("Test invalid snowball move")
    void testSnowballInvalidMove(){
        Snowball snowball = board.snowballInPosition(1, 0);
        assertEquals(1, snowball.getRow());
        assertEquals(0, snowball.getCol());

        boolean move = board.moveMonster(Direction.UP);

        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());
        assertTrue(move);

        move = board.moveMonster(Direction.UP);

        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());
        assertFalse(move);


    }
    @Test
    @DisplayName("Test create big snowball")
    void testCreateBigSnowball() {
        Snowball snowball = board.snowballInPosition(1, 0);
        snowball.setType(SnowballType.MID);
        assertEquals(SnowballType.MID, snowball.getType());

        snowball.move(Direction.UP, board);

        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());
        assertEquals(SnowballType.BIG, snowball.getType());
    }

    @Test
    @DisplayName("Test maintain big snowball size")
    void testMaintainBigSnowBall() {
        Snowball snowball = board.snowballInPosition(1, 0);
        snowball.setType(SnowballType.BIG);
        assertEquals(SnowballType.BIG, snowball.getType());

        snowball.move(Direction.UP, board);

        assertEquals(0, snowball.getRow());
        assertEquals(0, snowball.getCol());
        assertEquals(SnowballType.BIG, snowball.getType());
    }

    @Test
    @DisplayName("Test create incomplete snowman as final state")
    void testAverageBigSnowman() {
        Snowball midSnowball = new Snowball(1, 0, SnowballType.MID);
        Snowball bigSnowball = new Snowball(0, 0, SnowballType.BIG);
        board.getSnowballs().clear();
        board.getSnowballs().add(midSnowball);
        board.getSnowballs().add(bigSnowball);

        midSnowball.move(Direction.UP, board);

        assertEquals(PositionContent.SNOWMAN, board.getPositionContent(0, 0)); // Direto ao estado 'SNOWMAN'
    }

    @Test
    @DisplayName("Test complete snowman creation")
    void testCompleteSnowman() {
        Snowball smallSnowball = new Snowball(2, 0, SnowballType.SMALL);
        Snowball midSnowball = new Snowball(1, 0, SnowballType.MID);
        Snowball bigSnowball = new Snowball(0, 0, SnowballType.BIG);
        board.getSnowballs().clear();
        board.getSnowballs().add(smallSnowball);
        board.getSnowballs().add(midSnowball);
        board.getSnowballs().add(bigSnowball);

        smallSnowball.move(Direction.UP, board);

        assertEquals(PositionContent.SNOWMAN, board.getPositionContent(0, 0));
    }
}