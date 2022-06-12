package it.unibs.pajc.server;

import it.unibs.pajc.helpers.HelperClass;
import it.unibs.pajc.model.ExchangeDataClass;
import it.unibs.pajc.model.GameField;
import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 1234;
    private static GameField gameField;
    //private static ArrayList<ServerInstanceForClient> connectedClients = new ArrayList<>();
    private static ServerInstanceForClient firstClient = null;
    private static ServerInstanceForClient secondClient = null;
    private static ExchangeDataClass modeldata = null;

    public static void main(String[] args) {

        try {
            HelperClass.importImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameField = new GameField();

        modeldata = new ExchangeDataClass(gameField);

        try(
            ServerSocket server = new ServerSocket(PORT);
        ){
            int playerId = 1;

            while(true) {
                System.out.println("Prooooovaaaaaaaa...........");
                Socket client = server.accept(); //il server aspetta in ascolto di un client -> codice si ferma qui

                if(firstClient == null && secondClient == null) {
                    firstClient = new ServerInstanceForClient(client, gameField, modeldata, playerId);
                    playerId++;
                    Thread clientThread = new Thread(firstClient);
                    clientThread.start();
                }
                else if(firstClient != null && secondClient == null) {
                    secondClient = new ServerInstanceForClient(client, gameField, modeldata, playerId);
                    Thread clientThread = new Thread(secondClient);
                    clientThread.start();
                    startGame();
                } else {
                    client.close();
                }
            }


        } catch(IOException ex) {
            System.err.println("Errore di comunicazione: " + ex);
        }


    }

    /**
     * Quando entrambi i client sono connessi invio i dati a entrambi e do inizio alla partita
     */
    private static void startGame() {
        gameField.startGame();
        firstClient.sendToClient(modeldata);
        secondClient.sendToClient(modeldata);
        gameField.addChangeListener(Server::modelUpdated);
    }

    /**
     * Quando il model del server viene aggiornato allora tutti i client si aggiornano
     * @param e
     */
    private static void modelUpdated(ChangeEvent e) {
        /*
        for (ServerInstanceForClient serverInstance : connectedClients) {
            serverInstance.sendToClient(modeldata);
        }
        */
        firstClient.sendToClient(modeldata);
        secondClient.sendToClient(modeldata);
    }





}
