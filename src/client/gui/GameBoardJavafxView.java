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
//Boardgame dimensions
    private int HEIGHT = 540;
    private int WIDTH = 540;
    private int tileWidth = 180;
    private int tileHeight = 180;
//Boardmatrix for containing our tiles
    private Tile[][] board = new Tile[3][3];
    //List for possible win combinations
    private List<Combo> combos = new ArrayList<>();
    private Image crossImg = new Image("../res/crossWhite.png");
    private Image circleImg = new Image("../res/circleWhite.png");
    private FadeTransition fadeTransition;
    private FadeTransition fadeTransition2;
    private FadeTransition fadeTransition3;
    private Pane gameBoard = new Pane();
    private Pane root = new Pane();
    private String p1Name = "Adam";
    private String p2Name = "Steve";
    private String tieName = "Ties";
    private String p1Score = "0";
    private String p2Score = "0";
    private String tieScore = "0";
//Components for scoreboard
    private Label p1NameLbl = new Label(p1Name);
    private Label p2NameLbl = new Label(p2Name);
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
        resetBut.setOnAction(event ->{
            playable = true;
            resetBoard();
        });
        gameVBox.getChildren().addAll(createContent(), scoreBoardHbox);
        root.getChildren().add(gameVBox);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void checkTiles() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                break;
            }
        }
    }

    private void resetBoard(){
        for (Combo combo : combos){
            if(combo.isComplete()){
                //Looping through our tiles in the board and reset our images and texts
                    for(int i = 0; i < 3; i++ ){
                        for(int j = 0; j < 3; j++){
                            board[j][i].imgView.setImage(null);
                            board[j][i].text.setText(null);
                        }
                    }
            }
        }
    }

    private void playWinAnimation(Combo combo) {

//Winning animation on the cross and circles
        fadeTransition = FadeTransitionBuilder.create()
                .duration(Duration.seconds(.2))
                .node(combo.tiles[0].imgView)
                .fromValue(1)
                .toValue(0)
                .cycleCount(4)
                .autoReverse(true)
                .build();
        fadeTransition2 = FadeTransitionBuilder.create()
                .duration(Duration.seconds(.2))
                .node(combo.tiles[1].imgView)
                .fromValue(1)
                .toValue(0.1)
                .cycleCount(4)
                .autoReverse(true)
                .build();
        fadeTransition3 = FadeTransitionBuilder.create()
                .duration(Duration.seconds(.2))
                .node(combo.tiles[2].imgView)
                .fromValue(1)
                .toValue(0.1)
                .cycleCount(4)
                .autoReverse(true)
                .build();
        if(!turnX){
            System.out.println("SCOOOOOOOOOOREE!!!");
            int p1CurrScore = Integer.parseInt(p1ScoreLbl.getText());
                p1CurrScore = p1CurrScore + 1;
            p1ScoreLbl.setText(Integer.toString(p1CurrScore));
        }
        else if(turnX){
            System.out.println("SCOOOOOOOOOOREENNNNNNRRRR222222!!!");
            int p2CurrScore = Integer.parseInt(p2ScoreLbl.getText());
                p2CurrScore = p2CurrScore + 1;
            p2ScoreLbl.setText(Integer.toString(p2CurrScore));
        }
        fadeTransition.play();
        fadeTransition2.play();
        fadeTransition3.play();
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
                if (!playable){
                    return;
                }

                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!turnX)
                        return;

                    drawX();
                    turnX = false;
                    checkTiles();
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    if (turnX)
                        return;

                    drawO();
                    turnX = true;
                    checkTiles();
                }
            });
        }

        private String getValue() {
            return text.getText();
        }


        private void drawX() {
            imgView.setImage(crossImg);
            text.setText("X");
        }

        private void drawO() {
            imgView.setImage(circleImg);
            text.setText("O");
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
