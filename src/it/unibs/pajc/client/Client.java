package it.unibs.pajc.client;

import it.unibs.pajc.MainApp;
import it.unibs.pajc.helpers.HelperClass;
import it.unibs.pajc.helpers.Sound;
import it.unibs.pajc.model.ExchangeDataClass;
import it.unibs.pajc.model.GameField;
import it.unibs.pajc.server.Server;
import it.unibs.pajc.view.GameView;
import it.unibs.pajc.view.ScoreView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket serverConnection;
    private String serverIp;

    private MainApp frame;
    private GameView gameView;

    private int playerID;
    private ExchangeDataClass modelData = new ExchangeDataClass();

    private ReadFromServer readerFS;
    private WriteToServer writerTS;

    public Client(MainApp frame, String serverIp) {
        this.frame = frame;
        this.serverIp = serverIp;
    }

    /**
     * Metodo che tenta di avviare la connessione al server di ip fornito dall'utente nel costruttore della classe Client
     */
    public void startServerConnection() {
        try {
            serverConnection = new Socket();
            serverConnection.connect(new InetSocketAddress(serverIp, Server.PORT), 1000);

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

            writerTS.sendPlayerName(frame.getTxtPlayerName().getText());

            clientConnection();

        } catch(UnknownHostException e) {

            System.err.println("IP address of the host could not be determined : " + e.toString());

        } catch(IOException e) {
            System.err.println("Error in creating socket: " + e.toString());
            frame.getTextIP().setBorder(new LineBorder(Color.RED, 2));
            frame.getTextIP().setForeground(Color.RED);
            frame.getTextIP().setText("< NON-EXISTENT SERVER >");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * ====== PRIMO PANNELLO - PANNELLO DI ATTESA ======
     * pannello intermedio nel quale si rimane
     * fino all connessione del avversario.
     */
    private void clientConnection() {
        frame.getContentPane().removeAll();

        JPanel loadingPanel = new JPanel();

        frame.getContentPane().add(loadingPanel);
        loadingPanel.setOpaque(false);

        JLabel label = new JLabel("Waiting for the opponent connection...");
        label.setFont(new Font("Arial Black", Font.PLAIN, 40));

        frame.setTitle("Player #" + playerID);
        frame.getContentPane().setLayout(new GridBagLayout());
        loadingPanel.add(label);
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();
        frame.setVisible(true);

        frame.revalidate();
        frame.repaint();

        new Thread(() -> readerFS.waitForStartMsg()).start();
    }

    /**
     * ====== SECONDO PANNELLO - PANNELLO DI GIOCO ======
     * Pannello di gioco nel quale si rimane fino a che un giocatore
     * arriva a un punteggio pari a 3 (partita terminata).
     */
    private void gameInitialization() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.setTitle("Player #" + playerID);
        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();

        gameView = new GameView(GameField.leftFootballGoal, GameField.rightFootballGoal, playerID);
        gameView.setModelData(modelData, true);

        PlayerKeyboardListener kb = new PlayerKeyboardListener(writerTS);
        gameView.addKeyListener(kb);

        frame.setContentPane(gameView);

        frame.getContentPane().setFocusable(true);
        frame.getContentPane().requestFocusInWindow();
    }

    /**
     * ====== TERZO PANNELLO - PANNELLO RISULTATO ======
     * Pannello finale che stampa il risultato a video del vincitore
     * e del perdente.
     */
    private void printResultPanel() {
        frame.getContentPane().removeAll();

        JPanel resultPanel = new JPanel();

        JLabel label = new JLabel("");
        label.setFont(new Font("Arial Black", Font.PLAIN, 60));

        boolean imWinner;

        if(this.playerID == 1)
            imWinner = modelData.getPlayer1().isWinner();
        else
            imWinner = modelData.getPlayer2().isWinner();


        if(imWinner) {
            resultPanel.setOpaque(true);
            resultPanel.setBackground(Color.green);
            label.setText("YOU WIN!");
        }
        else {
            resultPanel.setOpaque(true);
            resultPanel.setBackground(Color.red);
            label.setText("YOU LOSE!");
        }

        resultPanel.setLayout(new GridBagLayout());
        resultPanel.add(label);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.setContentPane(resultPanel);
        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();
        frame.setVisible(true);

        frame.revalidate();
        frame.repaint();

        writerTS.sendCloseMessage();

        closeConnection();
    }


    /**
     * Metodo che si occupa di effettuare la chiusura delle connessioni aperte col server
     */
    private void closeConnection() {
        try {
            readerFS.close();
            writerTS.close();
            serverConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //==================================================================================================================
    //GESTIONE INTERAZIONE CON SERVER (fra client e server)
    //==================================================================================================================

    /**
     * classe che si occupa della lettura dal server
     */
    class ReadFromServer implements Runnable{
        //non serve l'attributo playerID in quanto possiamo accedervi direttamente essendo questa una classe interna
        private ObjectInputStream dataIn;

        public ReadFromServer(ObjectInputStream dataIn){
            this.dataIn = dataIn;

            System.out.println("RFS" + playerID + "\tRunnable created");
        }

        @Override
        public void run() {
            try {
                //continuiamo a leggere il model aggiornato dal server
                while (true){
                    modelData = (ExchangeDataClass) dataIn.readUnshared();

                    if(gameView != null) {
                        gameView.setModelData(modelData, false);
                        gameView.revalidate();
                        gameView.repaint();
                    }

                    if(checkWinner(modelData))
                        break;
                }

                printResultPanel();
            }catch (ClassNotFoundException | IOException e){
                System.out.println("IOException from RFC run() || pl" + playerID);
            }
        }

        private boolean checkWinner(ExchangeDataClass modelUpdated) {
            return (modelUpdated.getPlayer1().isWinner() || modelUpdated.getPlayer2().isWinner());
        }

        /**
        Metodo che mette in pausa il thread su cui gira il PlayerFrame finché non riceve dal server il comando per continuare.
        Dopo che è stato ricevuto il messaggio iniziale si può procedere e far partire i thread di lettura e scrittura.
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

        public void close() throws IOException {
            this.dataIn.close();
        }
    }

    /**
     * classe che si occupa di mandare i dati al server
     */
    class WriteToServer{
        //non serve l'attributo playerID in quanto possiamo accedervi direttamente essendo questa una classe interna
        private ObjectOutputStream dataOut;

        public WriteToServer(ObjectOutputStream dataOut){
            this.dataOut = dataOut;
            System.out.println("WFS" + playerID + "\tRunnable created");
        }

        /**
         * Metodo usato per mandare i dati al server. I dati inviati corrispondono a comandi
         * in formato stringa che verranno interpretati in maniera corretta dal server attraverso
         * un protocollo condiviso.
         * @param commands
         */
        public void sendToServer(String commands){
            try {
                dataOut.writeUnshared(commands);
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Metodo utilizzato per comunicare il nome utente all'avvio della connessione
        public void sendPlayerName(String playerName) {
            try {
               dataOut.writeUnshared(playerName);
               dataOut.flush();

            }catch (IOException e){
               System.out.println("IOException from RFC run() || pl" + playerID);
            }
        }

        public void close() throws IOException {
            this.dataOut.close();
        }

        //Metodo che dice al server di chiudere i canali di comunicazione col client
        public void sendCloseMessage() {
            try {
                dataOut.writeUnshared("CLOSE");
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



}

