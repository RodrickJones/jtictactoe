package com.rodrickgjones.jtictactoe;

import com.rodrickgjones.jtictactoe.game.Board;
import com.rodrickgjones.jtictactoe.javafx.Controller;
import com.rodrickgjones.jtictactoe.player.JavaFxPlayer;
import com.rodrickgjones.jtictactoe.player.Player;
import com.rodrickgjones.jtictactoe.player.ai.MaxWinsAi;
import com.rodrickgjones.jtictactoe.player.ai.MinimaxAi;
import com.rodrickgjones.jtictactoe.player.ai.RandomAi;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Map<String, String> params = getParameters().getNamed();
        Player opponent;
        switch (params.getOrDefault("difficulty", "hard").toLowerCase()) {
            case "easy":
                opponent = new RandomAi(Player.Symbol.O);
                break;
            case "medium":
                opponent = new MaxWinsAi(Player.Symbol.O);
                break;
            case "hard":
                opponent = new MinimaxAi(Player.Symbol.O, 3);
                break;
            case "impossible":
                opponent = new MinimaxAi(Player.Symbol.O, -1);
                break;
            default:
                throw new IllegalArgumentException(params.get("difficulty") + " is not a valid difficulty");
        }
        Board board = new Board(new JavaFxPlayer(Player.Symbol.X), opponent);
        Controller controller = new Controller(board);
        loader.setController(controller);
        Parent root = loader.load(getClass().getResourceAsStream("/com/rodrickgjones/jtictactoe/javafx/tictactoe.fxml"));
        primaryStage.setTitle("jTicTacToe");
        Scene scene = new Scene(root);
        switch (params.getOrDefault("theme", "light")) {
            case "dark":
                scene.getStylesheets().add("/com/rodrickgjones/jtictactoe/javafx/dark.css");
                break;
            default:
        }
        primaryStage.setScene(scene);
        primaryStage.show();
        controller.startGame();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
