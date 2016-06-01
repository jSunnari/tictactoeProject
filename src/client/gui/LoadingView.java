package client.gui;

import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

import java.net.URL;

/**
 * Created by Jonas on 2016-05-25.
 */
public class LoadingView extends VBox {
    private RotateTransition rotateTransition;
    private VBox vBox;
    private Button cancelButton = new Button("Cancel");
    private Label loadingLabel = new Label("Waiting for random opponent...");
    private URL resource = getClass().getResource("../res/Elevator Music - Vanoss Gaming Background Music (HD).mp3");

    private MediaPlayer elevatorMusic = new MediaPlayer(new Media(resource.toString()));

    public LoadingView() {
        vBox = this;

        Circle loadingCircle = new Circle(30, null);
        loadingCircle.setStroke(Color.WHITE);
        loadingCircle.setStrokeWidth(6);
        loadingCircle.getStrokeDashArray().addAll(10.,10.,10.,10.);
        loadingCircle.setStrokeLineCap(StrokeLineCap.ROUND);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(loadingCircle, loadingLabel, cancelButton);

        cancelButton.getStyleClass().add("form-button");

        rotateTransition = new RotateTransition(Duration.seconds(6), loadingCircle);
        rotateTransition.setByAngle(180);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.setAutoReverse(true);
    }

    public void cancelButtonListener(EventHandler<ActionEvent> buttonListener){
        cancelButton.setOnAction(buttonListener);
        elevatorMusic.stop();
    }

    public void play() {
        elevatorMusic.play();
        rotateTransition.play();
    }

    public void stop() {
        elevatorMusic.stop();
    }

}
