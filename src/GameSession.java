import java.util.Arrays;

/**
 * Classe GameSession.
 * Représente une session de jeu de type Puissance 4 entre deux joueurs.
 * Cette classe gère la grille de jeu, l'alternance des joueurs, ainsi que la vérification de la victoire ou du match nul.
 */
public class GameSession {

    // Constantes représentant les dimensions de la grille
    private static final int ROWS = 6;  // Nombre de lignes de la grille
    private static final int COLS = 7;  // Nombre de colonnes de la grille
    private final String joueur1;
    private final String joueur2;
    private final char[][] grille;      // La grille de jeu, initialisée avec des '-'
    private String joueurActuel;
    private boolean partieTerminee;

    /**
     * Crée une nouvelle session de jeu avec les deux joueurs spécifiés.
     * La grille est initialisée et le premier joueur commence.
     * Le premier joueur est celui qui lance la partie avec ask NomDuJoueur.
     *
     * @param joueur1 Le nom du premier joueur.
     * @param joueur2 Le nom du deuxième joueur.
     */
    public GameSession(String joueur1, String joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.grille = new char[ROWS][COLS];
        for (char[] ligne : grille) {
            Arrays.fill(ligne, '-');
        }
        this.joueurActuel = joueur1;
        this.partieTerminee = false;
    }

    /**
     * Effectue un coup dans une colonne donnée pour le joueur actuel.
     * Vérifie si le coup est valide, met à jour la grille et vérifie si un joueur a gagné ou si la partie est nulle.
     *
     * @param playerName Le nom du joueur effectuant le coup.
     * @param column     Le numéro de la colonne où le joueur souhaite jouer (1-indexé).
     * @return true si le coup a été effectué avec succès, false sinon.
     */
    public synchronized boolean jouerCoup(String playerName, int column) {
        if (partieTerminee) {
            return false;
        }
        if (!playerName.equals(joueurActuel)) {
            return false;
        }
        if (column < 1 || column > COLS) {
            return false;
        }
        column--;
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grille[row][column] == '-') {
                grille[row][column] = playerName.equals(joueur1) ? 'X' : 'O';
                if (verifierVictoire(playerName)) {
                    partieTerminee = true;
                } else if (estNul()) {
                    partieTerminee = true;
                } else {
                    joueurActuel = joueurActuel.equals(joueur1) ? joueur2 : joueur1;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère l'état actuel de la grille sous forme de chaîne de caractères.
     *
     * @return L'état actuel de la grille sous forme de chaîne de caractères.
     */
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

    /**
     * Vérifie si le joueur spécifié a gagné la partie.
     * Un joueur gagne s'il aligne 4 de ses symboles horizontalement, verticalement ou en diagonale.
     *
     * @param player Le nom du joueur à vérifier.
     * @return true si le joueur a gagné, false sinon.
     */
    public boolean verifierVictoire(String player) {
        char symbol = player.equals(joueur1) ? 'X' : 'O';
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (verifierAlignement(row, col, symbol, 1, 0) ||
                    verifierAlignement(row, col, symbol, 0, 1) ||
                    verifierAlignement(row, col, symbol, 1, 1) ||
                    verifierAlignement(row, col, symbol, 1, -1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si un alignement de 4 symboles du joueur existe à partir d'une position donnée dans une direction donnée.
     *
     * @param row    La ligne de départ de l'alignement.
     * @param col    La colonne de départ de l'alignement.
     * @param symbol Le symbole du joueur ('X' ou 'O').
     * @param dRow   Le déplacement sur les lignes.
     * @param dCol   Le déplacement sur les colonnes.
     * @return true si un alignement est trouvé, false sinon.
     */
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

    /**
     * Vérifie si la partie est nulle (aucune colonne vide).
     *
     * @return true si la partie est nulle, false sinon.
     */
    public boolean estNul() {
        for (int col = 0; col < COLS; col++) {
            if (grille[0][col] == '-') {
                return false;
            }
        }
        return true;
    }

    /**
     * Récupère la grille actuelle sous forme de tableau de caractères.
     *
     * @return Le tableau représentant la grille actuelle.
     */
    public synchronized char[][] getGrille() {
        return grille;
    }

    /**
     * Récupère le nom du joueur dont c'est le tour.
     *
     * @return Le nom du joueur dont c'est le tour.
     */
    public String getJoueurActuel() {
        return joueurActuel;
    }

    /**
     * Récupère le nom du premier joueur.
     *
     * @return Le nom du premier joueur.
     */
    public String getJoueur1() {
        return joueur1;
    }

    /**
     * Récupère le nom du deuxième joueur.
     *
     * @return Le nom du deuxième joueur.
     */
    public String getJoueur2() {
        return joueur2;
    }

    /**
     * Vérifie si la partie est terminée.
     *
     * @return true si la partie est terminée, false sinon.
     */
    public boolean getPartieTerminee() {
        return partieTerminee;
    }
}
