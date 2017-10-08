/**
 * Created by Tibi on 02/10/2017.
 */

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client{

    private static Scanner reader = new Scanner(System.in);

    public static boolean containsIllegals(String toExamine) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\^]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    public static void main(String[] args) {
        System.out.println("--Client up and running--");
        String username = AskForUser();

        String IP = "127.0.0.1";
        int PORT = 12456;

        try {
            InetAddress inetAddress = InetAddress.getByName(IP);
            Socket socket = new Socket(inetAddress, PORT);
            PrintWriter connectedMessage = new PrintWriter(socket.getOutputStream(), true);
            connectedMessage.println("JOIN " + username + ", " + IP + ":" + PORT);
            Scanner input = new Scanner(socket.getInputStream());
            String message = input.nextLine();
            if (!message.equals("J_OK")) {
                System.out.println(message);
                return;
            }
            System.out.println("You are now connected");
            new ServerThread(socket).start();
            while (true) {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.write(reader.nextLine() + "\n");
                out.flush();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String AskForUser() {
        String username = "";
        boolean ok = false;
        while (!ok) {
            System.out.print("Enter your username: ");
            username = reader.nextLine();
            if (username.length() >= 12) {
                System.out.println("Username should contain maximum 12 characters");
            }
            else if (containsIllegals(username)) {
                System.out.println("Only digits, letters, underscores and hyphens are accepted");
            }
            else {
                ok = true;
            }
        }

        return username;
    }

    public static class IAmAlive extends Thread {
        Socket socket;
        public IAmAlive(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                while (true) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    out.println("IAMV");
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    while(in.hasNextLine()) {
                        System.out.println(in.nextLine());
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
