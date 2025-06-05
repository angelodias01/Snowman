package pt.ipbeja.estig.po2.snowman.app.model;

/**
 * Monster class representing the movable element controlled by the player.
 */
public class Monster extends MobileElement {

    /**
     * Construtor do Monster
     * @param row linha inicial
     * @param col coluna inicial
     */
    public Monster(int row, int col) {
        super(row, col);
    }

    /**
     * Move o monstro na direção especificada
     * @param direction A direção do movimento
     * @param board Referência ao modelo do tabuleiro
     * @return true se o movimento foi bem sucedido, false caso contrário
     */
    @Override
    public boolean move(Direction direction, BoardModel board) {
        // Calcula a nova posição baseada na direção
        int newRow = this.row;
        int newCol = this.col;

        switch (direction) {
            case UP -> newRow--;
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }

        // Verifica se a nova posição é válida no tabuleiro
        if (!board.validPosition(newRow, newCol)) {
            return false;
        }

        // Verifica se existe uma bola de neve na posição de destino
        Snowball snowball = board.snowballInPosition(newRow, newCol);
        if (snowball != null) {
            // Tenta mover a bola de neve primeiro
            if (!snowball.move(direction, board)) {
                return false; // Se não puder mover a bola de neve, o monstro também não pode mover
            }
        }

        // Se chegou aqui, podemos mover o monstro
        // Remove a neve da posição atual
        board.setPositionContent(this.row, this.col, PositionContent.NO_SNOW);
        
        this.row = newRow;
        this.col = newCol;
        
        // Remove a neve da nova posição
        board.setPositionContent(this.row, this.col, PositionContent.NO_SNOW);

        return true;
    }

    /**
     * Verifica se o monstro pode mover para uma determinada posição
     * @param newRow nova linha
     * @param newCol nova coluna
     * @param board referência ao tabuleiro
     * @return true se pode mover, false caso contrário
     */
    private boolean canMoveTo(int newRow, int newCol, BoardModel board) {
        // Verifica se a posição está dentro dos limites do tabuleiro
        if (!board.validPosition(newRow, newCol)) {
            return false;
        }

        // Verifica se há uma bola de neve na posição
        Snowball snowball = board.snowballInPosition(newRow, newCol);
        if (snowball != null) {
            // Se há uma bola de neve, verifica se ela pode ser movida
            int snowballNewRow = snowball.getRow() + (newRow - this.row);
            int snowballNewCol = snowball.getCol() + (newCol - this.col);
            return board.validPosition(snowballNewRow, snowballNewCol) &&
                    board.snowballInPosition(snowballNewRow, snowballNewCol) == null;
        }

        // Se não há bola de neve, a posição é válida
        return true;
    }

    /**
     * Obtém a linha atual do monstro
     * @return linha atual
     */
    @Override
    public int getRow() {
        return super.getRow();
    }

    /**
     * Obtém a coluna atual do monstro
     * @return coluna atual
     */
    @Override
    public int getCol() {
        return super.getCol();
    }
}