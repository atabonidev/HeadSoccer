package it.unibs.pajc.client;

import it.unibs.pajc.model.BaseModel;
import it.unibs.pajc.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PlayerKeyboardListener extends BaseModel implements KeyListener {

    /*
     * aplica i controlli attualmente attivi
     */

    private ArrayList<String> currentActiveKeys = new ArrayList<>();

    private Player localPlayer;

    public ClientKeyboard(Player p) {

        localPlayer = p;
    }

    private void applyControls() {
        Player player1 = field.getPlayer1();

        for(String strkeyCode: currentActiveKeys) {

            switch (Integer.parseInt(strkeyCode)) {
                //se si preme il tasto sinistro la navicella viene ruotata di un tot a sinistra
                case KeyEvent.VK_LEFT -> {
                    player1.move(false);
                    player1.startAnimation();
                }
                //identifica la pressione del tasto destro della tastiera
                case KeyEvent.VK_RIGHT -> {
                    player1.move(true);
                    player1.startAnimation();
                }
                case KeyEvent.VK_UP -> player1.jump();
                case KeyEvent.VK_SPACE -> player1.kick(true);
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
            field.getPlayer1().setSpeed(0, 0);
            field.getPlayer1().stopAnimation();
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE)    //se si ha appena calciato
            field.getPlayer1().stopKicking();                       //si riporta la gamba nella posizione originale

        String key = "" + e.getKeyCode();
        currentActiveKeys.remove(key);
    }

}
