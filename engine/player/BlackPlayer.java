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


public class BlackPlayer extends Player {
    public BlackPlayer (final Board board,
                        final Collection<Move> whiteStandardLegalMoves,
                        final Collection<Move> blackStandardLegalMoves){
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);

    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastle(final Collection playerLegals,
                                                   final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove()&&!this.isInCheck()){
            //black kingsidecastle
            if(!this.board.getTile(6).isTileOccupied()&&
                    !this.board.getTile(5).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {

                        kingCastles.add(new KingSideCastleMove(this.board,
                                                                    this.playerKing,
                                                                    6,
                                                                    (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                    5));
                    }
                }
            }
            if(!this.board.getTile(1).isTileOccupied()&&
                    !this.board.getTile(2).isTileOccupied()&&
                    !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() &&
                        rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {


                        kingCastles.add(new QueenSideCastleMove(this.board,
                                                                    this.playerKing,
                                                                    2,
                                                                    (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                    3));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
