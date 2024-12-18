import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, ClientHandler> clients = new HashMap<>();
    private static Map<String, String> challenges = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void startGame(String player1, String player2) {
        Game game = new Game(player1, player2);
        game.start();
    }

    public static Map<String, ClientHandler> getClients() {
        return clients;
    }

    public static Map<String, String> getChallenges() {
        return challenges;
    }

    public static void handleChallenge(String challenger, String opponent) {
        challenges.put(opponent, challenger);
    }

}
