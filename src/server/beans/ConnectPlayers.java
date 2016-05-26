package server.beans;

/**
 * Object holding:
 * Userobject = the userobject for the user
 * NetworkCommunication = The users threaded class used for communication.
 */

import server.datamodel.User;
import server.network.NetworkCommunication;

/**
 * Created by Jonas on 2016-05-25.
 */

public class ConnectPlayers {
    private User user;
    private NetworkCommunication networkCommunication;

    public ConnectPlayers(User user, NetworkCommunication networkCommunication) {
        this.user = user;
        this.networkCommunication = networkCommunication;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NetworkCommunication getNetworkCommunication() {
        return networkCommunication;
    }

    public void setNetworkCommunication(NetworkCommunication networkCommunication) {
        this.networkCommunication = networkCommunication;
    }
}
