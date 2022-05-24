package it.unibs.pajc.client;

import it.unibs.pajc.model.BaseModel;
import it.unibs.pajc.model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PlayerKeyboardListener extends BaseModel implements KeyListener {

    /*
     * aplica i controlli attualmente attivi
     */

    private ArrayList<String> currentActiveKeys = new ArrayList<>();
    private Player controlledPlayer;
    private Timer controlsTimer;

    public PlayerKeyboardListener(Player controlledPlayer) {
        this.controlledPlayer = controlledPlayer;
        controlsTimer = new Timer(20, this::applyControls);
        controlsTimer.start();
    }


    private void applyControls(ActionEvent actionEvent) {
        for(String strkeyCode: currentActiveKeys) {
            switch (Integer.parseInt(strkeyCode)) {
                //se si preme il tasto sinistro la navicella viene ruotata di un tot a sinistra
                case KeyEvent.VK_LEFT -> {
                    controlledPlayer.move(false);
                    controlledPlayer.startAnimation();
                }
                //identifica la pressione del tasto destro della tastiera
                case KeyEvent.VK_RIGHT -> {
                    controlledPlayer.move(true);
                    controlledPlayer.startAnimation();
                }
                case KeyEvent.VK_UP -> controlledPlayer.jump();
                case KeyEvent.VK_SPACE -> controlledPlayer.kick(true);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String skey = "" + e.getKeyCode();
        if(!currentActiveKeys.contains(skey)) {
            currentActiveKeys.add(skey);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT) {    //si impone velocità in x nulla se si smette di premere i tasti
            controlledPlayer.setSpeed(0, 0);
            controlledPlayer.stopAnimation();
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE)    //se si ha appena calciato
            controlledPlayer.stopKicking();                       //si riporta la gamba nella posizione originale

        String key = "" + e.getKeyCode();
        currentActiveKeys.remove(key);
    }

}
