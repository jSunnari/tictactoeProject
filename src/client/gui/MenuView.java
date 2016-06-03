package client.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by Jonas on 2016-05-24.
 */

public class MenuView extends VBox{
    private VBox vBox;
    private Label play = new Label("Play");
    private Label results = new Label("Results");
    private Label settings = new Label("Account settings");
    private Label logout = new Label("Log out");
    private Label infoLabel = new Label();

    public MenuView() {
        vBox = this;
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(25);

        vBox.getChildren().addAll(play,results,settings,logout, infoLabel);

        play.getStyleClass().add("menu-label");
        results.getStyleClass().add("menu-label");
        settings.getStyleClass().add("menu-label");
        logout.getStyleClass().add("menu-label");
        infoLabel.getStyleClass().add("info-label");
    }

    public void playMenuListener (EventHandler<MouseEvent> listener){
        play.setOnMouseClicked(listener);
    }

    public void resultMenuListener(EventHandler<MouseEvent> listener){
        results.setOnMouseClicked(listener);
    }

    public void settingsMenuListener (EventHandler<MouseEvent> listener){
        settings.setOnMouseClicked(listener);
    }

    public void logoutMenuListener (EventHandler<MouseEvent> listener){
        logout.setOnMouseClicked(listener);
    }

    public void setInfoLabel(String message){
        infoLabel.setText(message);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(10000),
                ae -> infoLabel.setText("")));
        timeline.play();
    }
}
