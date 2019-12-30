package com.rodrickgjones.jtictactoe.player.ai;

import com.rodrickgjones.jtictactoe.game.Board;
import com.rodrickgjones.jtictactoe.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MinimaxAi extends ArtificialIntelligence {
    private final static Comparator<Node> SCORE_COMPARATOR = Comparator.comparingInt(Node::getScore);
    private final int depth;

    public MinimaxAi(Symbol symbol, int depth) {
        super(symbol);
        this.depth = depth;
    }

    @Override
    public Board.Square chooseSquare(Board board) {
        Node root = new Node(board.copy(), null, null);
        constructTree(root, depth);
        return minimax(root).getSquare();
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

    private static class Node {
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
                score = -(untakenSquareCount + 1);
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
