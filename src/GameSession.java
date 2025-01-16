import java.util.Arrays;

public class GameSession {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private final String joueur1;
    private final String joueur2;
    private final char[][] grille;
    private String joueurActuel;
    private boolean partieTerminee;

    public GameSession(String joueur1, String joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.grille = new char[ROWS][COLS];
        for (char[] ligne : grille) {
            Arrays.fill(ligne, '-');
        }
        this.joueurActuel = joueur1; // Le premier joueur commence
        this.partieTerminee = false;
    }

    public synchronized boolean jouerCoup(String playerName, int column) {
        if (partieTerminee) {
            return false; // La partie est déjà terminée
        }
        if (!playerName.equals(joueurActuel)) {
            return false; // Ce n'est pas le tour du joueur
        }
        if (column < 1 || column > COLS) {
            return false; // Colonne invalide
        }
        column--; // Convertir en index 0
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grille[row][column] == '-') {
                grille[row][column] = playerName.equals(joueur1) ? 'X' : 'O';
                if (verifierVictoire(playerName)) {
                    partieTerminee = true;
                } else if (estNul()) {
                    partieTerminee = true;
                } else {
                    joueurActuel = joueurActuel.equals(joueur1) ? joueur2 : joueur1; // Changer de joueur
                }
                return true;
            }
        }
        return false; // Colonne pleine
    }

    public synchronized String getEtatGrille() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                sb.append(grille[row][col]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean verifierVictoire(String player) {
        char symbol = player.equals(joueur1) ? 'X' : 'O';
        // Vérifie les alignements horizontaux, verticaux et diagonaux
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (verifierAlignement(row, col, symbol, 1, 0) || // Horizontal
                    verifierAlignement(row, col, symbol, 0, 1) || // Vertical
                    verifierAlignement(row, col, symbol, 1, 1) || // Diagonale \
                    verifierAlignement(row, col, symbol, 1, -1)) { // Diagonale /
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifierAlignement(int row, int col, char symbol, int dRow, int dCol) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r < 0 || r >= ROWS || c < 0 || c >= COLS || grille[r][c] != symbol) {
                return false;
            }
            count++;
        }
        return count == 4;
    }

    public boolean estNul() {
        for (int col = 0; col < COLS; col++) {
            if (grille [0][col] == '-') {
                return false; // Une colonne n'est pas pleine
            }
        }
        return true;
    }

    public synchronized char[][] getGrille() {
        return grille;
    }

    public String getJoueurActuel() {
        return joueurActuel;
    }

    public String getJoueur1() {
        return joueur1;
    }

    public String getJoueur2() {
        return joueur2;
    }

    public boolean getPartieTerminee() {
        return partieTerminee;
    }
}