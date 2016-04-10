package com.DoublesChess.engine.player;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.Move;
import com.DoublesChess.engine.board.Move.KingSideCastleMove;
import com.DoublesChess.engine.board.Move.QueenSideCastleMove;
import com.DoublesChess.engine.board.Tile;
import com.DoublesChess.engine.pieces.Piece;
import com.DoublesChess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer (final Board board,
                        final Collection<Move> whiteStandardLegalMoves,
                        final Collection<Move> blackStandardLegalMoves){

        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastle(final Collection playerLegals,
                                                   final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove()&&!this.isInCheck()){
            //white kingsidecastle
            if(!this.board.getTile(61).isTileOccupied()&&
                    !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        //todo add a castlemove instead of null
                        /*
                         constructor for CastleMove(final Board board,
                           final Piece movedPiece,
                           final int destinationCoordinate,
                           final Rook castleRook,
                           final int castleRookStart,
                           final int castleRookDestination)
                         */
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                                this.playerKing,
                                                                62,
                                                                (Rook)rookTile.getPiece(),
                                                                rookTile.getTileCoordinate(),
                                                                61));
                    }
                }
            }
            if(!this.board.getTile(59).isTileOccupied()&&
                    !this.board.getTile(58).isTileOccupied()&&
                    !this.board.getTile(57).isTileOccupied()){
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() &&
                        rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {

                        //todo add a castlemove instead of null
                        kingCastles.add(new QueenSideCastleMove(this.board,
                                                                this.playerKing,
                                                                58,
                                                                (Rook)rookTile.getPiece(),
                                                                rookTile.getTileCoordinate(),
                                                                59));
                    }
                }
             }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
