package client.gui;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by robin on 2016-06-02.
 */
public class Tile extends StackPane {
    //ImageView to hold the imported images either cross or circle
    public ImageView imgView = new ImageView();
    private int tileWidth = 180;
    private int tileHeight = 180;
    private AudioClip crossAudio = new AudioClip(GameBoardJavafxView.class.getResource("../res/plop.wav").toString());
    private AudioClip circleAudio = new AudioClip(GameBoardJavafxView.class.getResource("../res/plop.wav").toString());
    private Image crossImg = new Image("file:src/client/res/crossWhite.png");
    private Image circleImg = new Image("file:src/client/res/circleWhite.png");
    private int tieCounter = 0;
    //Create a textholder to help us identify the different images
    public Text text = new Text();
    public int id;
    private ScaleTransition scaleTransition;

    public Tile(int id) {
        this.id = id;
        Rectangle tileBorder = new Rectangle(tileWidth, tileHeight);
        tileBorder.setFill(Color.BLACK);
        tileBorder.setStroke(Color.WHITE);
        circleAudio.setRate(1.5);
        tileBorder.setStrokeWidth(10);
        text.setVisible(false);
        setAlignment(Pos.CENTER);
        scaleTransition = new ScaleTransition(Duration.seconds(0.15), imgView);
        scaleTransition.setByX(.01);
        scaleTransition.setByY(.01);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        getChildren().addAll(tileBorder, imgView);
    }

    public String getValue() {
        return text.getText();
    }

    public void drawX() {
        imgView.setImage(crossImg);
        tieCounter = 1 + tieCounter;
        crossAudio.play();
        scaleTransition.play();
        text.setText("X");
    }

    public void drawO() {
        imgView.setImage(circleImg);
        tieCounter = 1 + tieCounter;
        circleAudio.play();
        scaleTransition.play();
        text.setText("O");
    }

    public int getTileId(){
        return id;
    }
}