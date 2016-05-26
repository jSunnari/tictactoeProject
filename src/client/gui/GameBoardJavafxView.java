package client.gui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameBoardJavafxView extends Pane {

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
    private Image crossImg = new Image("file:src/client/res/crossWhite.png");
    private Image circleImg = new Image("file:src/client/res/circleWhite.png");
    private FadeTransition fadeTransition;
    private Pane gameBoard = new Pane();
    private Pane root;
    private String playerX = " (X)";
    private String playerO = " (O)";
    private String p1Name = "";
    private String p2Name = "";
    private String tieName = "Ties";
    private String p1Score = "0";
    private String p2Score = "0";
    private String tieScore = "0";
    private String turn = "*";
    //Components for scoreboard
    private Label p1NameLbl = new Label();
    private Label p2NameLbl = new Label();
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
        root = this;
        p1Vbox.getStyleClass().add("playerScoreVBoxes");
        p2Vbox.getStyleClass().add("playerScoreVBoxes");
        tieVbox.getStyleClass().add("playerScoreVBoxes");
        resetBut.getStyleClass().add("form-button");
        root.getStyleClass().add("gameboard-pane");
        showTurn();
        gameVBox.getChildren().addAll(createContent(), scoreBoardHbox);
        root.getChildren().add(gameVBox);
    }


    private Parent createContent() {

        //Setting a windowsize for our tictactoeGame
        gameBoard.setPrefSize(WIDTH, HEIGHT);
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

    public void resetGameListener (EventHandler<ActionEvent> buttonListener){
        resetBut.setOnAction(buttonListener);
    }

    public void setPlayable(boolean playable){
        this.playable = playable;
    }

    public void checkTiles() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                checkForTie();
                break;
            }
        }
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
        });
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

    public class Tile extends StackPane {
        //ImageView to hold the imported images either cross or circle
        private ImageView imgView = new ImageView();
        //Create a textholder to help us identify the different images
        private Text text = new Text();
        private int id;

        private Tile(int id) {
            this.id = id;
            Rectangle tileBorder = new Rectangle(tileWidth, tileHeight);
            tileBorder.setFill(Color.BLACK);
            tileBorder.setStroke(Color.WHITE);
            tileBorder.setStrokeWidth(10);
            text.setVisible(false);
            setAlignment(Pos.CENTER);
            getChildren().addAll(tileBorder, imgView);

            /*
            //A mouseEvent inside our tileClass in order to listen which tile is pressed on
            setOnMouseClicked(event -> {
                if (!playable)
                    return;

                if(!(getValue().equals("X") || getValue().equals("O"))) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (!turnX)
                            return;

                        drawX();
                        System.out.println(getTileId());
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
            */
        }

        public String getValue() {
            return text.getText();
        }

        public void drawX() {
            imgView.setImage(crossImg);
            tieCounter = 1 + tieCounter;
            text.setText("X");
        }

        public void drawO() {
            imgView.setImage(circleImg);
            tieCounter = 1 + tieCounter;
            text.setText("O");
        }

        public int getTileId(){
            return id;
        }
    }

    public Tile[][] getBoard(){
        return board;
    }

    public void checkForTie(){
        //The tieCounter counts the number of pieces are out on the board and checks if the game has not been won by anyone. Then it's a tie
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

    public void showTurn(){
        Platform.runLater(() -> {
            //Check if the playerLbl contains the char *, remove and add to other player if it does
            if(turnX){
                p2NameLbl.setText(p2Name + playerO);
                String changeTurn = p1NameLbl.getText();
                if(changeTurn.contains(turn)){
                    p1NameLbl.setText(p1Name + playerX);
                }else {
                    p1NameLbl.setText(changeTurn + turn);
                }
            }else{
                p1NameLbl.setText(p1Name + playerX);
                String changeTurn = p2NameLbl.getText();
                if(changeTurn.contains(turn)){
                    p2NameLbl.setText(p2Name + playerO);
                }else {
                    p2NameLbl.setText(changeTurn + turn);
                }
            }
        });
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayerX(String name){
        Platform.runLater(() -> {
            p1Name = name;
            p1NameLbl.setText(p1Name + playerX);
        });
    }

    public void setPlayerO(String name){
        Platform.runLater(() -> {
            p2Name = name;
            p2NameLbl.setText(p2Name + playerO);
        });
    }

}
