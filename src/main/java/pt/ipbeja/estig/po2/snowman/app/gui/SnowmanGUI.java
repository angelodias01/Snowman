package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ipbeja.estig.po2.snowman.app.model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SnowmanGUI extends Application {
    private BoardModel boardModel;
    private SnowmanBoard snowmanBoard;
    private LevelManager levelManager;
    private GameAudio audioPlayer;
    private String playerName;

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
        
        // Criar e inicializar o SnowmanBoard com o handler de conclusão de nível
        this.snowmanBoard = new SnowmanBoard(boardModel, this::handleLevelComplete);

        // Criar o layout raiz
        BorderPane root = new BorderPane();
        root.setCenter(snowmanBoard);

        // Configurar a cena e o palco
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Jogo do Boneco de Neve - Nível 1");

        audioPlayer.play("mus1.wav");

        // Requisitar foco para o tabuleiro
        snowmanBoard.requestFocus();
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
        // Salvar o jogo primeiro
        snowmanBoard.saveGameToFile();
        
        if (levelManager.hasNextLevel()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Nível Completo");
            alert.setHeaderText("Parabéns! Você completou o nível!");
            alert.setContentText("Deseja ir para o próximo nível?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Carregar próximo nível
                this.boardModel = levelManager.loadNextLevel();
                snowmanBoard.loadNewLevel(boardModel);
                // Atualizar título da janela
                Stage stage = (Stage) snowmanBoard.getScene().getWindow();
                stage.setTitle("Jogo do Boneco de Neve - Nível " + (levelManager.getCurrentLevelIndex() + 1));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Jogo Completo");
            alert.setHeaderText("Parabéns!");
            alert.setContentText("Você completou todos os níveis do jogo!");
            alert.showAndWait();
        }
    }

    private boolean getUserName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nome do Jogador");
        dialog.setHeaderText("Bem-vindo ao Jogo do Boneco de Neve!");
        dialog.setContentText("Por favor, insira seu nome (máximo 3 caracteres):");

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


    public static void main(String[] args) {
        launch();
    }
}