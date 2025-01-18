import java.io.*;
import java.net.*;

/**
 * Classe Client.
 * Elle gère la communication avec le serveur via des entrées et des sorties.
 * Elle permet à un utilisateur d'envoyer des commandes au serveur et de recevoir des messages en réponse.
 */
public class Client {
    
    private final String servAddresse;
    private final int servPort;
    private PrintWriter sortie;
    private BufferedReader entree;

    /**
     * Constructeur de la classe {@code Client}.
     *
     * @param servAddresse L'adresse du serveur auquel le client se connecte.
     * @param servPort Le port du serveur auquel le client se connecte.
     */
    public Client(String servAddresse, int servPort) {
        this.servAddresse = servAddresse;
        this.servPort = servPort;
    }

    /**
     * Méthode qui démarre la connexion au serveur, envoie les commandes de l'utilisateur et affiche les messages du serveur.
     * Elle crée un thread séparé pour lire les messages du serveur et les afficher.
     * Ensuite, elle attend les commandes de l'utilisateur via la console, les envoie au serveur et les transmet via le flux de sortie.
     */
    public void start() {
        try (Socket socket = new Socket(servAddresse, servPort)) {
            sortie = new PrintWriter(socket.getOutputStream(), true);
            entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            Thread getMessageServ = new Thread(() -> {
                try {
                    String ligne;
                    while ((ligne = entree.readLine()) != null) {
                        System.out.println(ligne);
                    }
                } catch (IOException e) {
                    System.err.println("Erreur de lecture des messages du serveur : " + e.getMessage());
                }
            });
            getMessageServ.start();
            String commande;
            while ((commande = console.readLine()) != null) {
                if (commande.trim().isEmpty()) {
                    continue;
                }
                sortie.println(commande);
            }
        } catch (IOException e) {
            System.err.println("Erreur de connexion au serveur : " + e.getMessage());
        }
    }

    /**
     * Méthode principale pour exécuter le client en ligne de commande.
     * Elle prend en paramètre l'adresse du serveur et le port à utiliser pour la connexion.
     *
     * @param args Les arguments de la ligne de commande : adresse du serveur et port du serveur.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage : java -cp bin Client <adresse_serveur> <port>");
            return;
        }
        String servAddresse = args[0];
        int servPort = Integer.parseInt(args[1]);
        new Client(servAddresse, servPort).start();
    }
}
