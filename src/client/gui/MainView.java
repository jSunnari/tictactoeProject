package client.gui;

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

    public MainView(Stage window) {
        this.window = window;
        window.setTitle("TicTacToe");

        buildApp();

        ImageView selectedImage = new ImageView(new Image(getClass().getResourceAsStream("/res/logo.png")));
        mainBorderPane.setTop(selectedImage);


        //Set scene:
        Scene scene = new Scene(mainBorderPane, 1024, 768);
        scene.getStylesheets().add("style.css"); //check the file style.css in /src.
        window.setScene(scene);
        window.show();
    }

    void buildApp() {
        mainBorderPane = new BorderPane();

    }

    void initApp() {


    }
}
