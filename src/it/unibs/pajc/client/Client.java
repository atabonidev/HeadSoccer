package it.unibs.pajc.client;

import it.unibs.pajc.helpers.HelperClass;
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

    private JFrame frame;
    private GameView gameView;

    private Player controlledPlayer = new Player();
    private int playerID;
    private ExchangeDataClass modelData = new ExchangeDataClass();

    private ReadFromServer readerFS;
    private WriteToServer writerTS;

    public Client(JFrame frame) {
        this.frame = frame;
    }

    public void startServerConnection() {

        try {
            serverConnection = new Socket("127.0.0.1", Server.PORT);
            HelperClass.importImages();   //si scaricano le immagini necessarie

            ObjectOutputStream out = new ObjectOutputStream(serverConnection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverConnection.getInputStream());

            playerID = Integer.parseInt((String) in.readUnshared());      //come prima cosa riceviamo dal server l'ID del player
            System.out.println("You are player #" + playerID);

            if(playerID == 1){
                System.out.println("Waiting for Player #2 to connect...");
            }

            //creazione classi di scrittura e lettura
            readerFS = new ReadFromServer(in);
            writerTS = new WriteToServer(out);

            clientConnection();

        } catch(UnknownHostException e) {

            System.err.println("IP address of the host could not be determined : " + e.toString());

        } catch(IOException e) {

            System.err.println("Error in creating socket: " + e.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clientConnection() {
        //frame.setVisible(false);
        frame.getContentPane().removeAll();

        JPanel loadingPanel = new JPanel();

        JLabel label = new JLabel("Waiting for the opponent connection...");
        label.setFont(new Font("Arial Black", Font.PLAIN, 40));

        frame.setTitle("Player #" + playerID);
        loadingPanel.setLayout(new GridBagLayout());
        loadingPanel.add(label);
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.getContentPane().add(loadingPanel);
        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();
        frame.setVisible(true);

        frame.revalidate();
        frame.repaint();

        new Thread(() -> readerFS.waitForStartMsg()).start();
    }


    private void gameInitialization() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.setTitle("Player #" + playerID);
        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();

        gameView = new GameView(GameField.leftFootballGoal, GameField.rightFootballGoal);

        gameView.setModelData(modelData, true);

            //una volta che viene modificato il modeldata (mandato nuovo dal server) viene aggiornato quello della game view
        /*modelData.addChangeListener(e -> {
            gameView.setModelData(modelData, false);
            gameView.revalidate();
            gameView.repaint();
        });*/

        PlayerKeyboardListener kb = new PlayerKeyboardListener(controlledPlayer);
        kb.addChangeListener(writerTS::sendToServer);
        gameView.addKeyListener(kb);

        frame.setContentPane(gameView);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }


    //==================================================================================================================
    //GESTIONE INTERAZIONE CON SERVER (fra client e server)
    //==================================================================================================================

    /**
     * classe che si occupa della lettura dal server
     */
    private class ReadFromServer implements Runnable{
        //non serve l'attributo playerID in quanto possiamo accedervi direttamente essendo questa una classe interna
        private ObjectInputStream dataIn;

        public ReadFromServer(ObjectInputStream dataIn){
            this.dataIn = dataIn;

            System.out.println("RFS" + playerID + "\tRunnable created");
        }

        @Override
        public void run() {
            try {
                /*
                Leggiamo le coordinate che ci vengono inviate dal server e sono quelle relative enemy che vengono settate.
                 */
                while (true){
                    ExchangeDataClass modelUpdated = (ExchangeDataClass) dataIn.readUnshared();
                    //modelData = modelUpdated;
                    if(gameView != null) {
                        gameView.setModelData(modelUpdated, false);
                        gameView.revalidate();
                        gameView.repaint();
                    }
                }
            }catch (ClassNotFoundException | IOException e){
                System.out.println("IOException from RFC run() || pl" + playerID);
            }
        }

        /**
        Metodo che mette in pausa il thread su cui gira il PlayerFrame finché non riceve dal server il comando per continuare.
        Dopo che è stato ricevuto il messaggio iniziale si può procedere e far partire i threads di lettura e scrittura.
         */
        public void waitForStartMsg(){
            try {
                String startMsg = (String) dataIn.readUnshared();
                System.out.println("Message from server: " + startMsg);

                //partenza thread di lettura e scrittura
                Thread readThread = new Thread(readerFS);
                readThread.start();

                gameInitialization();   //partenza gioco
            }catch (IOException | ClassNotFoundException e){
                System.out.println("IOException from waitForStartMsg() || pl" + playerID);
            }
        }
    }

    /**
     * classe che si occupa di mandare i dati al server
     */
    private class WriteToServer{
        //non serve l'attributo playerID in quanto possiamo accedervi direttamente essendo questa una classe interna
        private ObjectOutputStream dataOut;

        public WriteToServer(ObjectOutputStream dataOut){
            this.dataOut = dataOut;

            System.out.println("WFS" + playerID + "\tRunnable created");
        }

        public void sendToServer(ChangeEvent changeEvent){
            try {
                dataOut.writeUnshared(controlledPlayer);
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

