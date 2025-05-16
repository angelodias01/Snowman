package pt.ipbeja.estig.po2.snowman.app.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pt.ipbeja.estig.po2.snowman.app.model.*;

import java.util.ArrayList;
import java.util.List;

public class SnowmanGUI extends Application {

    @Override
    public void start(Stage stage) {
        // Criar um tabuleiro 5x5
        List<List<PositionContent>> grid = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(PositionContent.NO_SNOW);
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
        snowballs.add(new Snowball(1, 2, SnowballType.MID));

        // Criar o modelo do tabuleiro
        BoardModel boardModel = new BoardModel(grid, monster, snowballs);

        // Criar e inicializar o SnowmanBoard
        SnowmanBoard snowmanBoard = new SnowmanBoard(boardModel);

        // Criar o layout raiz
        BorderPane root = new BorderPane();
        root.setCenter(snowmanBoard);

        // Configurar a cena e o palco
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Jogo do Boneco de Neve");

        // Requisitar foco para o tabuleiro
        snowmanBoard.requestFocus();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
