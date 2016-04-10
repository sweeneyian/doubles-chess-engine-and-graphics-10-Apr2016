package com.DoublesChess.gui;

import com.DoublesChess.engine.Alliance;
import com.DoublesChess.engine.board.Board;
import com.DoublesChess.engine.board.Move;
import com.DoublesChess.engine.board.Tile;
import com.DoublesChess.engine.pieces.Piece;
import com.DoublesChess.gui.Table;

import com.DoublesChess.engine.player.MoveTransition;
import com.DoublesChess.engine.player.WhitePlayer;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;

import javafx.scene.Parent;
import javafx.scene.layout.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.DoublesChess.engine.board.BoardUtils.*;
import static com.DoublesChess.gui.GUIUtils.*;

/**
 * Created by Owner on 06/04/2016.
 */
public class BoardBorderPane extends BorderPane {
    private Group pieceGroup;
    private BoardPanel boardPanel;
    private AnchorPane AP;
    private Board engineBoard;
    public boolean showLegalMovesSetting;
    private BoardDirection boardDirection;
    private BoardNumber boardNumber;
    private HBox timeToolBar;
    private Clock clock1, clock2;
    private TakenPiecesPane takenPiecesPane;

    private Tile sourceTile;
    private Tile hoveredTile;
    private Tile destinationTile;
    private TilePanel sourceTilePanel;//
    private TilePanel hoveredTilePanel;
    private Piece humanMovedPiece;

    public void setBoardDirection(BoardDirection boardDirection) {
        this.boardDirection = boardDirection;
    }
    public BoardDirection getBoardDirection() {
        return this.boardDirection;
    }
    public void setShowLegalMoves(boolean showLegalMoves) {
        this.showLegalMovesSetting = showLegalMoves;
    }
    public void setEngineBoard (Board engineBoard){
        this.engineBoard = engineBoard;
    }
    public void resetBoardBorderPaneClocks(){
        this.clock1.resetClock();
        this.clock1.play();
        this.clock2.resetClock();
        this.clock2.stop();
    }

    public void drawBoard() {
        AP.getChildren().clear();
        boardPanel.getChildren().clear();
        boardPanel = new BoardPanel();
        AP.getChildren().addAll(boardPanel, pieceGroup);
    }

    public BoardBorderPane(final Board engineBoard, final BoardDirection direction, final BoardNumber boardNumber, final boolean showLegalMovesSetting){
        this.engineBoard = engineBoard;
        this.boardDirection = direction;
        this.boardNumber = boardNumber;
        this.pieceGroup = new Group();
        this.AP = new AnchorPane();
        this.timeToolBar = new HBox();
        this.takenPiecesPane = new TakenPiecesPane(boardDirection);
        this.clock1 = new Clock(GAME_TIME, FISCHER_BONUS, true);
        this.clock2 = new Clock(GAME_TIME, FISCHER_BONUS, true);
        this.clock2.stop();

        this.showLegalMovesSetting = showLegalMovesSetting;
        setMinSize(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);
        this.boardPanel = new BoardPanel();
        AP.getChildren().addAll(boardPanel, pieceGroup);
        this.timeToolBar.getChildren().addAll(clock1, clock2);
        this.timeToolBar.setAlignment(Pos.CENTER);
        this.timeToolBar.setSpacing(TILE_SIZE);
        setCenter(AP);
        setTop(timeToolBar);
        setRight(takenPiecesPane);
    }

    public class MoveLog{
        private final List<Move> moves;

        MoveLog(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return moves;
        }

        public void addMove (final Move move){
            this.moves.add(move);
        }

        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }
        public Move removeMove(final int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }

    }

    protected class BoardPanel extends TilePane {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            this.boardTiles = new ArrayList<>();
            setMinSize(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);
            setMaxSize(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);
            pieceGroup.getChildren().clear();

            for (int i = 0; i < NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(boardDirection.traverseTileID(i), Orientation.HORIZONTAL, H_TILE_GAP, V_TILE_GAP);
                this.boardTiles.add(tilePanel);
                tilePanel.setOnMousePressed(e->{
                    tilePanel.assignMoveTileColor();
                });

                tilePanel.setOnMouseReleased(e->{
                    tilePanel.assignTileColor();
                });
                getChildren().add(tilePanel);
            }
        }
        protected void drawBoard() {
            AP.getChildren().clear();
            boardPanel.getChildren().clear();
            boardPanel = new BoardPanel();
            AP.getChildren().addAll(boardPanel, pieceGroup);
        }
    }

    private int tileIDFromCoOrdinate(int x, int y) {
        int tileID = x + y * 8;
        return tileID;
    }

    class TilePanel extends TilePane {
        private final int tileID;
        private String originalColorString;

        TilePanel(final int tileID, Orientation orientation, double hgap, double vgap) {
            super(orientation, hgap, vgap);
            this.tileID = tileID;
            this.setOnMousePressed(e -> {
                System.out.println("tilePanel Tile ID: "+ this.tileID);
            });
            setMinSize(TILE_SIZE, TILE_SIZE);
            setMaxSize(TILE_SIZE, TILE_SIZE);
            assignTileColor();

            //setPosition();
            assignTilePieceIcon(engineBoard);
            highLightLegalMoves(engineBoard);
        }

        public void drawTilePanel (Board board) {

            assignTileColor();
            assignTilePieceIcon(board);

        }

        private void highLightLegalMoves(final Board board){
            if(showLegalMovesSetting){
                for (final Move move : pieceLegalMoves (board)){
                    if (move.getDestinationCoordinate() == this.tileID){
                        assignMoveTileColor();
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board){
            if (humanMovedPiece!= null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor() {
            if (FIRST_ROW[this.tileID] ||
                    THIRD_ROW[this.tileID] ||
                    FIFTH_ROW[this.tileID] ||
                    SEVENTH_ROW[this.tileID]) {
                setStyle(tileID % 2 != 0 ? LIGHT_TILE_COLOUR: DARK_TILE_COLOUR);
                originalColorString = getStyle();
            } else if (SECOND_ROW[this.tileID] ||
                    FOURTH_ROW[this.tileID] ||
                    SIXTH_ROW[this.tileID] ||
                    EIGHTH_ROW[this.tileID]) {
                setStyle(tileID % 2 == 0 ? LIGHT_TILE_COLOUR : DARK_TILE_COLOUR);
                originalColorString = getStyle();
            }
        }

        private void assignMoveTileColor() {
            if (showLegalMovesSetting) {
                if (FIRST_ROW[this.tileID] ||
                        THIRD_ROW[this.tileID] ||
                        FIFTH_ROW[this.tileID] ||
                        SEVENTH_ROW[this.tileID]) {
                    setStyle(tileID % 2 != 0 ? SELECTED_LIGHT_TILE_COLOUR : SELECTED_DARK_TILE_COLOUR);
                    originalColorString = getStyle();
                } else if (SECOND_ROW[this.tileID] ||
                        FOURTH_ROW[this.tileID] ||
                        SIXTH_ROW[this.tileID] ||
                        EIGHTH_ROW[this.tileID]) {
                    setStyle(tileID % 2 == 0 ? SELECTED_LIGHT_TILE_COLOUR : SELECTED_DARK_TILE_COLOUR);
                    originalColorString = getStyle();
                }
            }
        }

        public void assignTilePieceIcon(final Board board) {
            //TODO figure out why pathing not working 100%
            if (board.getTile(this.tileID).isTileOccupied()) {

                final String path = DEFAULT_PATH + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0, 1) +
                        board.getTile(this.tileID).getPiece().toString() + ".png";
                final int x = getCoOrdinateX(boardDirection.traverseTileID(this.tileID));
                final int y = getCoOrdinateY(boardDirection.traverseTileID(this.tileID));

                    final PieceImage pieceImage = new PieceImage(
                            engineBoard.getTile(tileID).getPiece(),
                            x,
                            y,
                            path);
                pieceGroup.getChildren().add(pieceImage);

                pieceImage.setOnMousePressed(e -> {

                    final int tileCoOrd = boardDirection.traverseTileID(tileIDFromCoOrdinate(x, y));

                    pieceImage.mouseX = e.getSceneX();
                    pieceImage.mouseY = e.getSceneY();

                    if (sourceTile == null) {
                        sourceTilePanel = boardPanel.boardTiles.get(boardDirection.traverseTileID(tileCoOrd));
                        sourceTile = engineBoard.getTile(tileCoOrd);
                        humanMovedPiece = sourceTile.getPiece();

                        if (showLegalMovesSetting) {
                            Collection<Move> selectedPiece = pieceLegalMoves(engineBoard);
                            for (Move move : selectedPiece) {
                                TilePanel tilePanel = boardPanel.boardTiles.get(boardDirection.traverseTileID(move.getDestinationCoordinate()));
                                tilePanel.assignMoveTileColor();

                            }
                        }
                    }

                });

                pieceImage.setOnMouseEntered(e -> {
                    pieceImage.toFront(); // beautiful.
                    if (showLegalMovesSetting) {
                        final int tileCoOrd = boardDirection.traverseTileID(tileIDFromCoOrdinate(x, y));
                        hoveredTilePanel = boardPanel.boardTiles.get(boardDirection.traverseTileID(tileCoOrd));
                        hoveredTile = engineBoard.getTile(tileCoOrd);

                        if (engineBoard.getTile(tileCoOrd).isTileOccupied() &&
                                engineBoard.getTile(tileCoOrd).getPiece().getPieceAlliance() == engineBoard.getCurrentPlayer().getAlliance()) {
                            hoveredTilePanel.setStyle(SELECTED_TILE_COLOUR);
                        }
                    }
                });
                pieceImage.setOnMouseExited(e -> {
                    final int tileCoOrd = boardDirection.traverseTileID(tileIDFromCoOrdinate(x, y));
                    boardPanel.boardTiles.get(boardDirection.traverseTileID(tileCoOrd)).setStyle(boardPanel.boardTiles.get(tileCoOrd).originalColorString);
                });

                pieceImage.setOnMouseDragged(e -> {
                    if(showLegalMovesSetting){
                        sourceTilePanel.setStyle(SELECTED_TILE_COLOUR);
                    }
                    pieceImage.relocate(
                            Math.max(0,Math.min(TILE_SIZE * WIDTH- TILE_SIZE, e.getSceneX() - pieceImage.mouseX + pieceImage.oldX)),
                            Math.max(0,Math.min(TILE_SIZE * WIDTH- TILE_SIZE, e.getSceneY() - pieceImage.mouseY + pieceImage.oldY)));
                    //limit where the piece can be moved to so funny stuff doesnt happpen
                });

                pieceImage.setOnMouseReleased(e -> {

                    int newX = toBoard(pieceImage.getLayoutX());
                    int newY = toBoard(pieceImage.getLayoutY());
                    int x0 = (int) pieceImage.getOldX();
                    int y0 = (int) pieceImage.getOldY();

                    final int tileCoOrd = boardDirection.traverseTileID(tileIDFromCoOrdinate(newX, newY));
                    destinationTile = engineBoard.getTile(tileCoOrd);
                    final Move move = Move.MoveFactory.createMove(engineBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                    final MoveTransition transition = engineBoard.getCurrentPlayer().makeMove(move);

                    if (transition.getMovesStatus().isDone()) {

                        engineBoard = transition.getTransitionBoard();

                        if (destinationTile.isTileOccupied()){
                            Piece takenPiece = destinationTile.getPiece();
                            takenPiecesPane.addPiece(takenPiece);
                        }
                        //todo add move to movelog
                        if (engineBoard.getCurrentPlayer().getAlliance()== Alliance.WHITE){
                            clock1.play();
                            clock2.stop();
                            clock2.addFisherBonus();
                        }else if(engineBoard.getCurrentPlayer().getAlliance()== Alliance.BLACK){
                            clock2.play();
                            clock1.stop();
                            clock1.addFisherBonus();
                        }
                        sourceTilePanel.setStyle(sourceTilePanel.originalColorString);
                        sourceTilePanel = null;
                        sourceTile = null;
                        humanMovedPiece = null;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard();
                            }
                        });
                    } else {
                        //null move - relocate to old position
                        pieceImage.relocate(x0, y0);
                        sourceTilePanel = null;
                        sourceTile = null;
                        humanMovedPiece = null;
                        Collection<Move> selectedPiece = pieceLegalMoves(engineBoard);
                        for (final TilePanel tilePanel: boardPanel.boardTiles){
                            tilePanel.assignTileColor();
                        }
                    }
                });
                // TODO special thanks to https://openclipart.org/search/?query=Chess+tile+
            }
        }
    }

    private int toBoard(double pixel) {
        return (int) ((pixel + TILE_SIZE / 2) / TILE_SIZE);
    }

    private int getCoOrdinateX(int tileID) {
        int x = tileID % 8;
        return x;

    }

    private int getCoOrdinateY(int tileID) {
        int y = (int) Math.floor(tileID / 8);
        return y;
    }


}
