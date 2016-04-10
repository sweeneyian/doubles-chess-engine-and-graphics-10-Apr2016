package com.DoublesChess.gui;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.pieces.Piece;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

import static com.DoublesChess.gui.BoardBorderPane.*;
import static com.DoublesChess.gui.GUIUtils.*;

/**
 * Created by Owner on 08/04/2016.
 */
public class TakenPiecesPane extends FlowPane {
    private BoardDirection boardDirection;
    private WhiteTakenPiecePane whiteTakenPiecesPane;
    private BlackTakenPiecePane blackTakenPiecePane;
    private Piece takenPiece;

    TakenPiecesPane(final BoardDirection boardDirection){
        this.boardDirection = boardDirection;
        setMaxSize(TILE_SIZE*2, TILE_SIZE*4);
        this.setPadding(new Insets(0,5,0,5)); //adds 5 spacing border left and right
        this.takenPiece = null;
        whiteTakenPiecesPane = new WhiteTakenPiecePane(Alliance.WHITE, GameType.DOUBLES);
        blackTakenPiecePane = new BlackTakenPiecePane(Alliance.BLACK, GameType.DOUBLES);
        if (boardDirection == BoardDirection.FLIPPED){
            getChildren().addAll(whiteTakenPiecesPane, blackTakenPiecePane);
        }
        else {
            getChildren().addAll(blackTakenPiecePane, whiteTakenPiecesPane);
        }
    }

    public void addPiece(Piece takenPiece) {
        this.takenPiece = takenPiece;
        if (takenPiece.getPieceAlliance() == Alliance.WHITE){
            whiteTakenPiecesPane.addTakenPiece(takenPiece);
        }else {
            blackTakenPiecePane.addTakenPiece(takenPiece);
        }
    }

    void redo(final BoardBorderPane.MoveLog moveLog){
        this.whiteTakenPiecesPane.getChildren().removeAll();
        this.blackTakenPiecePane.getChildren().removeAll();

    }
}
