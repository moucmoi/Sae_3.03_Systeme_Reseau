import java.io.*;
import java.net.*;

public class Client {
    private final String servAddresse;
    private final int servPort;
    private PrintWriter sortie;
    private BufferedReader entree;

    public Client(String servAddresse, int servPort) {
        this.servAddresse = servAddresse;
        this.servPort = servPort;
    }

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

    // Usage : java -cp bin Client <adresse_serveur> <port>
    public static void main(String[] args) {
        String servAddresse = args[0];
        int servPort = Integer.parseInt(args[1]);
        new Client(servAddresse, servPort).start();
    }
}