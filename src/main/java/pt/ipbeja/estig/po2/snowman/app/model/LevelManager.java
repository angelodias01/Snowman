package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelManager {
    private final List<Level> levels;
    private int currentLevelIndex;

    public LevelManager() {
        this.levels = new ArrayList<>();
        this.currentLevelIndex = 0;
        initializeLevels();
    }

    private List<List<PositionContent>> createLevel1Board() {
        List<List<PositionContent>> board = new ArrayList<>();
        int rows = 5;
        int cols = 5;
        
        // Criar um tabuleiro vazio com neve
        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.SNOW);
            }
            board.add(row);
        }
        
        return board;
    }

    private List<List<PositionContent>> createLevel2Board() {
        List<List<PositionContent>> board = new ArrayList<>();
        int rows = 6;
        int cols = 6;

        // Criar um tabuleiro maior vazio com neve
        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.SNOW);
            }
            board.add(row);
        }

        // Adicionar blocos em forma de cruz no centro
        board.get(2).set(2, PositionContent.BLOCK);
        board.get(2).set(3, PositionContent.BLOCK);
        board.get(3).set(2, PositionContent.BLOCK);
        board.get(3).set(3, PositionContent.BLOCK);

        return board;
    }


    private void initializeLevels() {
        // Nível 1 (mantemos o mesmo)
        List<List<PositionContent>> board1 = createLevel1Board();
        List<Snowball> snowballs1 = Arrays.asList(
                new Snowball(2, 3, SnowballType.SMALL),
                new Snowball(1, 2, SnowballType.MID),
                new Snowball(3, 1, SnowballType.BIG)
        );
        levels.add(new Level(board1, 0, 0, snowballs1, 1));

        // Nível 2 (novo layout)
        List<List<PositionContent>> board2 = createLevel2Board();
        List<Snowball> snowballs2 = Arrays.asList(
                new Snowball(1, 1, SnowballType.SMALL),  // Canto superior esquerdo
                new Snowball(1, 4, SnowballType.MID),    // Canto superior direito
                new Snowball(4, 1, SnowballType.BIG)     // Canto inferior esquerdo
        );
        levels.add(new Level(board2, 0, 0, snowballs2, 2));
    }




    public BoardModel getCurrentLevel() {
        return levels.get(currentLevelIndex).createBoardModel();
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }


    public boolean hasNextLevel() {
        return currentLevelIndex < levels.size() - 1;
    }

    public BoardModel loadNextLevel() {
        if (hasNextLevel()) {
            currentLevelIndex++;
            return getCurrentLevel();
        }
        return null;
    }
}