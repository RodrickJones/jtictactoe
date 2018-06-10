package com.rodrickgjones.jtictactoe.player.ai;

import com.rodrickgjones.jtictactoe.game.Board;
import com.rodrickgjones.jtictactoe.player.Player;
import com.rodrickgjones.jtictactoe.util.Random;

import java.util.*;

public class MinimaxAi extends ArtificialIntelligence {
    public MinimaxAi(Symbol symbol) {
        super(symbol);
    }

    @Override
    public Board.Square chooseSquare(Board board) {
        return minimax(board.copy(), this).getKey();
    }

    private Map.Entry<Board.Square, Integer> minimax(Board board, Player currentPlayer) {
        List<Board.Square> squares = board.getUntakenSquares();
        Map<Board.Square, Integer> scores = new HashMap<>(squares.size());
        int scoreHeuristic = squares.size() + 1;
        for (Board.Square square : squares) {
            board.captureSquare(square, currentPlayer);
            Player winner = board.getWinner();
            if (winner == null) {
                scores.put(square, board.isFull() ? 0 : minimax(board, currentPlayer == board.getPlayer1() ? board.getPlayer2() : board.getPlayer1()).getValue());
            } else if (currentPlayer == this) {
                scores.put(square, scoreHeuristic);
            } else {
                scores.put(square, -scoreHeuristic);
            }
            board.releaseSquare(square);
        }
        List<Map.Entry<Board.Square, Integer>> candidates = new ArrayList<>(9);
        int bestScore;
        if (currentPlayer == this) {
            bestScore = Integer.MIN_VALUE;
            for (Map.Entry<Board.Square, Integer> entry : scores.entrySet()) {
                int score = entry.getValue();
                if (score == bestScore) {
                    candidates.add(entry);
                } else if (entry.getValue() > bestScore) {
                    candidates.clear();
                    candidates.add(entry);
                    bestScore = entry.getValue();
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (Map.Entry<Board.Square, Integer> entry : scores.entrySet()) {
                int score = entry.getValue();
                if (score == bestScore) {
                    candidates.add(entry);
                } else if (entry.getValue() < bestScore) {
                    candidates.clear();
                    candidates.add(entry);
                    bestScore = entry.getValue();
                }
            }
        }
        return Random.nextElement(candidates);
    }

}
