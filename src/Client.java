/**
 * Created by Tibi on 02/10/2017.
 */

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    public static void main(String[] args) {
        System.out.println("--Client up and running--");
        Scanner reader = new Scanner(System.in);
        String input = reader.nextLine();

        String[] parts = input.split(" ");
        String command = parts[0];
        String username = parts[1].split(",")[0];
        String IP = parts[2].split(":")[0];
        int PORT = Integer.parseInt(parts[2].split(":")[1]);

        try {
            InetAddress inetAddress = InetAddress.getByName(IP);
            Socket socket = new Socket(inetAddress, PORT);
            while (true) {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                out.write(reader.nextLine() + "\n");
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
