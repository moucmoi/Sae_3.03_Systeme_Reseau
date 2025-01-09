import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            out.println("Entrez votre nom:");
            playerName = in.readLine();
            out.println("OK Connecté en tant que " + playerName);

            Server.getClients().put(playerName, this);

            out.println("Joueurs connectés: " + Server.getClients().keySet());
            out.println("Envoyez 'challenge [nom]' pour défier un autre joueur.");

            String command;
            while ((command = in.readLine()) != null) {
                if (command.startsWith("challenge")) {
                    String opponent = command.split(" ")[1];
                    if (Server.getClients().containsKey(opponent)) {
                        out.println("Vous avez défié " + opponent + ". Attente de l'acceptation.");
                        ClientHandler opponentHandler = Server.getClients().get(opponent);
                        opponentHandler.sendMessage(playerName + " vous défie. Tapez 'accept' pour accepter.");
                    } else {
                        out.println("Joueur " + opponent + " introuvable.");
                    }
                } else if (command.equals("accept")) {
                    String opponent = Server.getClients().keySet().stream()
                            .filter(name -> !name.equals(playerName))
                            .findFirst()
                            .orElse(null);

                    if (opponent != null) {
                        Server.startGame(playerName, opponent);
                    } else {
                        out.println("Aucun défi à accepter.");
                    }
                } else {
                    out.println("Commande invalide.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                Server.getClients().remove(playerName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public PrintWriter getOutput() {
        return out;
    }

    public BufferedReader getInput() {
        return in;
    }

    public String getPlayerName() {
        return playerName;
    }
}
