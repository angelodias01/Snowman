package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final List<List<PositionContent>> boardState;
    private final Monster monsterState;
    private final List<Snowball> snowballsState;

    public GameState(BoardModel board) {
        // Criar cópia profunda do estado atual do tabuleiro
        this.boardState = new ArrayList<>();
        for (List<PositionContent> row : board.getBoard()) {
            this.boardState.add(new ArrayList<>(row));
        }
        
        // Criar cópia do estado do monstro
        this.monsterState = new Monster(board.getMonster().getRow(), board.getMonster().getCol());
        
        // Criar cópia das bolas de neve
        this.snowballsState = new ArrayList<>();
        for (Snowball snowball : board.getSnowballs()) {
            this.snowballsState.add(new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType()));
        }
    }

    public List<List<PositionContent>> getBoardState() {
        return boardState;
    }

    public Monster getMonsterState() {
        return monsterState;
    }

    public List<Snowball> getSnowballsState() {
        return snowballsState;
    }
}