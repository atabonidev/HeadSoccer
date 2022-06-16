package it.unibs.pajc.server;

import it.unibs.pajc.helpers.HelperClass;
import it.unibs.pajc.model.ExchangeDataClass;
import it.unibs.pajc.model.GameField;
import it.unibs.pajc.model.Player;

import javax.swing.event.ChangeEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 1234;
    ServerSocket serverSocket;
    private Socket socketPl1;
    private Socket socketPl2;

    private GameField gameField;
    private ExchangeDataClass modeldata = null;

    private int numPlayers;
    private int maxPlayers = 2;   //massimo di giocatori permessi

    //istanze di lettura e scrittura relative ai singoli client gestiti
    private ReadFromClient pl1Reader;
    private ReadFromClient pl2Reader;

    private WriteToClient pl1Writer;
    private WriteToClient pl2Writer;

    public Server(){
        System.out.println("=======GAME SERVER ========");

        numPlayers = 0;
        maxPlayers = 2;

        try {
            serverSocket =  new ServerSocket(PORT);
            HelperClass.importImages();   //si scaricano le immagini necessarie

            gameField = new GameField();
            modeldata = new ExchangeDataClass(gameField);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * metodo che avvia il server e permette di accettare le connessioni con i client
     */
    public void startServer() {
        try {

            while (numPlayers < maxPlayers) {
                Socket client = serverSocket.accept();

                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                numPlayers++;
                //out.writeInt(numPlayers); //di manda al player il suo ID  ---> usato per dare il titolo alla schermata
                System.out.println("Player #" + numPlayers + "has connected.");

                //creazione delle classi di lettura e scrittura
                WriteToClient wtc = new WriteToClient(numPlayers, out);
                ReadFromClient rfc = new ReadFromClient(numPlayers, in);

                //assegnazione opportuna delle due istanze
                if (numPlayers == 1) {
                    socketPl1 = client;
                    pl1Reader = rfc;
                    pl1Writer = wtc;
                } else {
                    socketPl2 = client;
                    pl2Reader = rfc;
                    pl2Writer = wtc;

                    //solo in questo momento, ossia quando il secondo player si è connesso si fa partire il gioco
                    startGame();

                    Thread readThreadPl1 = new Thread(pl1Reader);
                    Thread readThreadPl2 = new Thread(pl2Reader);
                    readThreadPl1.start();
                    readThreadPl2.start();

                    gameField.addChangeListener(this::modelUpdated);
                }
            }
            System.out.println("No longer accepting connections");

        } catch (IOException e) {
            System.out.println("Error from accepting connections");
        }
    }

    /**
     * Quando entrambi i client sono connessi invio i dati a entrambi e do inizio alla partita
     */
    private  void startGame() {
        gameField.startGame();
        pl1Writer.sendStartMsg();
        pl2Writer.sendStartMsg();
    }

    /**
     * Quando il model del server viene aggiornato allora tutti i client si aggiornano
     * @param e
     */
    private  void modelUpdated(ChangeEvent e) {
        pl1Writer.sendDataToClient(modeldata);
        pl2Writer.sendDataToClient(modeldata);
    }

    //==================================================================================================================
    //GESTIONE INTERAZIONE CON SERVER (fra client e server)
    //==================================================================================================================

    /**
     * Classe che si occupa della lettura dei dati che arrivano dal client gestito
     */
    private class ReadFromClient implements Runnable{

        private int playerID;   //verrano create due istante di ReadFromClient, una per ogni player
        private ObjectInputStream dataIn;

        public ReadFromClient(int playerID, ObjectInputStream dataIn){
            this.playerID = playerID;
            this.dataIn = dataIn;

            System.out.println("RFC" + playerID + "\tRunnable created");
        }

        @Override
        public void run() {
            try {
                //impostazione del player che questa istanza del server prende in carico

                while (true){
                    Player clientControlledPlayer = (Player) dataIn.readObject();
                    Player modelCopyControlledPlayer = null;

                    if(this.playerID == 1) {
                        modelCopyControlledPlayer = gameField.getPlayer1();
                    }
                    else if(this.playerID == 2) {
                        modelCopyControlledPlayer = gameField.getPlayer2();
                    }
                    modelCopyControlledPlayer.setSpeed(0, clientControlledPlayer.getSpeed(0));
                    modelCopyControlledPlayer.setSpeed(1, clientControlledPlayer.getSpeed(1));
                }

            }catch (IOException | ClassNotFoundException e){
                System.out.println("IOException from RFC run()");
            }
        }
    }

    /**
     * Classe che si occupa di gestire la scrittura dei dai da mandare al client gestito.
     * Non è Runnable in quanto l'invio dei dati viene chiamato dal ChangeListener associato al gamefield, non c'è un invio
     * continuo dei dati ma solo "quando serve".
     */
    private class WriteToClient{

        private int playerID;   //verrano create due istante di ReadFromClient, una per ogni player
        private ObjectOutputStream dataOut;

        public WriteToClient(int playerID, ObjectOutputStream dataOut){
            this.playerID = playerID;
            this.dataOut = dataOut;

            System.out.println("WTC" + playerID + "\tRunnable created");
        }

        /*
        Metodo che manda la stringa iniziale ai client: deve essere mandata solo quando anche il secondo player si connette
        e non prima.
         */
        public void sendStartMsg(){
            try {

                /*
                Non per forza deve essere una stringa, può essere qualunque cosa. In ogni caso, il client si ferma finché
                non riceve questo messaggio.
                 */
                dataOut.writeUTF("We now have 2 players. Go!");

            }catch (IOException e){
                System.out.println("IOException from RFC run() || pl" + playerID);
            }
        }

        /**
         * metodo che si occupa di mandare, appena c'è un cambiamento nel gamefield, i dati necessari a entrambi i client
         */
        public void sendDataToClient(ExchangeDataClass modelData){
            try {
                dataOut.writeUnshared(modeldata);
                dataOut.flush();
            }catch (IOException  e){
                System.out.println("IOException  from WTC run()");
            }
        }
    }

    //MAIN______________________________________________________________________________________________________________
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
