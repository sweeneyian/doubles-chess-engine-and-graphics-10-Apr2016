package com.DoublesChess.gui;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.pieces.*;

import static com.DoublesChess.engine.pieces.Piece.*;
import static com.DoublesChess.engine.pieces.Piece.PieceType.PAWN;

/**
 * Created by Owner on 08/04/2016.
 */
public class WhiteTakenPiecePane extends SingleTakenPiecesPane{

    WhiteTakenPiecePane(Alliance takenPiecesAlliance, GUIUtils.GameType gameType) {
        super(Alliance.WHITE, gameType);
        if (takenPiecesAlliance!=Alliance.WHITE){
            System.out.println("Cannot create White TakenPiecePane for Black Pieces");
        }
    }

    @Override
    public void addTakenPiece(Piece piece) {
        switch (piece.getPieceType()){
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
