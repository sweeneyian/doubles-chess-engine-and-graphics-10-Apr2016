package com.DoublesChess.engine.pieces;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.BoardUtils;
import com.DoublesChess.engine.board.Move;

import java.awt.*;
import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cashedHashCode;

    public double mouseX, mouseY;
    public double oldX, oldY;

    Piece(final PieceType pieceType,
          final int piecePosition,
          final Alliance pieceAlliance,
          final boolean isFirstMove){
          //final Image img, int x, int y){

        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
        this.cashedHashCode = computeHashCode();
        this.isFirstMove = isFirstMove;

    }

    protected int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31* result+pieceAlliance.hashCode();
        result = 31* result+piecePosition;
        result = 31* result+(isFirstMove? 1:0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;;
        return pieceAlliance == otherPiece.getPieceAlliance()&&
               pieceType == otherPiece.getPieceType() &&
               piecePosition == otherPiece.getPiecePosition()&&
               isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode(){
    return this.computeHashCode();
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType{
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return true;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        };

        private String pieceName;
        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
        public abstract boolean isPawn();
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }

}
