package com.rodrickgjones.jtictactoe.game;

import com.rodrickgjones.jtictactoe.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Square[][] squares = new Square[3][3];
    private final Player player1;
    private final Player player2;
    private List<CaptureListener> listeners = new ArrayList<>();
    private int untakenSquareCount = 9;
    public Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        for (int c = 0; c < squares.length; c++) {
            for (int r = 0; r < squares[0].length; r++) {
                squares[c][r] = new Square(c, r);
            }
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Square getSquare(int column, int row) {
        return squares[column][row];
    }

    public void reset() {
        for (Square[] column : squares) {
            for (Square square : column) {
                square.reset();
            }
        }
        untakenSquareCount = 9;
    }

    public Board copy() {
        Board copy = new Board(player1, player2);
        for (int c = 0; c < squares.length; c++) {
            for (int r = 0; r < squares[0].length; r++) {
                copy.squares[c][r].setOwner(squares[c][r].getOwner());
            }
        }
        copy.untakenSquareCount = untakenSquareCount;
        return copy;
    }

    public boolean captureSquare(Square square, Player owner) {
        if (square == null) {
            return false;
        } else if (owner == null) {
            return releaseSquare(square);
        } else if (!square.isTaken()) {
            square.setOwner(owner);
            --untakenSquareCount;
            listeners.forEach(l -> l.onCapture(square));
            return true;
        }
        return false;
    }

    public boolean releaseSquare(Square square) {
        if (square == null || !square.isTaken()) {
            return false;
        }
        square.setOwner(null);
        untakenSquareCount++;
        listeners.forEach(l -> l.onCapture(square));
        return true;
    }

    public boolean captureSquare(int column, int row, Player owner) {
        return captureSquare(getSquare(column, row), owner);
    }

    public Player getWinner() {
        //check columns
        for (int c = 0; c < squares.length; c++) {
            Square square = squares[c][0];
            if (!square.isTaken()) continue;
            Player owner = square.getOwner();
            if (owner.equals(squares[c][1].getOwner()) && owner.equals(squares[c][2].getOwner())) {
                return owner;
            }
        }
        //check rows
        for (int r = 0; r < squares.length; r++) {
            Square square = squares[0][r];
            if (!square.isTaken()) continue;
            Player owner = square.getOwner();
            if (owner.equals(squares[1][r].getOwner()) && owner.equals(squares[2][r].getOwner())) {
                return owner;
            }
        }
        //check diagonals
        Square square = squares[1][1];
        if (!square.isTaken()) return null;
        Player owner = squares[1][1].getOwner();
        if (owner.equals(squares[0][0].getOwner()) && owner.equals(squares[2][2].getOwner())
                || owner.equals(squares[0][2].getOwner()) && owner.equals(squares[2][0].getOwner())) {
            return owner;
        }
        return null;
    }

    public boolean isFull() {
        return untakenSquareCount == 0;
    }

    public List<Square> getUntakenSquares() {
        List<Square> res = new ArrayList<>();
        for (Square[] array : squares) {
            for (Square square : array) {
                if (!square.isTaken()) {
                    res.add(square);
                }
            }
        }
        return res;
    }

    public int getUntakenSquareCount() {
        return untakenSquareCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < 3; r++) {
            builder.append("+-|-|-+\n");
            for (int c = 0; c < 3; c++) {
                builder.append('|');
                builder.append(squares[c][r].getOwnerSymbol());
            }
            builder.append("|\n");
        }
        builder.append("+-|-|-+");
        return builder.toString();
    }

    public void addListener(CaptureListener listener) {
        listeners.add(listener);
    }

    public class Square {
        private final int row;
        private final int column;
        private Player owner = null;
        Square(int column, int row) {
            this.column = column;
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }

        public Player getOwner() {
            return owner;
        }

        private void setOwner(Player owner) {
            this.owner = owner;
        }

        public String getOwnerSymbol() {
            return owner == null ? " " : owner.getSymbol().toString();
        }

        public boolean isTaken() {
            return owner != null;
        }

        private void reset() {
            this.owner = null;
        }

        @Override
        public int hashCode() {
            return column * 9 + row;
        }

        @Override
        public String toString() {
            return "Square(" + column + ", " + row + ", " + getOwnerSymbol() + ")";
        }
    }

    @FunctionalInterface
    public interface CaptureListener {
        void onCapture(Square capturedSquare);
    }
}
