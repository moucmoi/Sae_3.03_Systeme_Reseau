import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            out.println("Please enter your name: ");
            playerName = in.readLine();
            Server.addClient(playerName, this);
            out.println("OK Connected as " + playerName);

            String command;
            while ((command = in.readLine()) != null) {
                if (command.startsWith("challenge")) {
                    String opponentName = command.split(" ")[1];
                    ClientHandler opponent = Server.getClient(opponentName);

                    if (opponent != null) {
                        opponent.getOut().println(playerName + " has challenged you! Type 'accept' to play.");
                        String response = opponent.getIn().readLine();

                        if ("accept".equalsIgnoreCase(response)) {
                            out.println("OK Challenge accepted, starting game!");
                            opponent.getOut().println("OK Challenge accepted, starting game!");

                            // Start the game
                            new Game(this, opponent).start();
                        } else {
                            out.println(opponentName + " declined the challenge.");
                        }
                    } else {
                        out.println("ERR Player not found.");
                    }
                } else {
                    out.println("Unknown command.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Server.removeClient(playerName);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public String getPlayerName() {
        return playerName;
    }
}
