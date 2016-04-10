package com.DoublesChess.engine.pieces;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.BoardUtils;
import com.DoublesChess.engine.board.Move;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.DoublesChess.engine.board.Move.*;

/**
 * Created by Owner on 08/02/2016.
 */
public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVES_VECTOR_COORDINATES= {7,8,9,16};
    // white and black pawns have direction taken into account for candidate moves

    //constructor
    public Pawn(final int piecePosition,
                final Alliance pieceAlliance) {
        super(PieceType.PAWN,piecePosition, pieceAlliance, true);
    }

    public Pawn(final int piecePosition,
                final Alliance pieceAlliance,
                final boolean isFirstMove) {
        super(PieceType.PAWN,piecePosition, pieceAlliance, isFirstMove);
    }

    //new ImageIcon(ImageIO.read(new File(defaultPieceImagePath + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0,1)+
    //board.getTile(this.tileID).getPiece().toString()+".gif"))

    @Override
    public Collection<Move> calculateLegalMoves(Board board)
    {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVES_VECTOR_COORDINATES )
        {
            final int candidateDestinationCoordinate = this.piecePosition + this.getPieceAlliance().getDirection() * currentCandidateOffset;


            if (!BoardUtils.isaValidTileCoordinate(currentCandidateOffset)) {
                continue;
                // 0 < coordiate is on the board <64
            }

                // non attacking
                if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    // to do more work here
                    legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));

                }

                // pawn jump 2
                else if (currentCandidateOffset == 16 && this.isFirstMove() ){
                    final int behindCandiateDestinationCoordiinate = this.piecePosition + this.pieceAlliance.getDirection() * 8;
                    if (!board.getTile(behindCandiateDestinationCoordiinate).isTileOccupied() &&
                            !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        legalMoves.add(new PawnJumpMove(board, this, candidateDestinationCoordinate));

                        // if first move and no pieces in front pawn can move 2 forward (16 in array);
                    }
                }

            // attacking moves
            // and not in an eliminate edge of board cases
            else if ((currentCandidateOffset == 7) &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))
            {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied())
                {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    //to do more here for capturing onto final row promotion
                    legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                }
            }
            else if ((currentCandidateOffset == 9) &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))
            {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied())
                {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    //to do more here for capturing onto final row promotion
                    legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));

                }
            }

            //some enpassant stuff here
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}

