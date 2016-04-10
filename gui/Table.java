package com.DoublesChess.gui;

import static com.DoublesChess.gui.GUIUtils.*;
import com.DoublesChess.engine.board.Board;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Table extends Application {

    private Scene scene;
    private BorderPane BP;
    private FlowPane FP;
    private BoardBorderPane boardBorderPane1, boardBorderPane2; // stackpane for stacking grid of draggable pieces 1x1 on top of drid of board 8x8
    private Board engineBoard1, engineBoard2;
    private boolean showLegalMovesSetting;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.scene = new Scene(createContent(),TILE_SIZE*WIDTH*2.7, TILE_SIZE*HEIGHT*1.3);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DoublesChess");
        primaryStage.show();
    }

    private Parent createContent() {
        this.BP = new BorderPane();
        this.FP = new FlowPane();
        this.FP.setMinSize(TILE_SIZE* WIDTH *2.2, TILE_SIZE* HEIGHT);
        this.showLegalMovesSetting = true;

        engineBoard1 = Board.createStandardBoard();
        engineBoard2 = Board.createStandardBoard();

        boardBorderPane1 = new BoardBorderPane(engineBoard1, BoardDirection.NORMAL, BoardNumber.BOARD_ONE, showLegalMovesSetting);
        boardBorderPane1.setMinSize(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);

        boardBorderPane2 = new BoardBorderPane(engineBoard2, BoardDirection.FLIPPED, BoardNumber.BOARD_TWO, showLegalMovesSetting);
        boardBorderPane2.setMinSize(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);

        this.FP.getChildren().addAll(boardBorderPane1, boardBorderPane2);
        this.FP.setHgap(TILE_SIZE);


        this.BP.setCenter(this.FP);


        this.BP.setMinSize(TILE_SIZE * HEIGHT *2.2, TILE_SIZE*WIDTH *2.2);

        final int vBoxHeight = 25;
        final int vBoxPadding = 2;
        final int vBoxTotalHeight = vBoxHeight + vBoxPadding * 2;
        final int vBoxSpacing = 4;

        VBox vBox = new VBox();
        vBox.setPrefSize(vBoxHeight, vBoxHeight);
        vBox.setSpacing(vBoxSpacing);
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(vBoxPadding))));
        final MenuBar tableMenuBar = createTableMenuBar();
        vBox.getChildren().addAll(tableMenuBar);
        this.BP.setTop(vBox);


        return BP;
    }

    private MenuBar createTableMenuBar() {
        final MenuBar tableMenuBar = new MenuBar();
        tableMenuBar.getMenus().add(createFileMenu());
        tableMenuBar.getMenus().add(createPreferenceMenu());
        return tableMenuBar;
    }

    private Menu createFileMenu() {
        final Menu fileMenu = new Menu("File");
        final MenuItem openPGN = new MenuItem("Load PGN File");
        openPGN.setOnAction(e -> {
            System.out.println("Open that PGN file");
        });
        final MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> {
            System.exit(0);
        });

        final MenuItem newGame = new MenuItem("New game");
        newGame.setOnAction(e -> {

            this.boardBorderPane1.setEngineBoard(Board.createStandardBoard());
            this.boardBorderPane1.resetBoardBorderPaneClocks();
            this.boardBorderPane2.setEngineBoard(Board.createStandardBoard());
            this.boardBorderPane2.resetBoardBorderPaneClocks();
            this.boardBorderPane1.drawBoard();
            this.boardBorderPane2.drawBoard();

        });
        fileMenu.getItems().addAll(openPGN, exit, newGame);
        return fileMenu;
    }

    private Menu createPreferenceMenu() {
        final Menu preferenceMenu = new Menu("Preference");
        final MenuItem flipBoardMenu = new MenuItem("Flip Board");
        final CheckMenuItem showLegalMoves = new CheckMenuItem("Show Legal Moves");
        showLegalMoves.setSelected(true);


        flipBoardMenu.setOnAction(e -> {
            if (this.boardBorderPane1.getBoardDirection()==BoardDirection.FLIPPED){
                this.boardBorderPane1.setBoardDirection(BoardDirection.NORMAL);
                this.boardBorderPane2.setBoardDirection(BoardDirection.FLIPPED);
                this.boardBorderPane1.drawBoard();
                this.boardBorderPane2.drawBoard();

            }
            else {
                this.boardBorderPane1.setBoardDirection(BoardDirection.FLIPPED);
                this.boardBorderPane2.setBoardDirection(BoardDirection.NORMAL);
                this.boardBorderPane1.drawBoard();
                this.boardBorderPane2.drawBoard();
            }
        });

        showLegalMoves.setOnAction(e -> {
            if (showLegalMoves.isSelected()){
                this.showLegalMovesSetting = true;
                this.boardBorderPane1.setShowLegalMoves(true);
                this.boardBorderPane2.setShowLegalMoves(true);
                }
            else{
               this.showLegalMovesSetting = false;
                this.boardBorderPane1.setShowLegalMoves(false);
                this.boardBorderPane2.setShowLegalMoves(false);
            }

        });

        preferenceMenu.getItems().addAll(flipBoardMenu, showLegalMoves);

        return preferenceMenu;
    }

}