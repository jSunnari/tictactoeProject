package client.gui;

import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by robin on 2016-05-20.
 */
public class GameBoardView implements Runnable {
//Kopplingen till servern
    private String ip = "localhost";
    private int port = 22222;
    private Scanner scanner = new Scanner(System.in);
    private JFrame frame;
    private final int WIDTH = 506;
    private final int HEIGHT = 527;
    private Thread thread;
//Skriv och läsa in klasser mellan clienter och servern
    private Painter painter;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
//Serverkoppling i en socket
    private ServerSocket serverSocket;
//Grafik vi kommer använda i spelet
    private BufferedImage board;
    private BufferedImage redX;
    private BufferedImage blueX;
    private BufferedImage redCircle;
    private BufferedImage blueCircle;

    private String[] spaces = new String[9];

    private boolean yourTurn = false;
    private boolean circle = true;
    private boolean accepted = false;
//Blir det för många errors i vårat spel så sätter vi boolean till true
    private boolean unableToCommunicateWithOpponent = false;
    private boolean won = false;
    private boolean enemyWon = false;
//Våra rektanglar i spelbrädet
    private int lengthOfSpace = 160;
    private int errors = 0;
//Linjen som målas ut när någon får tre i rad
    private int firstSpot = -1;
    private int secondSpot = -1;
//Textstilerna som används i spelet
    private Font font = new Font("Verdana", Font.BOLD, 32);
    private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
    private Font largerFont = new Font("Verdana", Font.BOLD, 50);
//Strings som skrivs till clienterna
    private String waitingString = "Waiting for another player";
    private String unableToCommunWithOpponent = "Unable to communicate with opponent";
    private String wonString = "You are the champ";
    private String enemyWonString = "You suck! ";


    public GameBoardView(){
        System.out.println("Please input IP: ");
//Letar efter ip (localhost)
        ip = scanner.nextLine();
        System.out.println("Please input the port: ");
//Leter efter socketnummer
        port = scanner.nextInt();
//Letar efter en port som är mellan så många socketnummer det finns
        while(port < 1 || port > 65535){
            System.out.println("Chose a port between 1-65535");
            port = scanner.nextInt();
        }

//        loadImages();

    }

    @Override
    public void run() {

    }

    public static void main(String[] args){
        GameBoardView gameBoard = new GameBoardView();

    }

   public class Painter {

   }
//Skapar en metod som läser in vår spelgrafik
//   public class loadImages(){
//
//    }

}
