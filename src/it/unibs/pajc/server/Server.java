package it.unibs.pajc.server;

import it.unibs.pajc.model.ExchangeDataClass;
import it.unibs.pajc.model.GameField;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 1234;
    ServerSocket serverSocket;

    private GameField gameField;
    private CommandApplier commandApplierPl1;
    private CommandApplier commandApplierPl2;

    private int numPlayers = 0;
    private static final int maxPlayers = 2;   //massimo di giocatori permessi

    //istanze di lettura e scrittura relative ai singoli client gestiti
    private ReadFromClient pl1Reader;
    private ReadFromClient pl2Reader;

    private WriteToClient pl1Writer;
    private WriteToClient pl2Writer;

    private String pl1Name;
    private String pl2Name;

    public Server(){
        System.out.println("=======GAME SERVER ========");

        try {
            serverSocket =  new ServerSocket(PORT);

            gameField = new GameField();

            commandApplierPl1 = new CommandApplier(gameField.getPlayer1());
            commandApplierPl2 = new CommandApplier(gameField.getPlayer2());

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
                out.writeUnshared("" + numPlayers); //di manda al player il suo ID  ---> usato per dare il titolo alla schermata
                out.flush();
                System.out.println("Player #" + numPlayers + "has connected.");

                //creazione delle classi di lettura e scrittura
                WriteToClient wtc = new WriteToClient(numPlayers, out);
                ReadFromClient rfc = new ReadFromClient(numPlayers, in);

                //assegnazione opportuna delle due istanze
                if (numPlayers == 1) {
                    pl1Reader = rfc;
                    pl1Writer = wtc;
                    pl1Name = pl1Reader.receivePlayerName();
                } else {
                    pl2Reader = rfc;
                    pl2Writer = wtc;
                    pl2Name = pl2Reader.receivePlayerName();

                    //solo in questo momento, ossia quando il secondo player si è connesso si fa partire il gioco
                    gameField.getPlayer1().setPlayerName(pl1Name);
                    gameField.getPlayer2().setPlayerName(pl2Name);
                    startGame();

                    Thread readThreadPl1 = new Thread(pl1Reader);
                    Thread readThreadPl2 = new Thread(pl2Reader);
                    readThreadPl1.start();
                    readThreadPl2.start();

                    Thread writeThreadPl1 = new Thread(pl1Writer);
                    Thread writeThreadPl2 = new Thread(pl2Writer);
                    writeThreadPl1.start();
                    writeThreadPl2.start();
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

        public String receivePlayerName(){
            try {
                String playerName = (String) dataIn.readUnshared();
                return playerName;

            }catch (IOException | ClassNotFoundException e){
                System.out.println("IOException from RFC run() || pl" + playerID);
            }

            return null;
        }

        @Override
        public void run() {
            try {
                //impostazione del player che questa istanza del server prende in carico

                while (true){
                    String message = (String) dataIn.readUnshared();

                    if(message.equals("CLOSE"))
                        break;

                    String[] commands = message.split(", ");

                    if(this.playerID == 1)
                        commandApplierPl1.applyCommands(commands);
                    else if(this.playerID == 2)
                        commandApplierPl2.applyCommands(commands);

                }

                this.close();


            }catch (IOException | ClassNotFoundException e){
                System.out.println("IOException from RFC run()");
            }
        }

        public void close(){
            try {
                System.out.println("\n\n----------------------------");

                this.dataIn.close();

                if(playerID == 1)
                    pl1Writer.close();
                else
                    pl2Writer.close();

                System.out.println("RFC" + playerID + "\tRunnable closed");
                System.out.println("WTC" + playerID + "\tRunnable closed");
                System.out.println("----------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Classe che si occupa di gestire la scrittura dei dai da mandare al client gestito.
     * Non è Runnable in quanto l'invio dei dati viene chiamato dal ChangeListener associato al gamefield, non c'è un invio
     * continuo dei dati ma solo "quando serve".
     */
    private class WriteToClient implements Runnable{

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
                dataOut.writeUnshared("We now have 2 players. Go!");
                dataOut.flush();

            }catch (IOException e){
                System.out.println("IOException from RFC run() || pl" + playerID);
            }
        }

        @Override
        public void run() {

            try {
                while (true) {
                    ExchangeDataClass modelData = gameField.exportData();
                    dataOut.writeUnshared(modelData);
                    dataOut.reset();

                    if(modelData.getSoundClipIdentifier().isClipActive()) {
                        gameField.setClipActive(false);
                    }

                    //si stoppa momentaneamente il Thread
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException from WTC run()");
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException from WRC run()");
            }
        }

        public void close(){
            try {
                this.dataOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //MAIN______________________________________________________________________________________________________________
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
