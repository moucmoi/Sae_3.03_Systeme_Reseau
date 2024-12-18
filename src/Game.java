import java.io.PrintWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Game {
    private ClientHandler player1;
    private ClientHandler player2;
    private String[][] board = new String[6][7];
    private boolean gameWon = false;

    public Game(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;

        // Initialize board with empty cells
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = ".";
            }
        }
    }

    public void start() throws IOException {
        ClientHandler currentPlayer = player1;
        ClientHandler otherPlayer = player2;

        while (!gameWon) {
            currentPlayer.getOut().println("Your turn, " + currentPlayer.getPlayerName() + ". Choose a column (1-7): ");
            displayBoard(currentPlayer.getOut());
            displayBoard(otherPlayer.getOut());

            int column = Integer.parseInt(currentPlayer.getIn().readLine()) - 1;

            if (placeToken(column, currentPlayer.getPlayerName())) {
                gameWon = checkWinCondition(currentPlayer.getPlayerName());

                if (gameWon) {
                    currentPlayer.getOut().println("Congratulations! You won!");
                    otherPlayer.getOut().println("Game over! " + currentPlayer.getPlayerName() + " won!");
                }

                // Swap players
                ClientHandler temp = currentPlayer;
                currentPlayer = otherPlayer;
                otherPlayer = temp;
            } else {
                currentPlayer.getOut().println("Invalid move. Try again.");
            }
        }
    }

    private boolean placeToken(int column, String playerName) {
        if (column < 0 || column >= 7) return false;

        for (int i = 5; i >= 0; i--) {
            if (board[i][column].equals(".")) {
                board[i][column] = playerName.equals(player1.getPlayerName()) ? "X" : "O";
                return true;
            }
        }
        return false;
    }

    private boolean checkWinCondition(String playerName) {
        String token = playerName.equals(player1.getPlayerName()) ? "X" : "O";

        // Check rows, columns, and diagonals
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (checkDirection(i, j, 1, 0, token) || // Horizontal
                    checkDirection(i, j, 0, 1, token) || // Vertical
                    checkDirection(i, j, 1, 1, token) || // Diagonal \
                    checkDirection(i, j, 1, -1, token))  // Diagonal /
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, String token) {
        int count = 0;

        for (int i = 0; i < 4; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;

            if (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r][c].equals(token)) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
    }

    private void displayBoard(PrintWriter out) {
        for (String[] row : board) {
            for (String cell : row) {
                out.print(cell + " ");
            }
            out.println();
        }
        out.println();
    }
}
