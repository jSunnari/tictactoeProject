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
    private User cuttOpponent;
    private boolean connectedToServer = false;

    //VIEWS:
    private UserdetailsView userdetailsView = new UserdetailsView();
    private MenuView menuView = new MenuView();
    private SettingsView settingsView = new SettingsView();
    private UserdetailsView updateAccount = new UserdetailsView();
    private LoadingView loadingView = new LoadingView();

    //Gets the mainview and loginform from ClientApp-class (main-class).
    public ClientController(MainView mainView, LoginView loginView) {
        this.mainView = mainView;
        this.loginView = loginView;

        connect();

        /**
         * LoginView, Listeners:
         */
        //LOG IN - call the method login():
        loginView.loginBtnListener(event -> login());

        //CREATE ACCOUNT(Menu button) - change the content to the userDetailsView:
        loginView.createAccountBtnListener(event -> mainView.setMainContent(userdetailsView));

        //CONNECTION SETTINGS - change the content to settingsView:
        loginView.settingsBtnListener(event -> mainView.setMainContent(settingsView));

        //QUIT GAME - closes application:
        loginView.quitBtnListener(event -> disconnect(true));

        /**
         * UserdetailsView, Listeners:
         */
        //CREATE ACCOUNT - call method createAccount():
        userdetailsView.createAccountBtn(event -> createAccount());

        //BACK - switches to the loginform again.
        userdetailsView.backBtnListener(event -> mainView.setMainContent(loginView));
        updateAccount.backBtnListener(event -> mainView.setMainContent(menuView));

        /**
         * MenuView, Listeners:
         */
        menuView.playMenuListener(event -> play());

        //LOGOUT - calls method logout();
        menuView.logoutMenuListener(event -> logout());

        //ACCOUNT SETTINGS -
        menuView.settingsMenuListener(event -> updateAccountSettings());

        /**
         * SettingsView, Listeners:
         */
        //OK - calls method updateConnection();
        settingsView.okBtnListener(event -> updateConnection());

        //BACK - switches to the loginform again.
        settingsView.backBtnListener(event -> mainView.setMainContent(loginView));

    }

    void connect(){
        //Try to connect to server:
        String ip = settingsView.getIpNumber();
        int port = settingsView.getPort();
        networkConnection = new NetworkConnection(ip, port, this);
        //Getting a reference to the class NetworkCommunication:
        networkCommunication = networkConnection.getNetworkCommunication();
    }

    /**
     * Update connection-method,
     *
     * Disconnects then connects again to new settings,
     * changes the content to loginscreen again:
     */
    void updateConnection(){
        loginView.setErrorLabel("");
        if (connectedToServer) {
            disconnect(false);
        }
        connect();
        mainView.setMainContent(loginView);
    }

    /**
     * Log in-method,
     *
     * Validates and sends a login-request to the server:
     */
    void login() {

        //Login data:
        String username = loginView.getUserTextField();
        String password = loginView.getPasswordTextField();

        //If username and password is filled, and if connection went well:
        if (!username.equals("") && !password.equals("") && networkCommunication != null) {
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

        //If the boolean login is true in the user-object sent from the server:
        if (currUser.isLogin()){
            Platform.runLater(() -> mainView.setMainContent(menuView));
            loginView.clearFields();
        }
        //Else, the username or password was incorrect:
        else {
            Platform.runLater(() -> loginView.setErrorLabel("Username or password is incorrect, please try again.."));
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

        if (!userdetailsView.checkFields()){
            User user = new User(username,password,email);
            networkCommunication.send("createUser", user);
        }
        else{
            Platform.runLater(() -> userdetailsView.setValidationLabel("error", "Enter all fields."));
        }
    }

    /**
     * Response from networkcommunication after trying to creating an account.
     * @param res =
     *            If the username already exists res = error
     *            If the user was created, res = created.
     */
    public void createAccountResponse(String res, String message){
        Platform.runLater(() -> userdetailsView.setValidationLabel(res, message));
    }

    void updateAccountSettings(){
        updateAccount.initUpdateSettings();
        updateAccount.setUsername(currUser.getUsername());
        updateAccount.setPassword(currUser.getPassword());
        updateAccount.setEmail(currUser.getEmail());
        Platform.runLater(() -> mainView.setMainContent(updateAccount));

        updateAccount.updateAccountBtn(event -> {
            if (!updateAccount.checkFields()) {
                currUser.setUsername(updateAccount.getUsername());
                currUser.setPassword(updateAccount.getPassword());
                currUser.setEmail(updateAccount.getEmail());
                networkCommunication.send("updateUser", currUser);
                updateAccount.setValidationLabel("updated", "Account settings updated!");
            }
            else {
                updateAccount.setValidationLabel("error", "Enter all fields.");
            }
        });
    }

    void play(){
        mainView.setMainContent(loadingView);
        loadingView.play();
        networkCommunication.send("startGame", "");
    }

    public void opponentConnected(User opponentUser){
        cuttOpponent = opponentUser;
        Platform.runLater(() -> loadingView.testConnected(cuttOpponent.getUsername()));

        loadingView.testListener(event -> {
            networkCommunication.send("gameDrawX", cuttOpponent);
        });
    }

    public void test(String test){
        Platform.runLater(() -> loadingView.testConnected(test));
    }

    public void setConnectedToServer(boolean connected){
        connectedToServer = connected;
    }

    /**
     * Disconnect, calls method in networkConnection:
     */
    public void disconnect(boolean exit) {
        networkConnection.disconnect(exit);
    }

}
