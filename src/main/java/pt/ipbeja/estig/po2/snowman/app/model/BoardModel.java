package pt.ipbeja.estig.po2.snowman.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents the main model for the game board.
 * It manages the game state, including the board layout, monster, snowballs, and undo/redo history.
 */
public class BoardModel {

    private final List<List<PositionContent>> board;
    private final Monster monster;
    private final List<Snowball> snowballs;

    private final Stack<GameState> history;
    private final Stack<GameState> redoHistory;

    private final List<List<PositionContent>> initialBoard; // Initial state of the board
    private final int initialMonsterRow; // Monster's initial row
    private final int initialMonsterCol; // Monster's initial column
    private final List<Snowball> initialSnowballs; // Initial state of snowballs

    /**
     * Constructs a new BoardModel with the provided initial configuration.
     *
     * @param board The initial game board layout.
     * @param monster The monster object in the game.
     * @param snowballs The list of snowballs present on the board.
     */
    public BoardModel(List<List<PositionContent>> board, Monster monster, List<Snowball> snowballs) {
        this.board = board;
        this.monster = monster;
        this.snowballs = snowballs;
        this.history = new Stack<>();
        this.redoHistory = new Stack<>();

        // Store initial board state
        this.initialBoard = new ArrayList<>();
        for (List<PositionContent> row : board) {
            this.initialBoard.add(new ArrayList<>(row));
        }

        this.initialMonsterRow = monster.getRow();
        this.initialMonsterCol = monster.getCol();

        // Deep copy of initial snowballs
        this.initialSnowballs = new ArrayList<>();
        for (Snowball snowball : snowballs) {
            this.initialSnowballs.add(new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType()));
        }
    }

    /**
     * @return Number of rows in the board.
     */
    public int getRows() {
        return board.size();
    }

    /**
     * @return Number of columns in the board.
     */
    public int getCols() {
        return board.isEmpty() ? 0 : board.get(0).size();
    }

    /**
     * @return The current monster in the game.
     */
    public Monster getMonster() {
        return this.monster;
    }

    /**
     * @return The list of snowballs currently on the board.
     */
    public List<Snowball> getSnowballs() {
        return this.snowballs;
    }

    /**
     * @return The current board layout.
     */
    public List<List<PositionContent>> getBoard() {
        return this.board;
    }

    /**
     * Retrieves the content at a given position on the board.
     *
     * @param row Row index.
     * @param col Column index.
     * @return Content of the position.
     * @throws IndexOutOfBoundsException if indices are out of range.
     */
    public PositionContent getPositionContent(int row, int col) {
        if (row < 0 || row >= board.size() || col < 0 || col >= board.get(row).size()) {
            throw new IndexOutOfBoundsException("Row or column is out of bounds");
        }
        return board.get(row).get(col);
    }

    /**
     * Sets a new content value at the specified position.
     *
     * @param row Row index.
     * @param col Column index.
     * @param content New content for the position.
     */
    public void setPositionContent(int row, int col, PositionContent content) {
        this.board.get(row).set(col, content);
    }

    /**
     * Checks if a given position is within the board and not blocked.
     *
     * @param row Row index.
     * @param col Column index.
     * @return true if the position is valid; false otherwise.
     */
    public boolean validPosition(int row, int col) {
        if (row < 0 || row >= board.size() || col < 0 || col >= board.get(0).size()) {
            return false;
        }
        return board.get(row).get(col) != PositionContent.BLOCK;
    }

    /**
     * Searches for a snowball at the specified position.
     *
     * @param row Row index.
     * @param col Column index.
     * @return Snowball at the position or null if none is found.
     */
    public Snowball snowballInPosition(int row, int col) {
        for (Snowball snowball : snowballs) {
            if (snowball.getRow() == row && snowball.getCol() == col) {
                return snowball;
            }
        }
        return null;
    }

    /**
     * Moves the given snowball in a specified direction.
     *
     * @param direction Direction of movement.
     * @param snowball Snowball to move.
     * @return true if the move was successful; false otherwise.
     */
    public boolean moveSnowball(Direction direction, Snowball snowball) {
        return snowball.move(direction, this);
    }

    /**
     * Moves the monster in the given direction and saves state for undo.
     *
     * @param direction Direction to move.
     * @return true if the monster moved; false otherwise.
     */
    public boolean moveMonster(Direction direction) {
        saveState(); // Save current state
        boolean moved = monster.move(direction, this);
        if (!moved) {
            history.pop(); // Discard state if move failed
        }
        return moved;
    }

    /**
     * Saves the current state of the game for undo purposes.
     */
    private void saveState() {
        history.push(new GameState(this));
        redoHistory.clear(); // Clear redo history
    }

    /**
     * Undoes the last move and restores the previous state.
     *
     * @return true if undo was successful; false otherwise.
     */
    public boolean undo() {
        if (history.isEmpty()) return false;

        GameState currentState = new GameState(this);
        GameState previousState = history.pop();
        redoHistory.push(currentState);

        // Restore board state
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                board.get(i).set(j, previousState.getBoardState().get(i).get(j));
            }
        }

        // Restore monster state
        monster.setRow(previousState.getMonsterState().getRow());
        monster.setCol(previousState.getMonsterState().getCol());

        // Restore snowballs
        snowballs.clear();
        snowballs.addAll(previousState.getSnowballsState());

        return true;
    }

    /**
     * Redoes the last undone move and restores the future state.
     *
     * @return true if redo was successful; false otherwise.
     */
    public boolean redo() {
        if (redoHistory.isEmpty()) return false;

        GameState currentState = new GameState(this);
        GameState redoState = redoHistory.pop();
        history.push(currentState);

        // Restore board state
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                board.get(i).set(j, redoState.getBoardState().get(i).get(j));
            }
        }

        // Restore monster state
        monster.setRow(redoState.getMonsterState().getRow());
        monster.setCol(redoState.getMonsterState().getCol());

        // Restore snowballs
        snowballs.clear();
        snowballs.addAll(redoState.getSnowballsState());

        return true;
    }

    /**
     * Resets the level to its initial configuration.
     * Clears the undo/redo history and restores the initial board, monster, and snowballs.
     */
    public void resetLevel() {
        history.clear();
        redoHistory.clear();

        // Restore initial board
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                board.get(i).set(j, initialBoard.get(i).get(j));
            }
        }

        // Restore monster
        monster.setRow(initialMonsterRow);
        monster.setCol(initialMonsterCol);

        // Restore initial snowballs
        snowballs.clear();
        for (Snowball snowball : initialSnowballs) {
            snowballs.add(new Snowball(snowball.getRow(), snowball.getCol(), snowball.getType()));
        }
    }

    /**
     * Checks whether the level is complete.
     * A level is considered complete if at least one snowman exists on the board.
     *
     * @return true if the level is complete; false otherwise.
     */
    public boolean isLevelComplete() {
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