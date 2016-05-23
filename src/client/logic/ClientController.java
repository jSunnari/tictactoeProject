package client.logic;

/**
 * ClientController
 */

import client.beans.User;
import client.gui.LoginView;
import client.gui.MainView;
import client.gui.UserdetailsView;
import client.network.NetworkCommunication;
import client.network.NetworkConnection;

/**
 * Created by Jonas on 2016-05-20.
 */

public class ClientController{
    private MainView mainView;
    private LoginView loginView;
    private UserdetailsView userdetailsView = new UserdetailsView();
    private NetworkConnection networkConnection;
    private NetworkCommunication networkCommunication;
    private User currUser;

    //Gets the mainview and loginform from ClientApp-class (main-class).
    public ClientController(MainView mainView, LoginView loginView) {

        networkConnection = new NetworkConnection("localhost", 50123);


        this.mainView = mainView;
        this.loginView = loginView;

        //Getting a reference to the class NetworkCommunication:
        networkCommunication = networkConnection.getNetworkCommunication();

        /**
         * LoginView, Listeners:
         */
        //LOG IN - call the method login():
        loginView.loginBtnListener(event -> login());

        //CREATE ACCOUNT(Menu button) - Change the content to the userDetailsView:
        loginView.createAccountBtnListener(event -> mainView.setMainContent(userdetailsView));

        //QUIT GAME - closes application:
        loginView.quitBtnListener(event1 -> System.exit(0));

        /**
         * UserdetailsView, Listeners:
         */
        //CREATE ACCOUNT - call method createAccount():
        userdetailsView.createAccountBtn(event1 -> createAccount());

        //BACK - switches to the loginform again.
        userdetailsView.backBtnListener(event -> mainView.setMainContent(loginView));

    }

    /**
     * LOG IN METHOD,
     * Validation and
     */
    void login() {

        String username = loginView.getUserTextField();
        String password = loginView.getPasswordTextField();
        if (!username.equals("") && !password.equals("")) {
            loginView.setErrorLabel("");
            System.out.println(username + " " + password);

            User login = new User(username, password);
            currUser = networkCommunication.login(login);


        }
        else {
            loginView.setErrorLabel("Fyll i alla fält.");
        }
    }

    /**
     * CREATE ACCOUNT METHOD,
     *
     */
    void createAccount() {
        String username = userdetailsView.getUsername();
        String password = userdetailsView.getPassword();
        String email = userdetailsView.getEmail();

        User user = new User(username,password,email);

        networkCommunication.createUser(user);
    }

    public void disconnect() {
        networkConnection.disconnect();
    }




}
