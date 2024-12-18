import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.contains("Please enter your name:")) {
                    String name = userIn.readLine();
                    out.println(name);
                } else if (serverMessage.contains("OK Connected as")) {
                    while (true) {
                        String command = userIn.readLine();
                        out.println(command);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
