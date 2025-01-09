import java.io.PrintWriter;

public class Plateau {
    private static final int LIGNES = 6;
    private static final int COLONNES = 7;
    private static final char VIDE = '.';
    private char[][] grille;

    public Plateau() {
        this.grille = new char[LIGNES][COLONNES];
        initialiser();
    }

    public void initialiser() {
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                grille[i][j] = VIDE;
            }
        }
    }

    public void afficher(PrintWriter output) {
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                output.print(grille[i][j] + " ");
            }
            output.println();
        }
        output.println("1 2 3 4 5 6 7");
    }

    public boolean ajouterJeton(int colonne, char jeton) {
        for (int i = LIGNES - 1; i >= 0; i--) {
            if (grille[i][colonne] == VIDE) {
                grille[i][colonne] = jeton;
                return true;
            }
        }
        return false;
    }

    public boolean verifierVictoire(int colonne, char jeton) {
        for (int i = 0; i < LIGNES; i++) {
            if (grille[i][colonne] == jeton && (
                verifierDirection(i, colonne, jeton, 1, 0) ||
                verifierDirection(i, colonne, jeton, 0, 1) ||
                verifierDirection(i, colonne, jeton, 1, 1) ||
                verifierDirection(i, colonne, jeton, 1, -1))) {
                return true;
            }
        }
        return false;
    }

    private boolean verifierDirection(int ligne, int colonne, char jeton, int dLigne, int dColonne) {
        int count = 1;
        count += compterJetons(ligne, colonne, jeton, dLigne, dColonne);
        count += compterJetons(ligne, colonne, jeton, -dLigne, -dColonne);
        return count >= 4;
    }

    private int compterJetons(int ligne, int colonne, char jeton, int dLigne, int dColonne) {
        int count = 0;
        int l = ligne + dLigne;
        int c = colonne + dColonne;
        while (l >= 0 && l < LIGNES && c >= 0 && c < COLONNES && grille[l][c] == jeton) {
            count++;
            l += dLigne;
            c += dColonne;
        }
        return count;
    }

    public boolean estPlein() {
        for (int j = 0; j < COLONNES; j++) {
            if (grille[0][j] == VIDE) {
                return false;
            }
        }
        return true;
    }
}
