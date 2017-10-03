/**
 * Created by Tibi on 03/10/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ClientThread extends Thread {
    public Socket socket;
    private ActiveClients clients;
    private String userName;

    ClientThread(Socket socket, ActiveClients clients) {
        this.socket = socket;
        this.clients = clients;
        this.userName = null;
    }

    public String getUserName() {
        return userName;
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            String clientSentence;
            while (!socket.isClosed()) {
                clientSentence = in.nextLine();
                if (clientSentence.contains("JOIN")) {
                    String[] parts = clientSentence.split(" ");
                    String username = parts[1].split(",")[0];
                    this.userName = username;
                    System.out.println(username + " connected");
                }
                if (clientSentence.equals("LIST")) {
                    String userList = "";
                    for (ClientThread client : clients.getClients()) {
                        userList += client.getUserName() + " ";
                    }
                    clientSentence = "Active users: " + userList;
                }
                if (clientSentence.contains("QUIT")) {
                    System.out.println("Client disconnected");
                    clients.remove(this.socket.getPort());
                }

                if(!clientSentence.contains("JOIN") && !clientSentence.equals("QUIT")) {
                    System.out.println(this.getUserName() + ": " + clientSentence);
                }

                //String capitalizedSentence = clientSentence.toUpperCase() + '\n';
                //write out line to socket
                Iterator<ClientThread> iter = clients.getClients().iterator();
                while(iter.hasNext()) {
                    ClientThread client = iter.next();
                    if (client.socket.getPort() == this.socket.getPort()) {
                        continue;
                    }
                    PrintWriter out = new PrintWriter(client.socket.getOutputStream());

                    if (clientSentence.contains("JOIN")) {
                        out.println(this.getUserName() + " connected");
                    }
                    else if (clientSentence.equals("QUIT")) {
                        out.println(this.getUserName() + " disconnected");
                        this.socket.close();
                    }
                    else {
                        out.println(this.getUserName() + ": " + clientSentence);
                    }
                    out.flush();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
