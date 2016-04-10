package com.DoublesChess.engine.player;


import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.Move;
import com.DoublesChess.engine.pieces.King;
import com.DoublesChess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastle(legalMoves,opponentMoves)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves ).isEmpty(); // beautiful line
        if (this.isInCheck()){
            System.out.println("CHECK");
        }

    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentMoves) {
    final List<Move> attackMoves = new ArrayList<>();

        for (final Move move: opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add( move );
            }
        }
    return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing(){
        for (final Piece piece : getActivePieces()){
            if (piece.getPieceType().isKing() ){
                return (King)piece;
            }
        }
        throw new RuntimeException("Should not reach here. Not a valid board without king");
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
    return this.isInCheck && !hasEscapeMoves();
    }

    public boolean inInStaleMate(){
        return !isInCheck&&!hasEscapeMoves();
    }

    protected boolean hasEscapeMoves(){
        for (final Move move: this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if (transition.getMovesStatus().isDone()){
                 return true;
            }
        }
        return false;
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){

        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();

        // making a move, takes a move, calculates if it exposes moving players king to check. On transitionBoard, the opponents king would be the
        //person who is making the move. Calculate moves by opponent on that transition board on our king. Should not have any.
        final Collection<Move> kingAttacked = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());
        //System.out.println("Im here");
        //this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves ).isEmpty(); //

        if (!kingAttacked.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();


    public King getPlayerKing() {
        return playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    protected abstract Collection<Move> calculateKingCastle(Collection playerLegals, Collection<Move> opponentLegals);
}
