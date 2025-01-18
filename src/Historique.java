import java.io.*;
import java.util.*;

/**
 * La classe Historique permet de gérer l'historique des parties jouées.
 * Elle permet d'ajouter une partie à l'historique et de récupérer les parties jouées par un joueur spécifique.
 * L'historique est stocké dans un fichier CSV.
 */
public class Historique {
    // Nom du fichier pour enregistrer l'historique
    private static final String FILE_NAME = "historique.csv";

    /**
     * Ajoute une entrée dans l'historique des parties jouées.
     * 
     * @param joueur Le nom du joueur ayant joué la partie.
     * @param adversaire Le nom de l'adversaire.
     * @param res Le résultat de la partie (par exemple "win", "lose", "draw").
     */
    public static synchronized void ajoutHisto(String joueur, String adversaire, String res) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter sortie = new PrintWriter(bw)) {

            // Ajout de l'entrée dans le fichier CSV
            sortie.println(String.format("%s,%s,%s,%s", joueur, adversaire, res, new Date()));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture de l'historique : " + e.getMessage());
        }
    }

    /**
     * Récupère l'historique des parties d'un joueur spécifique.
     * 
     * @param joueur Le nom du joueur dont on souhaite récupérer l'historique.
     * @return Une liste de chaînes contenant les informations des parties du joueur.
     */
    public static List<String> getHisto(String joueur) {
        List<String> historique = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // Lecture de chaque ligne du fichier CSV
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Si la ligne correspond au joueur, on l'ajoute à l'historique
                if (parts.length == 4 && parts[0].equals(joueur)) {
                    historique.add(String.format("Adversaire: %s, Résultat: %s, Date: %s", parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de l'historique : " + e.getMessage());
        }
        return historique;
    }
}
