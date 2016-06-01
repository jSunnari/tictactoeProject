package client.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameBoardJavafxView extends HBox {

    private boolean playable = true;
    //Boardgame dimensions
    private int HEIGHT = 540;
    private int WIDTH = 540;
    private int tileWidth = 180;
    private int tileHeight = 180;
    private int tieCounter = 0;
    //Boardmatrix for containing our tiles
    private Tile[][] board = new Tile[3][3];
    //List for possible win combinations
    private List<Combo> combos = new ArrayList<>();
    //Game graphics
    private Image crossImg = new Image("file:src/client/res/crossWhite.png");
    private Image circleImg = new Image("file:src/client/res/circleWhite.png");
    private FadeTransition fadeTransition;
    private AudioClip crossAudio = new AudioClip(GameBoardJavafxView.class.getResource("../res/plop.wav").toString());
    private AudioClip circleAudio = new AudioClip(GameBoardJavafxView.class.getResource("../res/plop.wav").toString());
    //Scoreboard components
    private String playerX = " (X)";
    private String playerO = " (O)";
    private String p1Name = "";
    private String p2Name = "";
    private String tieName = "Ties";
    private int p1Score = 0;
    private int p2Score = 0;
    private int tieScore = 0;
    private Label p1NameLbl = new Label();
    private Label p2NameLbl = new Label();
    private Label tieNameLbl = new Label(tieName);
    private Label p1ScoreLbl = new Label("0");
    private Label p2ScoreLbl = new Label("0");
    private Label tieScoreLbl = new Label("0");
    private Label playAgainLbl = new Label();
    //New game and exit game buttons
    private Button resetBut = new Button("Play again");
    private Button exitBut = new Button("Exit game");
    //Gameboard layouts
    private Pane gameBoard = new Pane();
    private HBox gameBoardHbox = new HBox();
    private VBox p1Vbox = new VBox(p1NameLbl, p1ScoreLbl);
    private VBox p2Vbox = new VBox(p2NameLbl, p2ScoreLbl);
    private VBox tieVbox = new VBox( tieNameLbl, tieScoreLbl);
    private VBox butHbox = new VBox(resetBut, exitBut, playAgainLbl);
    private VBox scoreBoardVbox = new VBox( p1Vbox, tieVbox, p2Vbox, butHbox);

    public GameBoardJavafxView(){

        gameBoardHbox = this;
        resetBut.setVisible(false);
        exitBut.setVisible(false);

        //Placement for the different components
        p1Vbox.setAlignment(Pos.TOP_CENTER);
        p2Vbox.setAlignment(Pos.TOP_CENTER);
        tieVbox.setAlignment(Pos.TOP_CENTER);
        scoreBoardVbox.setAlignment(Pos.TOP_CENTER);

        resetBut.setPrefWidth(230);
        exitBut.setPrefWidth(230);

        scoreBoardVbox.setPadding(new Insets(80));
        gameBoardHbox.getChildren().addAll(createContent(), scoreBoardVbox);
        gameBoardHbox.setPadding(new Insets(50, 0, 0, 30));
        gameBoardHbox.setAlignment(Pos.TOP_CENTER);
        //Adding css classes to our components
        circleAudio.setRate(1.5);
        p1Vbox.getStyleClass().add("playerScoreVBoxes");
        p2Vbox.getStyleClass().add("playerScoreVBoxes");
        tieVbox.getStyleClass().add("playerScoreVBoxes");
        resetBut.getStyleClass().add("form-button");
        exitBut.getStyleClass().add("form-button");


    }
    //
    private Parent createContent() {
        gameBoard.setPrefSize(WIDTH, HEIGHT);
        //Setting a windowsize for our tictactoeGame
        int id = 1;
        //A loop within a loop to add our tiles to the board matrix
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile(id);
                id++;
                tile.setTranslateX(j * tileWidth);
                tile.setTranslateY(i * tileHeight);
                gameBoard.getChildren().add(tile);
                board[j][i] = tile;
            }
        }
        //Setting up different types of win scenarios
        //Adding all the possible ways to win in an arraylist
        //Which we can later compare to the laid out pieces on our board
        // horizontal
        /*
        *0{0, 1, 2}
        *1{0, 1, 2}
        *2{0, 1, 2}
         */
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[0][x], board[1][x], board[2][x]));
        }
        // vertical
        /*
        * 0, 1, 2
        *{0, 0, 0}
        *{1, 1, 1}
        *{2, 2, 2}
         */
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[y][0], board[y][1], board[y][2]));
        }
        // diagonals
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return gameBoard;
    }

    public void resetGameListener (EventHandler<ActionEvent> buttonListener){
        resetBut.setOnAction(buttonListener);
    }
    public void exitGameListener (EventHandler<ActionEvent> buttonListener){
        exitBut.setOnAction(buttonListener);
    }

    public void setPlayable(boolean playable){
        this.playable = playable;
    }

    //A boolean that compare our current tileplacements with our saved "Winning combinations"
    public boolean checkTiles() {
        boolean winningPlayer = false;
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                winningPlayer = true;
                playable = false;
                playWinAnimation(combo);
                break;
            }
        }
        return winningPlayer;
    }

    public void resetBoard(){
        //Looping through our tiles in the board and reset our images and texts
        for(int i = 0; i < 3; i++ ){
            for(int j = 0; j < 3; j++){
                board[j][i].imgView.setImage(null);
                board[j][i].text.setText(null);
            }
        }
        playable = true;
    }


    private void playWinAnimation(Combo combo) {
        Platform.runLater(() -> {
            //Winning animation on the cross and circles
            for(int i = 0; i < 3; i++) {
                fadeTransition = new FadeTransition(Duration.seconds(.2), combo.tiles[i].imgView);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0.1);
                fadeTransition.setCycleCount(4);
                fadeTransition.setAutoReverse(true);
                fadeTransition.play();
            }
        });
    }
    //Methods that increases the score on the scoreboard
    public void incPlayer1Score(){
        Platform.runLater(() -> {
            p1Score++;
            p1ScoreLbl.setText(String.valueOf(p1Score));
        });
    }

    public void incPlayer2Score(){
        Platform.runLater(() ->{
            p2Score++;
            p2ScoreLbl.setText(String.valueOf(p2Score));
        });
    }

    public void incTieScore(){
        Platform.runLater(() ->{
            tieScore++;
            tieScoreLbl.setText(String.valueOf(tieScore));
        });
    }

    private class Combo {
        private Tile[] tiles;
        private Combo(Tile... tiles) {
            this.tiles = tiles;
        }
        //If our tiles doesnt match up with our different combinations we return false, because nobody has won
        private boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;
        //we return true if our combo conditions are met
            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    public class Tile extends StackPane {
        //ImageView to hold the imported images either cross or circle
        private ImageView imgView = new ImageView();
        //Create a textholder to help us identify the different images
        private Text text = new Text();
        private int id;
        private ScaleTransition scaleTransition;

        private Tile(int id) {
            this.id = id;
            Rectangle tileBorder = new Rectangle(tileWidth, tileHeight);
            tileBorder.setFill(Color.BLACK);
            tileBorder.setStroke(Color.WHITE);
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

    public Tile[][] getBoard(){
        return board;
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayerX(String name, String turn){
        Platform.runLater(() -> {
            p1Name = name;
            p1NameLbl.setText(p1Name + playerX + turn);
        });
    }

    public void setPlayerO(String name, String turn){
        Platform.runLater(() -> {
            p2Name = name;
            p2NameLbl.setText(p2Name + playerO + turn);
        });
    }

    public void setPlayAgainBtnVisible(boolean visible){
        Platform.runLater(() -> resetBut.setVisible(visible));
    }

    public void setExitBtnVisible(boolean visible){
        Platform.runLater(() -> exitBut.setVisible(visible));
    }

    public void setPlayAgainLbl(String message){
        Platform.runLater(() -> playAgainLbl.setText(message));
    }

}
