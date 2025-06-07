package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.List;
import java.util.Stack;

public class BoardModel {

    private final List<List<PositionContent>> board;
    private final Monster monster;
    private final List<Snowball> snowballs;
    // ... outros atributos existentes ...
    private final Stack<GameState> history;
    
    public BoardModel(List<List<PositionContent>> board, Monster monster, List<Snowball> snowballs) {
        this.board = board;
        this.monster = monster;
        this.snowballs = snowballs;
        // ... construtor existente ...
        this.history = new Stack<>();
    }
    
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
    }
    
    /**
     * Desfaz a última jogada, restaurando o estado anterior
     * @return true se foi possível desfazer, false se não houver jogadas para desfazer
     */
    public boolean undo() {
        if (history.isEmpty()) {
            return false;
        }
        
        GameState previousState = history.pop();
        
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
}