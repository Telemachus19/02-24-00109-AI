package com.connectfourgui;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import java.util.*;
import java.util.function.Function;

import static com.almasb.fxgl.dsl.FXGL.*;


public class ConnectFourGUI extends GameApplication {
    static Scanner console = new Scanner(System.in);
    protected static final int PLAYER = 1, AI = 2, EMPTY = 0;

    protected static final int TILE_SIZE = 100;
    protected static final int SPEED = 5;
    protected static final int MAX_ROWS = 7, MAX_COLS = 6;
    protected static final int MAX_WIDTH = MAX_COLS * TILE_SIZE;
    protected static final int MAX_HEIGHT = (MAX_ROWS + 1) * TILE_SIZE;
    protected int col, row, playerScore = 0, aiScore = 0;
    protected static final int CELL_LENGTH = 4;
    protected int[][] TerminalBoard = createBoard();
    protected int depth;
    public static boolean alphaBeta = true;
    private static double startTime,endTime;

    public static int[][] createBoard() {
        return new int[MAX_ROWS][MAX_COLS];
    }

    public static void dropPiece(int[][] board, int row, int col, int piece) {
        board[row][col] = piece;
    }

    public static boolean isValidLocation(int[][] board, int col) {
        return board[MAX_ROWS - 1][col] == 0;
    }

    public static int getOpenRow(int[][] board, int col) {
        int r = -1;
        for (r = 0; r < MAX_ROWS; r++) {
            if (board[r][col] == 0) return r;
        }
        return r;
    }
    public static TreeNode boardTree;

    public static void printBoard(int[][] board) {
        for (int i = MAX_ROWS - 1; i > -1; i--) {
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


    public static boolean GameOver(int[][] board, int piece) {
//        int cnt = 0;
//        for (int col = 0; col < MAX_COLS; col++) {
//            if (!isValidLocation(board, col)) cnt++;
//        }
//        return cnt == MAX_COLS;
//         Horizontal
        for (int c = 0; c < MAX_COLS - 3; c++) {
            for (int r = 0; r < MAX_ROWS; r++) {
                if (board[r][c] == piece
                        && board[r][c + 1] == piece
                        && board[r][c + 2] == piece
                        && board[r][c + 3] == piece) {
                    return true;
                }
            }
        }
        // vertical check
        for (int c = 0; c < MAX_COLS; c++) {
            for (int r = 0; r < MAX_ROWS - 3; r++) {
                if (board[r][c] == piece
                        && board[r + 1][c] == piece
                        && board[r + 2][c] == piece
                        && board[r + 3][c] == piece) {
                    return true;
                }
            }
        }
        // Positive Diagonal check
        for (int c = 0; c < MAX_COLS - 3; c++) {
            for (int r = 0; r < MAX_ROWS - 3; r++) {
                if (board[r][c] == piece
                        && board[r + 1][c + 1] == piece
                        && board[r + 2][c + 2] == piece
                        && board[r + 3][c + 3] == piece) {
                    return true;
                }
            }
        }
        // negative Diagonal check
        for (int c = 0; c < MAX_COLS - 3; c++) {
            for (int r = 3; r < MAX_ROWS; r++) {
                if (board[r][c] == piece
                        && board[r - 1][c + 1] == piece
                        && board[r - 2][c + 2] == piece
                        && board[r - 3][c + 3] == piece) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int evaluateCell(int[] cell, int piece) {
        int cellScore = 0;
        int enemyPiece = PLAYER;
        if (piece == PLAYER) enemyPiece = AI;
        if (countPieces(cell, piece) == 4) cellScore += 100;
        else if (countPieces(cell, piece) == 3 && countPieces(cell, EMPTY) == 1) cellScore += 5;
        else if (countPieces(cell, piece) == 2 && countPieces(cell, EMPTY) == 2) cellScore += 2;

        if (countPieces(cell, enemyPiece) == 3 && countPieces(cell, EMPTY) == 1) cellScore -= 4;
        if (countPieces(cell, enemyPiece) == 4) cellScore -= 100;

        return cellScore;
    }

    public static int countPieces(int[] arr, int piece) {
        int cnt = 0;
        for (int j : arr) {
            if ((j == piece)) {
                ++cnt;
            }
        }
        return cnt;
    }

    private static int scorePosition(int[][] board, int piece) {
        int boardScore = 0;
        // Score center column and favour it.
        int[] centerArr = makeColArr(board, MAX_COLS / 2);
        int centerCount = countPieces(centerArr, piece);
        boardScore += centerCount * 3;

        // Score Horizontal
        for (int row = 0; row < MAX_ROWS; row++) {
            int[] rowArr = makeRowArr(board, row);
            for (int col = 0; col < MAX_COLS - 3; col++) {
                int[] cell = new int[CELL_LENGTH];
                for (int i = 0; i < CELL_LENGTH; i++) {
                    cell[i] = rowArr[col + i];
                }
                boardScore += evaluateCell(cell, piece);
            }
        }
        // Score Vertical
        for (int col = 0; col < MAX_COLS; col++) {
            int[] colArr = makeColArr(board, col);
            for (int row = 0; row < MAX_ROWS - 3; row++) {
                int[] cell = new int[CELL_LENGTH];
                for (int i = 0; i < CELL_LENGTH; i++) {
                    cell[i] = colArr[row + i];
                }
                boardScore += evaluateCell(cell, piece);
            }
        }
        // score Positive Diagonally
        for (int row = 0; row < MAX_ROWS - 3; row++) {
            for (int col = 0; col < MAX_COLS - 3; col++) {
                int[] cell = new int[CELL_LENGTH];
                for (int i = 0; i < CELL_LENGTH; i++) {
                    cell[i] = board[row + i][col + i];
                }
                boardScore += evaluateCell(cell, piece);
            }
        }
        // score negative Diagonally
        for (int row = 0; row < MAX_ROWS - 3; row++) {
            for (int col = 0; col < MAX_COLS - 3; col++) {
                int[] cell = new int[CELL_LENGTH];
                for (int i = 0; i < CELL_LENGTH; i++) {
                    cell[i] = board[row + 3 - i][col + i];
                }
                boardScore += evaluateCell(cell, piece);
            }
        }
        return boardScore;
    }

    private static int[] makeColArr(int[][] board, int c) {
        int[] colArr = new int[MAX_ROWS];
        for (int row = 0; row < MAX_ROWS; row++) {
            colArr[row] = board[row][c];
        }
        return colArr;
    }

    private static int[] makeRowArr(int[][] board, int r) {
        int[] rowArr = new int[MAX_COLS];
        System.arraycopy(board[r], 0, rowArr, 0, MAX_COLS);
        return rowArr;
    }

    private static boolean isTerminalNode(int[][] terminalBoard) {
        return GameOver(terminalBoard, PLAYER) || GameOver(terminalBoard, AI) || getValidLocations(terminalBoard).isEmpty();
    }


    public static ArrayList<Integer> getValidLocations(int[][] board) {
        ArrayList<Integer> validLocations = new ArrayList<>();
        for (int c = 0; c < MAX_COLS; c++) {
            if (isValidLocation(board, c)) validLocations.add(c);
        }
        return validLocations;
    }


    private static int[] minimax(int[][] board, int depth, boolean maxPlayer,TreeNode node) {
        ArrayList<Integer> validLocations = getValidLocations(board);
        boolean isTerminalNode = isTerminalNode(board);
        TreeNode newTreeNode;
        if (depth == 0 || isTerminalNode) {
            if (isTerminalNode) {
                if (GameOver(board, AI)) {
                    return new int[]{-1, Integer.MAX_VALUE};
                }
                else if (GameOver(board, PLAYER)) {
                    return new int[]{-1, Integer.MIN_VALUE};
                } else // game is over, and there is no more valid moves
                    return new int[]{-1, 0};
            } else {
                return new int[]{-1, scorePosition(board, AI)};
            }
        }
        int value;
        int column = validLocations.get(FXGLMath.random(0, validLocations.size() - 1));
        if (maxPlayer) {
            value = Integer.MIN_VALUE;
            for (int col : validLocations) {
                int row = getOpenRow(board, col);
                int[][] boardCopy = new int[MAX_ROWS][MAX_COLS];
                for (int r = 0; r < MAX_ROWS; r++) {
                    System.arraycopy(board[r], 0, boardCopy[r], 0, MAX_COLS);
                }
                dropPiece(boardCopy, row, col, AI);
                newTreeNode = new TreeNode(Integer.toString(scorePosition(boardCopy,AI)));
                int newScore = minimax(boardCopy, depth - 1, false, newTreeNode)[1];
                newTreeNode.name = Integer.toString(newScore);
                if (newScore > value) {
                    value = newScore;
                    column = col;
                }
                node.addChild(newTreeNode);
                node.name = Integer.toString(value);
            }

        } else {
            value = Integer.MAX_VALUE;
            for (int col : validLocations) {
                int row = getOpenRow(board, col);
                int[][] boardCopy = new int[MAX_ROWS][MAX_COLS];
                for (int r = 0; r < MAX_ROWS; r++) {
                    System.arraycopy(board[r], 0, boardCopy[r], 0, MAX_COLS);
                }
                dropPiece(boardCopy, row, col, PLAYER);
                newTreeNode = new TreeNode(Integer.toString(scorePosition(boardCopy,PLAYER)));
                int newScore = minimax(boardCopy, depth - 1, true, newTreeNode)[1];
                newTreeNode.name = Integer.toString(newScore);
                if (newScore < value) {
                    value = newScore;
                    column = col;
                }
                node.addChild(newTreeNode);
                node.name = Integer.toString(value);
            }

        }
        return new int[]{column, value};
    }

    private static int[] minimax(int[][] board, int depth, int alpha, int beta, boolean maxPlayer,TreeNode node) {
        ArrayList<Integer> validLocations = getValidLocations(board);
        boolean isTerminalNode = isTerminalNode(board);
        TreeNode newTreeNode;
        if (depth == 0 || isTerminalNode) {
            if (isTerminalNode) {
                if (GameOver(board, AI)) return new int[]{-1, Integer.MAX_VALUE};
                else if (GameOver(board, PLAYER)) {
                    return new int[]{-1, Integer.MIN_VALUE};
                } else // game is over, and there is no more valid moves
                    return new int[]{-1, 0};
            } else {
                return new int[]{-1, scorePosition(board, AI)};
            }
        }
        int value;
        int column = validLocations.get(FXGLMath.random(0, validLocations.size() - 1));
        if (maxPlayer) {
            value = Integer.MIN_VALUE;
            for (int col : validLocations) {
                int row = getOpenRow(board, col);
                int[][] boardCopy = new int[MAX_ROWS][MAX_COLS];
                for (int r = 0; r < MAX_ROWS; r++) {
                    System.arraycopy(board[r], 0, boardCopy[r], 0, MAX_COLS);
                }
                dropPiece(boardCopy, row, col, AI);
//                System.out.println("------------------------");
//                System.out.println("MAX Current Board : ");
//                printBoard(boardCopy);
//                System.out.println("Depth : " + depth);
//                System.out.println("Score : " + scorePosition(boardCopy,AI));
//                System.out.println("------------------------");
                newTreeNode = new TreeNode(Integer.toString(scorePosition(boardCopy,AI)));
                int newScore = minimax(boardCopy, depth - 1, alpha, beta, false,newTreeNode)[1];
                if (newScore > value) {
                    value = newScore;
                    column = col;
                }
                alpha = Integer.max(alpha, value);
                newTreeNode.name = newScore + ":" + alpha + ":" + beta;
                if (alpha >= beta) break;
                node.addChild(newTreeNode);
                node.name = Integer.toString(value);
            }
        } else {
            value = Integer.MAX_VALUE;
            for (int col : validLocations) {
                int row = getOpenRow(board, col);
                int[][] boardCopy = new int[MAX_ROWS][MAX_COLS];
                for (int r = 0; r < MAX_ROWS; r++) {
                    System.arraycopy(board[r], 0, boardCopy[r], 0, MAX_COLS);
                }
                dropPiece(boardCopy, row, col, PLAYER);
//                System.out.println("------------------------");
//                System.out.println("MIN Current Board : ");
//                printBoard(boardCopy);
//                System.out.println("Depth : " + depth);
//                System.out.println("Score : " + scorePosition(boardCopy,PLAYER));
//                System.out.println("------------------------");
                newTreeNode = new TreeNode(Integer.toString(scorePosition(boardCopy,PLAYER)));
                int newScore = minimax(boardCopy, depth - 1, alpha, beta, true,newTreeNode)[1];
                if (newScore < value) {
                    value = newScore;
                    column = col;
                }
                beta = Integer.min(beta, value);
                newTreeNode.name = newScore + ":" + alpha + ":" + beta;
                if (alpha >= beta) break;
                node.addChild(newTreeNode);
                node.name = Integer.toString(value);
            }
        }
        return new int[]{column, value};
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAX_WIDTH);
        settings.setHeight(MAX_HEIGHT);
        settings.setTitle("Connect 4");
        settings.setVersion("0.0.1");
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
//        settings.setGameMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new ConnectFourMainMenu.MyMainMenu();
            }
        });

    }

    private void createTile() {
        int x, y;
        Color ballColor = Color.BLACK;
        for (int col = 0; col < MAX_COLS; col++, ballColor = Color.BLACK) {
            for (int row = MAX_ROWS - 1; row > -1; row--) {
                x = (col) * TILE_SIZE;
                y = (MAX_ROWS - row) * TILE_SIZE;
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
            int x = (int) getGameWorld().getSingleton(EntityType.PLAYER).getX();
            if (x > 0) {
                getGameWorld().getSingleton(EntityType.PLAYER).translateX(-SPEED);
            }

        });
        FXGL.onKey(KeyCode.D, () -> {
            int x = (int) getGameWorld().getSingleton(EntityType.PLAYER).getX();
            if (x < getGameScene().getAppWidth() - TILE_SIZE) {
                getGameWorld().getSingleton(EntityType.PLAYER).translateX(SPEED);
            }
        });
        FXGL.onKeyDown(KeyCode.S, () -> {
            int posX = (int) getGameWorld().getSingleton(EntityType.PLAYER).getX();
            boolean userPlayed = userTurn(posX);
            if (userPlayed) {
                AITurn();
            } else {
                showMessage("Please Choose a correct position", () -> getGameController().resumeEngine());
            }
            updateScore();
        });
        FXGL.onBtnDown(MouseButton.PRIMARY, () -> {
            System.out.println(input.getMousePositionUI());
            int posX = (int) input.getMouseXWorld();
            boolean userPlayed = userTurn(posX);
            if (userPlayed) {
                AITurn();
            } else {
                showMessage("Please Choose a correct position", () -> getGameController().resumeEngine());
            }
            updateScore();
        });
    }

    @Override
    protected void onUpdate(double tpf) {

        if (GameOver(TerminalBoard, PLAYER) && playerScore > aiScore) {
            System.out.println("Player 1 Wins");
            showMessage("Player 1 Wins\n" + "AI Score : " + aiScore + "\nPlayer Score : " + playerScore, () -> getGameController().exit());
        }
        if (GameOver(TerminalBoard, AI) && aiScore > playerScore) {
            showMessage("Player 2 Wins\n" + "AI Score : " + aiScore + "\nPlayer Score : " + playerScore, () -> getGameController().exit());
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
            System.out.println("--------------------");
            createTile();
            changeBallColor(Color.YELLOW);
            return true;
        }
        return false;
    }

    protected void AITurn() {
        boardTree = new TreeNode();
        if (alphaBeta) {
            startTime = System.currentTimeMillis();
            col = minimax(TerminalBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true,boardTree)[0];
            endTime = System.currentTimeMillis();
            System.out.println(boardTree);
            System.out.println("Time Taken : " + (endTime - startTime) + " Millie Seconds");

        } else {
            startTime = System.currentTimeMillis();
            col = minimax(TerminalBoard, 5, true,boardTree)[0];
            endTime = System.currentTimeMillis();
            System.out.println(boardTree);
            System.out.println("Time Taken : " + (endTime - startTime) + " Millie Seconds");
        }
        row = getOpenRow(TerminalBoard, col);
        dropPiece(TerminalBoard, row, col, AI);
        printBoard(TerminalBoard);
        System.out.println("--------------------");
        createTile();
        changeBallColor(Color.RED);
    }

    protected void updateScore() {
        aiScore += scorePosition(TerminalBoard, AI);
        playerScore += scorePosition(TerminalBoard, PLAYER);
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
