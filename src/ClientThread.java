/**
 * Created by Tibi on 03/10/2017.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
                if (!in.hasNextLine()) {
                    continue;
                }
                clientSentence = in.nextLine();
                if (clientSentence.contains("JOIN")) {
                    String[] parts = clientSentence.split(" ");
                    String username = parts[1].split(",")[0];
                    if (!CheckIfUsernameExists(this.socket, this.getUserName())) {
                        this.userName = username;
                        System.out.println(username + " connected");
                        System.out.println(this.getUserName() + " if");
                    }
                    else {
                        System.out.println(this.getUserName() + " else");
                        clients.remove(this.socket.getPort());
                    }
                }
                if (clientSentence.contains("QUIT")) {
                    System.out.println(this.getUserName() + " disconnected");
                    clients.remove(this.socket.getPort());
                }

                if (clientSentence.contains("IMAV")) {
                    System.out.println(this.getUserName() + " is alive");
                }

                if (!clientSentence.contains("JOIN") && !clientSentence.equals("QUIT")) {
                    System.out.println(this.getUserName() + ": " + clientSentence);
                }

                Iterator<ClientThread> iter = clients.getClients().iterator();
                while (iter.hasNext()) {
                    ClientThread client = iter.next();
                    PrintWriter out = new PrintWriter(client.socket.getOutputStream());
                    if (client.socket.getPort() == this.socket.getPort()) {
                        if (clientSentence.contains("JOIN")) {
                            if (!CheckIfUsernameExists(this.socket, this.getUserName())) {
                                out.println("J_OK");
                            } else {
                                out.println("J_ER 1: Username already in use. Try again!");
                            }
                        }
                    } else {

                        if (clientSentence.contains("JOIN")) {
                            out.println(this.getUserName() + " connected");
                            out.println(ShowActiveUsersList());
                        } else if (clientSentence.equals("QUIT")) {
                            out.println(this.getUserName() + " disconnected");
                            out.println(ShowActiveUsersList());
                            this.socket.close();
                        } else {
                            out.println(this.getUserName() + ": " + clientSentence);
                        }
                    }
                    out.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String ShowActiveUsersList() {
        String userList = "Active users: ";
        for (ClientThread client : clients.getClients()) {
            userList += client.getUserName() + " ";
        }

        return userList;
    }

    public boolean CheckIfUsernameExists(Socket socket, String username) {
        for (ClientThread client : clients.getClients()) {
            if (client.socket.getPort() != socket.getPort()) {
                if (client.getUserName().equals(username)) {
                    return true;
                }
            }
        }

        return false;
    }
}
