import java.util.Scanner;

public class Game {
    private String joueur1;
    private String joueur2;
    private String joueurActuel;
    private Plateau plateau;

    private static final char SYMBOLE_JOUEUR1 = 'X';
    private static final char SYMBOLE_JOUEUR2 = 'O';

    public Game(String joueur1, String joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurActuel = joueur1; // Le joueur1 commence par défaut
        this.plateau = new Plateau();
    }

    public void start() {
        System.out.println("Le jeu entre " + joueur1 + " et " + joueur2 + " a commencé !");
        Scanner scanner = new Scanner(System.in);

        boolean jeuTermine = false;
        char symboleActuel = SYMBOLE_JOUEUR1;

        plateau.afficherPlateau();

        while (!jeuTermine) {
            System.out.println("C'est au tour de " + (joueurActuel.equals(joueur1) ? joueur1 : joueur2) + ".");
            System.out.println("Choisissez une colonne (1-7) :");

            int col;
            while (true) {
                col = scanner.nextInt() - 1; // Convertir en index 0-based
                if (col < 0 || col >= 7 || plateau.estColonnePleine(col)) {
                    System.out.println("Colonne invalide. Essayez à nouveau.");
                } else {
                    break;
                }
            }

            int ligne = plateau.deposerJeton(col, symboleActuel);
            plateau.afficherPlateau();

            if (plateau.verifierVictoire(ligne, col, symboleActuel)) {
                System.out.println("Félicitations ! " + (joueurActuel.equals(joueur1) ? joueur1 : joueur2) + " a gagné !");
                jeuTermine = true;
            } else if (plateau.estPlein()) {
                System.out.println("La partie est un match nul !");
                jeuTermine = true;
            } else {
                // Changer de joueur
                joueurActuel = joueurActuel.equals(joueur1) ? joueur2 : joueur1;
                symboleActuel = (symboleActuel == SYMBOLE_JOUEUR1) ? SYMBOLE_JOUEUR2 : SYMBOLE_JOUEUR1;
            }
        }

        scanner.close();
    }
}
