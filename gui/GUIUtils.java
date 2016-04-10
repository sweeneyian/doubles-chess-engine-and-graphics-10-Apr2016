package com.DoublesChess.gui;

import com.google.common.collect.Lists;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by Owner on 05/04/2016.
 */
public class GUIUtils {
    public static final String LIGHT_TILE_COLOUR = "-fx-background-color: #FFFACD;";
    public static final String DARK_TILE_COLOUR = "-fx-background-color: #8B4513;";
    public static final String SELECTED_DARK_TILE_COLOUR = "-fx-background-color: #469F27;";
    public static final String SELECTED_LIGHT_TILE_COLOUR = "-fx-background-color: #99FD80;";
    //public static final String SELECTED_TILE_COLOUR = "-fx-background-color: #33FF33;";
    public static final String SELECTED_TILE_COLOUR = "-fx-background-color: #0000FF;";
    public static final String RED_COLOUR = "-fx-background-color: #FF0000;";
    public static final String BLACK_COLOUR = "-fx-background-color: #000000;";
    public static final String GREY_COLOUR = "-fx-background-color: #AAAAAA;";
    public static final String WHITE_COLOUR = "-fx-background-color: #FFFFFF;";
    public static final String DEFAULT_PATH = "art/clean/";
    public static final int TILE_SIZE = 40;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public final static double H_TILE_GAP = 0;
    public final static double V_TILE_GAP = 0;
    public final static Duration GAME_TIME = new Duration(120000);
    public final static Duration FISCHER_BONUS = new Duration(3000);
    public final static int OFF_BOARD_POSITION = -999;

    public enum BoardDirection {
        NORMAL {
            @Override
            List<BoardBorderPane.TilePanel> traverse(final List<BoardBorderPane.TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            int traverseTileID(final int tileID) {
                return tileID;
            }

            @Override
            BoardDirection opposite() {

                System.out.println("Board Flipped");
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<BoardBorderPane.TilePanel> traverse(final List<BoardBorderPane.TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            int traverseTileID(final int tileID) {

                return (63 - tileID);
            }

            @Override
            BoardDirection opposite() {
                System.out.println("Board Normal");
                return NORMAL;
            }
        };

        abstract List<BoardBorderPane.TilePanel> traverse(final List<BoardBorderPane.TilePanel> boardTiles);

        abstract int traverseTileID(final int tileID);

        abstract BoardDirection opposite();
    }

    public enum GameType {
        STANDARD {
            @Override
            boolean takenPiecesCanBeMoved() {
                return false;
            }
        }, NINE_SIXTY {
            @Override
            boolean takenPiecesCanBeMoved() {
                return false;
            }
        }, DOUBLES {
            @Override
            boolean takenPiecesCanBeMoved() {
                return true;
            }
        };

        abstract boolean takenPiecesCanBeMoved ();
    }
    public enum BoardNumber {
        BOARD_ONE {
            @Override
            int BoardNumberTransLation() {
                return 1;
            }
        }, BOARD_TWO {
            @Override
            int BoardNumberTransLation() {
                return 2;
            }
        };

        abstract int BoardNumberTransLation ();
    }

}
