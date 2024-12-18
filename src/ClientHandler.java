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
            out.println("Please enter your name:");
            playerName = in.readLine();
            out.println("OK Connected as " + playerName);

            Server.getClients().put(playerName, this);

            String command;
            while ((command = in.readLine()) != null) {
                if (command.startsWith("challenge")) {
                    String opponent = command.split(" ")[1];
                    Server.handleChallenge(playerName, opponent);
                } else if (command.equals("accept")) {
                    String opponent = Server.getChallenges().get(playerName);
                    if (opponent != null) {
                        Server.startGame(playerName, opponent);
                    } else {
                        out.println("No challenge to accept.");
                    }
                } else {
                    out.println("Invalid command.");
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
}
