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
        System.out.print("Enter your username: ");
        Scanner reader = new Scanner(System.in);
        String username = reader.nextLine();

        /*String[] parts = input.split(" ");
        String command = parts[0];
        String username = parts[1].split(",")[0];
        String IP = parts[2].split(":")[0];
        int PORT = Integer.parseInt(parts[2].split(":")[1]);*/
        String IP = "127.0.0.1";
        int PORT = 12456;

        try {
            InetAddress inetAddress = InetAddress.getByName(IP);
            Socket socket = new Socket(inetAddress, PORT);
            new ServerThread(socket).start();

            PrintWriter connectedMessage = new PrintWriter(socket.getOutputStream());
            connectedMessage.println("JOIN " + username + ", " + IP + ":" + PORT);
            connectedMessage.flush();

            while (true) {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.write(reader.nextLine() + "\n");
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ServerThread extends Thread {
        Socket socket;
        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try{
                while (true) {
                    Scanner in = new Scanner(socket.getInputStream());
                    System.out.println(in.nextLine());
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
