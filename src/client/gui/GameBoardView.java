package client.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by robin on 2016-05-20.
 */
public class GameBoardView implements Runnable {
//Connection to the server
    private String ip;
    private int port;
    private Scanner scanner = new Scanner(System.in);
    private JFrame frame;
    private final int WIDTH = 506;
    private final int HEIGHT = 527;
    private Thread thread;
//Read and write variables for writes between server and clients
    private Painter painter;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
//Serverconnecting variable
    private ServerSocket serverSocket;
//Graphicvariables for the images we use in the game
    private BufferedImage board;
    private BufferedImage whiteX;
    private BufferedImage whiteCircle;
    private String[] spaces = new String[9];
    private boolean yourTurn = false;
    private boolean circle = true;
    private boolean accepted = false;
//If there are to many errors this boolean is set to true
    private boolean unableToCommunicateWithOpponent = false;
    private boolean won = false;
    private boolean enemyWon = false;
    private boolean tie = false;
//The rectangle spaces in the board
    private int lengthOfSpace = 160;
//Line that writes is there is three in a row
    private int firstSpot = -1;
    private int secondSpot = -1;
//Textfont in the game
    private Font font = new Font("Verdana", Font.BOLD, 32);
    private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
    private Font largerFont = new Font("Verdana", Font.BOLD, 50);
//Message strings for the clients
    private String waitingString = "Waiting for another player";
    private String unableToCommunWithOpponentStr = "Unable to communicate with opponent";
    private String wonString = "You are the champ";
    private String enemyWonString = "You suck! ";
    private String tieString = "It's a tie";
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
    private VBox p1Vbox = new VBox(p1NameLbl, p1ScoreLbl);
    private VBox p2Vbox = new VBox(p2NameLbl, p2ScoreLbl);
    private VBox tieVbox = new VBox(tieNameLbl, tieScoreLbl);
    private HBox scoreBoardHbox = new HBox(p1Vbox, p2Vbox, tieVbox);
//The root of roots
    private BorderPane root = new BorderPane();
    private int[][] wins = new int[][]{
    //Possible setups of tiles to win the game, horizontal, vertical, diagonal
            {0, 1, 2} ,{3, 4, 5} ,{6, 7, 8},
            {0, 3, 6} ,{1, 4, 7} ,{2, 5, 8},
            {0, 4, 8} ,{2, 4, 6}
    };

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

        loadImages();

        painter = new Painter();
        painter.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        if(!connect()) initializeServer();

        frame = new JFrame();
        frame.setTitle("Tic-Tac-Toe");
        frame.setContentPane(painter);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        thread = new Thread(this, "TicTacToe");
        thread.start();
    }

    @Override
    public void run() {
        while(true){
            tick();
            painter.repaint();
            if(!circle && !accepted){
                listenForServerRequest();
            }
        }
    }
    private void render(Graphics g){
        g.drawImage(board, 0 ,0, null);
        if(unableToCommunicateWithOpponent){
            g.setColor(Color.RED);
            g.setFont(smallerFont);
            Graphics2D g2 = (Graphics2D) g;
//Smooths our text, if the text is big it might look pixel (Maybe remove?)
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//Method that checks if the passed in string is in the font that we are using
            int stringWidth = g2.getFontMetrics().stringWidth(unableToCommunWithOpponentStr);
            g.drawString(unableToCommunWithOpponentStr, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
            return;
        }

        if(accepted){
            //If opponent is connected
            for (int i = 0; i < spaces.length; i++){
                if(spaces[i] != null) {
                    //Checks the array indexes if it holds a X or a O
                    if (spaces[i].equals("X")) {
                        if (circle) {
                            //If circle is true, paint the circle, LengthOfSpace = the rectangle image will be painted inside, +10 is the width of the border
                            g.drawImage(whiteX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                        } else {
                            g.drawImage(whiteX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                        }
                    }else if (spaces[i].equals("O")) {
                        if(circle) {
                            g.drawImage(whiteCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                        } else {
                            g.drawImage(whiteCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);

                        }
                    }
                }
            }
            //Checks if anyone has won
            if(won || enemyWon){
                Graphics2D g2 = (Graphics2D) g;
             //Setting the line through the lookALike objects
                g2.setStroke(new BasicStroke(10));
                g.setColor(Color.black);
                g.drawLine(firstSpot % 3 * lengthOfSpace + 10 * firstSpot % 3 + lengthOfSpace / 2, (int) (firstSpot / 3) * lengthOfSpace + 10 * (int)(firstSpot / 3) + lengthOfSpace / 2,
                        secondSpot % 3 * lengthOfSpace + 10 * secondSpot % 3 + lengthOfSpace / 2, (int) (secondSpot / 3) * lengthOfSpace + 10 * (int) (secondSpot / 3) + lengthOfSpace / 2);

                g.setColor(Color.RED);
                g.setFont(largerFont);

                if(won){
                    int stringWidth = g2.getFontMetrics().stringWidth(wonString);
                    g.drawString(wonString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
                }else if(enemyWon){
                    int stringWidth = g2.getFontMetrics().stringWidth(enemyWonString);
                    g.drawString(enemyWonString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
                }
            }
            if(tie){
                Graphics2D g2 = (Graphics2D) g;
                g.setColor(Color.RED);
                g.setFont(largerFont);
                int stringWidth = g2.getFontMetrics().stringWidth(tieString);
                g.drawString(tieString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
            }
        //If noone has connected yet
        }else {
            g.setColor(Color.RED);
            g.setFont(font);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(waitingString);
            g.drawString(waitingString, WIDTH / 2 - stringWidth, HEIGHT / 2);
        }
    }
    private void tick(){
//        if(errors >= 10) unableToCommunicateWithOpponent = true;
        if(!yourTurn){
            try{
                int space = dis.readInt();
                if(circle) spaces[space] = "X";
                else spaces[space] = "O";
                checkForEnemyWin();
                checkForTie();

                yourTurn = true;
            }catch(IOException e){
             e.printStackTrace();
//                errors++;
            }
        }
    }

    private void checkForWin(){
        for(int i = 0; i < wins.length; i++){
                if (circle) {
                    if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        won = true;
                    }
                } else {
                    if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        won = true;
                    }
                }
        }
    }
    private void checkForEnemyWin(){
        for(int i = 0; i < wins.length; i++){
            if(spaces[i] != null) {
                if (circle) {
                    if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        enemyWon = true;
                    }
                } else {
                    if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        enemyWon = true;
                    }
                }
            }
        }
    }
    private void checkForTie(){
        //Check if every square is filled, then it's a tie
      if(!won && !enemyWon) {
          for (int i = 0; i < spaces.length; i++) {
              if (spaces[i] == null) {
                  return;
              }
          }
          tie = true;
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

    //Create a method that loads our gameGraphics
    public void loadImages(){
        try{
            board = ImageIO.read(getClass().getResourceAsStream("/boardWhiteLines.png"));
            whiteX = ImageIO.read(getClass().getResourceAsStream("/crossWhite.png"));
            whiteCircle = ImageIO.read(getClass().getResourceAsStream("/circleWhite.png"));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        GameBoardView gameBoard = new GameBoardView();

    }
//TODO Change to VBOX JPanel just for testing
   private class Painter extends JPanel implements MouseListener {

        public Painter(){
            setFocusable(true);
            requestFocus();
            setBackground(Color.black);
            addMouseListener(this);
        }
        @Override
        public void paintComponent(Graphics g){
    //Synchronize method incase the JPanel isn't up to date with the windowframe
            super.paintComponent(g);
            render(g);
            Toolkit.getDefaultToolkit().sync();
        }

       @Override
       public void mouseClicked(MouseEvent e) {
        if(enemyWon || won){
            for(int i = 0; i <spaces.length; i++){
                spaces[i] = null;
            }
        }
            if(accepted){
                if(yourTurn && !unableToCommunicateWithOpponent && !won && !enemyWon){
                    int x = e.getX() / lengthOfSpace;
                    int y = e.getY() / lengthOfSpace;
                    y *= 3;
                    int position = x + y;

                    if(spaces[position] == null){
                        if(!circle) spaces[position] = "X";
                        else spaces[position] = "O";
                        yourTurn = false;
                        repaint();
                        Toolkit.getDefaultToolkit().sync();

                        try{
                            dos.writeInt(position);
                            dos.flush();
                        }catch (IOException el){
//                            errors++;
                            el.printStackTrace();
                        }
                        System.out.println("DATA WAS SENT");
                        checkForWin();
                        checkForTie();
                    }
                }
            }
       }

       @Override
       public void mousePressed(MouseEvent e) {

       }

       @Override
       public void mouseReleased(MouseEvent e) {

       }

       @Override
       public void mouseEntered(MouseEvent e) {

       }

       @Override
       public void mouseExited(MouseEvent e) {

       }
   }
}
