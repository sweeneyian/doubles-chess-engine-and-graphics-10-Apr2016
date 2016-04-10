package com.DoublesChess.engine.pieces;

import com.DoublesChess.engine.board.BoardUtils;
import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.Move;
import com.DoublesChess.engine.board.Move.MajorMove;
import com.DoublesChess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.DoublesChess.engine.board.Move.*;


public class Knight extends Piece {

    private static final int[] CANDIDATE_MOVES_VECTOR_COORDINATES= {-17, -15, -10, -6, 6, 10, 15, 17};

    //constructor
    public Knight(final int piecePosition,
                  final Alliance pieceAlliance) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance, true);
    }

    public Knight(final int piecePosition,
                  final Alliance pieceAlliance,
                  final boolean isFirstMove) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        int candidateDestinationCoordinate =0 ;
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVES_VECTOR_COORDINATES){
            candidateDestinationCoordinate = this.piecePosition+currentCandidateOffset;
            if(BoardUtils.isaValidTileCoordinate(candidateDestinationCoordinate)){
                if((isFirstColumnExclusion(this.piecePosition, currentCandidateOffset))
                        ||(isSecondColumnExclusion(this.piecePosition, currentCandidateOffset))
                        ||(isSeventColumnExcelusion(this.piecePosition,currentCandidateOffset))
                        ||(isEighthColumnExclusion(this.piecePosition,currentCandidateOffset))) {
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied())
                {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

                }
                else
                {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance)
                    {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    //colums exception
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&((candidateOffset==-17)||(candidateOffset==-10)||
                (candidateOffset==6)||(candidateOffset==15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition]&&((candidateOffset==-10)||(candidateOffset==6));
    }

    private static boolean isSeventColumnExcelusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition]&&((candidateOffset==-6)||(candidateOffset==10));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == 17) || (candidateOffset == 10) ||
                (candidateOffset == -6) || (candidateOffset == -15));
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }
}