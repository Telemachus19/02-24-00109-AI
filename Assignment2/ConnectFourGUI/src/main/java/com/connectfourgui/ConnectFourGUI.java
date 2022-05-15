package com.connectfourgui;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static com.almasb.fxgl.dsl.FXGL.*;


public class ConnectFourGUI extends GameApplication {
    static Scanner console = new Scanner(System.in);
    private Entity player;
    private static final int PLAYER = 1,AI = 2,EMPTY = 0;

    private static final int TILE_SIZE = 100;
    private static final int SPEED = 5;
    private static final int MAX_ROWS = 6, MAX_COLS = 7;
    private static final int MAX_WIDTH = MAX_COLS * TILE_SIZE;
    private static final int MAX_HEIGHT = (MAX_ROWS + 1) * TILE_SIZE;
    int col, row, turn = 0;
    static final int CELL_LENGTH = 4;
    int[][] TerminalBoard = createBoard();

    public static int[][] createBoard() {
        return new int[MAX_ROWS][MAX_COLS];
    }

    public static void dropPiece(int[][] board, int row, int col, int piece) {
        board[row][col] = piece;
    }

    public static boolean isValidLocation(int[][] board, int col) {
        return board[0][col] == 0;
    }

    public static int getOpenRow(int[][] board, int col) {
        int openRow = 0;
        for (int row = MAX_ROWS - 1; row > -1; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return openRow;
    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                System.out.printf("%d\t", board[i][j]);
            }
            System.out.println();
        }
    }

    public static int getTerminalInput(int i) {
        int input;
        do {
            System.out.printf("Player %d make your selection (0-6): ", i);
            input = console.nextInt();
            System.out.println("input = " + input);
        } while (input > 6 || input < 0);
        return input;
    }



    public static boolean GameOver(int[][] board) {
        int cnt = 0;
        for(int col = 0; col < MAX_COLS;col++){
            if(!isValidLocation(board,col))
                cnt++;
        }
        return cnt == MAX_COLS;
    }
    public static int countPieces(int[] arr, int piece){
        int cnt = 0;
        for (int j : arr) {
            if ((j == piece)) {
                ++cnt;
            }
        }
        return cnt;
    }
    private static int[] makeColArr(int[][] board, int c){
        int[] colArr = new int[MAX_ROWS];
        for(int row = 0; row < MAX_ROWS;row++){
            colArr[row] = board[row][c];
        }
        return colArr;
    }
    private static int[] makeRowArr(int[][] board,int r){
        int[] rowArr = new int[MAX_COLS];
        System.arraycopy(board[r], 0, rowArr, 0, MAX_COLS);
        return rowArr;
    }
    private static int evaluateCell(int[] cell,int piece){
        int cellScore = 0;
        int enemyPiece = PLAYER;
        if(piece == PLAYER)
            enemyPiece = AI;
        if (countPieces(cell, piece) == 4)
            cellScore += 100;
        else if(countPieces(cell,piece) == 3 && countPieces(cell,EMPTY) == 1)
            cellScore += 5;
        else if(countPieces(cell,piece) == 2 && countPieces(cell,EMPTY) == 2)
            cellScore += 2;

        if(countPieces(cell,enemyPiece) == 3 && countPieces(cell,EMPTY) == 1)
            cellScore -= 4;

        return cellScore;
    }
    public static ArrayList<Integer> successor(int[][] board){
        ArrayList<Integer> validLocations = new ArrayList<>();
        for(int c = 0; c < MAX_COLS; c++){
            if(isValidLocation(board,c))
                validLocations.add(c);
        }
        return validLocations;
    }
    private static int scorePosition(int[][] board,int piece){
        int boardScore = 0;
        // Score center column and favour it.
        int[] centerArr = makeColArr(board,MAX_COLS/2);
        int centerCount = countPieces(centerArr,piece);
        boardScore += centerCount * 3;

        // Score Horizontal
        for(int row = 0 ; row < MAX_ROWS; row++){
            int[] rowArr = makeRowArr(board,row);
            for(int col = 0; col < MAX_COLS - 3;col++){
                int[] cell = new int[CELL_LENGTH];
                System.arraycopy(rowArr,col,cell,0,CELL_LENGTH);
                boardScore += evaluateCell(cell,piece);
            }
        }
        // Score Vertical
        for(int col = 0; col < MAX_COLS;col++){
            int[] colArr = makeColArr(board,col);
            for(int row = 0 ; row < MAX_ROWS -3 ; row++){
                int[] cell = new int[CELL_LENGTH];
                System.arraycopy(colArr,row,cell,0,CELL_LENGTH);
                boardScore += evaluateCell(cell,piece);
            }
        }
        // score Positive Diagonally
        for(int row = 0; row < MAX_ROWS - 3; row++){
            for(int col = 0 ; col < MAX_COLS - 3;col++){
                int[] cell = new int[CELL_LENGTH];
                for(int i =0; i < CELL_LENGTH;i++){
                    cell[i] = board[row + i][col + i];
                }
                boardScore += evaluateCell(cell,piece);
            }
        }
        // score negative Diagonally
        for(int row = 0; row < MAX_ROWS - 3; row++){
            for(int col = 0 ; col < MAX_COLS - 3;col++){
                int[] cell = new int[CELL_LENGTH];
                for(int i =0; i < CELL_LENGTH;i++){
                    cell[i] = board[row + 3 - i][col + i];
                }
                boardScore += evaluateCell(cell,piece);
            }
        }
        return boardScore;
    }
    private  static  boolean isTerminalNode(int[][] terminalBoard){
        return GameOver(terminalBoard) || successor(terminalBoard).isEmpty();
    }
    private static int[] minimax(int[][] board, int depth, int alpha, int beta, boolean maxPlayer){
        ArrayList<Integer> validLocations = successor(board);
        boolean isTerminalNode = isTerminalNode(board);
        if (depth == 0 || isTerminalNode){
            if(isTerminalNode){
                if(GameOver(board))
                    return new int[]{0, Integer.MAX_VALUE};
                else // game is over, and there is no more valid moves
                    return new int[]{0,0};
            }else{
                return new int[]{0,scorePosition(board,AI)};
            }
        }
        if (maxPlayer){
            int value;
            int column= validLocations.get(FXGLMath.random(0,validLocations.size() - 1)) ;
            value = Integer.MIN_VALUE;
            for(int col = 0; col < validLocations.size();col++){
                int row = getOpenRow(board,col);
                int[][] boardCopy = new int[MAX_ROWS][MAX_COLS];
                for(int r = 0; r < MAX_ROWS;r++){
                    System.arraycopy(board[r], 0, boardCopy[r], 0, MAX_COLS);
                }
                dropPiece(boardCopy,row,col,AI);
                int newScore = minimax(boardCopy,depth-1,alpha,beta,false)[1];
                if(newScore > value){
                    value = newScore;
                    column = col;
                }
                alpha = Integer.max(alpha,value);
                if(alpha >= beta)
                    break;
            }
            return new int[]{column,value};
        }else{
            int value;
            int column= validLocations.get(FXGLMath.random(0,validLocations.size() - 1)) ;
            value = Integer.MAX_VALUE;
            for(int col = 0; col < validLocations.size();col++){
                int row = getOpenRow(board,col);
                int[][] boardCopy = new int[MAX_ROWS][MAX_COLS];
                for(int r = 0; r < MAX_ROWS;r++){
                    System.arraycopy(board[r], 0, boardCopy[r], 0, MAX_COLS);
                }
                dropPiece(boardCopy,row,col,PLAYER);
                int newScore = minimax(boardCopy,depth-1,alpha,beta,true)[1];
                if(newScore < value){
                    value = newScore;
                    column = col;
                }
                beta = Integer.min(beta,value);
                if(alpha >= beta)
                    break;
            }
            return new int[]{column,value};
        }
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAX_WIDTH);
        settings.setHeight(MAX_HEIGHT);
        settings.setTitle("Welcome");
        settings.setVersion("0.1");
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
/*        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu(){
                return new ConnectFourMainMenu();
            }
        });*/

    }

    private void createTile() {
        int x, y;
        Color ballColor = Color.BLACK;
        for (int col = 0; col < MAX_COLS; col++, ballColor = Color.BLACK) {
            for (int row = 0; row < MAX_ROWS; row++) {
                x = col * TILE_SIZE;
                y = (row + 1) * TILE_SIZE;
                if (TerminalBoard[row][col] == 1) {
                    ballColor = Color.RED;
                } else if (TerminalBoard[row][col] == 2) {
                    ballColor = Color.YELLOW;
                }
                FXGL.spawn("Tile", new SpawnData(x, y).put("Tile Size", TILE_SIZE).put("color", ballColor));
            }
        }
    }

    @Override
    protected void initInput() {
        Input input = getInput();
        EventHandler<MouseEvent> handler = event -> {
            getGameWorld().getSingleton(EntityType.PLAYER).setX(input.getMouseXUI() - (TILE_SIZE / 2));
        };
        input.addEventHandler(MouseEvent.MOUSE_MOVED, handler);

        FXGL.onKey(KeyCode.A, () -> {
            getGameWorld().getSingleton(EntityType.PLAYER).translateX(-SPEED);
        });
        FXGL.onKey(KeyCode.D, () -> {
            getGameWorld().getSingleton(EntityType.PLAYER).translateX(SPEED);
        });
        FXGL.onKeyDown(KeyCode.S, () -> {
            int posX = (int) getGameWorld().getSingleton(EntityType.PLAYER).getX();
            userTurn(posX);
            AITurn();
        });
        FXGL.onBtnDown(MouseButton.PRIMARY, () -> {
            System.out.println(input.getMousePositionUI());
            int posX = (int) input.getMouseXWorld();
            boolean userPlayed = userTurn(posX);
            if(userPlayed){
                AITurn();
            }else{
                showMessage("Please Choose a correct position",() -> getGameController().resumeEngine());
            }

        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if (GameOver(TerminalBoard)) {
            System.out.println("Player 1 Wins");
            showMessage("Player 1 Wins", () -> getGameController().exit());
        }
        if (GameOver(TerminalBoard)) {
            showMessage("Player 2 Wins", () -> getGameController().exit());
        }
    }

    protected void changeBallColor(Color color) {
        double horizontalPosition = getGameWorld().getSingleton(EntityType.PLAYER).getX();
        getGameWorld().getSingleton(EntityType.PLAYER).removeFromWorld();
        spawn("Player", new SpawnData(horizontalPosition, 0).put("Tile Size", TILE_SIZE).put("color", color));
    }

    protected boolean userTurn(int x) {
        col = FXGLMath.floor(x / TILE_SIZE);
        if (isValidLocation(TerminalBoard, col)) {
            row = getOpenRow(TerminalBoard, col);
            dropPiece(TerminalBoard, row, col, PLAYER);
            printBoard(TerminalBoard);
            createTile();
            changeBallColor(Color.YELLOW);
            return true;
        }
        return false;
    }

    protected void AITurn() {
        col = minimax(TerminalBoard,5,Integer.MIN_VALUE,Integer.MAX_VALUE,false)[0];
        while(!isValidLocation(TerminalBoard,col)){
            col = minimax(TerminalBoard,5,Integer.MIN_VALUE,Integer.MAX_VALUE,false)[0];
        }
        row = getOpenRow(TerminalBoard, col);
        dropPiece(TerminalBoard, row, col, AI);
        printBoard(TerminalBoard);
        createTile();
        changeBallColor(Color.RED);
    }
    @Override
    protected void initGame() {
        FXGL.getGameScene().setBackgroundColor(Color.BLACK);
        FXGL.getGameWorld().addEntityFactory(new ConnectFourFactory());
        createTile();
        spawn("Player", new SpawnData((MAX_WIDTH / 2) - TILE_SIZE / 2, 0).put("Tile Size", TILE_SIZE).put("color", Color.RED));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
