package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BoardModel {

    private final List<List<PositionContent>> board;
    private final Monster monster;
    private final List<Snowball> snowballs;
    private final Stack<GameState> history;
    private final Stack<GameState> redoHistory;

    /**
     * Retorna a lista de bolas de neve no tabuleiro
     * @return Lista de Snowball
     */
    public List<Snowball> getSnowballs() {
        return this.snowballs;
    }
    /**
     * Retorna o monstro do jogo
     * @return o objeto Monster que representa o monstro no jogo
     */
    public Monster getMonster() {
        return this.monster;
    }


    /**
     * Gets the number of rows in the board.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return board.size();
    }

    /**
     * Gets the number of columns in the board.
     *
     * @return The number of columns.
     */
    public int getCols() {
        return board.isEmpty() ? 0 : board.get(0).size();
    }


    // Existing methods...

    /**
     * Sets the content of a specific position on the board.
     *
     * @param row    The row of the position.
     * @param col    The column of the position.
     * @param content The new content for the position.
     */
    public void setPositionContent(int row, int col, PositionContent content) {
        this.board.get(row).set(col, content);
    }

    /**
     * Retrieves the content of a specific position on the board.
     *
     * @param row The row index of the position.
     * @param col The column index of the position.
     * @return The PositionContent at the specified row and column.
     * @throws IndexOutOfBoundsException if the row or col is out of bounds.
     */
    public PositionContent getPositionContent(int row, int col) {
        if (row < 0 || row >= board.size() || col < 0 || col >= board.get(row).size()) {
            throw new IndexOutOfBoundsException("Row or column is out of bounds");
        }
        return board.get(row).get(col);
    }


    /**
     * Guarda o estado atual do jogo antes de fazer uma jogada
     */
    private void saveState() {
        history.push(new GameState(this));
        redoHistory.clear(); // Clear redo history when a new move is made
    }

    /**
     * Desfaz a última jogada, restaurando o estado anterior
     * @return true se foi possível desfazer, false se não houver jogadas para desfazer
     */
    public boolean undo() {
        if (history.isEmpty()) {
            return false;
        }

        GameState currentState = new GameState(this); // Save current state for redo
        GameState previousState = history.pop();

        // Add the current state to redo history
        redoHistory.push(currentState);

        // Restaurar o estado do tabuleiro
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                board.get(i).set(j, previousState.getBoardState().get(i).get(j));
            }
        }

        // Restaurar posição do monstro
        monster.setRow(previousState.getMonsterState().getRow());
        monster.setCol(previousState.getMonsterState().getCol());

        // Restaurar estado das bolas de neve
        snowballs.clear();
        snowballs.addAll(previousState.getSnowballsState());

        return true;
    }


    /**
     * Refaz a última jogada desfeita
     * @return true se foi possível refazer, false se não houver jogadas para refazer
     */
    public boolean redo() {
        if (redoHistory.isEmpty()) {
            return false;
        }

        // Save current state to undo stack
        GameState currentState = new GameState(this);
        GameState redoState = redoHistory.pop();
        history.push(currentState);

        // Restaurar o estado do tabuleiro
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                board.get(i).set(j, redoState.getBoardState().get(i).get(j));
            }
        }

        // Restaurar posição do monstro
        monster.setRow(redoState.getMonsterState().getRow());
        monster.setCol(redoState.getMonsterState().getCol());

        // Restaurar estado das bolas de neve
        snowballs.clear();
        snowballs.addAll(redoState.getSnowballsState());

        return true;
    }


    /**
     * Modificar o método moveMonster para salvar o estado antes do movimento
     */
    public boolean moveMonster(Direction direction) {
        saveState(); // Salvar estado antes do movimento
        
        boolean moved = monster.move(direction, this);
        if (!moved) {
            history.pop(); // Se o movimento falhou, remover o estado salvo
        }
        return moved;
    }

    /**
     * Checks if a given position is valid on the board.
     * A valid position must be within bounds and cannot contain a block.
     *
     * @param row The row index to check.
     * @param col The column index to check.
     * @return true if the position is valid; false otherwise.
     */
    public boolean validPosition(int row, int col) {
        // Check if row and column are within bounds
        if (row < 0 || row >= board.size() || col < 0 || col >= board.get(0).size()) {
            return false;
        }

        // Check if position is not a BLOCK
        return board.get(row).get(col) != PositionContent.BLOCK;
    }

    /**
     * Returns the snowball found in the specified position, or null if no snowball is present.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The snowball object at the position, or null if no snowball is found.
     */
    public Snowball snowballInPosition(int row, int col) {
        for (Snowball snowball : snowballs) {
            if (snowball.getRow() == row && snowball.getCol() == col) {
                return snowball;
            }
        }
        return null; // No snowball found at the position
    }

    /**
     * Attempts to move a snowball in the specified direction on the board.
     *
     * @param direction The direction to move the snowball.
     * @param snowball The snowball to be moved.
     * @return true if the snowball was successfully moved; false otherwise.
     */
    public boolean moveSnowball(Direction direction, Snowball snowball) {
        return snowball.move(direction, this);
    }

/**
 * Retorna o tabuleiro atual do jogo
 * @return Lista bidimensional representando o tabuleiro
 */
public List<List<PositionContent>> getBoard() {
    return this.board;
}
    private final List<List<PositionContent>> initialBoard; // Nova variável para guardar estado inicial
    private final int initialMonsterRow; // Posição inicial do monstro
    private final int initialMonsterCol;
    private final List<Snowball> initialSnowballs; // Estado inicial das bolas de neve

    public BoardModel(List<List<PositionContent>> board, Monster monster, List<Snowball> snowballs) {
        this.board = board;
        this.monster = monster;
        this.snowballs = snowballs;
        this.history = new Stack<>();
        this.redoHistory = new Stack<>();


        // Guardar estado inicial
        this.initialBoard = new ArrayList<>();
        for (List<PositionContent> row : board) {
            this.initialBoard.add(new ArrayList<>(row));
        }
        this.initialMonsterRow = monster.getRow();
        this.initialMonsterCol = monster.getCol();
        
        // Copiar bolas de neve iniciais
        this.initialSnowballs = new ArrayList<>();
        for (Snowball snowball : snowballs) {
            this.initialSnowballs.add(new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType()));
        }
    }

    /**
     * Reinicia o nível para o estado inicial
     */
    public void resetLevel() {
        // Limpar histórico
        this.history.clear();
        this.redoHistory.clear();


        // Restaurar tabuleiro
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                board.get(i).set(j, initialBoard.get(i).get(j));
            }
        }
        
        // Restaurar monstro
        monster.setRow(initialMonsterRow);
        monster.setCol(initialMonsterCol);
        
        // Restaurar bolas de neve
        snowballs.clear();
        for (Snowball snowball : initialSnowballs) {
            snowballs.add(new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType()));
        }
    }

    /**
     * Verifica se existe um boneco de neve completo no tabuleiro
     * @return true se houver um boneco de neve completo, false caso contrário
     */
    public boolean isLevelComplete() {
    // Verificar se existe um boneco de neve completo no tabuleiro
    for (int row = 0; row < getRows(); row++) {
        for (int col = 0; col < getCols(); col++) {
            if (getPositionContent(row, col) == PositionContent.SNOWMAN) {
                return true;
            }
        }
    }
    return false;
}
}