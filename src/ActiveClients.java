/**
 * Created by Tibi on 03/10/2017.
 */

import java.util.ArrayList;

public class ActiveClients {
    private volatile ArrayList<ClientThread> clients = new ArrayList<>();

    public void add(ClientThread client) {
        clients.add(client);
    }

    public ArrayList<ClientThread> getClients() {
        return clients;
    }

    public int countClients() {
        return this.clients.size();
    }

    public void remove(int port) {
        for(int i=0;i<clients.size();i++) {
            if(clients.get(i).socket.getPort() == port) {
                clients.remove(i);
            }
        }
    }
}
