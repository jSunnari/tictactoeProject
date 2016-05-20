package client;

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
        MainView mainView = new MainView(primaryStage);
        ClientController clientController = new ClientController(mainView);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}
