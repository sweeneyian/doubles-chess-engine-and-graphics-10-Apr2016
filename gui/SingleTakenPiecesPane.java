package com.DoublesChess.gui;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.pieces.*;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.security.auth.kerberos.KerberosKey;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.StreamHandler;

import static com.DoublesChess.gui.GUIUtils.*;

/**
 * Created by Owner on 08/04/2016.
 */
public abstract class SingleTakenPiecesPane extends GridPane{

    private Alliance takenPiecesAlliance;
    private GameType gameType;
    protected TakenQueenImage takenQueenImage;
    protected TakenPawnImage takenPawnImage;
    protected TakenRookImage takenRookImage;
    protected TakenKnightImage takenKnightImage;
    protected TakenBishopImage takenBishopImage;

    SingleTakenPiecesPane(final Alliance takenPiecesAlliance, final GameType gameType){

        this.gameType = gameType;
        setHgap(0);
        setVgap(0);
        this.takenPiecesAlliance = takenPiecesAlliance;
        setMinSize(2*TILE_SIZE,4*TILE_SIZE);
        if (this.takenPiecesAlliance == Alliance.BLACK){
            setStyle(WHITE_COLOUR);
        } else{
            setStyle(GREY_COLOUR);
        }
        this.takenBishopImage = new TakenBishopImage(new Bishop(OFF_BOARD_POSITION, takenPiecesAlliance));
        this.takenKnightImage = new TakenKnightImage(new Knight(OFF_BOARD_POSITION, takenPiecesAlliance));
        this.takenQueenImage = new TakenQueenImage(new Queen(OFF_BOARD_POSITION, takenPiecesAlliance));
        this.takenRookImage = new TakenRookImage(new Rook(OFF_BOARD_POSITION, takenPiecesAlliance));
        this.takenPawnImage = new TakenPawnImage(new Pawn(OFF_BOARD_POSITION, takenPiecesAlliance));

        add(takenPawnImage, 0, 0);
        add(takenKnightImage, 0, 1);
        add(takenBishopImage, 0, 2);
        add(takenRookImage, 0, 3);
        add(takenQueenImage, 1, 0);
    }

    public abstract void addTakenPiece(final Piece piece);

    public abstract class TakenPieceImage extends GridPane{
        public PieceImage takenPieceImage;
        protected int numberOfSamePiece;
        protected Text numberText;
        TakenPieceImage (Piece piece){
            final String path = DEFAULT_PATH + piece.getPieceAlliance().toString().substring(0, 1) + piece.toString() + ".png";
            this.takenPieceImage = new PieceImage(piece,0,0,path);
            this.numberOfSamePiece = 0;
            this.numberText = new Text(String.valueOf(numberOfSamePiece));
            this.numberText.setFont(Font.font(TILE_SIZE/3));
            this.setMinSize(TILE_SIZE, TILE_SIZE);
            this.setMaxSize(TILE_SIZE, TILE_SIZE);

        }
        public abstract void addTakenPieceToTakenPieceImage();
        public abstract void removeTakenPiece();
    }

    public class TakenPawnImage extends TakenPieceImage{
    TakenPawnImage (final Pawn pawn){
        super(pawn);
    }

    @Override
    public void addTakenPieceToTakenPieceImage() {
        this.numberOfSamePiece++;
        if (this.numberOfSamePiece ==1 ){
            this.add(this.numberText,0,0);
            this.add(this.takenPieceImage,0,0,4,4);
        }
        this.numberText.setText(String.valueOf(this.numberOfSamePiece));
    }
    @Override
    public void removeTakenPiece() {
            this.numberOfSamePiece--;
            if (numberOfSamePiece == 0)
                this.getChildren().clear();
        }
    }
    public class TakenRookImage extends TakenPieceImage{
        TakenRookImage (final Rook rook){
            super(rook);
        }

        @Override
        public void addTakenPieceToTakenPieceImage() {
            this.numberOfSamePiece++;
            if (this.numberOfSamePiece ==1 ){
                this.add(this.numberText,0,0);
                this.add(this.takenPieceImage,0,0,4,4);
            }
            this.numberText.setText(String.valueOf(this.numberOfSamePiece));
        }
        @Override
        public void removeTakenPiece() {
            this.numberOfSamePiece--;
            if (numberOfSamePiece == 0)
                this.getChildren().clear();
        }
    }
    public class TakenQueenImage extends TakenPieceImage{
        TakenQueenImage(final Queen queen){
            super(queen);
        }

        @Override
        public void addTakenPieceToTakenPieceImage() {
            this.numberOfSamePiece++;
            if (this.numberOfSamePiece ==1 ){
                this.add(this.numberText,0,0);
                this.add(this.takenPieceImage,0,0,4,4);
            }
            this.numberText.setText(String.valueOf(this.numberOfSamePiece));
        }

        @Override
        public void removeTakenPiece() {
            this.numberOfSamePiece--;
            if (numberOfSamePiece == 0)
                this.getChildren().clear();
        }
    }
    public class TakenBishopImage extends TakenPieceImage{
        TakenBishopImage(final Bishop bishop){
            super(bishop);
        }

        @Override
        public void addTakenPieceToTakenPieceImage() {
            this.numberOfSamePiece++;
            if (this.numberOfSamePiece ==1 ){
                this.add(this.numberText,0,0);
                this.add(this.takenPieceImage,0,0,4,4);
            }
            this.numberText.setText(String.valueOf(this.numberOfSamePiece));
        }

        @Override
        public void removeTakenPiece() {
            this.numberOfSamePiece--;
            if (numberOfSamePiece == 0)
                this.getChildren().clear();
        }
    }
    public class TakenKnightImage extends TakenPieceImage{
        TakenKnightImage(final Knight knight){
            super(knight);
        }

        @Override
        public void addTakenPieceToTakenPieceImage() {
            this.numberOfSamePiece++;
            if (this.numberOfSamePiece ==1 ){
                this.add(this.numberText,0,0);
                this.add(this.takenPieceImage,0,0,4,4);
            }
            this.numberText.setText(String.valueOf(this.numberOfSamePiece));
        }

        @Override
        public void removeTakenPiece() {
            this.numberOfSamePiece--;
            if (numberOfSamePiece == 0)
                this.getChildren().clear();
        }
    }
}
