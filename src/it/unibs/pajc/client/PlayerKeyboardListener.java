package it.unibs.pajc.client;

import it.unibs.pajc.model.BaseModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PlayerKeyboardListener extends BaseModel implements KeyListener {

    /*
     * aplica i controlli attualmente attivi
     */
    public static final int STOP_ANIMATION = 1;
    public static final int STOP_KICKING = 2;

    private Client.WriteToServer writeToServer;
    private ArrayList<String> commands = new ArrayList<>();
    private Timer controlsTimer;
    private StringBuffer formattedData = new StringBuffer("");

    public PlayerKeyboardListener(Client.WriteToServer writeToServer) {
        this.writeToServer = writeToServer;
        this.controlsTimer = new Timer(20, this::applyControls);
        this.controlsTimer.start();
    }


    private void applyControls(ActionEvent actionEvent) {
        for(String command: commands) {
            int actualCommand = 0;

            switch (Integer.parseInt(command)) {
                case KeyEvent.VK_LEFT -> actualCommand = KeyEvent.VK_LEFT;
                case KeyEvent.VK_RIGHT -> actualCommand = KeyEvent.VK_RIGHT;
                case KeyEvent.VK_UP -> actualCommand = KeyEvent.VK_UP;
                case KeyEvent.VK_SPACE -> actualCommand = KeyEvent.VK_SPACE;
                case STOP_ANIMATION -> actualCommand = STOP_ANIMATION;
                case STOP_KICKING -> actualCommand = STOP_KICKING;
            }

            if(actualCommand != 0) {
                formattedData.append(actualCommand + ", ");
            }
        }

        if(!formattedData.isEmpty()) {
            writeToServer.sendToServer(formattedData.toString().substring(0, formattedData.length() -2));
            formattedData.setLength(0);
        }

        if(commands.contains(""+STOP_ANIMATION))
            commands.remove(""+STOP_ANIMATION);

        if(commands.contains(""+STOP_KICKING))
            commands.remove(""+STOP_KICKING);

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String skey = "" + e.getKeyCode();

        if(!commands.contains(skey)) {
            commands.add(skey);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)
            commands.add(""+STOP_ANIMATION);

        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            commands.add(""+STOP_KICKING);


        String key = "" + e.getKeyCode();
        commands.remove(key);
    }

}
