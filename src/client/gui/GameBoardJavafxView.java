package client.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoardJavafxView extends Application {

    private boolean playable = true;
    private boolean turnX = true;
    private boolean gameTie = false;
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
    private Image crossImg = new Image("../res/crossWhite.png");
    private Image circleImg = new Image("../res/circleWhite.png");
    private FadeTransition fadeTransition;
    private Pane gameBoard = new Pane();
    private Pane root = new Pane();
    private String playerX = " (X)";
    private String playerO = " (O)";
    private String p1Name = "Adam";
    private String p2Name = "Steve";
    private String tieName = "Ties";
    private String p1Score = "0";
    private String p2Score = "0";
    private String tieScore = "0";
    private String turn = "*";
//Components for scoreboard
    private Label p1NameLbl = new Label(p1Name + playerX);
    private Label p2NameLbl = new Label(p2Name + playerO);
    private Label tieNameLbl = new Label(tieName);
    private Label p1ScoreLbl = new Label(p1Score);
    private Label p2ScoreLbl = new Label(p2Score);
    private Label tieScoreLbl = new Label(tieScore);
//Layout boxes for the scoreboard
    private Button resetBut = new Button("PLAY AGAIN!!");
    private VBox p1Vbox = new VBox(p1NameLbl, p1ScoreLbl);
    private VBox p2Vbox = new VBox(p2NameLbl, p2ScoreLbl);
    private VBox tieVbox = new VBox(tieNameLbl, tieScoreLbl);
    private HBox scoreBoardHbox = new HBox(p1Vbox, tieVbox, p2Vbox, resetBut);
    private VBox gameVBox = new VBox();

    public GameBoardJavafxView(){
        p1Vbox.getStyleClass().add("playerScoreVBoxes");
        p2Vbox.getStyleClass().add("playerScoreVBoxes");
        tieVbox.getStyleClass().add("playerScoreVBoxes");
        resetBut.getStyleClass().add("form-button");
        showTurn();
    }


    private Parent createContent() {
//Setting a windowsize for our tictactoeGame
        gameBoard.setPrefSize(WIDTH, HEIGHT);
//A loop within a loop to add our tiles to the board matrix
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * tileWidth);
                tile.setTranslateY(i * tileHeight);
                gameBoard.getChildren().add(tile);
                board[j][i] = tile;
            }
        }
        // horizontal
        /*
        *{0, 1, 2}
        *{0, 1, 2}
        *{0, 1, 2}
         */
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }
        // vertical
        /*
        *{0, 0, 0}
        *{1, 1, 1}
        *{2, 2, 2}
         */
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }
        // diagonals
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return gameBoard;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameVBox.getChildren().addAll(createContent(), scoreBoardHbox);
        root.getChildren().add(gameVBox);
        resetBut.setOnAction(event ->{
            playable = true;
            checkForTie();
            resetBoard();
        });
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add("../res/style.css"); //check the file style.css in /res.
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=VT323");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Press+Start+2P");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checkTiles() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                checkForTie();
                break;
            }
        }
    }

    private void resetBoard(){
        //Looping through our tiles in the board and reset our images and texts
        for(int i = 0; i < 3; i++ ){
            for(int j = 0; j < 3; j++){
                board[j][i].imgView.setImage(null);
                board[j][i].text.setText(null);
            }
        }
    }

    private void playWinAnimation(Combo combo) {

//Winning animation on the cross and circles
    for(int i = 0; i < 3; i++) {
        fadeTransition = new FadeTransition(Duration.seconds(.2), combo.tiles[i].imgView);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0.1);
        fadeTransition.setCycleCount(4);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

        if(!turnX){
            int p1CurrScore = Integer.parseInt(p1ScoreLbl.getText());
                p1CurrScore = p1CurrScore + 1;
            p1ScoreLbl.setText(Integer.toString(p1CurrScore));
        }
        else if(turnX){
            int p2CurrScore = Integer.parseInt(p2ScoreLbl.getText());
                p2CurrScore = p2CurrScore + 1;
            p2ScoreLbl.setText(Integer.toString(p2CurrScore));
        }
    }

    private class Combo {
        private Tile[] tiles;
        private Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        private boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private class Tile extends StackPane {
//ImageView to hold the imported images either cross or circle
        private ImageView imgView = new ImageView();
//Create a textholder to help us identify the different images
        private Text text = new Text();

        private Tile() {
            Rectangle tileBorder = new Rectangle(tileWidth, tileHeight);
            tileBorder.setFill(Color.BLACK);
            tileBorder.setStroke(Color.WHITE);
            tileBorder.setStrokeWidth(10);
            text.setVisible(false);
            setAlignment(Pos.CENTER);
            getChildren().addAll(tileBorder, imgView);
//A mouseEvent inside our tileClass in order to listen which tile is pressed on
            setOnMouseClicked(event -> {
                if (!playable)
                    return;

                if(!(getValue().equals("X") || getValue().equals("O"))) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (!turnX)
                            return;

                        drawX();
                        turnX = false;
                        showTurn();
                        checkTiles();
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        if (turnX)
                            return;

                        drawO();
                        turnX = true;
                        showTurn();
                        checkTiles();
                    }
                }
            });
        }

        private String getValue() {
            return text.getText();
        }


        private void drawX() {
            imgView.setImage(crossImg);
            tieCounter = 1 + tieCounter;
            text.setText("X");
        }

        private void drawO() {
            imgView.setImage(circleImg);
            tieCounter = 1 + tieCounter;
            text.setText("O");
        }
    }

//    public void resetBoardBut(){
//        resetBut.setOnAction(event ->{
//            playable = true;
//            resetBoard();
//        });
//    }
    private void checkForTie(){
        System.out.println(tieCounter);
        if(tieCounter == 9 && playable){
            gameTie = true;
        }
        if(gameTie){
            int currTieScore = Integer.parseInt(tieScoreLbl.getText());
            currTieScore = currTieScore + 1;
            tieScoreLbl.setText(Integer.toString(currTieScore));
        }
        tieCounter = 0;
        gameTie = false;
    }
    private void showTurn(){
        if(turnX){
            String changeTurn = p1NameLbl.getText();
            if(changeTurn.contains(turn)){
                   p1NameLbl.setText(p1Name + playerX);
            }else {
                System.out.println(changeTurn);
                p1NameLbl.setText(changeTurn + turn);
            }
        }else{
            String changeTurn = p2NameLbl.getText();

            if(changeTurn.contains(turn)){
                p2NameLbl.setText(p2Name + playerO);
            }else {
                System.out.println(changeTurn);
                p2NameLbl.setText(changeTurn + turn);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
