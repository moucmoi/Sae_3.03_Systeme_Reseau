import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Serveur {
    public final ConcurrentHashMap<String, ControleurClient> joueursEnAttente = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, GameSession> partiesActives = new ConcurrentHashMap<>();
    public final ControleurStats gestionnaireStats = new ControleurStats();

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur démarré sur le port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                ControleurClient handler = new ControleurClient(this, socket);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Serveur().start(12345);
    }
}