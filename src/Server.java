import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Serveur démarré. En attente de clients...");

            Socket client1Socket = serverSocket.accept();
            ClientHandler client1 = new ClientHandler(client1Socket);
            client1.start();

            Socket client2Socket = serverSocket.accept();
            ClientHandler client2 = new ClientHandler(client2Socket);
            client2.start();

            client1.join();
            client2.join();

            Game game = new Game(client1.getNom(), client2.getNom());
            game.start(
                new Scanner(client1.getInput()), client1.getOutput(),
                new Scanner(client2.getInput()), client2.getOutput()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
