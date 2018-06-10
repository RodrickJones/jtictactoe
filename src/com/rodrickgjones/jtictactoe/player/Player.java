package com.rodrickgjones.jtictactoe.player;

import com.rodrickgjones.jtictactoe.game.Board;

public abstract class Player {
    private final Symbol symbol;
    protected Player(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public abstract Board.Square chooseSquare(Board board);

    @Override
    public String toString() {
        return symbol.toString();
    }

    public enum Symbol {
        X, O
    }
}
