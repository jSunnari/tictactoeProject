package client.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadFactory;

/**
 * Created by robin on 2016-05-20.
 */
public class GameBoardView extends Application implements Runnable {
//Connection to the server
    private String ip;
    private int port;
    private Scanner scanner = new Scanner(System.in);
//    private JFrame frame;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private Thread thread;
//Read and write variables for writes between server and clients
    private Painter painter;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
//Serverconnecting variable
    private ServerSocket serverSocket;
//Graphicvariables for the images we use in the game
//    private BufferedImage board;
//    private BufferedImage whiteX;
//    private BufferedImage whiteCircle;
    private String[] spaces = new String[9];
    private boolean yourTurn = false;
    private boolean circle = true;
    private boolean accepted = false;
//If there are to many errors this boolean is set to true
//    private boolean unableToCommunicateWithOpponent = false;
//    private boolean won = false;
//    private boolean enemyWon = false;
//    private boolean tie = false;
////The rectangle spaces in the board
//    private int lengthOfSpace = 160;
////Line that writes is there is three in a row
//    private int firstSpot = -1;
//    private int secondSpot = -1;
//Textfont in the game
//    private Font font = new Font("Verdana", Font.BOLD, 32);
//    private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
//    private Font largerFont = new Font("Verdana", Font.BOLD, 50);
//Message strings for the clients
//    private String waitingString = "Waiting for another player";
//    private String unableToCommunWithOpponentStr = "Unable to communicate with opponent";
//    private String wonString = "You are the champ";
//    private String enemyWonString = "You suck! ";
//    private String tieString = "It's a tie";
    private String p1Name = "Adam";
    private String p2Name = "Steve";
    private String tieName = "Ties";
    private String p1Score = "0";
    private String p2Score = "0";
    private String tieScore = "0";
//Components for scoreboard
//    private Label p1NameLbl = new Label(p1Name);
//    private Label p2NameLbl = new Label(p2Name);
//    private Label tieNameLbl = new Label(tieName);
//    private Label p1ScoreLbl = new Label(p1Score);
//    private Label p2ScoreLbl = new Label(p2Score);
//    private Label tieScoreLbl = new Label(tieScore);
//Layout boxes for the scoreboard
//    private VBox p1Vbox = new VBox(p1NameLbl, p1ScoreLbl);
//    private VBox p2Vbox = new VBox(p2NameLbl, p2ScoreLbl);
//    private VBox tieVbox = new VBox(tieNameLbl, tieScoreLbl);
//    private HBox scoreBoardHbox = new HBox(p1Vbox, p2Vbox, tieVbox);
//The root of roots
    private Pane root = new Pane();
    private Tile[][] board = new Tile[3][3];
    private List<Combo> wins = new ArrayList<>();

    private GameBoardView(){
        System.out.println("Please input IP: ");
//Catching a IP number from console
        ip = scanner.nextLine();
        System.out.println("Please input the port: ");
//Catching a port number from console
        port = scanner.nextInt();
//Searching for a port between the min and max portnumbers
        while(port < 1 || port > 65535){
            System.out.println("Chose a port between 1-65535");
            port = scanner.nextInt();
        }

        if(!connect()) initializeServer();
        thread = new Thread(this, "TicTacToe");
        thread.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(showBoard()));
        primaryStage.show();
    }

    @Override
    public void run() {
        while(true){
            tick();
            if(!circle && !accepted){
                listenForServerRequest();
            }
        }
    }

    private void tick(){
//        if(errors >= 10) unableToCommunicateWithOpponent = true;
        if(!yourTurn){
            try{
                int space = dis.readInt();
                if(circle) spaces[space] = "X";
                else spaces[space] = "O";
//                checkForEnemyWin();
//                checkForTie();
                yourTurn = true;
            }catch(IOException e){
             e.printStackTrace();
//                errors++;
            }
        }
    }

    private void listenForServerRequest(){
        Socket socket = null;
            try{
                socket = serverSocket.accept();
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                accepted = true;
                System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
            }catch(IOException e){
                e.printStackTrace();
            }
    }
    private boolean connect(){
        try{
            socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Mellan dos och dis");
            dis = new DataInputStream(socket.getInputStream());
            accepted = true;
        }catch (IOException e){
            System.out.println("Unable to connect to the address: Starting a server");
            return false;
        }
        System.out.println("Succesfully connected to server");
        return true;
    }
//Starting our server
    private void initializeServer(){
        try{
            serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
        }catch(IOException e){
            e.printStackTrace();
        }
        yourTurn = true;
        circle = false;
    }

    private Parent showBoard(){

        root.setPrefSize(WIDTH, 600);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);
                root.getChildren().add(tile);
                board[j][i] = tile;
            }
        }

        // horizontal
        for (int y = 0; y < 3; y++) {
            wins.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }

        // vertical
        for (int x = 0; x < 3; x++) {
            wins.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        // diagonals
        wins.add(new Combo(board[0][0], board[1][1], board[2][2]));
        wins.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return root;
    }

    private void checkState() {
        for (Combo combo : wins) {
            if (combo.isComplete()) {
                yourTurn = false;
                break;
            }
        }
    }
    private class Combo {
        private Tile[] tiles;
        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private class Tile extends StackPane {
        private Text text = new Text();

        public Tile() {
            Rectangle border = new Rectangle(200, 200);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {

                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!yourTurn)
                        return;

                    drawX();
                    yourTurn = false;
                    checkState();
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    if (yourTurn)
                        return;

                    drawO();
                    yourTurn = true;
                    checkState();
                }
            });
        }

        public double getCenterX() {
            return getTranslateX() + 100;
        }

        public double getCenterY() {
            return getTranslateY() + 100;
        }

        public String getValue() {
            return text.getText();
        }

        private void drawX() {
            text.setText("X");
        }

        private void drawO() {
            text.setText("O");
        }
    }
    public static void main(String[] args){
        GameBoardView GBV = new GameBoardView();
        launch(args);
    }

}
