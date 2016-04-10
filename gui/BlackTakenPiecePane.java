package com.DoublesChess.gui;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.pieces.Piece;

/**
 * Created by Owner on 08/04/2016.
 */
public class BlackTakenPiecePane extends SingleTakenPiecesPane {

    BlackTakenPiecePane(Alliance takenPiecesAlliance, GUIUtils.GameType gameType) {
        super(Alliance.BLACK, gameType);
        if (takenPiecesAlliance != Alliance.BLACK) {
            System.out.println("Cannot create Black TakenPiecePane for White Pieces");
        }
    }

    @Override
    public void addTakenPiece(Piece piece) {
        switch (piece.getPieceType()) {
            case PAWN:
                takenPawnImage.addTakenPieceToTakenPieceImage();
                break;
            case QUEEN:
                takenQueenImage.addTakenPieceToTakenPieceImage();
                break;
            case ROOK:
                takenRookImage.addTakenPieceToTakenPieceImage();
                break;
            case KNIGHT:
                takenKnightImage.addTakenPieceToTakenPieceImage();
                break;
            case BISHOP:
                takenBishopImage.addTakenPieceToTakenPieceImage();
                break;
            default:
                System.out.println("dont know how we got there");
                break;
        }
    }
}
