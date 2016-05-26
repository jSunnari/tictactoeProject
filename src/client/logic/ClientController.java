package client.logic;

/**
 * ClientController
 */

import client.beans.MarkerData;
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
    private User currOpponent;
    private boolean connectedToServer = false;
    private GameBoardJavafxView.Tile[][] board;
    private boolean yourTurn = false;

    //VIEWS:
    private UserdetailsView userdetailsView = new UserdetailsView();
    private MenuView menuView = new MenuView();
    private SettingsView settingsView = new SettingsView();
    private UserdetailsView updateAccount = new UserdetailsView();
    private LoadingView loadingView = new LoadingView();
    private GameBoardJavafxView gameBoardView = new GameBoardJavafxView();

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

        /**
         * GameBoardView, Listeners:
         */

        gameBoardView.resetGameListener(event -> networkCommunication.send("resetGame", currOpponent));


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

    /**
     * *************** GAME CONTROLS *******************
     */


    void play(){
        mainView.setMainContent(loadingView);
        loadingView.play();
        networkCommunication.send("startGame", "");
    }

    public void resetGame(){
        gameBoardView.checkForTie();
        gameBoardView.setPlayable(true);
        gameBoardView.resetBoard();
    }

    void clickOnTile(){
        board = gameBoardView.getBoard();

        //Looping through our tiles in the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GameBoardJavafxView.Tile currentTile = board[j][i];
                currentTile.setOnMouseClicked(event -> {
                    if (gameBoardView.isPlayable()) {

                        System.out.println(currentTile.getValue());

                        if (yourTurn && !(currentTile.getValue().equals("X") || currentTile.getValue().equals("O"))) {
                            sendMarkerData(currentTile);
                        }
                    }
                });
            }
        }
    }

    void sendMarkerData(GameBoardJavafxView.Tile currentTile){
        MarkerData markerData = new MarkerData(currentTile.getTileId(), currOpponent.getUsername());
        if (currUser.getPlayer() == 1){
            markerData.setMarkerType("X");
        }
        else if(currUser.getPlayer() == 2){
            markerData.setMarkerType("O");
        }
        networkCommunication.send("markerData", markerData);
    }

    public void drawMarker(MarkerData markerData){
        //Looping through our tiles in the board
        for(int i = 0; i < 3; i++ ){
            for(int j = 0; j < 3; j++){
                if (board[j][i].getTileId() == markerData.getMarkerId()){

                    if (markerData.getMarkerType().equals("X")){
                        board[j][i].drawX();
                        System.out.println("value x: " + board[j][i].getValue());
                        clickOnTile();

                    }
                    else if (markerData.getMarkerType().equals("O")){
                        board[j][i].drawO();
                        System.out.println("value o: " + board[j][i].getValue());
                        clickOnTile();

                    }

                }
            }
        }

        gameBoardView.checkTiles();

        if (yourTurn){
            yourTurn = false;

            if (currUser.getPlayer() == 1){
                gameBoardView.setPlayerX(currUser.getUsername());
                gameBoardView.setPlayerO(currOpponent.getUsername() + "*");

            }
            else if(currUser.getPlayer() == 2) {
                gameBoardView.setPlayerO(currUser.getUsername());
                gameBoardView.setPlayerX(currOpponent.getUsername() + "*");
            }

        }
        else{
            yourTurn = true;

            if (currUser.getPlayer() == 1){
                gameBoardView.setPlayerX(currUser.getUsername() + "*");
                gameBoardView.setPlayerO(currOpponent.getUsername());
            }
            else if(currUser.getPlayer() == 2) {
                gameBoardView.setPlayerO(currUser.getUsername() + "*");
                gameBoardView.setPlayerX(currOpponent.getUsername());
            }
        }
    }

    public void opponentConnected(User opponentUser){
        currOpponent = opponentUser;

        System.out.println("curent user: " + currUser.getPlayer());
        System.out.println("opponent: " + opponentUser.getPlayer());

        if (currUser.getPlayer() == 1){
            yourTurn = true;
            gameBoardView.setPlayerX(currUser.getUsername() + "*");
            gameBoardView.setPlayerO(opponentUser.getUsername());
        }
        else if(currUser.getPlayer() == 2) {
            gameBoardView.setPlayerO(currUser.getUsername());
            gameBoardView.setPlayerX(opponentUser.getUsername() + "*");
        }

        Platform.runLater(() -> mainView.setMainContent(gameBoardView));
        clickOnTile();
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
