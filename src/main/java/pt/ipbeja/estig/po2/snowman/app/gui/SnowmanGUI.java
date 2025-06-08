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

public class SnowmanGUI extends Application {
    private BoardModel boardModel;
    private SnowmanBoard snowmanBoard;
    private LevelManager levelManager;
    private GameAudio audioPlayer;
    private String playerName;
    private VBox leaderboardPanel;  // Add this field
    private ListView<String> leaderboardListView;
    private static final double PADDING_VALUE = 10.0;

    @Override
    public void start(Stage stage) {
        if (!getUserName()) {
            // If user cancels the dialog, close the application
            Platform.exit();
            return;
        }


        this.audioPlayer = new GameAudio();
        this.levelManager = new LevelManager();
        this.boardModel = createInitialBoard();

        createLeaderboardPanel();

        // Criar e inicializar o SnowmanBoard com o handler de conclusão de nível
        this.snowmanBoard = new SnowmanBoard(boardModel, this::handleLevelComplete, playerName);

        // Criar o layout raiz
        BorderPane root = new BorderPane();
        root.setCenter(snowmanBoard);
        root.setRight(leaderboardPanel);

        // Configurar a cena e o palco
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("SnowMan Game - Level 1");

        audioPlayer.play("mus1.wav");

        // Requisitar foco para o tabuleiro
        snowmanBoard.requestFocus();

        updateLeaderboard();

        stage.show();
    }

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

        // Adicionar alguns elementos ao tabuleiro
        grid.get(2).set(2, PositionContent.SNOW);
        grid.get(1).set(1, PositionContent.SNOW);
        grid.get(3).set(3, PositionContent.SNOW);

        // Criar o monstro e as bolas de neve
        Monster monster = new Monster(0, 0);
        List<Snowball> snowballs = new ArrayList<>();
        snowballs.add(new Snowball(2, 3, SnowballType.SMALL));
        snowballs.add(new Snowball(1, 2, SnowballType.SMALL));
        snowballs.add(new Snowball(3, 1, SnowballType.SMALL));

        return new BoardModel(grid, monster, snowballs);
    }

    /**
     * Handler chamado quando um nível é completado
     */
    private void handleLevelComplete(Void unused) {
        if (levelManager.hasNextLevel()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Level Complete");
            alert.setHeaderText("Congratulations! You've completed the level!");
            alert.setContentText("Would you like to go to the next level?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.boardModel = levelManager.loadNextLevel();
                snowmanBoard.loadNewLevel(boardModel);
                Stage stage = (Stage) snowmanBoard.getScene().getWindow();
                stage.setTitle("Snowman Game - Level " + (levelManager.getCurrentLevelIndex() + 1));
            }
        } else {
            // Salvar pontuação apenas quando o jogo terminar
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

    private void createLeaderboardPanel() {
        leaderboardPanel = new VBox(10);  // usando valor direto ao invés de PADDING_VALUE
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

            // Add header
            leaderboardListView.getItems().add(String.format("%-3s | %-4s | %-10s", "USER", "SCORE", "DATE"));
            leaderboardListView.getItems().add("--------------------------");

            // Add scores
            scores.forEach(score -> leaderboardListView.getItems().add(score));

        } catch (IOException e) {
            e.printStackTrace();
            leaderboardListView.getItems().clear();
            leaderboardListView.getItems().add("Error loading scores");
        }
    }


    public static void main(String[] args) {
        launch();
    }
}