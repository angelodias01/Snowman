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
import java.io.FileWriter;
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
 * The SnowmanBoard class represents the graphical user interface component of the Snowman game.
 * It extends VBox and implements the View interface to provide the visual representation and
 * user interaction handling for the game board.
 *
 * Key Features:
 * - Game board visualization using JavaFX GridPane
 * - Player movement controls (keyboard input)
 * - Game state management (undo/redo functionality)
 * - Score tracking and leaderboard management
 * - Movement logging
 *
 * Design Features:
 * - MVC Pattern: Acts as the View component, interfacing with BoardModel
 * - Observer Pattern: Updates display in response to model changes
 * - Command Pattern: Handles undo/redo operations
 *
 * @author Snowman Game Development Team
 * @version 1.0
 * @since 2025-06-08
 */

public class SnowmanBoard extends VBox implements View {

    /**
     * Callback triggered when a level is completed
     */
    private final Consumer<Void> onLevelComplete;

    /**
     * Button for resetting the current level
     */
    private final Button resetButton;

    /**
     * Button for undoing the last move
     */
    private Button undoButton;

    /**
     * Button for redoing previously undone moves
     */
    private Button redoButton;

    /**
     * The game's logic model
     */
    private BoardModel boardModel;

    /**
     * The visual grid representing the game board
     */
    private final GridPane board;

    /**
     * Text area for displaying movement history
     */
    private final TextArea movementsLog;

    /**
     * Current player's name
     */
    private String playerName;

    /**
     * Current level score
     */
    private int score;

    /**
     * File name for storing leaderboard data
     */
    private static final String LEADERBOARD_FILE = "leaderboard.txt";

    /**
     * Accumulated score across all levels
     */
    private int totalGameScore = 0;

    // Load images for the game elements
    private final Image snowImage = new Image(getClass().getResourceAsStream("/images/snow.png"));
    private final Image blockImage = new Image(getClass().getResourceAsStream("/images/block.png"));
    private final Image snowmanImage = new Image(getClass().getResourceAsStream("/images/snowman.png"));
    private final Image monsterImage = new Image(getClass().getResourceAsStream("/images/monster.png"));

    /**
     * Constructs the SnowmanBoard, initializing the game board UI, control buttons,
     * and setting up event handling and layout.
     *
     * @param boardModel      the initial model of the board for the current level
     * @param onLevelComplete a callback function to execute when the level is completed
     * @param playerName      the name of the player
     */
    public SnowmanBoard(BoardModel boardModel, Consumer<Void> onLevelComplete, String playerName) {
        this.boardModel = boardModel;
        this.onLevelComplete = onLevelComplete;
        this.board = new GridPane();

        // Text area to log player movements
        this.movementsLog = new TextArea();
        this.movementsLog.setEditable(false);
        this.movementsLog.setPrefRowCount(3);

        this.playerName = playerName;
        this.score = 0;

        // Setup Reset button
        this.resetButton = new Button("Reiniciar Nível");
        this.resetButton.setOnAction(e -> resetLevel());

        // Setup Undo and Redo buttons
        configureUndoButton();
        configureRedoButton();

        // Layout for control buttons
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.TOP_LEFT);
        controls.getChildren().add(resetButton);
        controls.getChildren().add(undoButton);
        controls.getChildren().add(redoButton);

        // Initialize the board UI
        setupBoard();

        // Add all UI elements to the main container
        this.getChildren().addAll(board, controls, movementsLog);

        // Enable keyboard input
        this.setOnKeyPressed(this::handleKeyPress);
        this.setFocusTraversable(true);
    }

    /**
     * Configures the undo button and its action handler.
     * Allows the player to undo the last move and updates the board accordingly.
     */
    private void configureUndoButton() {
        this.undoButton = new Button("Undo Move(CTRL+Z)");
        this.undoButton.setOnAction(e -> {
            if (boardModel.undo()) {
                movementsLog.appendText("Move undone\n");
                updateBoard();
            }
        });
    }

    /**
     * Configures the redo button and its action handler.
     * Allows the player to redo a previously undone move and updates the board accordingly.
     */
    private void configureRedoButton() {
        this.redoButton = new Button("Redo Move(CTRL+X)");
        this.redoButton.setOnAction(e -> {
            if (boardModel.redo()) {
                movementsLog.appendText("Move redone\n");
                updateBoard();
            }
        });
    }

    /**
     * Loads a new level by replacing the current board model, clearing the movement log,
     * resetting the score, and updating the board UI.
     *
     * @param newBoard the new board model to be loaded
     */
    public void loadNewLevel(BoardModel newBoard) {
        this.totalGameScore += this.score; // Add score from the previous level
        this.score = 0;                    // Reset current level score
        this.boardModel = newBoard;       // Assign the new board model
        this.movementsLog.clear();        // Clear movement history log
        updateBoard();                    // Refresh the visual board
        this.requestFocus();              // Ensure the board receives keyboard focus
    }


    /**
     * Resets the current level after confirming the user's intent.
     * Clears the move log, resets the board state, and updates the view.
     */
    private void resetLevel() {
        // Show a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Level");
        alert.setHeaderText("Are you sure you want to reset the level?");
        alert.setContentText("All progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Reset the level state in the model
            boardModel.resetLevel();
            // Clear the move history log
            movementsLog.clear();
            movementsLog.appendText("Level reset\n");
            // Refresh the visual board
            updateBoard();
            // Ensure keyboard focus returns to the board
            this.requestFocus();
        }
    }

    /**
     * Handles key press events for game controls, including:
     * - Movement using arrow keys
     * - Undo (Ctrl+Z)
     * - Redo (Ctrl+X)
     * - Reset level (R)
     *
     * @param event the KeyEvent to handle
     */
    private void handleKeyPress(KeyEvent event) {
        // Reset level on 'R' key press
        if (event.getCode() == KeyCode.R) {
            resetLevel();
            event.consume();
            return;
        }

        // Undo last move with Ctrl+Z
        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            if (boardModel.undo()) {
                movementsLog.appendText("Move undone\n");
                updateBoard();
            }
            event.consume();
            return;
        }

        // Redo move with Ctrl+X
        if (event.isControlDown() && event.getCode() == KeyCode.X) {
            if (boardModel.redo()) {
                movementsLog.appendText("Move redone\n");
                updateBoard();
            }
            event.consume();
            return;
        }

        // Determine direction based on arrow key
        Direction direction = null;
        switch (event.getCode()) {
            case UP -> direction = Direction.UP;
            case DOWN -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.LEFT;
            case RIGHT -> direction = Direction.RIGHT;
        }

        // Execute move if direction is valid
        if (direction != null) {
            boolean moved = boardModel.moveMonster(direction);
            if (moved) {
                this.score += 1; // Increase score for each move
                movementsLog.appendText("Monster moved " + direction + " (Score: " + score + ")\n");
                updateBoard();
            }
        }

        // Consume the event to prevent further propagation
        event.consume();
    }

    /**
     * Updates the board UI by reloading the board state visually.
     * Also checks for level completion and triggers the callback if the level is complete.
     */
    @Override
    public void updateBoard() {
        setupBoard(); // Rebuild the board based on the current model state

        // If the level is complete, trigger the level complete callback
        if (boardModel.isLevelComplete()) {
            onLevelComplete.accept(null);
        }
    }


    /**
     * Sets up the visual representation of the game board using a GridPane.
     * Adds coordinate labels for columns and rows, and populates each cell
     * with the appropriate graphical content.
     */
    private void setupBoard() {
        board.getChildren().clear(); // Clear previous content

        // Add column letter labels (A, B, C, ...)
        for (int col = 0; col <= boardModel.getCols(); col++) {
            if (col > 0) {
                Label colLabel = new Label(String.valueOf((char) ('A' + col - 1)));
                board.add(colLabel, col, 0);
            }
        }

        // Add row number labels (1, 2, 3, ...)
        for (int row = 0; row <= boardModel.getRows(); row++) {
            if (row > 0) {
                Label rowLabel = new Label(String.valueOf(row));
                board.add(rowLabel, 0, row);
            }
        }

        // Populate the grid with game cells
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                Label cell = createCell(row, col);
                board.add(cell, col + 1, row + 1);
            }
        }
    }

    /**
     * Creates a graphical cell for the board at the given position.
     *
     * @param row the row index
     * @param col the column index
     * @return a Label representing the cell with the correct image
     */
    private Label createCell(int row, int col) {
        Label cell = new Label();
        cell.setMinSize(50, 50);
        cell.setStyle("-fx-border-color: black; -fx-alignment: center;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);

        // Check if monster is at this position
        if (boardModel.getMonster().getRow() == row &&
                boardModel.getMonster().getCol() == col) {
            imageView.setImage(monsterImage);
        } else {
            // Check if a snowball is at this position
            Snowball snowball = boardModel.snowballInPosition(row, col);
            if (snowball != null) {
                // Assign correct snowball or snowman part image
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
                // Set image based on static content of the position
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
     * Logs the user's interaction with a cell. This method is a placeholder
     * for future expansion (e.g., click-based movement).
     *
     * @param row the row index of the clicked cell
     * @param col the column index of the clicked cell
     */
    private void handleCellClick(int row, int col) {
        // Log the cell that was clicked
        String move = String.format("(%d, %c) -> (%d, %c)",
                row + 1, (char) ('A' + col), row + 1, (char) ('A' + col));
        movementsLog.appendText(move + "\n");

        // Game completion is checked in updateBoard()
    }

    /**
     * Saves the current game state to a uniquely named text file in the user's Documents/Snowman folder.
     * Displays success or error alerts depending on the outcome.
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
     * Creates the full path for the save file, ensuring that the "Snowman" folder exists.
     *
     * @param filename the name of the file to be created
     * @return a Path object representing the full file path
     * @throws IOException if the directory cannot be created
     */
    private Path createFilePath(String filename) throws IOException {
        String documentsPath = System.getProperty("user.home") + "/Documents";
        Path snowmanPath = Paths.get(documentsPath, "Snowman");

        if (!Files.exists(snowmanPath)) {
            Files.createDirectories(snowmanPath); // Ensure directory exists
        }

        return snowmanPath.resolve(filename);
    }

    /**
     * Writes game data to the specified file, including the board map,
     * movements, number of moves, and snowman positions.
     *
     * @param filePath the path where the game data will be written
     * @throws IOException if an I/O error occurs during writing
     */
    private void saveGameData(Path filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile()))) {
            saveMap(writer);
            saveMovements(writer);
            saveMoveCount(writer);
            saveSnowmanPosition(writer);
        }
    }

    /**
     * Saves the current game board map to the provided PrintWriter.
     * The map includes snow, no snow, blocks, and the snowman,
     * represented by characters: S, -, B, M respectively.
     *
     * @param writer the PrintWriter to which the map will be written
     */
    private void saveMap(PrintWriter writer) {
        writer.println("=== MAPA UTILIZADO ===");

        for (int i = 0; i < boardModel.getRows(); i++) {
            for (int j = 0; j < boardModel.getCols(); j++) {
                PositionContent content = boardModel.getPositionContent(i, j);
                writer.print(switch (content) {
                    case SNOW -> "S ";
                    case NO_SNOW -> "- ";
                    case BLOCK -> "B ";
                    case SNOWMAN -> "M ";
                });
            }
            writer.println();
        }
    }

    /**
     * Logs all player movements to the provided PrintWriter.
     * Movements are retrieved directly from the UI component `movementsLog`.
     *
     * @param writer the PrintWriter to which the movements will be written
     */
    private void saveMovements(PrintWriter writer) {
        writer.println("\n=== MOVIMENTOS REALIZADOS ===");
        writer.println(movementsLog.getText());
    }

    /**
     * Counts and writes the total number of player movements to the file.
     * It considers the number of lines in `movementsLog` as the number of moves.
     *
     * @param writer the PrintWriter to which the total move count will be written
     */
    private void saveMoveCount(PrintWriter writer) {
        long moveCount = movementsLog.getText().lines().count();
        writer.println("\n=== TOTAL DE MOVIMENTOS ===");
        writer.println(moveCount);
    }

    /**
     * Finds and writes the position of the snowman on the game board.
     * The position is formatted as (rowNumber, columnLetter).
     *
     * @param writer the PrintWriter to which the snowman position will be written
     */
    private void saveSnowmanPosition(PrintWriter writer) {
        writer.println("\n=== POSIÇÃO DO BONECO DE NEVE ===");

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
     * Displays a success alert dialog to inform the user that the file was saved.
     *
     * @param filePath the path to the file that was successfully saved
     */
    private void showSuccessAlert(Path filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Arquivo Salvo");
        alert.setHeaderText("Jogo salvo com sucesso!");
        alert.setContentText("O arquivo foi salvo em:\n" + filePath.toString());
        alert.showAndWait();
    }

    /**
     * Displays an error alert dialog when a file save operation fails.
     *
     * @param e the IOException that occurred during the save process
     */
    private void showErrorAlert(IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Erro ao salvar o arquivo");
        alert.setContentText("Não foi possível salvar o arquivo do jogo: " + e.getMessage());
        alert.showAndWait();
    }

    /**
     * Saves the current player's score to the leaderboard file.
     * Creates the leaderboard file if it does not exist and appends the score.
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
     * Ensures the leaderboard file and directory exist, and returns the file path.
     *
     * @return the path to the leaderboard file
     * @throws IOException if the directory or file cannot be created
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
     * Appends the current player's score to the leaderboard file.
     * The score is formatted with username, total score, and current date/time.
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

        String scoreEntry = String.format("%-3s | %4d | %s%n", playerName, finalScore, dateTime);

        Files.write(filePath, scoreEntry.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * Displays the leaderboard with all recorded scores in a dialog.
     * If the leaderboard does not exist or is empty, shows an appropriate message.
     */
    public void showLeaderboard() {
        try {
            Path leaderboardPath = createLeaderboardFile();
            if (!Files.exists(leaderboardPath)) {
                showNoLeaderboardAlert();
                return;
            }

            List<String> scores = Files.readAllLines(leaderboardPath);

            if (scores.isEmpty()) {
                showNoLeaderboardAlert();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Leaderboard");
            alert.setHeaderText("Top Scores");

            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setPrefRowCount(10);
            textArea.setPrefColumnCount(40);

            StringBuilder content = new StringBuilder();
            content.append(String.format("%-3s | %-4s | %-19s%n", "USR", "SCOR", "DATE"));
            content.append("--------------------------------\n");

            scores.forEach(score -> content.append(score));

            textArea.setText(content.toString());

            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (IOException e) {
            showErrorAlert(e);
        }
    }

    /**
     * Displays an alert to indicate that no scores are available in the leaderboard.
     */
    private void showNoLeaderboardAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Leaderboard");
        alert.setHeaderText("No Scores Yet");
        alert.setContentText("There are no scores recorded yet.");
        alert.showAndWait();
    }
}