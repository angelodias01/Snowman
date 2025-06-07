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

    private void initializeLevels() {
        // Nível 1
        List<List<PositionContent>> board1 = createLevel1Board();
        List<Snowball> snowballs1 = Arrays.asList(
            new Snowball(2, 3, SnowballType.SMALL),
            new Snowball(1, 2, SnowballType.MID),
            new Snowball(3, 1, SnowballType.BIG)
        );
        levels.add(new Level(board1, 0, 0, snowballs1, 1));

        // Nível 2
        List<List<PositionContent>> board2 = createLevel2Board();
        List<Snowball> snowballs2 = Arrays.asList(
            new Snowball(3, 4, SnowballType.SMALL),
            new Snowball(2, 2, SnowballType.MID),
            new Snowball(4, 1, SnowballType.BIG)
        );
        levels.add(new Level(board2, 0, 0, snowballs2, 2));
    }

    private List<List<PositionContent>> createLevel1Board() {
        // Implementar layout do nível 1
        List<List<PositionContent>> board = new ArrayList<>();
        // ... configurar tabuleiro do nível 1
        return board;
    }

    private List<List<PositionContent>> createLevel2Board() {
        // Implementar layout do nível 2
        List<List<PositionContent>> board = new ArrayList<>();
        // ... configurar tabuleiro do nível 2
        return board;
    }

    public BoardModel getCurrentLevel() {
        return levels.get(currentLevelIndex).createBoardModel();
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