/**
 * Created by Tibi on 02/10/2017.
 */

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<ClientThread> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("--Server up--");
        final int PORT = 12456;

        try {
            ServerSocket server = new ServerSocket(PORT);
            while (true) {
                Socket socket = server.accept();
                ClientThread client = new ClientThread(socket);
                client.start();
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) {
        for (ClientThread client : clients) {

        }
    }
}
