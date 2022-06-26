package it.unibs.pajc.client;

import it.unibs.pajc.helpers.HelperClass;
import it.unibs.pajc.model.ExchangeDataClass;
import it.unibs.pajc.model.GameField;
import it.unibs.pajc.server.Server;
import it.unibs.pajc.view.GameView;
import javax.swing.*;
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
            //HelperClass.importFonts();     //si scarica il font necessario

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
        frame.getContentPane().removeAll();

        JPanel loadingPanel = new JPanel();

        loadingPanel.setOpaque(false);

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

        PlayerKeyboardListener kb = new PlayerKeyboardListener(writerTS);
        gameView.addKeyListener(kb);

        frame.setContentPane(gameView);
    }

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


    //Chiude le connessioni col server
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
                /*
                Leggiamo le coordinate che ci vengono inviate dal server e sono quelle relative enemy che vengono settate.
                 */
                while (true){
                    modelData = (ExchangeDataClass) dataIn.readUnshared();
                    if(gameView != null) {
                        gameView.setModelData(modelData, false);
                        gameView.revalidate();
                        gameView.repaint();
                        if(checkWinner(modelData))
                            break;
                    }
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

        public void sendToServer(String commands){
            try {
                dataOut.writeUnshared(commands);
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close() throws IOException {
            this.dataOut.close();
        }

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

