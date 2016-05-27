package client.gui;

/**
 * Main "frame" for client application.
 * Starts with showing the login-form, then switches content depending on users action.
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by Jonas on 2016-05-20.
 */

public class MainView {
    private Stage window;
    private BorderPane mainBorderPane;
    private ImageView logoImageView;
    private LoginView loginView;

    //Gets the stage and loginform from ClientApp-class (main-class).
    public MainView(Stage window, LoginView loginView) {
        this.window = window;
        this.loginView = loginView;
        window.setTitle("TicTacToe");
        window.getIcons().add(new Image("file:src/client/res/TicTacToeIconSmall.png"));
        //Build and init components:
        buildApp();
        initApp();

        //Set scene:
        Scene scene = new Scene(mainBorderPane, 1024, 768);
        //Add css-file and google-fonts:
        scene.getStylesheets().add("file:src/client/res/style.css"); //check the file style.css in /res.
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=VT323");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Press+Start+2P");

        window.setScene(scene);
        window.show();
    }

    //Method for building components:
    void buildApp() {
        mainBorderPane = new BorderPane();
        logoImageView = new ImageView(new Image("file:src/client/res/logo.png"));
    }

    //Method for initializing components:
    void initApp() {
        mainBorderPane.setTop(logoImageView);
        BorderPane.setAlignment(logoImageView, Pos.CENTER);
        BorderPane.setMargin(logoImageView, new Insets(15,0,0,0));
        mainBorderPane.setCenter(loginView);
    }

    /**
     * Changes the main content of the frame.
     * @param view = Any node/form/layout etc.
     */
    public void setMainContent(Node view){
        mainBorderPane.setCenter(view);
    }
}
