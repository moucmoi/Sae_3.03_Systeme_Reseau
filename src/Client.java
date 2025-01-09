import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String adresse = args[0]; 
        String nomJoueur = args[1]; 

        try (Socket socket = new Socket(adresse, 555);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            output.println(nomJoueur);  // Envoi du nom du joueur

            String serverMessage;
            while ((serverMessage = input.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.contains("defier")) {
                    String command = console.readLine();
                    output.println(command);  // Envoyer la commande pour défier un joueur
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
