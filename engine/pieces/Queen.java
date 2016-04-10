package com.DoublesChess.engine.pieces;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.BoardUtils;
import com.DoublesChess.engine.board.Move;
import com.DoublesChess.engine.board.Move.AttackMove;
import com.DoublesChess.engine.board.Move.MajorMove;
import com.DoublesChess.engine.board.Tile;
import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece{

    private static final int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-9,-8,-7,-1,1,7,8,9};

    public Queen(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance, true);
    }

    public Queen(final int piecePosition,
                 final Alliance pieceAlliance,
                 final boolean isFirstMove) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCordinateOffset:CANDIDATE_MOVES_VECTOR_COORDINATES) {
            //System.out.println("Start of candidateCordinateOffset forloop: "+candidateCordinateOffset);
            int candidateDestinationCoordinate = this.piecePosition;

            //0 < while candidate destination <63
            while (BoardUtils.isaValidTileCoordinate(candidateDestinationCoordinate)) {


                if (isFirstColumnExclusion(candidateDestinationCoordinate , candidateCordinateOffset) ||
                        isEighthColumnExclusion( candidateDestinationCoordinate, candidateCordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate += candidateCordinateOffset;
                if (BoardUtils.isaValidTileCoordinate(candidateDestinationCoordinate)) {
                    //check for if there is a piece and if its ours

                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    //if candidate destination has no piece
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    //else if it does have a piece and if its in not in our alliance
                    else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                        }
                        break;
                        // break. stop searchin past once we see a piece. we cant jump over it
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    public boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset == -1 || candidateOffset == -9 ||candidateOffset == 7);
    }

    public boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset == 1 ||candidateOffset == 9 || candidateOffset == -7);
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }
}
