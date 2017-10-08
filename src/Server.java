/**
 * Created by Tibi on 02/10/2017.
 */

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ActiveClients clients = new ActiveClients();

    public static void main(String[] args) {
        System.out.println("--Server up--");
        final int PORT = 12456;

        try {
            ServerSocket server = new ServerSocket(PORT);
            while (true) {
                Socket socket = server.accept();
                ClientThread client = new ClientThread(socket, clients);
                client.start();
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
