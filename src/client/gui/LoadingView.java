package client.gui;

import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

/**
 * Created by Jonas on 2016-05-25.
 */
public class LoadingView extends VBox {
    private RotateTransition rotateTransition;
    private VBox vBox;
    private Label loadingLabel = new Label("Waiting for random opponent...");
    private Label test = new Label();

    public LoadingView() {
        vBox = this;

        Circle loadingCircle = new Circle(30, null);
        loadingCircle.setStroke(Color.WHITE);
        loadingCircle.setStrokeWidth(6);
        loadingCircle.getStrokeDashArray().addAll(10.,10.,10.,10.);
        loadingCircle.setStrokeLineCap(StrokeLineCap.ROUND);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(loadingCircle, loadingLabel, test);

        rotateTransition = new RotateTransition(Duration.seconds(6), loadingCircle);
        rotateTransition.setByAngle(180);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.setAutoReverse(true);
    }

    public void play() {
        rotateTransition.play();
    }

    public void stop() {
        rotateTransition.stop();
    }

    public void testConnected(String opponent){
        test.setText(opponent + " has joined the game");
    }

    public void testListener(EventHandler<MouseEvent> listener){
        test.setOnMouseClicked(listener);
    }

}
