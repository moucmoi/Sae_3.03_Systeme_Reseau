public class Plateau {
    private static final int LIGNES = 6;
    private static final int COLONNES = 7;
    private static final char VIDE = '.';
    private char[][] plateau;

    public Plateau() {
        plateau = new char[LIGNES][COLONNES];
        initialiserPlateau();
    }

    // Initialise le plateau avec des cases vides
    public void initialiserPlateau() {
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                plateau[i][j] = VIDE;
            }
        }
    }

    // Affiche le plateau
    public void afficherPlateau() {
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                System.out.print(plateau[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("1 2 3 4 5 6 7"); // Nombres des colonnes
    }

    // Vérifie si une colonne est pleine
    public boolean estColonnePleine(int col) {
        return plateau[0][col] != VIDE;
    }

    // Dépose un jeton dans une colonne et retourne la ligne où il est tombé
    public int deposerJeton(int col, char symboleJoueur) {
        for (int ligne = LIGNES - 1; ligne >= 0; ligne--) {
            if (plateau[ligne][col] == VIDE) {
                plateau[ligne][col] = symboleJoueur;
                return ligne;
            }
        }
        return -1; // Retourne -1 si la colonne est pleine (mais cela ne devrait pas arriver)
    }

    // Vérifie si le plateau est plein
    public boolean estPlein() {
        for (int j = 0; j < COLONNES; j++) {
            if (plateau[0][j] == VIDE) {
                return false;
            }
        }
        return true;
    }

    // Vérifie s'il y a une victoire dans la direction donnée (horizontale, verticale ou diagonale)
    public boolean verifierVictoire(int ligne, int col, char symboleJoueur) {
        return verifierDirection(ligne, col, symboleJoueur, 1, 0) || // Verticale
               verifierDirection(ligne, col, symboleJoueur, 0, 1) || // Horizontale
               verifierDirection(ligne, col, symboleJoueur, 1, 1) || // Diagonale /
               verifierDirection(ligne, col, symboleJoueur, 1, -1);  // Diagonale \
    }

    private boolean verifierDirection(int ligne, int col, char symboleJoueur, int deltaLigne, int deltaCol) {
        int compteur = 1;
        compteur += compterJetons(ligne, col, symboleJoueur, deltaLigne, deltaCol);
        compteur += compterJetons(ligne, col, symboleJoueur, -deltaLigne, -deltaCol);
        return compteur >= 4;
    }

    private int compterJetons(int ligne, int col, char symboleJoueur, int deltaLigne, int deltaCol) {
        int compteur = 0;
        int l = ligne + deltaLigne;
        int c = col + deltaCol;
        while (l >= 0 && l < LIGNES && c >= 0 && c < COLONNES && plateau[l][c] == symboleJoueur) {
            compteur++;
            l += deltaLigne;
            c += deltaCol;
        }
        return compteur;
    }

    // Getter pour le tableau du plateau
    public char[][] getPlateau() {
        return plateau;
    }
}
