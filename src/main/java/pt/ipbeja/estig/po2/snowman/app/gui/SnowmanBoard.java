package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pt.ipbeja.estig.po2.snowman.app.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;
import java.util.List;

/**
 * The SnowmanBoard class is the main GUI component responsible for displaying the game board,
 * handling user interactions (keyboard and button controls), and managing game state updates.
 * <p>
 * This class uses JavaFX controls and layout panes to visually represent the game board,
 * snowballs, monster, and other game elements. It also handles user input for controlling
 * the monster, undoing/redoing moves, restarting the level, saving game data, and displaying
 * the leaderboard.
 * <p>
 * Key Features:
 * - Visual rendering of the board with images for snowballs, monster, blocks, snow, and snowmen.
 * - Keyboard controls: arrow keys to move, CTRL+Z for undo, CTRL+X for redo, R to restart.
 * - Movement log displayed in a non-editable TextArea.
 * - Save game state to a timestamped file in user's Documents/Snowman folder.
 * - Save and display leaderboard data.
 *
 * @author Ângelo Dias
 * @author Edgar Brito
 */

public class SnowmanBoard extends VBox implements View {
    private final Consumer<Void> onLevelComplete;
    private final Button resetButton;
    private Button undoButton;
    private Button redoButton;
    private BoardModel boardModel;
    private final GridPane board;
    private final TextArea movementsLog;
    private String playerName;
    private int score;
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private int totalGameScore = 0;

    // Images for game elements
    private final Image snowImage = new Image(getClass().getResourceAsStream("/images/snow.png"));
    private final Image blockImage = new Image(getClass().getResourceAsStream("/images/block.png"));
    private final Image snowmanImage = new Image(getClass().getResourceAsStream("/images/snowman.png"));
    private final Image monsterImage = new Image(getClass().getResourceAsStream("/images/monster.png"));

    /**
     * Constructs a new SnowmanBoard GUI component.
     *
     * @param boardModel      The BoardModel representing the game state and logic.
     * @param onLevelComplete Callback executed when the level is completed.
     * @param playerName      The name of the player (for leaderboard and score tracking).
     */
    public SnowmanBoard(BoardModel boardModel, Consumer<Void> onLevelComplete, String playerName) {
        this.boardModel = boardModel;
        this.onLevelComplete = onLevelComplete;
        this.board = new GridPane();
        this.movementsLog = new TextArea();
        this.movementsLog.setEditable(false);
        this.movementsLog.setPrefRowCount(3);
        this.playerName = playerName;
        this.score = 0;

        this.resetButton = new Button("Restart Level");
        this.resetButton.setOnAction(e -> resetLevel());

        configureUndoButton();
        configureRedoButton();

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.TOP_LEFT);
        controls.getChildren().add(resetButton);
        controls.getChildren().add(undoButton);
        controls.getChildren().add(redoButton);

        setupBoard();
        this.getChildren().addAll(board, controls, movementsLog);

        this.setOnKeyPressed(this::handleKeyPress);
        this.setFocusTraversable(true);
    }

    /**
     * Configures the Undo button and its action handler.
     */
    private void configureUndoButton() {
        this.undoButton = new Button("Undo Move(CTRL+Z)");
        this.undoButton.setOnAction(e -> {
            if (boardModel.undo()) {
                movementsLog.appendText("Movement undone\n");
                updateBoard();
            }
        });
    }

    /**
     * Configures the Redo button and its action handler.
     */
    private void configureRedoButton() {
        this.redoButton = new Button("Redo Move(CTRL+X)");
        this.redoButton.setOnAction(e -> {
            if (boardModel.redo()) {
                movementsLog.appendText("Movimento refeito\n");
                updateBoard();
            }
        });
    }

    /**
     * Loads a new game level by resetting the board model and UI components,
     * and preserving total game score across levels.
     *
     * @param newBoard The new BoardModel instance for the new level.
     */
    public void loadNewLevel(BoardModel newBoard) {
        this.totalGameScore += this.score; // Acumular pontuação do nível anterior
        this.score = 0;
        this.boardModel = newBoard;
        this.movementsLog.clear();
        updateBoard();
        this.requestFocus();
    }

    /**
     * Resets the current level after user confirmation.
     * Clears the movement log and updates the board display.
     */
    private void resetLevel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Restart Level");
        alert.setHeaderText("Are you sure you want to restart the level?");
        alert.setContentText("All progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boardModel.resetLevel();

            movementsLog.clear();
            movementsLog.appendText("Level restarted\n");

            updateBoard();

            this.requestFocus();
        }
    }

    /**
     * Handles key press events for game control:
     * - Arrow keys for monster movement
     * - CTRL+Z for undo
     * - CTRL+X for redo
     * - R key to restart the level
     *
     * @param event The KeyEvent captured.
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.R) {
            resetLevel();
            event.consume();
            return;
        }

        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            if (boardModel.undo()) {
                movementsLog.appendText("Movement undone\n");
                updateBoard();
            }
            event.consume();
            return;
        }

        if (event.isControlDown() && event.getCode() == KeyCode.X) {
            if (boardModel.redo()) {
                movementsLog.appendText("Movement redone\n");
                updateBoard();
            }
            event.consume();
            return;
        }

        Direction direction = null;
        switch (event.getCode()) {
            case UP -> direction = Direction.UP;
            case DOWN -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.LEFT;
            case RIGHT -> direction = Direction.RIGHT;
        }

        if (direction != null) {
            boolean moved = boardModel.moveMonster(direction);
            if (moved) {
                this.score += 1;
                movementsLog.appendText("Monster moved to " + direction + " (Score: " + score + ")\n");
                updateBoard();
            }
        }

        event.consume();
    }

    /**
     * Updates the board UI to reflect the current game state.
     * Also triggers the level complete callback if the level is finished.
     */
    @Override
    public void updateBoard() {
        setupBoard();
        if (boardModel.isLevelComplete()) {
            onLevelComplete.accept(null);
        }
    }

    /**
     * Sets up the visual representation of the game board grid,
     * including column and row labels and cell contents.
     */
    private void setupBoard() {
        board.getChildren().clear();

        for (int col = 0; col <= boardModel.getCols(); col++) {
            if (col > 0) {
                Label colLabel = new Label(String.valueOf((char) ('A' + col - 1)));
                board.add(colLabel, col, 0);
            }
        }

        for (int row = 0; row <= boardModel.getRows(); row++) {
            if (row > 0) {
                Label rowLabel = new Label(String.valueOf(row));
                board.add(rowLabel, 0, row);
            }
        }

        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                Label cell = createCell(row, col);
                board.add(cell, col + 1, row + 1);
            }
        }
    }

    /**
     * Creates a Label representing a single cell on the board,
     * including the appropriate image based on the current game state.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return A Label node configured with the correct image and style.
     */
    private Label createCell(int row, int col) {
        Label cell = new Label();
        cell.setMinSize(50, 50);
        cell.setStyle("-fx-border-color: black; -fx-alignment: center;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);

        if (boardModel.getMonster().getRow() == row &&
                boardModel.getMonster().getCol() == col) {
            imageView.setImage(monsterImage);
        } else {

            Snowball snowball = boardModel.snowballInPosition(row, col);
            if (snowball != null) {
                switch (snowball.getType()) {
                    case SMALL ->
                            imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowball_small.png")));
                    case MID ->
                            imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowball_mid.png")));
                    case BIG ->
                            imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowball_big.png")));
                    case MID_SMALL ->
                            imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowman_partial1.png")));
                    case BIG_SMALL ->
                            imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowman_partial2.png")));
                    case BIG_MID ->
                            imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowman_partial3.png")));
                    case COMPLETE -> imageView.setImage(snowmanImage);
                }
            } else {
                PositionContent content = boardModel.getPositionContent(row, col);
                switch (content) {
                    case NO_SNOW -> imageView.setImage(new Image(getClass().getResourceAsStream("/images/grass.png")));
                    case SNOW -> imageView.setImage(snowImage);
                    case BLOCK -> imageView.setImage(blockImage);
                    case SNOWMAN -> imageView.setImage(snowmanImage);
                }
            }
        }

        cell.setGraphic(imageView);
        return cell;
    }

    /**
     * Saves the current game state to a uniquely named file.
     * <p>
     * The filename is generated using a timestamp in the format "snowmanyyyyMMddHHmmss.txt"
     * to ensure uniqueness and prevent overwriting previous saves.
     * <p>
     * The method attempts to create the necessary directories and write the game data to the file.
     * If successful, a confirmation alert is shown to the user. If an error occurs during saving,
     * an error alert is displayed instead.
     */
    public void saveGameToFile() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = "snowman" + timestamp + ".txt";

        try {
            Path filePath = createFilePath(filename);
            saveGameData(filePath);
            showSuccessAlert(filePath);
        } catch (IOException e) {
            showErrorAlert(e);
        }
    }

    /**
     * Creates the full file path for the given filename inside the Snowman directory.
     * If the Snowman directory does not exist, it is created.
     *
     * @param filename the name of the file to create the path for
     * @return the full Path to the file within the Snowman directory
     * @throws IOException if an I/O error occurs while creating directories
     */
    private Path createFilePath(String filename) throws IOException {
        Path snowmanPath = getSnowmanDirectory();
        if (!Files.exists(snowmanPath)) {
            Files.createDirectories(snowmanPath);
        }
        return snowmanPath.resolve(filename);
    }

    /**
     * Retrieves the path to the Snowman directory inside the user's Documents folder.
     *
     * @return the Path representing the Snowman directory
     */
    private Path getSnowmanDirectory() {
        String documentsPath = System.getProperty("user.home") + "/Documents";
        return Paths.get(documentsPath, "Snowman");
    }

    /**
     * Saves the current game data to the specified file path.
     * Ensures that all necessary parent directories exist before writing.
     *
     * @param filePath the Path of the file where the game data will be saved
     * @throws IOException if an I/O error occurs during saving
     */
    private void saveGameData(Path filePath) throws IOException {
        checkAndCreateParentDirectories(filePath);
        writeGameData(filePath);
    }

    /**
     * Checks if the parent directories of the given file path exist,
     * and creates them if they do not.
     *
     * @param filePath the Path whose parent directories need to be checked/created
     * @throws IOException if an I/O error occurs while creating directories
     */
    private void checkAndCreateParentDirectories(Path filePath) throws IOException {
        Path parent = filePath.getParent();
        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    /**
     * Writes the game data to the specified file path using a PrintWriter.
     * The actual content is written by the writeGameContent method.
     *
     * @param filePath the Path of the file to write the game data to
     * @throws IOException if an I/O error occurs during writing
     */
    private void writeGameData(Path filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writeGameContent(writer);
        }
    }

    /**
     * Writes all relevant game content to the provided PrintWriter.
     * This includes the map, movements, move count, and snowman position.
     *
     * @param writer the PrintWriter used to write the game data
     */
    private void writeGameContent(PrintWriter writer) {
        saveMap(writer);
        saveMovements(writer);
        saveMoveCount(writer);
        saveSnowmanPosition(writer);
    }

    /**
     * Writes the map section header and map content to the PrintWriter.
     *
     * @param writer the PrintWriter used to write the map data
     */
    private void saveMap(PrintWriter writer) {
        writer.println("=== MAP USED ===");
        writeMapContent(writer);
    }

    /**
     * Writes the entire map content row by row to the given PrintWriter.
     *
     * @param writer the PrintWriter used to write the map content
     */
    private void writeMapContent(PrintWriter writer) {
        for (int i = 0; i < boardModel.getRows(); i++) {
            writeMapRow(writer, i);
            writer.println();
        }
    }

    /**
     * Writes a single row of the map to the PrintWriter.
     *
     * @param writer the PrintWriter used to write the map row
     * @param row the index of the row to write
     */
    private void writeMapRow(PrintWriter writer, int row) {
        for (int col = 0; col < boardModel.getCols(); col++) {
            PositionContent content = boardModel.getPositionContent(row, col);
            writer.print(getContentSymbol(content));
        }
    }

    /**
     * Returns the symbol string representing the given PositionContent.
     *
     * @param content the PositionContent to convert
     * @return the corresponding symbol string for the content
     */
    private String getContentSymbol(PositionContent content) {
        return switch (content) {
            case SNOW -> "S ";
            case NO_SNOW -> "- ";
            case BLOCK -> "B ";
            case SNOWMAN -> "M ";
        };
    }

    /**
     * Writes the movements log header and the logged movements to the PrintWriter.
     *
     * @param writer the PrintWriter used to write the movements data
     */
    private void saveMovements(PrintWriter writer) {
        writer.println("\n===  MOVEMENTS MADE ===");
        writer.println(movementsLog.getText());
    }

    /**
     * Saves the total count of movements made to the given PrintWriter.
     *
     * @param writer the PrintWriter used to write the move count
     */
    private void saveMoveCount(PrintWriter writer) {
        long moveCount = movementsLog.getText().lines().count();
        writer.println("\n=== TOTAL MOVEMENTS ===");
        writer.println(moveCount);
    }

    /**
     * Saves the position of the snowman on the board to the given PrintWriter.
     * Prints the position as (row, column letter).
     *
     * @param writer the PrintWriter used to write the snowman position
     */
    private void saveSnowmanPosition(PrintWriter writer) {
        writer.println("\n=== POSITION OF THE SNOWMAN ===");

        for (int i = 0; i < boardModel.getRows(); i++) {
            for (int j = 0; j < boardModel.getCols(); j++) {
                if (boardModel.getPositionContent(i, j) == PositionContent.SNOWMAN) {
                    writer.printf("(%d, %c)%n", i + 1, (char) ('A' + j));
                    return;
                }
            }
        }
    }

    /**
     * Shows an informational alert dialog indicating the file was saved successfully.
     *
     * @param filePath the path where the file was saved
     */
    private void showSuccessAlert(Path filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saved file");
        alert.setHeaderText("Game saved successfully!");
        alert.setContentText("The file was saved in:\n" + filePath.toString());
        alert.showAndWait();
    }

    /**
     * Shows an error alert dialog with the exception message when saving fails.
     *
     * @param e the IOException that occurred during saving
     */
    private void showErrorAlert(IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error saving the file");
        alert.setContentText("Unable to save the game file: " + e.getMessage());
        alert.showAndWait();
    }

    /**
     * Saves the current player's score to the leaderboard file.
     * Creates the leaderboard file and directory if they don't exist,
     * then appends the formatted score entry.
     */
    public void saveScore() {
        try {
            Path leaderboardPath = createLeaderboardFile();
            appendScoreToLeaderboard(leaderboardPath);
            showSuccessAlert(leaderboardPath);
        } catch (IOException e) {
            showErrorAlert(e);
        }
    }

    /**
     * Creates the leaderboard file inside the "Snowman" directory
     * within the user's Documents folder if it doesn't already exist.
     *
     * @return the Path to the leaderboard file
     * @throws IOException if directory creation or file resolution fails
     */
    private Path createLeaderboardFile() throws IOException {
        String documentsPath = System.getProperty("user.home") + "/Documents";
        Path snowmanPath = Paths.get(documentsPath, "Snowman");

        if (!Files.exists(snowmanPath)) {
            Files.createDirectories(snowmanPath);
        }

        return snowmanPath.resolve(LEADERBOARD_FILE);
    }

    /**
     * Appends the player's score entry to the leaderboard file.
     * Creates the file if it does not exist.
     *
     * The entry format is: playerName | finalScore | timestamp
     *
     * @param filePath the path to the leaderboard file
     * @throws IOException if writing to the file fails
     */
    private void appendScoreToLeaderboard(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        int finalScore = totalGameScore + score;

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String scoreEntry = String.format("%-3s | %4d | %s%n",
                playerName, finalScore, dateTime);

        Files.write(filePath, scoreEntry.getBytes(),
                StandardOpenOption.APPEND);
    }
}