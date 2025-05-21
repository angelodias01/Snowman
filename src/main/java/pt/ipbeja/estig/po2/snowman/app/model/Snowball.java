package pt.ipbeja.estig.po2.snowman.app.model;

public class Snowball extends MobileElement {
    private SnowballType type;

    /**
     * Construtor
     * @param row - Linha inicial
     * @param col - Coluna inicial
     * @param type - Tipo/tamanho inicial da bola de neve
     */
    public Snowball(int row, int col, SnowballType type) {
        super(row, col);
        this.type = type;
    }

    public SnowballType getType() {
        return type;
    }

    public void setType(SnowballType type) {
        this.type = type;
    }

    /**
     * Aumenta o tamanho da bola de neve quando ela coleta neve do chão
     */
    public void increaseSnowballType() {
        switch (type) {
            case SMALL -> setType(SnowballType.MID);
            case MID -> setType(SnowballType.BIG);
        }
    }

    /**
     * Implementação do movimento da bola de neve
     * @param direction - Direção do movimento
     * @param board - Referência ao tabuleiro
     * @return true se o movimento foi bem sucedido
     */
    @Override
    public boolean move(Direction direction, BoardModel board) {
        int newRow = this.getRow() + direction.getDeltaRow();
        int newCol = this.getCol() + direction.getDeltaCol();

        if (!board.validPosition(newRow, newCol)) {
            return false;
        }

        // Verifica se há outra bola de neve na posição de destino
        Snowball otherSnowball = board.snowballInPosition(newRow, newCol);
        if (otherSnowball != null) {
            return tryToCombineSnowballs(otherSnowball, board);
        }

        // Verifica se há neve no chão
        PositionContent destination = board.getPositionContent(newRow, newCol);
        if (destination == PositionContent.SNOW) {
            increaseSnowballType();
            board.setPositionContent(newRow, newCol, PositionContent.NO_SNOW);
        }

        // Move a bola de neve para a nova posição
        this.setRow(newRow);
        this.setCol(newCol);
        return true;
    }

    /**
     * Tenta combinar duas bolas de neve
     * @param other - A outra bola de neve
     * @param board - Referência ao tabuleiro
     * @return true se a combinação foi bem sucedida
     */
    private boolean tryToCombineSnowballs(Snowball other, BoardModel board) {
        SnowballType newType = calculateCombinedType(this.type, other.type);
        if (newType != null) {
            // Remove as bolas originais
            board.getSnowballs().remove(other);
            board.getSnowballs().remove(this);

            if (newType == SnowballType.COMPLETE) {
                // Cria o boneco de neve completo
                board.setPositionContent(other.getRow(), other.getCol(), PositionContent.SNOWMAN);
            } else {
                // Cria uma nova bola combinada
                Snowball combinedSnowball = new Snowball(other.getRow(), other.getCol(), newType);
                board.getSnowballs().add(combinedSnowball);
            }
            return true;
        }
        return false;
    }

    /**
     * Calcula o tipo resultante da combinação de duas bolas de neve
     * @param type1 - Tipo da primeira bola
     * @param type2 - Tipo da segunda bola
     * @return O novo tipo ou null se não puderem ser combinadas
     */
    private SnowballType calculateCombinedType(SnowballType type1, SnowballType type2) {
        if ((type1 == SnowballType.SMALL && type2 == SnowballType.MID) ||
            (type2 == SnowballType.SMALL && type1 == SnowballType.MID)) {
            return SnowballType.MID_SMALL;
        }
        if ((type1 == SnowballType.SMALL && type2 == SnowballType.BIG) ||
            (type2 == SnowballType.SMALL && type1 == SnowballType.BIG)) {
            return SnowballType.BIG_SMALL;
        }
        if ((type1 == SnowballType.MID && type2 == SnowballType.BIG) ||
            (type2 == SnowballType.MID && type1 == SnowballType.BIG)) {
            return SnowballType.BIG_MID;
        }
        
        // Verifica combinações para formar o boneco completo
        if (hasAllThreeParts(type1, type2)) {
            return SnowballType.COMPLETE;
        }
        
        return null;
    }

    /**
     * Verifica se as duas bolas de neve juntas formam um boneco completo
     */
    private boolean hasAllThreeParts(SnowballType type1, SnowballType type2) {
        return (type1 == SnowballType.BIG_MID && type2 == SnowballType.SMALL) ||
               (type2 == SnowballType.BIG_MID && type1 == SnowballType.SMALL) ||
               (type1 == SnowballType.BIG_SMALL && type2 == SnowballType.MID) ||
               (type2 == SnowballType.BIG_SMALL && type1 == SnowballType.MID) ||
               (type1 == SnowballType.MID_SMALL && type2 == SnowballType.BIG) ||
               (type2 == SnowballType.MID_SMALL && type1 == SnowballType.BIG);
    }
}