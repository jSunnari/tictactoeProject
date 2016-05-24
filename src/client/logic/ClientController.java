package client.logic;

/**
 * ClientController
 */

import client.beans.User;
import client.gui.*;
import client.network.NetworkCommunication;
import client.network.NetworkConnection;
import javafx.application.Platform;

/**
 * Created by Jonas on 2016-05-20.
 */

public class ClientController{
    private MainView mainView;
    private LoginView loginView;
    private NetworkConnection networkConnection;
    private NetworkCommunication networkCommunication;
    private User currUser;

    //VIEWS:
    private UserdetailsView userdetailsView = new UserdetailsView();
    private MenuView menuView = new MenuView();
    private SettingsView settingsView = new SettingsView();

    //Gets the mainview and loginform from ClientApp-class (main-class).
    public ClientController(MainView mainView, LoginView loginView) {

        this.mainView = mainView;
        this.loginView = loginView;


        /**
         * LoginView, Listeners:
         */
        //LOG IN - call the method login():
        loginView.loginBtnListener(event -> login());

        //CREATE ACCOUNT(Menu button) - Change the content to the userDetailsView:
        loginView.createAccountBtnListener(event -> mainView.setMainContent(userdetailsView));

        loginView.settingsBtnListener(event -> mainView.setMainContent(settingsView));

        //QUIT GAME - closes application:
        loginView.quitBtnListener(event -> System.exit(0));

        /**
         * UserdetailsView, Listeners:
         */
        //CREATE ACCOUNT - call method createAccount():
        userdetailsView.createAccountBtn(event -> createAccount());

        //BACK - switches to the loginform again.
        userdetailsView.backBtnListener(event -> mainView.setMainContent(loginView));

        /**
         * MenuView, Listeners:
         */
        //LOGOUT - calls method logout();
        menuView.logoutMenuListener(event -> logout());

        /**
         * SettingsView, Listeners:
         */
        //BACK - switches to the loginform again.
        settingsView.backBtnListener(event -> mainView.setMainContent(loginView));

    }



    /**
     * Log in-method,
     * Connect to server,
     * Validation and sends a login-request to the server:
     */
    void login() {
        //Connect to server:
        String ip = settingsView.getIpNumber();
        int port = settingsView.getPort();
        networkConnection = new NetworkConnection(ip, port, this);
        //Getting a reference to the class NetworkCommunication:
        networkCommunication = networkConnection.getNetworkCommunication();

        //Login:
        String username = loginView.getUserTextField();
        String password = loginView.getPasswordTextField();
        if (!username.equals("") && !password.equals("")) {
            loginView.setErrorLabel("");
            User login = new User(username, password);
            networkCommunication.send("login", login);
        }
        else {
            loginView.setErrorLabel("Enter all fields.");
        }
    }

    /**
     * If client cant connect to server:
     */
    public void connectionFailed(){
        Platform.runLater(() -> loginView.setErrorLabel("Unable to connect to server, check connection settings."));
    }

    /**
     * SetCurrentUser - after connection and login went well,
     * @param user = User-object after the server has validated it.
     */
    public void setCurrentUser(User user){
        currUser = user;
        //If the boolean login is true in the new user-object:
        if (currUser.isLogin()){
            System.out.println("inloggad"); //TEST
            Platform.runLater(() -> mainView.setMainContent(menuView));
            loginView.clearFields();
        }
        //Else, the username or password was incorrect:
        else {
            Platform.runLater(() -> loginView.setErrorLabel("Username or password incorrent, please try again.."));
        }
    }

    /**
     * Logout-method,
     * sends user to the loginview and nulls the current user.
     */
    void logout(){
        Platform.runLater(() -> mainView.setMainContent(loginView));
        currUser = null;
    }

    /**
     * Create account-method,
     *
     */
    void createAccount() {
        String username = userdetailsView.getUsername();
        String password = userdetailsView.getPassword();
        String email = userdetailsView.getEmail();

        User user = new User(username,password,email);

        networkCommunication.send("createUser", user);
    }

    public void createAccountResponse(boolean error){
        Platform.runLater(() -> userdetailsView.setValidationLabel(error));
    }

    /**
     * Disconnect, calls method in networkConnection:
     */
    public void disconnect() {
        if (currUser != null) {
            networkConnection.disconnect();
        }
    }

}
