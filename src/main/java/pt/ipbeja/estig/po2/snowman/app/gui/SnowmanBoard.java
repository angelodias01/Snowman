package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import pt.ipbeja.estig.po2.snowman.app.model.BoardModel;
import pt.ipbeja.estig.po2.snowman.app.model.PositionContent;
import pt.ipbeja.estig.po2.snowman.app.model.View;

public class SnowmanBoard extends GridPane implements View {

    private BoardModel boardModel;

    // Constructor that initializes the SnowmanBoard with the board model
    public SnowmanBoard(BoardModel boardModel) {
        this.boardModel = boardModel;
        drawBoard();
    }

    /**
     * Method responsible for drawing the board, using PositionContent to determine grid contents
     */
    private void drawBoard() {
        this.getChildren().clear();
        for (int row = 0; row < boardModel.getRows(); row++) {
            for (int col = 0; col < boardModel.getCols(); col++) {
                Label label = new Label();
                PositionContent content = boardModel.getPositionContent(row, col);
                label.setMinSize(50, 50);
                label.setStyle("-fx-border-color: black; -fx-alignment: center;");

                // Set label text or background based on PositionContent
                switch (content) {
                    case NO_SNOW -> label.setStyle("-fx-background-color: white; -fx-border-color: black;");
                    case SNOW -> label.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
                    case BLOCK -> label.setStyle("-fx-background-color: grey; -fx-border-color: black;");
                    case SNOWMAN -> label.setStyle("-fx-background-color: orange; -fx-border-color: black;");
                }
                this.add(label, col, row);
            }
        }
    }

    /**
     * Updates the board view whenever the board state changes
     */
    public void updateBoard() {
        drawBoard();
    }
}