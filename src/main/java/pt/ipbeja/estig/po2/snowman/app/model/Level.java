package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final List<List<PositionContent>> initialBoard;
    private final int monsterStartRow;
    private final int monsterStartCol;
    private final List<Snowball> initialSnowballs;
    private final int levelNumber;

    public Level(List<List<PositionContent>> board, int monsterRow, int monsterCol, 
                List<Snowball> snowballs, int levelNumber) {
        this.initialBoard = new ArrayList<>();
        for (List<PositionContent> row : board) {
            this.initialBoard.add(new ArrayList<>(row));
        }
        this.monsterStartRow = monsterRow;
        this.monsterStartCol = monsterCol;
        this.initialSnowballs = new ArrayList<>(snowballs);
        this.levelNumber = levelNumber;
    }

    public BoardModel createBoardModel() {
        List<List<PositionContent>> boardCopy = new ArrayList<>();
        for (List<PositionContent> row : initialBoard) {
            boardCopy.add(new ArrayList<>(row));
        }
        
        Monster monster = new Monster(monsterStartRow, monsterStartCol);
        List<Snowball> snowballsCopy = new ArrayList<>();
        for (Snowball snowball : initialSnowballs) {
            snowballsCopy.add(new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType()));
        }
        
        return new BoardModel(boardCopy, monster, snowballsCopy);
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}