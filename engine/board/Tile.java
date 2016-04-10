package com.DoublesChess.engine.board;

import com.DoublesChess.engine.pieces.Piece;

import com.google.common.collect.ImmutableMap;
import com.sun.javafx.geom.Rectangle;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile extends Rectangle {

    protected final int tileCoordinate;


    private static final Map<Integer, EmptyTile > EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles(){

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0 ; i < BoardUtils.NUM_TILES ; i++){
        emptyTileMap.put(i, new EmptyTile(i));
        }
        // return Collections.unmodifiableMap(emptyMap); Java option
        return ImmutableMap.copyOf(emptyTileMap);
        //or import googles guava library
    }

    // if we want to create more tiles outside of the immutable map. saves machine resources.
    // create classes immutable, wont mutate, makes thread handling stronger, no synchronization issues
    // create tile: we can either create a tile with a piece or one of the oen from the empty board tile cache
    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null? new OccuppiedTile(tileCoordinate, piece):EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    //constructor
    // when we create a tile with a coordinate, it gets that coordinate.
    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileCoordinate(){
        return this.tileCoordinate;
    }




    // constructor for Empty Squares
    public static final class EmptyTile extends Tile{
        private EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied (){
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }

        @Override
        public String toString(){
            return "-";
        }


    }

    // constructor for occupied squares. also passes piece.
    public static final class OccuppiedTile extends Tile{
        private final Piece pieceOnTile;

        private OccuppiedTile(int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied(){
            return true;
        }

        @Override
        public Piece getPiece(){
            return this.pieceOnTile ;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack()? getPiece().toString().toLowerCase():getPiece().toString();
        }
    }

}
