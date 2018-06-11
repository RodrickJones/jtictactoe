package com.rodrickgjones.jtictactoe.player.ai;

import com.rodrickgjones.jtictactoe.game.Board;
import com.rodrickgjones.jtictactoe.player.Player;
import com.rodrickgjones.jtictactoe.util.Random;

import java.util.*;

public class MinimaxAi extends ArtificialIntelligence {
    private final static Comparator<Node> SCORE_COMPARATOR = Comparator.comparingInt(Node::getScore);
    private final static boolean USE_OLD = false;
    private final int depth;
    public MinimaxAi(Symbol symbol, int depth) {
        super(symbol);
        this.depth = depth;
    }

    @Override
    public Board.Square chooseSquare(Board board) {
        long start = System.nanoTime();
        Board.Square res;
        if (USE_OLD) {
            res = oldMinimax(board.copy(), this).getKey();
        } else {
            Node root = new Node(board.copy(), null, null);
            constructTree(root, depth);
            res = minimax(root).getSquare();
        }
        System.out.println(System.nanoTime() - start);
        return res;
    }

    private Map.Entry<Board.Square, Integer> oldMinimax(Board board, Player currentPlayer) {
        List<Board.Square> squares = board.getUntakenSquares();
        Map<Board.Square, Integer> scores = new HashMap<>(squares.size());
        int scoreHeuristic = board.getUntakenSquareCount() + 1;
        for (Board.Square square : squares) {
            board.captureSquare(square, currentPlayer);
            Player winner = board.getWinner();
            if (winner == null) {
                scores.put(square, board.isFull() ? 0 : oldMinimax(board, currentPlayer == board.getPlayer1() ? board.getPlayer2() : board.getPlayer1()).getValue());
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

    private Node minimax(Node node) {
        if (node.isTerminal()) {
            return node;
        } else if (node.getPlayer() == this) {
            return node.getChildren().stream().max(SCORE_COMPARATOR).orElseThrow(NoSuchElementException::new);
        } else {
            return node.getChildren().stream().min(SCORE_COMPARATOR).orElseThrow(NoSuchElementException::new);
        }
    }

    private void constructTree(Node current, int depth) {
        if (depth == 0) {
            return;
        }
        Board board = current.getBoard();
        Player childPlayer = current.getPlayer() == board.getPlayer1() ? board.getPlayer2() : board.getPlayer1();
        board.captureSquare(current.getSquare(), current.getPlayer());
        List<Board.Square> untakenSquares = board.getUntakenSquares();
        current.evaluate(current.getPlayer() == this);
        if (!current.isTerminal()) {
            for (Board.Square square : untakenSquares) {
                Node child = new Node(board, square, childPlayer);
                current.getChildren().add(child);
                constructTree(child, depth - 1);
            }
        }
        board.releaseSquare(current.getSquare());
    }

    private class Node {
        private final Board board;
        private final Board.Square square;
        private final Player player;
        private final List<Node> children;
        private int score;
        private boolean terminal;

        private Node(Board board, Board.Square square, Player player) {
            this.board = board;
            this.square = square;
            this.player = player;
            this.children = new ArrayList<>(board.getUntakenSquareCount() - 1);
        }

        private void evaluate(boolean maximizingPlayer) {
            Player winner = board.getWinner();
            int untakenSquareCount = board.getUntakenSquareCount();
            if (winner == null) {
                if (untakenSquareCount == 0) {
                    terminal = true;
                    score = 0;
                }
            } else if (maximizingPlayer) {
                terminal = true;
                score = untakenSquareCount + 1;
            } else {
                terminal = true;
                score =  -(untakenSquareCount + 1);
            }
        }

        private Board getBoard() {
            return board;
        }

        private Board.Square getSquare() {
            return square;
        }

        private Player getPlayer() {
            return player;
        }

        private int getScore() {
            return score;
        }

        private boolean isTerminal() {
            return terminal;
        }

        private List<Node> getChildren() {
            return children;
        }
    }

}
