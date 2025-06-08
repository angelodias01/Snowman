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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

/**
 * SnowmanBoard is a custom JavaFX VBox component that implements the game view.
 * It displays the game board, control buttons (undo, redo, reset), and a movement log.
 */
/**
 * SnowmanBoard is a JavaFX component that visually represents the game board
 * and manages user interactions such as movement, undo/redo, reset, and saving.
 */
public class SnowmanBoard extends VBox implements View {

    // Callback function triggered when the level is completed
    private final Consumer<Void> onLevelComplete;

    // Game control buttons
    private final Button resetButton;
<<<<<<< Updated upstream
=======
    private Button undoButton;
    private Button redoButton;

    // Current game model representing the board state
>>>>>>> Stashed changes
    private BoardModel boardModel;

    // JavaFX layout container for the visual board
    private final GridPane board;

    // Text area to log all player movements
    private final TextArea movementsLog;

    // Game asset images
    private final Image snowImage = new Image(getClass().getResourceAsStream("/images/snow.png"));
    private final Image blockImage = new Image(getClass().getResourceAsStream("/images/block.png"));
    private final Image snowmanImage = new Image(getClass().getResourceAsStream("/images/snowman.png"));
    private final Image monsterImage = new Image(getClass().getResourceAsStream("/images/monster.png"));

    /**
     * Constructor for SnowmanBoard.
     * Initializes the visual components, sets up event listeners, and binds to the game model.
     *
     * @param boardModel the model representing the current game state
     * @param onLevelComplete callback to trigger when the level is completed
     */
    public SnowmanBoard(BoardModel boardModel, Consumer<Void> onLevelComplete) {
        this.boardModel = boardModel;
        this.onLevelComplete = onLevelComplete;
        this.board = new GridPane();
        this.movementsLog = new TextArea();
        this.movementsLog.setEditable(false);
        this.movementsLog.setPrefRowCount(3);

        // Criar botão de reset
        this.resetButton = new Button("Reiniciar Nível");
        this.resetButton.setOnAction(e -> resetLevel());

<<<<<<< Updated upstream
        // Criar uma HBox para os controles
        HBox controls = new HBox(10); // 10 pixels de espaçamento
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().add(resetButton);
=======
        configureUndoButton();
        configureRedoButton();

        // Layout for control buttons
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.TOP_LEFT);
        controls.getChildren().addAll(resetButton, undoButton, redoButton);
>>>>>>> Stashed changes

        setupBoard();
        this.getChildren().addAll(board, controls, movementsLog);

<<<<<<< Updated upstream
        // Adicionar handler de teclado
=======
        // Enable keyboard input
>>>>>>> Stashed changes
        this.setOnKeyPressed(this::handleKeyPress);
        this.setFocusTraversable(true);
    }

<<<<<<< Updated upstream
=======
    /**
     * Configures the undo button and its associated event handler.
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
     * Configures the redo button and its associated event handler.
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
     * Loads a new level into the board.
     *
     * @param newBoard the new board model to load
     */
>>>>>>> Stashed changes
    public void loadNewLevel(BoardModel newBoard) {
        this.boardModel = newBoard;
        this.movementsLog.clear();
        updateBoard();
        this.requestFocus();
    }

    /**
     * Resets the current level after user confirmation.
     */
    private void resetLevel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Level");
        alert.setHeaderText("Are you sure you want to reset the level?");
        alert.setContentText("All progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boardModel.resetLevel();
            movementsLog.clear();
            movementsLog.appendText("Level reset\n");
            updateBoard();
            this.requestFocus();
        }
    }

    /**
     * Handles keyboard input for movement and shortcuts.
     *
     * @param event the key event
     */
    private void handleKeyPress(KeyEvent event) {
        // Handle shortcuts
        if (event.getCode() == KeyCode.R) {
            resetLevel();
            event.consume();
            return;
        }

        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            if (boardModel.undo()) {
                movementsLog.appendText("Move undone\n");
                updateBoard();
            }
            event.consume();
            return;
        }

<<<<<<< Updated upstream
        // Código existente para movimentação
        Direction direction = null;
        switch (event.getCode()) {
            case UP -> direction = Direction.UP;
            case DOWN -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.LEFT;
            case RIGHT -> direction = Direction.RIGHT;
        }
=======
        if (event.isControlDown() && event.getCode() == KeyCode.X) {
            if (boardModel.redo()) {
                movementsLog.appendText("Move redone\n");
                updateBoard();
            }
            event.consume();
            return;
        }

        // Handle directional movement
        Direction direction = switch (event.getCode()) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case LEFT -> Direction.LEFT;
            case RIGHT -> Direction.RIGHT;
            default -> null;
        };
>>>>>>> Stashed changes

        if (direction != null) {
            boolean moved = boardModel.moveMonster(direction);
            if (moved) {
                movementsLog.appendText("Monster moved " + direction + "\n");
                updateBoard();
            }
        }

        event.consume();
    }

    /**
     * Updates the board’s visual representation to reflect the model.
     * Also checks for level completion.
     */
    @Override
    public void updateBoard() {
        setupBoard();
        if (boardModel.isLevelComplete()) {
            onLevelComplete.accept(null);
        }
    }

    /**
     * Sets up the visual grid with headers and content based on the model.
     */
    private void setupBoard() {
        board.getChildren().clear();

        // Column headers (A, B, C...)
        for (int col = 1; col <= boardModel.getCols(); col++) {
            Label colLabel = new Label(String.valueOf((char) ('A' + col - 1)));
            board.add(colLabel, col, 0);
        }

        // Row headers (1, 2, 3...)
        for (int row = 1; row <= boardModel.getRows(); row++) {
            Label rowLabel = new Label(String.valueOf(row));
            board.add(rowLabel, 0, row);
        }

        // Populate cells
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                Label cell = createCell(row, col);
                board.add(cell, col + 1, row + 1);
            }
        }
    }

    /**
     * Creates a label for a given board cell, with an image representing its content.
     *
     * @param row the row index
     * @param col the column index
     * @return a configured Label node
     */
    private Label createCell(int row, int col) {
        Label cell = new Label();
        cell.setMinSize(50, 50);
        cell.setStyle("-fx-border-color: black; -fx-alignment: center;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);

        // Check if the monster is in this position
        if (boardModel.getMonster().getRow() == row && boardModel.getMonster().getCol() == col) {
            imageView.setImage(monsterImage);
        } else {
            // Check for snowball or static terrain
            Snowball snowball = boardModel.snowballInPosition(row, col);
            if (snowball != null) {
                imageView.setImage(switch (snowball.getType()) {
                    case SMALL -> new Image(getClass().getResourceAsStream("/images/snowball_small.png"));
                    case MID -> new Image(getClass().getResourceAsStream("/images/snowball_mid.png"));
                    case BIG -> new Image(getClass().getResourceAsStream("/images/snowball_big.png"));
                    case MID_SMALL -> new Image(getClass().getResourceAsStream("/images/snowman_partial1.png"));
                    case BIG_SMALL -> new Image(getClass().getResourceAsStream("/images/snowman_partial2.png"));
                    case BIG_MID -> new Image(getClass().getResourceAsStream("/images/snowman_partial3.png"));
                    case COMPLETE -> snowmanImage;
                });
            } else {
                PositionContent content = boardModel.getPositionContent(row, col);
                imageView.setImage(switch (content) {
                    case NO_SNOW -> new Image(getClass().getResourceAsStream("/images/grass.png"));
                    case SNOW -> snowImage;
                    case BLOCK -> blockImage;
                    case SNOWMAN -> snowmanImage;
                });
            }
        }

        cell.setGraphic(imageView);
        return cell;
    }

    /**
     * Logs a click event on a specific cell (currently for debugging).
     */
    private void handleCellClick(int row, int col) {
        String move = String.format("(%d, %c) -> (%d, %c)", row + 1, (char) ('A' + col), row + 1, (char) ('A' + col));
        movementsLog.appendText(move + "\n");
    }

<<<<<<< Updated upstream
    void saveGameToFile() {
=======
    /**
     * Saves the current game state to a file in the user's Documents/Snowman directory.
     */
    public void saveGameToFile() {
>>>>>>> Stashed changes
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = "snowman" + timestamp + ".txt";

        try {
            // Obter o caminho para a pasta Documentos
            String documentsPath = System.getProperty("user.home") + "/Documents";

<<<<<<< Updated upstream
            // Criar o caminho para a pasta Snowman dentro de Documentos
            Path snowmanPath = Paths.get(documentsPath, "Snowman");

            // Criar a pasta se não existir
            if (!Files.exists(snowmanPath)) {
                Files.createDirectories(snowmanPath);
=======
    /**
     * Creates the file path for saving the game.
     */
    private Path createFilePath(String filename) throws IOException {
        String documentsPath = System.getProperty("user.home") + "/Documents";
        Path snowmanPath = Paths.get(documentsPath, "Snowman");

        if (!Files.exists(snowmanPath)) {
            Files.createDirectories(snowmanPath);
        }

        return snowmanPath.resolve(filename);
    }

    /**
     * Writes the current game data to the specified file.
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
     * Saves the current map layout to the file.
     */
    private void saveMap(PrintWriter writer) {
        writer.println("=== MAP USED ===");
        for (int i = 0; i < boardModel.getRows(); i++) {
            for (int j = 0; j < boardModel.getCols(); j++) {
                PositionContent content = boardModel.getPositionContent(i, j);
                writer.print(switch (content) {
                    case SNOW -> "S ";
                    case NO_SNOW -> "- ";
                    case BLOCK -> "B ";
                    case SNOWMAN -> "M ";
                });
>>>>>>> Stashed changes
            }

<<<<<<< Updated upstream
            // Criar o caminho completo do arquivo
            Path filePath = snowmanPath.resolve(filename);

            // Salvar o arquivo
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile()))) {
                // 1. Salvar o mapa
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
=======
    /**
     * Saves the text from the movement log.
     */
    private void saveMovements(PrintWriter writer) {
        writer.println("\n=== MOVES PERFORMED ===");
        writer.println(movementsLog.getText());
    }

    /**
     * Saves the number of moves made.
     */
    private void saveMoveCount(PrintWriter writer) {
        long moveCount = movementsLog.getText().lines().count();
        writer.println("\n=== TOTAL MOVES ===");
        writer.println(moveCount);
    }

    /**
     * Saves the final position of the snowman.
     */
    private void saveSnowmanPosition(PrintWriter writer) {
        writer.println("\n=== SNOWMAN POSITION ===");
        for (int i = 0; i < boardModel.getRows(); i++) {
            for (int j = 0; j < boardModel.getCols(); j++) {
                if (boardModel.getPositionContent(i, j) == PositionContent.SNOWMAN) {
                    writer.printf("(%d, %c)%n", i + 1, (char) ('A' + j));
                    return;
>>>>>>> Stashed changes
                }

                // 2. Salvar movimentos
                writer.println("\n=== MOVIMENTOS REALIZADOS ===");
                writer.println(movementsLog.getText());

                // 3. Quantidade de movimentos
                long moveCount = movementsLog.getText().lines().count();
                writer.println("\n=== TOTAL DE MOVIMENTOS ===");
                writer.println(moveCount);

                // 4. Posição do boneco de neve
                writer.println("\n=== POSIÇÃO DO BONECO DE NEVE ===");
                for (int i = 0; i < boardModel.getRows(); i++) {
                    for (int j = 0; j < boardModel.getCols(); j++) {
                        if (boardModel.getPositionContent(i, j) == PositionContent.SNOWMAN) {
                            writer.printf("(%d, %c)%n", i + 1, (char)('A' + j));
                            break;
                        }
                    }
                }

                // Mostrar mensagem de sucesso
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Arquivo Salvo");
                alert.setHeaderText("Jogo salvo com sucesso!");
                alert.setContentText("O arquivo foi salvo em:\n" + filePath.toString());
                alert.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao salvar o arquivo");
            alert.setContentText("Não foi possível salvar o arquivo do jogo: " + e.getMessage());
            alert.showAndWait();
        }
    }
<<<<<<< Updated upstream
}
=======

    /**
     * Shows an alert to inform the user the file has been saved successfully.
     */
    private void showSuccessAlert(Path filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("File Saved");
        alert.setHeaderText("Game successfully saved!");
        alert.setContentText("The file was saved at:\n" + filePath.toString());
        alert.showAndWait();
    }

    /**
     * Shows an alert if an error occurs while saving the file.
     */
    private void showErrorAlert(IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error saving the file");
        alert.setContentText("Failed to save the game file: " + e.getMessage());
        alert.showAndWait();
    }
}
>>>>>>> Stashed changes
