package it.unibs.pajc.server;

import it.unibs.pajc.model.GameField;
import it.unibs.pajc.model.Player;

import java.io.*;
import java.net.Socket;

public class ServerInstanceForClient implements Runnable {

    private Socket client;
    private GameField gameField;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int controlledPlayerId;

    public ServerInstanceForClient(Socket client, GameField gameField, int controlledPlayerId) {
        this.client = client;
        this.gameField = gameField;
        this.controlledPlayerId = controlledPlayerId;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());

            //Mando i dati attuai al client
            sendToClient(gameField.getGameObjects());

        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                this.client.close();
            } catch(Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }

    public void sendToClient(Serializable obj) {
        try {
            out.writeObject(obj);
            out.reset();
        } catch (IOException e) {
            System.err.println("Error, data not sent: " + e.toString());
        }
    }

    private void receiveFromClient() {
        try {
            while(true) {
                Player clientControlledPlayer = (Player) in.readObject();
                Player modelCopyControlledPlayer = null;

                if(this.controlledPlayerId == 1) {
                    modelCopyControlledPlayer = this.gameField.getPlayer1();
                }
                else if(this.controlledPlayerId == 2) {
                    modelCopyControlledPlayer = this.gameField.getPlayer2();
                }

                modelCopyControlledPlayer.setSpeed(0, clientControlledPlayer.getSpeed(0));
                modelCopyControlledPlayer.setSpeed(1, clientControlledPlayer.getSpeed(1));
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
