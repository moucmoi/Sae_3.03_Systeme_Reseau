import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static Map<String, ClientHandler> getClients() {
        return clients;
    }

    public static void main(String[] args) {
        String adresse = (args[0]);
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(adresse, 555));
            System.out.println("Serveur démarré. En attente de clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startGame(String player1, String player2) {
        ClientHandler client1 = clients.get(player1);
        ClientHandler client2 = clients.get(player2);

        Game game = new Game(player1, player2);
        game.start(
                new Scanner(client1.getInput()), client1.getOutput(),
                new Scanner(client2.getInput()), client2.getOutput()
        );
    }
}
