import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe Serveur.
 * Le serveur gère les connexions des joueurs et les sessions de jeu en cours.
 * Il accepte les connexions sur un port donné, crée des sessions de jeu et garde une trace des joueurs.
 */
public class Serveur {
    public final ConcurrentHashMap<String, ControleurClient> joueursEnAttente = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, GameSession> partiesActives = new ConcurrentHashMap<>();
    public final ControleurStats gestionnaireStats = new ControleurStats();

    /**
     * Démarre le serveur et attend des connexions sur le port spécifié.
     *
     * @param port Le port sur lequel le serveur écoute les connexions.
     */
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur démarré sur le port " + port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Nouvelle connexion acceptée : " + socket.getInetAddress());
                    ControleurClient handler = new ControleurClient(this, socket);
                    new Thread(handler).start();
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'acceptation d'une connexion : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
        }
    }

    /**
     * Point d'entrée du serveur, démarre le serveur en écoutant sur le port spécifié.
     *
     * @param args Arguments de la ligne de commande (le premier argument est le port).
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage : java Serveur <port>");
            return;
        }
        try {
            int port = Integer.parseInt(args[0]);
            new Serveur().start(port);
        } catch (NumberFormatException e) {
            System.err.println("Le port doit être un nombre valide.");
        }
    }
}
