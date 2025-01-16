import java.io.*;
import java.util.*;

public class Historique {
    private static final String FILE_NAME = "historique.csv";

    // quand une partie se fait ou se fini, la partie s'ajoute à l'historique
    public static synchronized void ajoutHisto(String joueur, String adversaire, String res) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter sortie = new PrintWriter(bw)) {

            sortie.println(String.format("%s,%s,%s,%s", joueur, adversaire, res));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture de l'historique : " + e.getMessage());
        }
    }

    //récupérer l'historique
    public static List<String> getHisto(String joueur) {
        List<String> historique = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
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