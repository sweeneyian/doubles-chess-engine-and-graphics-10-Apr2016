package com.DoublesChess.engine.board;

import com.DoublesChess.engine.board.Board.Builder;
import com.DoublesChess.engine.pieces.Pawn;
import com.DoublesChess.engine.pieces.Piece;
import com.DoublesChess.engine.pieces.Rook;
import com.DoublesChess.gui.GUIUtils;

import java.util.Collection;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;
    // int originCoordinate; // we will need this for tracking moves
    public static final Move NULL_MOVE = new NullMove();

    private Move (final Board board,
            final Piece movedPiece,
            final int destinationCoordinate)    {
        this.movedPiece = movedPiece;
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;

        //this caused huge problems
        this.isFirstMove = false;
    }

/*    private Move(final Board board, final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }*/

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result =1;

        result = prime*result+this.movedPiece.getPiecePosition();
        result = prime*result+this.movedPiece.hashCode();

        return result;
    }

    @Override
    public  boolean equals(final Object other){
        if(this==other){
            return true;
        } if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return  getDestinationCoordinate()==otherMove.getDestinationCoordinate()&&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute()  {
        final Builder builder = new Builder();
        // get all of the pieces that isnt the moved piece
        //todo hashcode and equals for pieces
        for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        // get all of the opponets pieces
        for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
            // "this" is a move
        }
        //set the piece that was moved
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    /*we need the moved piece, but with its position set to final coordinate
    setPiece needs a piece, which is movedPiece, but we need to call the Move Class method movePiece which changes its
    coordinate to destiination cooordinate.
    movePiece() method not to be confused with movedPiece Piece)
    */

    public static class MajorMove extends Move {

        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            // get all of the pieces that isnt the moved piece
            //todo hashcode and equals for pieces
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            // get all of the opponets pieces
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
                // "this" is a move
            }
            //set the piece that was moved
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class AttackMove extends Move{

        final Piece attackedPiece;

        public AttackMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode()+super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if (this==other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove =(AttackMove)other;
            return super.equals(otherAttackMove)&&getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();

            // get all of the pieces that isnt the moved piece
            for(final Piece piece:  this.board.getCurrentPlayer().getActivePieces()){

                //TODO hashcode and equals for pieces
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            // get all of the opponets pieces less the attacked piece
            for(final Piece piece:  this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if (piece != attackedPiece) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            //return builder with all of the peices
            return builder.build();
        }


        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.getAttackedPiece();
        }
    }

    public static class PawnMove extends MajorMove{

        public PawnMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals (final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movedPiece,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJumpMove extends Move{

        public PawnJumpMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();

            // get all of the pieces that isnt the moved piece
            for(final Piece piece:  this.board.getCurrentPlayer().getActivePieces()){

                //TODO hashcode and equals for pieces
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            // get all of the opponets pieces
            for(final Piece piece:  this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassant(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }



    protected static abstract class CastleMove extends Move{

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        private CastleMove(final Board board,
                           final Piece movedPiece,
                           final int destinationCoordinate,
                           final Rook castleRook,
                           final int castleRookStart,
                           final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove (){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            // get all of the pieces that isnt the moved piece
            for(final Piece piece:  this.board.getCurrentPlayer().getActivePieces()){

                //TODO hashcode and equals for pieces
                // dont add rook or king this time
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            // get all of the opponets pieces
            for(final Piece piece:  this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            // 2 pieces getting moved the same side
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            //return builder with all of the peices
            return builder.build();

        }
    }

    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString(){
            return "0-0";
        }
    }
    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board,
                                    final Piece movedPiece,
                                    final int destinationCoordinate,
                                    final Rook castleRook,
                                    final int castleRookStart,
                                    final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public String toString(){
            return "0-0-0";
        }
    }

    protected static abstract class DoublesMoveFromTable extends MajorMove{

        protected final GUIUtils.GameType gameType;

        private DoublesMoveFromTable(final Board board,
                                     final Piece movedPiece,
                                     final int emptyTileDestination,
                                     final GUIUtils.GameType gameType
        ){
            super(board, movedPiece, emptyTileDestination);
            this.gameType = gameType;
            if(movedPiece.getPiecePosition()!=GUIUtils.OFF_BOARD_POSITION){
                System.out.println("trying to add a piece that is not off the board.");
            }
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            // get all of the pieces that isnt the moved piece
            for(final Piece piece:  this.board.getCurrentPlayer().getActivePieces()){
                builder.setPiece(piece);
            }
            // get all of the opponets pieces
            for(final Piece piece:  this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(new Piece() {
                @Override
                public Collection<Move> calculateLegalMoves(Board board) {
                    return null;
                }

                @Override
                public Piece movePiece(Move move) {
                    return null;
                }
            });
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            //return builder with all of the peices
            return builder.build();

        }

    }

    public static final class NullMove extends Move{

        public NullMove() {
                super(null, null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute the Null Move");
        }
    }

    public static class MoveFactory{

        private MoveFactory(){

            throw new RuntimeException("Not Instansiable");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            //System.out.println("move factory: current coordinate: " + currentCoordinate  +   " destination cooridinate: " + destinationCoordinate);
            for (final Move move : board.getAllLegalMoves() ){

                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.destinationCoordinate== destinationCoordinate) {

                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
