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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

public class SnowmanBoard extends VBox implements View {
    private final Consumer<Void> onLevelComplete;
    private final Button resetButton;
    private BoardModel boardModel;
    private final GridPane board;
    private final TextArea movementsLog;

    // Imagens para os elementos do jogo
    private final Image snowImage = new Image(getClass().getResourceAsStream("/images/snow.png"));
    private final Image blockImage = new Image(getClass().getResourceAsStream("/images/block.png"));
    private final Image snowmanImage = new Image(getClass().getResourceAsStream("/images/snowman.png"));
    private final Image monsterImage = new Image(getClass().getResourceAsStream("/images/monster.png"));

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

        // Criar uma HBox para os controles
        HBox controls = new HBox(10); // 10 pixels de espaçamento
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().add(resetButton);

        setupBoard();
        this.getChildren().addAll(board, controls, movementsLog);

        // Adicionar handler de teclado
        this.setOnKeyPressed(this::handleKeyPress);
        this.setFocusTraversable(true);
    }

    public void loadNewLevel(BoardModel newBoard) {
        this.boardModel = newBoard;
        this.movementsLog.clear();
        updateBoard();
        this.requestFocus();
    }

    /**
     * Reinicia o nível atual
     */
    private void resetLevel() {
        // Mostrar diálogo de confirmação
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reiniciar Nível");
        alert.setHeaderText("Tem certeza que deseja reiniciar o nível?");
        alert.setContentText("Todo o progresso será perdido.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Reiniciar o nível
            boardModel.resetLevel();
            // Limpar o log de movimentos
            movementsLog.clear();
            movementsLog.appendText("Nível reiniciado\n");
            // Atualizar a visualização
            updateBoard();
            // Retomar o foco para capturar eventos de teclado
            this.requestFocus();
        }
    }

    // Adicionar também ao handleKeyPress para permitir reiniciar com tecla R
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.R) {
            resetLevel();
            event.consume();
            return;
        }

        // Verificar primeiro se é CTRL+Z para undo
        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            if (boardModel.undo()) {
                movementsLog.appendText("Movimento desfeito\n");
                updateBoard();
            }
            event.consume();
            return;
        }

        // Código existente para movimentação
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
        if (boardModel.isLevelComplete()) {
            onLevelComplete.accept(null);
        }
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
                    case COMPLETE -> imageView.setImage(snowmanImage);
                }
            } else {
                // Verificar outros conteúdos
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

    private void handleCellClick(int row, int col) {
        // Registrar movimento no log
        String move = String.format("(%d, %c) -> (%d, %c)",
            row + 1, (char)('A' + col), row + 1, (char)('A' + col));
        movementsLog.appendText(move + "\n");

        // A verificação de fim de jogo já é feita no updateBoard()
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