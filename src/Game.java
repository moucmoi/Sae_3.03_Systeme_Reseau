import java.io.PrintWriter;
import java.util.Scanner;

public class Game {
    private String joueur1;
    private String joueur2;
    private Plateau plateau;
    private boolean partieTerminee;

    public Game(String joueur1, String joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.plateau = new Plateau();
        this.partieTerminee = false;
    }

    public void start(Scanner input1, PrintWriter output1, Scanner input2, PrintWriter output2) {
        plateau.initialiser();
        char jetonCourant = 'X';
        String joueurCourant = joueur1;
        PrintWriter outputCourant;
        Scanner inputCourant;
        
        while (!partieTerminee) {
            outputCourant = joueurCourant.equals(joueur1) ? output1 : output2;
            inputCourant = joueurCourant.equals(joueur1) ? input1 : input2;

            plateau.afficher(outputCourant);
            outputCourant.println("À vous de jouer (" + jetonCourant + "). Choisissez une colonne (1-7) :");

            int colonne;
            try {
                colonne = Integer.parseInt(inputCourant.nextLine()) - 1;
            } catch (NumberFormatException e) {
                outputCourant.println("Entrée invalide. Réessayez.");
                continue;
            }

            if (colonne < 0 || colonne >= 7 || !plateau.ajouterJeton(colonne, jetonCourant)) {
                outputCourant.println("Colonne invalide ou pleine. Réessayez.");
                continue;
            }

            if (plateau.verifierVictoire(colonne, jetonCourant)) {
                partieTerminee = true;
                outputCourant.println("Félicitations ! Vous avez gagné.");
                break;
            }

            if (plateau.estPlein()) {
                output1.println("Match nul !");
                output2.println("Match nul !");
                partieTerminee = true;
                break;
            }

            joueurCourant = joueurCourant.equals(joueur1) ? joueur2 : joueur1;
            jetonCourant = (jetonCourant == 'X') ? 'O' : 'X';
        }
    }
}
