package it.unibs.pajc.server;

import it.unibs.pajc.model.GameField;
import it.unibs.pajc.model.GameStatus;

import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static final int PORT = 1234;
    private static GameField gameField = new GameField();
    private static ArrayList<ServerInstanceForClient> connectedClients = new ArrayList<>();
    private static ServerInstanceForClient firstClient = null;
    private static ServerInstanceForClient secondClient = null;

    public static void main(String[] args) {

        gameField.addChangeListener(Server::modelUpdated);

        try(
            ServerSocket server = new ServerSocket(PORT);
        ){
            int playerId = 1;

            while(true) {
                Socket client = server.accept();

                if(firstClient == null && secondClient == null) {
                    firstClient = new ServerInstanceForClient(client, gameField, playerId);
                    playerId = 2;
                    Thread clientThread = new Thread(firstClient);
                    clientThread.start();
                    GameStatus.SetGameState(GameStatus.LOADING);
                }
                else if(firstClient != null && secondClient == null) {
                    secondClient = new ServerInstanceForClient(client, gameField, playerId);
                    Thread clientThread = new Thread(secondClient);
                    clientThread.start();
                    GameStatus.SetGameState(GameStatus.PLAYING);
                }
            }


        } catch(IOException ex) {
            System.err.println("Errore di comunicazione: " + ex);
        }


    }

    /**
     * Quando il model del server viene aggiornato allora tutti i client si aggiornano
     * @param e
     */
    private static void modelUpdated(ChangeEvent e) {
        for (ServerInstanceForClient serverInstance : connectedClients) {
            serverInstance.sendToClient(gameField.getGameObjects());
        }
    }





}
