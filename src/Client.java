import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println(input.readLine());
            output.println(console.readLine());

            String serverMessage;
            while ((serverMessage = input.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.contains("Choisissez une colonne")) {
                    output.println(console.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
