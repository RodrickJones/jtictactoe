package com.rodrickgjones.jtictactoe;

import com.rodrickgjones.jtictactoe.javafx.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Controller controller = new Controller();
        loader.setController(controller);
        Parent root = loader.load(getClass().getResourceAsStream("javafx/tictactoe.fxml"));
        primaryStage.setTitle("jTicTacToe");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        controller.startGame();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
