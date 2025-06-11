package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ipbeja.estig.po2.snowman.app.model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The main JavaFX application class for the Snowman game.
 * <p>
 * This class is responsible for setting up the primary stage, initializing
 * the game components such as the board, audio player, level manager,
 * and leaderboard panel. It handles user input for the player name,
 * loads the initial game board, and manages the overall game UI.
 * <p>
 * Key features:
 * >Prompts the player to enter their name before starting the game.</li>
 * >Initializes the game board and user interface components.</li>
 * >Sets up the SnowmanBoard for gameplay and handles level completion events.</li>
 * >Displays the leaderboard on the right side of the window.</li>
 * >Plays background audio during gameplay.</li>
 * <p>
 * Extends {@link Application} to launch the JavaFX GUI.
 * </p>
 *
 *  @author Ângelo Dias(24288), Edgar Brito(22895)
 */
public class SnowmanGUI extends Application {
    private BoardModel boardModel;
    private SnowmanBoard snowmanBoard;
    private LevelManager levelManager;
    private GameAudio audioPlayer;
    private String playerName;
    private VBox leaderboardPanel;
    private ListView<String> leaderboardListView;

    /**
     * The main entry point for the JavaFX application.
     * Initializes the game window, game components, and user interface.
     * Requests the player's name before launching the game.
     *
     * @param stage The primary stage for this application, onto which
     *              the application scene can be set.
     */
    @Override
    public void start(Stage stage) {
        if (!getUserName()) {
            Platform.exit();
            return;
        }

        this.audioPlayer = new GameAudio();
        this.levelManager = new LevelManager();
        this.boardModel = createInitialBoard();

        createLeaderboardPanel();

        this.snowmanBoard = new SnowmanBoard(boardModel, this::handleLevelComplete, playerName);

        BorderPane root = new BorderPane();
        root.setCenter(snowmanBoard);
        root.setRight(leaderboardPanel);

        Scene scene = new Scene(root, 800, 400);
        stage.setScene(scene);
        stage.setTitle("SnowMan Game - Level 1");

        audioPlayer.play("mus1.wav");

        snowmanBoard.requestFocus();

        updateLeaderboard();

        stage.show();
    }

    /**
     * Creates the initial game board configuration.
     * <p>
     * Constructs a 5x5 grid filled with snow and places some initial snowballs and the monster.
     *
     * @return A {@link BoardModel} representing the starting state of the game board.
     */
    private BoardModel createInitialBoard() {
        // Criar um tabuleiro 5x5
        List<List<PositionContent>> grid = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(PositionContent.SNOW);
            }
            grid.add(row);
        }

        grid.get(2).set(2, PositionContent.SNOW);
        grid.get(1).set(1, PositionContent.SNOW);
        grid.get(3).set(3, PositionContent.SNOW);

        Monster monster = new Monster(0, 0);
        List<Snowball> snowballs = new ArrayList<>();
        snowballs.add(new Snowball(2, 3, SnowballType.SMALL));
        snowballs.add(new Snowball(1, 2, SnowballType.SMALL));
        snowballs.add(new Snowball(3, 1, SnowballType.SMALL));

        return new BoardModel(grid, monster, snowballs);
    }

    /**
     * Handles the event when the current level is completed.
     * <p>
     * If there is a next level available, prompts the user to proceed to it.
     * If the user confirms, loads the next level and updates the UI accordingly.
     * <p>
     * If there are no more levels, saves the game state and score,
     * updates the leaderboard, and shows a completion message.
     *
     * @param unused A placeholder parameter (not used).
     */
    private void handleLevelComplete(Void unused) {
        if (levelManager.hasNextLevel()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Level Complete");
            alert.setHeaderText("Congratulations! You've completed the level!");
            alert.setContentText("Would you like to go to the next level?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Player chose to continue
                this.boardModel = levelManager.loadNextLevel();
                snowmanBoard.loadNewLevel(boardModel);
                Stage stage = (Stage) snowmanBoard.getScene().getWindow();
                stage.setTitle("Snowman Game - Level " + (levelManager.getCurrentLevelIndex() + 1));
            } else {
                // Player chose NOT to continue — save and show thank-you message
                snowmanBoard.saveGameToFile();
                snowmanBoard.saveScore();
                updateLeaderboard();

                Alert exitAlert = new Alert(Alert.AlertType.INFORMATION);
                exitAlert.setTitle("Game Saved");
                exitAlert.setHeaderText("Progress Saved");
                exitAlert.setContentText("Your progress and score have been saved. Thanks for playing!");
                exitAlert.showAndWait();
            }
        } else {
            // Last level was completed
            snowmanBoard.saveGameToFile();
            snowmanBoard.saveScore();
            updateLeaderboard();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Complete");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You've completed all the levels of the game!");
            alert.showAndWait();
        }
    }

    /**
     * Prompts the user to enter their name before starting the game.
     * <p>
     * Limits the player name input to a maximum of 3 characters.
     * Converts the entered name to uppercase and trims whitespace.
     * If the user cancels or enters an invalid name, returns false.
     *
     * @return {@code true} if a valid player name was entered; {@code false} otherwise.
     */
    private boolean getUserName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Player's name");
        dialog.setHeaderText("Welcome to the Snowman Game!");
        dialog.setContentText("Please enter your name (maximum 3 characters):");

        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 3) {
                dialog.getEditor().setText(oldValue);
            }
        });

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            playerName = result.get().toUpperCase().trim();
            return true;
        }
        return false;
    }

    /**
     * Creates the leaderboard panel UI component.
     * <p>
     * The panel includes a title label, a list view to display leaderboard entries,
     * and a refresh button to update the leaderboard.
     * The panel is padded and organized vertically.
     */
    private void createLeaderboardPanel() {
        leaderboardPanel = new VBox(10);
        leaderboardPanel.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("LEADERBOARD");
        titleLabel.setFont(new javafx.scene.text.Font(16));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        leaderboardListView = new ListView<>();
        leaderboardListView.setPrefHeight(300);

        javafx.scene.control.Button refreshButton = new javafx.scene.control.Button("Refresh");
        refreshButton.setOnAction(e -> updateLeaderboard());

        leaderboardPanel.getChildren().addAll(titleLabel, leaderboardListView, refreshButton);
    }

    /**
     * Updates the leaderboard ListView by reading the scores from the leaderboard file.
     * <p>
     * If the leaderboard file does not exist, displays a placeholder message.
     * If an error occurs while reading the file, displays an error message.
     * The leaderboard is displayed with a header and the list of scores.
     */
    private void updateLeaderboard() {
        try {
            Path leaderboardPath = Paths.get(System.getProperty("user.home"), "Documents", "Snowman", "leaderboard.txt");
            if (!Files.exists(leaderboardPath)) {
                leaderboardListView.getItems().clear();
                leaderboardListView.getItems().add("No scores yet");
                return;
            }

            List<String> scores = Files.readAllLines(leaderboardPath);
            leaderboardListView.getItems().clear();

            leaderboardListView.getItems().add(String.format("%-3s | %-4s | %-10s", "USER", "SCORE", "DATE"));
            leaderboardListView.getItems().add("--------------------------");

            scores.forEach(score -> leaderboardListView.getItems().add(score));

        } catch (IOException e) {
            e.printStackTrace();
            leaderboardListView.getItems().clear();
            leaderboardListView.getItems().add("Error loading scores");
        }
    }

    /**
     * Main entry point for launching the Snowman GUI application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch();
    }
}