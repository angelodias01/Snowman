package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pt.ipbeja.estig.po2.snowman.app.model.BoardModel;
import pt.ipbeja.estig.po2.snowman.app.model.PositionContent;
import pt.ipbeja.estig.po2.snowman.app.model.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SnowmanBoard extends VBox implements View {
    private final BoardModel boardModel;
    private final GridPane board;
    private final TextArea movementsLog;

    // Imagens para os elementos do jogo
    private final Image snowImage = new Image("/images/snow.png");
    private final Image blockImage = new Image("/images/block.png");
    private final Image snowmanImage = new Image("/images/snowman.png");
    private final Image monsterImage = new Image("/images/monster.png");

    public SnowmanBoard(BoardModel boardModel) {
        this.boardModel = boardModel;
        this.board = new GridPane();
        this.movementsLog = new TextArea();
        this.movementsLog.setEditable(false);
        this.movementsLog.setPrefRowCount(3);

        setupBoard();
        this.getChildren().addAll(board, movementsLog);
    }

    private void setupBoard() {
        board.getChildren().clear();

        // Adicionar letras para as colunas
        for (int col = 0; col <= boardModel.getCols(); col++) {
            if (col > 0) {
                Label colLabel = new Label(String.valueOf((char)('A' + col - 1)));
                board.add(colLabel, col, 0);
            }
        }

        // Adicionar números para as linhas
        for (int row = 0; row <= boardModel.getRows(); row++) {
            if (row > 0) {
                Label rowLabel = new Label(String.valueOf(row));
                board.add(rowLabel, 0, row);
            }
        }

        // Desenhar o tabuleiro
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                Label cell = createCell(row, col);
                board.add(cell, col + 1, row + 1);
            }
        }
    }

    private Label createCell(int row, int col) {
        Label cell = new Label();
        cell.setMinSize(50, 50);
        cell.setStyle("-fx-border-color: black; -fx-alignment: center;");

        PositionContent content = boardModel.getPositionContent(row, col);
        ImageView imageView = new ImageView();
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);

        switch (content) {
            case NO_SNOW -> cell.setStyle("-fx-background-color: white; -fx-border-color: black;");
            case SNOW -> imageView.setImage(snowImage);
            case BLOCK -> imageView.setImage(blockImage);
            case SNOWMAN -> imageView.setImage(snowmanImage);
        }

        cell.setGraphic(imageView);

        // Adicionar evento de clique
        final int finalRow = row;
        final int finalCol = col;
        cell.setOnMouseClicked(event -> handleCellClick(finalRow, finalCol));

        return cell;
    }

    private void handleCellClick(int row, int col) {
        // Implementar lógica de movimento aqui
        // Registrar movimento no log
        String move = String.format("(%d, %c) -> (%d, %c)",
            row + 1, (char)('A' + col), row + 1, (char)('A' + col));
        movementsLog.appendText(move + "\n");

        // Verificar se o jogo terminou após o movimento
        checkGameEnd();
    }

    private void checkGameEnd() {
        if (isGameComplete()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Jogo Completo");
            alert.setHeaderText("Parabéns! Você completou o nível!");
            alert.setContentText("Deseja jogar novamente?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                saveGameToFile();
                resetGame();
            }
        }
    }

    private boolean isGameComplete() {
        // Implementar lógica para verificar se o boneco de neve está completo
        return false; // Temporário
    }

    private void saveGameToFile() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = "snowman" + timestamp + ".txt";
        // Implementar salvamento do arquivo
    }

    private void resetGame() {
        // Implementar reset do jogo
    }

    @Override
    public void updateBoard() {
        setupBoard();
    }
}