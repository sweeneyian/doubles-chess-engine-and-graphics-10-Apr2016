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

public class Bishop extends Piece {

    private static final int[] CANDIDATE_MOVES_VECTOR_COORDINATES = {-9,-7,7,9};

    public Bishop(final int piecePosition,
                   final Alliance pieceAlliance) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance, true);
    }

    public Bishop(final int piecePosition,
                  final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance, isFirstMove);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset:CANDIDATE_MOVES_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;

            //while candidate destination is not above or below the board
            while (BoardUtils.isaValidTileCoordinate(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEigthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
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
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    public boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset == -9 ||candidateOffset== 7);
    }

    public boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset == 9 ||candidateOffset== -7);
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }
}
