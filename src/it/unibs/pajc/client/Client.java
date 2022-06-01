package it.unibs.pajc.client;

import it.unibs.pajc.model.ExchangeDataClass;
import it.unibs.pajc.model.GameField;
import it.unibs.pajc.model.Player;
import it.unibs.pajc.server.Server;
import it.unibs.pajc.view.GameView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket serverConnection;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private JFrame frame;
    private GameView gameView;
    private Thread serverListener;
    private Thread waitingThread;
    private Player controlledPlayer = new Player();
    private ExchangeDataClass modelData;

    public Client(JFrame frame) {
        this.frame = frame;
    }

    public void startServerConnection() {
        try {
            serverConnection = new Socket("127.0.0.1", Server.PORT);

            in = new ObjectInputStream(serverConnection.getInputStream());
            out = new ObjectOutputStream(serverConnection.getOutputStream());

            clientConnection();

        } catch(UnknownHostException e) {

            System.err.println("IP address of the host could not be determined : " + e.toString());

        } catch(IOException e) {

            System.err.println("Error in creating socket: " + e.toString());

        }
    }

    private void clientConnection() {
        frame.getContentPane().removeAll();

        JPanel loadingPanel = new JPanel();

        JLabel label = new JLabel("Waiting for the opponent connection...");
        label.setFont(new Font("Arial Black", Font.PLAIN, 40));

        loadingPanel.setLayout(new GridBagLayout());
        loadingPanel.add(label);
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.setContentPane(loadingPanel);
        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();

        waitingThread = new Thread(this::waitingForOtherClient);
        waitingThread.start();
    }

    private void waitingForOtherClient() {
        try {

            while(true) {
                if(in.readObject() instanceof ExchangeDataClass) {
                    gameInitialization();
                    break;
                }
            }

        } catch (IOException e) {

            System.out.println(e.toString());

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void gameInitialization() {
        frame.getContentPane().removeAll();

        frame.setLayout(new BorderLayout());
        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();

        gameView = new GameView(GameField.leftFootballGoal, GameField.rightFootballGoal);

        gameView.setModelData(modelData);

        serverListener = new Thread(this::receiveFromServer);
        serverListener.start();

        PlayerKeyboardListener kb = new PlayerKeyboardListener(controlledPlayer);
        kb.addChangeListener(this::sendToServer);
        gameView.addKeyListener(kb);

        frame.setContentPane(gameView);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);


    }

    private void sendToServer(ChangeEvent e) {
        try {
            out.writeUnshared(this.controlledPlayer);
            out.flush();
        } catch (IOException ex) {
            System.err.println("Error, data not sent: " + ex.toString());
        }
    }

    private void receiveFromServer() {
        try {

            while(true) {

                ExchangeDataClass serverModelData = (ExchangeDataClass) in.readObject();

                modelData.updateData(serverModelData);

                gameView.revalidate();
                gameView.repaint();
            }

        } catch (IOException e) {

            System.out.println(e.toString());

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }






}
