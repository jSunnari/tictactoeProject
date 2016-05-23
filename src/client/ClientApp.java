package client;

/**
 * MAIN METHOD,
 * Starts the client-application.
 */

import client.gui.LoginView;
import client.gui.MainView;
import client.logic.ClientController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Jonas on 2016-05-19.
 */

public class ClientApp extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Create an object of the LoginView and the MainView,
         * Create and object of the clientController and send both the MainView and the LoginView.
         */
        LoginView loginView = new LoginView();
        MainView mainView = new MainView(primaryStage, loginView);
        ClientController clientController = new ClientController(mainView, loginView);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}
