package com.rodrickgjones.jtictactoe.javafx;

import com.rodrickgjones.jtictactoe.player.JavaFxPlayer;
import com.rodrickgjones.jtictactoe.player.Player;
import com.rodrickgjones.jtictactoe.player.ai.ArtificialIntelligence;
import com.rodrickgjones.jtictactoe.player.ai.MaxWinsAi;
import com.rodrickgjones.jtictactoe.player.ai.MinimaxAi;
import com.rodrickgjones.jtictactoe.game.Board;
import com.rodrickgjones.jtictactoe.player.ai.RandomAi;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label messageLabel;
    @FXML
    private GridPane buttonGrid;

    private final static Font BUTTON_FONT = new Font(24);
    private Player player1 = new JavaFxPlayer(Player.Symbol.X);
    private Player player2 = new MinimaxAi(Player.Symbol.O);
    private final Board board = new Board(player1, player2);
    private final Button[][] buttons = new Button[3][3];
    private Player currentPlayer = player1;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board.addListener(square -> buttons[square.getColumn()][square.getRow()]
                .setText(square.getOwner().getSymbol().toString()));
        board.addListener(square -> {
            Player winner = board.getWinner();
            if (winner != null) {
                Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
                winAlert.setTitle("Game Over!");
                winAlert.setHeaderText("Game Over!");
                winAlert.setContentText(winner + " wins!");
                winAlert.showAndWait();
                reset();
            } else if (board.isFull()) {
                Alert drawAlert = new Alert(Alert.AlertType.INFORMATION);
                drawAlert.setTitle("Game Over!");
                drawAlert.setHeaderText("Game Over!");
                drawAlert.setContentText("Nobody wins! Game ends in a draw!");
                drawAlert.showAndWait();
                reset();
            }
        });
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++) {
                Button square = new Button();
                buttons[c][r] = square;
                buttonGrid.add(square, c, r);
                square.setFont(BUTTON_FONT);
                square.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                int finalC = c;
                int finalR = r;
                square.setOnAction(e -> {
                    if (currentPlayer instanceof JavaFxPlayer && board.captureSquare(finalC, finalR, currentPlayer)) {
                        togglePlayer();
                    }
                });
            }
        }
    }

    public void startGame() {
        if (getCurrentPlayer() instanceof ArtificialIntelligence) {
            Board.Square square = getCurrentPlayer().chooseSquare(getBoard());
            getBoard().captureSquare(square, getCurrentPlayer());
        }
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void togglePlayer() {
        if (board.isFull()) {
            return;
        }
        currentPlayer = currentPlayer == player1 ? player2 : player1;
        messageLabel.setText(currentPlayer + "'s turn");
        if (currentPlayer instanceof ArtificialIntelligence) {
            Board.Square square = currentPlayer.chooseSquare(board);
            board.captureSquare(square.getColumn(), square.getRow(), currentPlayer);
            togglePlayer();
        }
    }

    private void reset() {
        board.reset();
        for (Button[] arr : buttons) {
            for (Button button : arr) {
                button.setText(null);
            }
        }
    }
}
