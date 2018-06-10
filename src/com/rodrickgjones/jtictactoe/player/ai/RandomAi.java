package com.rodrickgjones.jtictactoe.player.ai;

import com.rodrickgjones.jtictactoe.game.Board;
import com.rodrickgjones.jtictactoe.util.Random;

public class RandomAi extends ArtificialIntelligence {
    public RandomAi(Symbol symbol) {
        super(symbol);
    }

    @Override
    public Board.Square chooseSquare(Board board) {
        return Random.nextElement(board.getUntakenSquares());
    }
}
