package com.DoublesChess.gui;
import com.DoublesChess.engine.pieces.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import static com.DoublesChess.gui.GUIUtils.OFF_BOARD_POSITION;
import static com.DoublesChess.gui.GUIUtils.TILE_SIZE;



public class PieceImage extends StackPane {

    public Piece piece;
    public double mouseX, mouseY;
    public double oldX, oldY;


    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public PieceImage (Piece piece, int x, int y, String path){
        this.piece = piece;

        moveImage(x, y );
        Image image = new Image(this.getClass().getResource(path).toExternalForm());
        ImageView imageView = new ImageView(image);
        if (piece.getPiecePosition() == OFF_BOARD_POSITION){
            imageView.setFitWidth(TILE_SIZE);
            imageView.setFitHeight(TILE_SIZE);
        }else {
            imageView.setFitWidth(TILE_SIZE);
            imageView.setFitHeight(TILE_SIZE);
        }

        getChildren().addAll(imageView);

    }

    public void moveImage (int x, int y){
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

}