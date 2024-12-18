import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 1234)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readLine());
            String name = console.readLine();
            out.println(name);
            System.out.println(in.readLine());

            while (true) {
                String serverMessage = in.readLine();
                System.out.println(serverMessage);
                if (serverMessage.contains("Your turn")) {
                    out.println(console.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
