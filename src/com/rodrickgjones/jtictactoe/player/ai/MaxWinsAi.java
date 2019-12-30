package com.rodrickgjones.jtictactoe.player.ai;

import com.rodrickgjones.jtictactoe.game.Board;
import com.rodrickgjones.jtictactoe.player.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaxWinsAi extends ArtificialIntelligence {
    public MaxWinsAi(Symbol symbol) {
        super(symbol);
    }

    @Override
    public Board.Square chooseSquare(Board board) {
        List<Board.Square> squares = board.getUntakenSquares();
        Map<Board.Square, Integer> winScores = new HashMap<>(squares.size());
        Board boardCopy = board.copy();
        squares.forEach(square -> winScores.put(square, generateWinScore(boardCopy, square, this)));
        return winScores.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);
    }

    private int generateWinScore(Board board, Board.Square square, Player currentPlayer) {
        int score = 0;
        board.captureSquare(square.getColumn(), square.getRow(), currentPlayer);
        Player winner = board.getWinner();
        List<Board.Square> squares = board.getUntakenSquares();
        int scoreHeuristic = squares.size() + 1;
        if (winner == null) {
            for (Board.Square nextSquare : squares) {
                score += generateWinScore(board, nextSquare,
                        currentPlayer == board.getPlayer1() ? board.getPlayer2() : board.getPlayer1());
            }
        } else if (winner == this) {
            score = scoreHeuristic;
        } else {
            score = -scoreHeuristic;
        }
        board.captureSquare(square, null);
        return score;
    }
}
