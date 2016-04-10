package com.DoublesChess.engine.player;

import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.Move;


public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard,
                          final Move move,
                          final MoveStatus moveStatus){
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMovesStatus() {

    return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return transitionBoard;
    }
}
