package it.unibs.pajc.view;

import it.unibs.pajc.helpers.HelperClass;
import it.unibs.pajc.model.GameField;
import it.unibs.pajc.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
    RICORDATI CHE NON C'E NETTA DISTINZIONE TRA VIEW E CONTROLLER IN JAVA SWING QUINDI QUESTO
    E' UN FOTTUTISSIMO CONTROLLER
 */

public class GameView extends JPanel implements KeyListener {

    private BufferedImage fieldBgImage;
    private BufferedImage leftDoor;

    GameField field = new GameField();
    private ArrayList<String> currentActiveKeys = new ArrayList<>();

    public GameView() {
        initInputs();
        importGameField();
    }

    private void initInputs() {
        this.setFocusable(true);
        this.requestFocus();


    }

    /*
     * aplica i controlli attualmente attivi
     */
    private void applyControls() {
        Player player1 = field.getPlayer1();

        for(String strkeyCode: currentActiveKeys) {

            switch (Integer.parseInt(strkeyCode)) {
                //se si preme il tasto sinistro la navicella viene ruotata di un tot a sinistra
                case KeyEvent.VK_LEFT -> player1.move(false);
                //identifica la pressione del tasto destro della tastiera
                case KeyEvent.VK_RIGHT -> player1.move(true);
            }
        }

    }


    private void importGameField() {

        fieldBgImage = HelperClass.getCompleteGameField(HelperClass.getImagesForField());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.fillRect(0,0, 1000, 60);
        g2.translate(0, 60);
        //disegno campo di gioco con porte
        g2.drawImage(fieldBgImage, 0, 0, null);

        //sistema di rif spostato al centro all'altezza del campo da gioco.
        g2.translate(500, 386); //sistema di riferimento con origine in centro
        g2.scale(1,-1);

        //cose di prova
        g2.setColor(Color.red);
        g2.fillOval(0, 0, 5, 5);
        g2.fillRect(100, 0, 40, 60);

    }

    @Override
    public void keyTyped(KeyEvent e) {
        Player player1 = field.getPlayer1();

        if(e.getKeyCode() == KeyEvent.VK_UP) {
            player1.jump();
        }
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
        String key = "" + e.getKeyCode();
        currentActiveKeys.remove(key);
    }
}
