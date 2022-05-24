package it.unibs.pajc.server;

import it.unibs.pajc.model.GameField;

import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static final int PORT = 1234;
    private static GameField gameField = new GameField();
    private static ArrayList<ServerInstanceForClient> connectedClients = new ArrayList<>();

    public static void main(String[] args) {

        gameField.addChangeListener(Server::modelUpdated);

        try(
                ServerSocket server = new ServerSocket(PORT);
        ){
            int playerId = 1;

            while(true) {
                Socket client = server.accept();

                if(connectedClients.size() < 2) {
                    ServerInstanceForClient p = new ServerInstanceForClient(client, gameField, playerId);
                    connectedClients.add(p);
                    playerId++;
                    Thread clientThread = new Thread(p);
                    clientThread.start();
                } else {
                    client.close();
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
