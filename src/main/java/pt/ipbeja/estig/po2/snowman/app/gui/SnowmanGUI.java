package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pt.ipbeja.estig.po2.snowman.app.model.BoardModel;
import pt.ipbeja.estig.po2.snowman.app.model.Direction;
import pt.ipbeja.estig.po2.snowman.app.model.Monster;
import pt.ipbeja.estig.po2.snowman.app.model.PositionContent;
import pt.ipbeja.estig.po2.snowman.app.model.Snowball;

import java.util.ArrayList;
import java.util.List;

public class SnowmanGUI extends Application {

    @Override
    public void start(Stage stage) {
        // Create the board model with an example configuration (e.g., 5x5 board)
        List<List<PositionContent>> grid = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(PositionContent.NO_SNOW);
            }
            grid.add(row);
        }
        grid.get(2).set(2, PositionContent.SNOW);
        Monster monster = new Monster(1, 1);
        List<Snowball> snowballs = new ArrayList<>();
        snowballs.add(new Snowball(2, 3, pt.ipbeja.estig.po2.snowman.app.model.SnowballType.SMALL));
        BoardModel boardModel = new BoardModel(grid, monster, snowballs);

        // Create and initialize the SnowmanBoard
        SnowmanBoard snowmanBoard = new SnowmanBoard(boardModel);

        // Create the root pane
        BorderPane root = new BorderPane();
        root.setCenter(snowmanBoard); // Add the board to the center of the layout

        // Example: Trigger updates (e.g., move monster or snowballs)
        boardModel.moveMonster(Direction.RIGHT);
        snowmanBoard.updateBoard(); // Refresh the display

        // Setup the scene and stage
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Snowman Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}