/**
 * Created by Tibi on 03/10/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientThread extends Thread {
    private Socket socket;
    private ArrayList<ClientThread> clients;

    ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            while(in.hasNext()){
                System.out.println(in.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
