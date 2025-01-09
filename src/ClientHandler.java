import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private String nom;
    private PrintWriter output;
    private BufferedReader input;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    public String getNom() {
        return nom;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public BufferedReader getInput() {
        return input;
    }

    @Override
    public void run() {
        try {
            output.println("Entrez votre nom :");
            this.nom = input.readLine();
            output.println("Bienvenue " + nom + " !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
