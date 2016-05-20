package client.gui;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by robin on 2016-05-20.
 */
public class GameBoardView implements Runnable {
//Connection to the server
    private String ip = "localhost";
    private int port = 22222;
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
//The rectangle spaces in the board
    private int lengthOfSpace = 160;
    private int errors = 0;
//Line that writes is there is three in a row
    private int firstSpot = -1;
    private int secondSpot = -1;
//Textfont in the game
    private Font font = new Font("Verdana", Font.BOLD, 32);
    private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
    private Font largerFont = new Font("Verdana", Font.BOLD, 50);
//Message strings for the clients
    private String waitingString = "Waiting for another player";
    private String unableToCommunWithOpponent = "Unable to communicate with opponent";
    private String wonString = "You are the champ";
    private String enemyWonString = "You suck! ";


    public GameBoardView(){
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
    public void render(Graphics g){

    }
    public void tick(){

    }
    private void listenForServerRequest(){

    }
    private boolean connect(){
        try{
            socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.flush();
            dis = new DataInputStream(socket.getInputStream());
            accepted = true;
        }catch (IOException e){
            System.out.println("Unable to connect to the address");
            return false;
        }
        System.out.println("Succesfully connected to server");
        return true;
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
    //I HAVE NO IDEA WHY!!
        private static final long serialVersionUID = 1;

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
