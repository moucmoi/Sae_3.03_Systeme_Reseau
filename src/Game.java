import java.util.Scanner;

public class Game {
    private String player1;
    private String player2;
    private String currentPlayer;
    private boolean gameOver;

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = '.';
    private static final char PLAYER1 = 'X';
    private static final char PLAYER2 = 'O';
    private static char[][] board = new char[ROWS][COLS];

    public Game(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.board = new char[6][7];
        this.gameOver = false;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void start() {
        System.out.println("Game started between " + player1 + " and " + player2);
        Scanner scanner = new Scanner(System.in);
        initializeBoard();
        boolean gameOver = false;
        char currentPlayer = PLAYER1;

        System.out.println("Welcome to Connect Four!");
        printBoard();

        while (!gameOver) {
            System.out.println("Player " + (currentPlayer == PLAYER1 ? "1 (X)" : "2 (O)") + ", choose a column (1-7):");
            int col;
            while (true) {
                col = scanner.nextInt() - 1;
                if (col < 0 || col >= COLS || board[0][col] != EMPTY) {
                    System.out.println("Invalid column. Please try again.");
                } else {
                    break;
                }
            }

            int row = dropToken(col, currentPlayer);
            printBoard();

            if (checkWin(row, col, currentPlayer)) {
                System.out.println("Player " + (currentPlayer == PLAYER1 ? "1 (X)" : "2 (O)") + " wins!");
                gameOver = true;
            } else if (isBoardFull()) {
                System.out.println("It's a draw!");
                gameOver = true;
            }

            currentPlayer = (currentPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
        }

        scanner.close();
    }

    private static void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("1 2 3 4 5 6 7");
    }

    private static int dropToken(int col, char player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                return row;
            }
        }
        return -1;
    }

    private static boolean checkWin(int row, int col, char player) {
        return checkDirection(row, col, player, 1, 0) ||
               checkDirection(row, col, player, 0, 1) ||
               checkDirection(row, col, player, 1, 1) ||
               checkDirection(row, col, player, 1, -1);
    }

    private static boolean checkDirection(int row, int col, char player, int rowDelta, int colDelta) {
        int count = 1;
        count += countTokens(row, col, player, rowDelta, colDelta);
        count += countTokens(row, col, player, -rowDelta, -colDelta);
        return count >= 4;
    }

    private static int countTokens(int row, int col, char player, int rowDelta, int colDelta) {
        int count = 0;
        int r = row + rowDelta;
        int c = col + colDelta;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
            count++;
            r += rowDelta;
            c += colDelta;
        }
        return count;
    }

    private static boolean isBoardFull() {
        for (int j = 0; j < COLS; j++) {
            if (board[0][j] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    public boolean makeMove(int column) {
        if (column < 0 || column >= 7 || board[0][column] != ' ') {
            return false;
        }

        int row = 5;
        while (row >= 0 && board[row][column] != ' ') {
            row--;
        }

        if (row >= 0) {
            board[row][column] = currentPlayer.equals(player1) ? 'X' : 'O';
            if (checkWin(row, column)) {
                gameOver = true;
                return true;
            }
            currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
            return true;
        }
        return false;
    }

    private boolean checkWin(int row, int col) {
        char symbol = board[row][col];
        return (checkDirection(row, col, 1, 0, symbol) ||
                checkDirection(row, col, 0, 1, symbol) ||
                checkDirection(row, col, 1, 1, symbol) ||
                checkDirection(row, col, 1, -1, symbol));
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, char symbol) {
        int count = 1;
        int r = row + dRow;
        int c = col + dCol;
        while (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r][c] == symbol) {
            count++;
            r += dRow;
            c += dCol;
        }

        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r][c] == symbol) {
            count++;
            r -= dRow;
            c -= dCol;
        }

        return count >= 4;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public char[][] getBoard() {
        return board;
    }

    public String getWinner() {
        return gameOver ? currentPlayer : null;
    }
}
