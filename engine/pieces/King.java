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


public class King extends Piece {
    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES= {-9,-8,-7,-1,1,7,8,9};

    //constructor
    public King(final int piecePosition,
                 final Alliance pieceAlliance,
                 final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
    }
    public King(final int piecePosition,
                final Alliance pieceAlliance) {
        super(PieceType.KING, piecePosition, pieceAlliance, true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset:CANDIDATE_MOVES_VECTOR_COORDINATES )
        {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if((isFirstColumnExclusion(this.piecePosition, currentCandidateOffset))
                ||(isEighthColumnExclusion(this.piecePosition,currentCandidateOffset)))
            {
                continue;
            }

            if (BoardUtils.isaValidTileCoordinate(candidateDestinationCoordinate))
            {
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
    public King movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    private boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset)
    {
        return BoardUtils.FIRST_COLUMN[currentPosition]&&((candidateOffset==-1)||(candidateOffset==-9)||
                (candidateOffset==7));
    }

    private boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset)
    {
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&((candidateOffset==1)||(candidateOffset==9)||
                (candidateOffset==-7));
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
}
