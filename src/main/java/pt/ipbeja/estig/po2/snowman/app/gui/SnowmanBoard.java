package pt.ipbeja.estig.po2.snowman.app.gui;



import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pt.ipbeja.estig.po2.snowman.app.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SnowmanBoard extends VBox implements View {
    private final BoardModel boardModel;
    private final GridPane board;
    private final TextArea movementsLog;

    // Imagens para os elementos do jogo
    private final Image snowImage = new Image(getClass().getResourceAsStream("/images/snow.png"));
    private final Image blockImage = new Image(getClass().getResourceAsStream("/images/block.png"));
    private final Image snowmanImage = new Image(getClass().getResourceAsStream("/images/snowman.png"));
    private final Image monsterImage = new Image(getClass().getResourceAsStream("/images/monster.png"));

    public SnowmanBoard(BoardModel boardModel) {
        this.boardModel = boardModel;
        this.board = new GridPane();
        this.movementsLog = new TextArea();
        this.movementsLog.setEditable(false);
        this.movementsLog.setPrefRowCount(3);

        setupBoard();
        this.getChildren().addAll(board, movementsLog);

        // Adicionar handler de teclado
        this.setOnKeyPressed(this::handleKeyPress);

        // Importante para receber eventos de teclado
        this.setFocusTraversable(true);
    }

    private void handleKeyPress(KeyEvent event) {
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
                movementsLog.appendText("Monstro moveu para " + direction + "\n");
                updateBoard();
            }
        }

        // Consumir o evento para evitar propagação
        event.consume();
    }

    @Override
    public void updateBoard() {
        setupBoard();
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

        ImageView imageView = new ImageView();
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);

        // Verificar monstro primeiro
        if (boardModel.getMonster().getRow() == row &&
            boardModel.getMonster().getCol() == col) {
            imageView.setImage(monsterImage);
        } else {
            // Verificar bolas de neve
            Snowball snowball = boardModel.snowballInPosition(row, col);
            if (snowball != null) {
                switch (snowball.getType()) {
                    case SMALL -> imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowball_small.png")));
                    case MID -> imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowball_mid.png")));
                    case BIG -> imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowball_big.png")));
                    case MID_SMALL -> imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowman_partial1.png")));
                    case BIG_SMALL -> imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowman_partial2.png")));
                    case BIG_MID -> imageView.setImage(new Image(getClass().getResourceAsStream("/images/snowman_partial3.png")));
                }
            } else {
                // Verificar outros conteúdos
                PositionContent content = boardModel.getPositionContent(row, col);
                switch (content) {
                    case NO_SNOW -> cell.setStyle("-fx-background-color: white; -fx-border-color: black;");
                    case SNOW -> imageView.setImage(snowImage);
                    case BLOCK -> imageView.setImage(blockImage);
                    case SNOWMAN -> imageView.setImage(snowmanImage);
                }
            }
        }

        cell.setGraphic(imageView);
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
}