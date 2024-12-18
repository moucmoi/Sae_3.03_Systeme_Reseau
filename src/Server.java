import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server started...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            new Thread(clientHandler).start();
        }
    }

    public static void addClient(String name, ClientHandler clientHandler) {
        clients.put(name, clientHandler);
    }

    public static ClientHandler getClient(String name) {
        return clients.get(name);
    }

    public static void removeClient(String name) {
        clients.remove(name);
    }
}
