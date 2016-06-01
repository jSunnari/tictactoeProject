package client.logic;

/**
 * ClientController,
 * The controller for the client-app.
 */

import client.beans.MarkerData;
import client.beans.User;
import client.gui.*;
import client.network.NetworkCommunication;
import client.network.NetworkConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Jonas on 2016-05-20.
 */

public class ClientController{
    private NetworkConnection networkConnection;
    private NetworkCommunication networkCommunication;
    private boolean connectedToServer = false;

    //GAME VARIABLES:
    private User currUser;
    private User currOpponent;
    private GameBoardJavafxView.Tile[][] board;
    private boolean yourTurn = false;
    private int clickCounter = 0;
    private ObservableList<User> highscoreList = FXCollections.observableArrayList();
    private boolean opponentReqRematch = false;

    //VIEWS:
    private MainView mainView;
    private LoginView loginView;
    private UserdetailsView userdetailsView = new UserdetailsView();
    private MenuView menuView = new MenuView();
    private SettingsView settingsView = new SettingsView();
    private UserdetailsView updateAccount = new UserdetailsView();
    private LoadingView loadingView = new LoadingView();
    private GameBoardJavafxView gameBoardView = new GameBoardJavafxView();
    private ResultView resultView = new ResultView();

    //Gets the mainview and loginform from ClientApp-class (main-class).
    public ClientController(MainView mainView, LoginView loginView) {
        this.mainView = mainView;
        this.loginView = loginView;

        //Connect to server:
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
        userdetailsView.backBtnListener(event -> resetValidation());
        userdetailsView.backBtnListener(event -> mainView.setMainContent(loginView));
        updateAccount.backBtnListener(event -> mainView.setMainContent(menuView));

        /**
         * MenuView, Listeners:
         */
        //PLAY - call method play();
        menuView.playMenuListener(event -> play());

        menuView.resultMenuListener(event -> updateResults());

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
         * LoadingView, Listeners:
         */
        //CANCEL - calls method cancelGame();
        loadingView.cancelButtonListener(event -> cancelGame());

        /**
         * GameBoardView, Listeners:
         */
        //RESET GAME - sends a command to server.
        gameBoardView.resetGameListener(event -> requestPlayAgain());

        //EXIT GAME - calls method stoppedGame();
        gameBoardView.exitGameListener(event -> stoppedGame());

        /**
         * ResultView, Listeners:
         */
        //BACK - switch view to main-menu.
        resultView.backBtnListener(event -> mainView.setMainContent(menuView));

    }

    /**
     * Connect to server.
     */
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
     * (Used when changing the connection settings)
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
     * Gets response to either connectionFailed or setCurrentUser.
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
            Platform.runLater(() -> {
                mainView.setMainContent(menuView);
                mainView.setTitle(currUser.getUsername());
            });
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
        mainView.setTitle("");
        currUser = null;
    }

    /**
     * Update scores of currentuser from database,
     * Get newest higscore-list from database,
     * Change the view to resultView.
     */
    void updateResults(){
        networkCommunication.send("getUpdatedUser", currUser);
        networkCommunication.send("getHighscore", "");
    }

    /**
     * When the server is done with updating results, change view:
     */
    public void resultsUpdated(){
        resultView.setHighscoreList(highscoreList);
        resultView.setCurrentUser(currUser);
        Platform.runLater(() -> mainView.setMainContent(resultView));
    }

    /**
     * Create account-method,
     * Sends a request to create a new user,
     * The createAccountResponse()-method will then be called and showing if creating user was successful or not.
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

    void resetValidation(){
        Platform.runLater(() -> userdetailsView.setValidationLabel("back", ""));
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

    /**
     * Update a users information, fill the form with the current data.
     * If OK-button is clicked, take the new data and send to the database.
     */
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

    //------------------------------------------ GAME CONTROLS ---------------------------------------------------------

    /**
     * Play game,
     * change the view to the loading view,
     * send a command to the server saying to are ready to play.
     * (when another player is ready to play, the opponentConnected()-method will be called)
     */
    void play(){
        mainView.setMainContent(loadingView);
        loadingView.play();
        networkCommunication.send("startGame", "");
    }

    /**
     * When an opponent has connected, the game will start:
     * @param opponentUser = the opponent player.
     */
    public void opponentConnected(User opponentUser){
        currOpponent = opponentUser;

        if (currUser.getPlayer() == 1){
            yourTurn = true;
            gameBoardView.setPlayerX(currUser.getUsername(), "*");
            gameBoardView.setPlayerO(opponentUser.getUsername(), "");
        }
        else if(currUser.getPlayer() == 2) {
            yourTurn = false;
            gameBoardView.setPlayerO(currUser.getUsername(), "");
            gameBoardView.setPlayerX(opponentUser.getUsername(), "*");
        }

        Platform.runLater(() -> mainView.setMainContent(gameBoardView));
        loadingView.stop();
        clickOnTile();
    }


    /**
     * Cancel game,
     * The player will get removed from the servers "players in a game"-list.
     * Resets game and changes view to the main menu again.
     */
    void cancelGame(){
        networkCommunication.send("removeFromGameList", "");
        resetGame();
        currOpponent = null;
        mainView.setMainContent(menuView);
    }

    /**
     * You stopped a game,
     * Updates the player with the new scores/results,
     * Calls a command to stop the game and sends the opponents user object so that the server can stop that players game too.
     */
    void stoppedGame(){
        networkCommunication.send("updateUser", currUser);
        networkCommunication.send("stopGame", currOpponent);
        cancelGame();
    }

    /**
     * An opponent stopped a game,
     * Update the player with the new scores/results,
     * Remove the player from the server "players in a game"-list.
     * Resets the game and changes the view to the main menu again.
     */
    public void opponentStoppedGame(){
        networkCommunication.send("updateUser", currUser);
        networkCommunication.send("removeFromGameList", "");
        resetGame();
        currOpponent = null;
        Platform.runLater(() -> mainView.setMainContent(menuView));
    }

    void requestPlayAgain(){
        if (!opponentReqRematch) {
            networkCommunication.send("playAgain", currOpponent);
            gameBoardView.setPlayAgainBtnVisible(false);
            gameBoardView.setPlayAgainLbl("Waiting for " + currOpponent.getUsername() + "..");
        }
        else{
            networkCommunication.send("resetGame", currOpponent);
        }
    }

    public void opponentRequestedPlayAgain(){
        opponentReqRematch = true;
        gameBoardView.setPlayAgainLbl(currOpponent.getUsername() + " wants to play again!");
    }

    /**
     * Resets the gameboard,
     * The clickcounter gets a value of 0,
     * The gameboard is playable again and the old markers a gone.
     */
    public void resetGame(){
        clickCounter = 0;
        gameBoardView.setPlayAgainLbl("");
        gameBoardView.setPlayable(true);
        gameBoardView.setPlayAgainBtnVisible(false);
        gameBoardView.setExitBtnVisible(false);
        gameBoardView.resetBoard();
        opponentReqRematch = false;
    }

    /**
     * Listens for when clicking on a tile,
     * Checks if it's your turn, and checks if the tile is empty,
     * Sends the data to the method sendMarkerData()
     */
    void clickOnTile(){
        board = gameBoardView.getBoard();

        //Looping through our tiles in the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GameBoardJavafxView.Tile currentTile = board[j][i];
                currentTile.setOnMouseClicked(event -> {
                    if (gameBoardView.isPlayable()) {

                        if (yourTurn && !(currentTile.getValue().equals("X") || currentTile.getValue().equals("O"))) {
                            sendMarkerData(currentTile);
                        }
                    }
                });
            }
        }
    }

    /**
     * Creates an object of MarkerData holding the tileID, if it's a X or O and the opponent-data.
     * Sends the data to the server.
     *
     * @param currentTile = the tile the player clicked on.
     */
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

    /**
     * Method for drawing a marker - (X or O):
     * @param markerData = markerdata including tile-id (1 to 9), opponentdata and if it is an O or X.
     */
    public void drawMarker(MarkerData markerData){
        //Boolean holding the winning player:
        boolean winningPlayer;

        //Looping through our tiles in the board until finding the right "tile" to draw on:
        for(int i = 0; i < 3; i++ ){
            for(int j = 0; j < 3; j++){
                if (board[j][i].getTileId() == markerData.getMarkerId()){

                    if (markerData.getMarkerType().equals("X")){
                        board[j][i].drawX();
                        clickCounter++;
                        clickOnTile();

                    }
                    else if (markerData.getMarkerType().equals("O")){
                        board[j][i].drawO();
                        clickCounter++;
                        clickOnTile();
                    }
                }
            }
        }

        //Checking if there is a winning player after each draw:
        winningPlayer = gameBoardView.checkTiles();

        /**
         * If there is a winning player and it's the current users turn,
         * that means that the current user has won the round.
         * else its the opponent who won the round.
         * If there is no winning player and the clickcounter i 9,
         * that means that there is a tie game.
         */
        if (winningPlayer && yourTurn){
            networkCommunication.send("winningPlayer", currOpponent);
            currUser.setWonMatches(currUser.getWonMatches()+1);
            gameBoardView.setPlayAgainBtnVisible(true);
            gameBoardView.setExitBtnVisible(true);
        }
        else if(winningPlayer && !yourTurn){
            currUser.setLostMatches(currUser.getLostMatches()+1);
            gameBoardView.setPlayAgainBtnVisible(true);
            gameBoardView.setExitBtnVisible(true);
        }
        else if(!winningPlayer && clickCounter == 9){
            currUser.setTieMatches(currUser.getTieMatches()+1);
            gameBoardView.incTieScore();
            gameBoardView.setPlayAgainBtnVisible(true);
            gameBoardView.setExitBtnVisible(true);
        }

        /**
         * Method that checks whose turn it is and changes the gameboard-marker beside the playername.
         */
        if (yourTurn){
            yourTurn = false;

            if (currUser.getPlayer() == 1){
                gameBoardView.setPlayerX(currUser.getUsername(), "");
                gameBoardView.setPlayerO(currOpponent.getUsername(), "*");

            }
            else if(currUser.getPlayer() == 2) {
                gameBoardView.setPlayerO(currUser.getUsername(), "");
                gameBoardView.setPlayerX(currOpponent.getUsername(), "*");
            }
        }
        else{
            yourTurn = true;

            if (currUser.getPlayer() == 1){
                gameBoardView.setPlayerX(currUser.getUsername(), "*");
                gameBoardView.setPlayerO(currOpponent.getUsername(), "");
            }
            else if(currUser.getPlayer() == 2) {
                gameBoardView.setPlayerO(currUser.getUsername(), "*");
                gameBoardView.setPlayerX(currOpponent.getUsername(), "");
            }
        }
    }

    /**
     * Sets the "local" score to the winning player of the round:
     * @param winningPlayer = player who won round.
     */
    public void setScore(User winningPlayer){

        if (winningPlayer.getPlayer() == 1){
            gameBoardView.incPlayer1Score();
            gameBoardView.playWinSound();
        }else{
            gameBoardView.playLostSound();
        }
        if(winningPlayer.getPlayer() == 2) {
            gameBoardView.incPlayer2Score();
            gameBoardView.playWinSound();
        }else{
            gameBoardView.playLostSound();
        }

    }

    /**
     * Update current user:
     * @param user = updated user:
     */
    public void updateCurrUser(User user){
        currUser = user;
    }

    /**
     * Clear the highscore-list:
     */
    public void clearHighscoreList(){
        highscoreList.clear();
    }

    /**
     * Add to the highscore-list:
     * @param user = user (including scores).
     */
    public void addToHighscoreList(User user){
        highscoreList.add(user);
    }

    /**
     * Sets if connected or not.
     * @param connected
     */
    public void setConnectedToServer(boolean connected){
        connectedToServer = connected;
    }

    /**
     * Disconnect, calls method in networkConnection (if connected):
     */
    public void disconnect(boolean exit) {
        if (connectedToServer) {
            networkConnection.disconnect(exit);
        }
        else {
            System.exit(0);
        }
    }

}
