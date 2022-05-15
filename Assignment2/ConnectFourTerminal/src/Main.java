import java.util.Scanner;

public class Main {
    static Scanner console = new Scanner(System.in);
    private static final int MAX_ROWS = 6, MAX_COLS = 7;
    private static final int[] PIECES = {1, 2};

    public static int[][] createBoard() {
        int[][] matrix = new int[MAX_ROWS][MAX_COLS];
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                System.out.print(matrix[row][col] + "\t");
            }
            System.out.println();
        }
        return matrix;
    }

    public static void dropPiece(int[][] board, int row, int col, int piece) {
        board[row][col] = piece;
    }

    public static boolean isValidLocation(int[][] board, int col) {
        return board[0][col] == 0;
    }

    public static int getOpenRow(int[][] board, int col) {
        int openRow = -1;
        for (int row = MAX_ROWS - 1; row > -1; row--) {
            if (board[row][col] == 0) {
                openRow = row;
                break;
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

    public static int getInput(int i) {
        int input;
        do {
            System.out.printf("Player %d make your selection (0-6): ", i);
            input = console.nextInt();
            System.out.println("input = " + input);
        } while (input > 6 || input < 0);
        return input;
    }

    public static boolean isSolvable(int[][] board, int piece) {
        boolean solved = false;
        // Horizontal Check
        for (int col = 0; col < MAX_COLS - 3 && !solved; col++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                if (board[row][col] == piece
                        && board[row][col + 1] == piece
                        && board[row][col + 2] == piece
                        && board[row][col + 3] == piece) {
                    solved = true;
                    break;
                }
            }
        }
        // Vertical check
        for (int col = 0; col < MAX_COLS && !solved; col++) {
            for (int row = 0; row < MAX_ROWS - 3; row++) {
                if (board[row][col] == piece
                        && board[row + 1][col] == piece
                        && board[row + 2][col] == piece
                        && board[row + 3][col] == piece) {
                    solved = true;
                    break;
                }
            }
        }
        // Diagonal check positive direction
        for (int col = 0; col < MAX_COLS - 3 && !solved; col++) {
            for (int row = 0; row < MAX_ROWS - 3; row++) {
                if (board[row][col] == piece
                        && board[row + 1][col + 1] == piece
                        && board[row + 2][col + 2] == piece
                        && board[row + 3][col + 3] == piece) {
                    solved = true;
                    break;
                }
            }
        }
        // Diagonal Check Negative Direction
        for (int col = 0; col < MAX_COLS - 3 && !solved; col++) {
            for (int row = 3; row < MAX_ROWS; row++) {
                if (board[row][col] == piece
                        && board[row - 1][col + 1] == piece
                        && board[row - 2][col + 2] == piece
                        && board[row - 3][col + 3] == piece) {
                    solved = true;
                    break;
                }
            }
        }
        return solved;
    }

    public static void main(String[] args) {
        int col, row, turn = 0;
        System.out.println("Hello World this is just testing");
        boolean gameOver = false;

        int[][] board = createBoard();
        while (!gameOver) {
            if (turn % 2 == 0) {
                col = getInput(1);
                if (isValidLocation(board, col)) {
                    row = getOpenRow(board, col);
                    dropPiece(board, row, col, PIECES[0]);
                    printBoard(board);
                    if (isSolvable(board, PIECES[0])) {
                        gameOver = true;
                        System.out.println("Player 1 Wins");
                    }
                }
            } else {
                col = getInput(2);
                if (isValidLocation(board, col)) {
                    row = getOpenRow(board, col);
                    dropPiece(board, row, col, PIECES[1]);
                    printBoard(board);
                    if (isSolvable(board, PIECES[1])) {
                        gameOver = true;
                        System.out.println("Player 2 wins");
                    }

                }
            }
            turn++;
        }
    }
}
