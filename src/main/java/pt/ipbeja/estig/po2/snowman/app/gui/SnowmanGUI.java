package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ipbeja.estig.po2.snowman.app.model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SnowmanGUI is the main entry point for the Snowman game application.
 * It initializes the game window, board, audio, and handles user interaction
 * such as name input and level progression.
 */
public class SnowmanGUI extends Application {
<<<<<<< Updated upstream
    private BoardModel boardModel;
    private SnowmanBoard snowmanBoard;
    private LevelManager levelManager;
=======
    private BoardModel boardModel;        // Represents the current state of the game board
    private SnowmanBoard snowmanBoard;    // GUI component that renders and interacts with the game board
    private LevelManager levelManager;    // Manages game level loading and progression
    private GameAudio audioPlayer;        // Handles background music and sound effects
    private String playerName;            // Stores the player’s name (limited to 3 characters)
>>>>>>> Stashed changes

    /**
     * Entry point of the JavaFX application. Sets up the main game UI and initializes the game state.
     */
    @Override
    public void start(Stage stage) {
<<<<<<< Updated upstream
=======
        // Prompt for user name. If user cancels, exit the application.
        if (!getUserName()) {
            Platform.exit();
            return;
        }

        // Initialize game components
        this.audioPlayer = new GameAudio();
>>>>>>> Stashed changes
        this.levelManager = new LevelManager();
        this.boardModel = createInitialBoard(); // Load the initial level

        // Initialize the main game board with a callback for when a level is completed
        this.snowmanBoard = new SnowmanBoard(boardModel, this::handleLevelComplete);

        // Set up the UI layout using a BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(snowmanBoard); // Place the board at the center of the window

        // Create and set the scene
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Jogo do Boneco de Neve - Nível 1");

<<<<<<< Updated upstream
        // Requisitar foco para o tabuleiro
=======
        // Start playing background music
        audioPlayer.play("mus1.wav");

        // Focus the game board for key input and show the window
>>>>>>> Stashed changes
        snowmanBoard.requestFocus();

        stage.show();
    }

    /**
     * Creates the initial 5x5 board layout with snow, a monster, and snowballs.
     * @return a fully initialized BoardModel object
     */
    private BoardModel createInitialBoard() {
        List<List<PositionContent>> grid = new ArrayList<>();

        // Fill the board with SNOW content
        for (int i = 0; i < 5; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(PositionContent.SNOW);
            }
            grid.add(row);
        }

        // (Optional) Customize specific cells — here we leave them as SNOW
        grid.get(2).set(2, PositionContent.SNOW);
        grid.get(1).set(1, PositionContent.SNOW);
        grid.get(3).set(3, PositionContent.SNOW);

        // Create a monster at the top-left corner
        Monster monster = new Monster(0, 0);

        // Create and position snowballs on the board
        List<Snowball> snowballs = new ArrayList<>();
        snowballs.add(new Snowball(2, 3, SnowballType.SMALL));
        snowballs.add(new Snowball(1, 2, SnowballType.SMALL));
        snowballs.add(new Snowball(3, 1, SnowballType.SMALL));

        return new BoardModel(grid, monster, snowballs);
    }

    /**
     * Callback executed when a level is completed by the player.
     * Offers to load the next level if available, otherwise congratulates the player.
     */
    private void handleLevelComplete(Void unused) {
        // Save the current game state
        snowmanBoard.saveGameToFile();

        if (levelManager.hasNextLevel()) {
            // Ask the player if they want to proceed to the next level
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Nível Completo");
            alert.setHeaderText("Parabéns! Você completou o nível!");
            alert.setContentText("Deseja ir para o próximo nível?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Load and display the next level
                this.boardModel = levelManager.loadNextLevel();
                snowmanBoard.loadNewLevel(boardModel);

                // Update the window title to reflect the new level
                Stage stage = (Stage) snowmanBoard.getScene().getWindow();
                stage.setTitle("Jogo do Boneco de Neve - Nível " + (levelManager.getCurrentLevelIndex() + 1));
            }
        } else {
            // All levels completed — show final congratulations
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Jogo Completo");
            alert.setHeaderText("Parabéns!");
            alert.setContentText("Você completou todos os níveis do jogo!");
            alert.showAndWait();
        }
    }

<<<<<<< Updated upstream
=======
    /**
     * Prompts the user to enter a name before starting the game.
     * Limits input to 3 characters.
     * @return true if a valid name was entered, false if the dialog was canceled
     */
    private boolean getUserName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nome do Jogador");
        dialog.setHeaderText("Bem-vindo ao Jogo do Boneco de Neve!");
        dialog.setContentText("Por favor, insira seu nome (máximo 3 caracteres):");

        // Add listener to enforce a 3-character limit
        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 3) {
                dialog.getEditor().setText(oldValue);
            }
        });

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            playerName = result.get().toUpperCase().trim(); // Save name in uppercase
            return true;
        }
        return false;
    }

    /**
     * Main method — launches the JavaFX application.
     * @param args command-line arguments (unused)
     */
>>>>>>> Stashed changes
    public static void main(String[] args) {
        launch(); // Triggers the JavaFX application lifecycle
    }
}
