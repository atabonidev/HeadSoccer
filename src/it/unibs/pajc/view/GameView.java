package it.unibs.pajc.view;

import it.unibs.pajc.helpers.*;
import it.unibs.pajc.model.GameField;
import it.unibs.pajc.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.ReplicateScaleFilter;
import java.util.ArrayList;

/*
    RICORDATI CHE NON C'E NETTA DISTINZIONE TRA VIEW E CONTROLLER IN JAVA SWING QUINDI QUESTO
    E' UN FOTTUTISSIMO CONTROLLER
 */

public class GameView extends JPanel implements KeyListener {

    private BufferedImage fieldBgImage;
    private BufferedImage leftDoor;

    private GameField field = new GameField();
    private ArrayList<String> currentActiveKeys = new ArrayList<>();

    private ScoreView scoreView = new ScoreView(0, 0, 1000, 60, field.getScore());

    public GameView() {
        importGameField();
        update();   //anche lui lo mette nel costruttore
    }

    private void initInputs() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
    }

    //copiato un attimo da RED
    public void update(){
        /*  GAME LOOP
         * mi serve per poter invocare lo step next ogni arco di tempo deciso
         */
        Timer t = new Timer(20, (e) -> {
            applyControls();

            //ora si chiama lo step next direttamente su space
            field.update();     //-> update
            repaint();  //-> render
        });

        t.start();
        initInputs();

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
                case KeyEvent.VK_UP -> player1.jump();
                case KeyEvent.VK_SPACE -> player1.kick(true);
            }
        }

    }


    private void importGameField() {
        fieldBgImage = HelperClass.getCompleteGameField();
    }

    /**
     * dubbione -> il campo rimane fisso, non dovrebbe essere messo qui
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        scoreView.draw(g2);
        g2.translate(0, 60);
        //disegno campo di gioco con porte
        g2.drawImage(fieldBgImage, 0, 0, null);

        //sistema di rif spostato al centro all'altezza del campo da gioco.
        g2.translate(500, 386); //sistema di riferimento con origine in centro
        g2.scale(1,-1);

        //DISEGNO PORTE
        g2.drawImage(field.getLeftFootballGoal().getPngImg(), (int)field.getLeftFootballGoal().getPosX(), (int)field.getLeftFootballGoal().getPosY(), null);
        g2.drawImage(field.getRightFootballGoal().getPngImg(), (int)field.getRightFootballGoal().getPosX(), (int)field.getRightFootballGoal().getPosY(), null);

        //DISEGNO PALLA
        g2.drawImage(field.getBall().getPngImg(), (int)field.getBall().getPosX(), (int)field.getBall().getPosY(), null);

        //DISEGNO PERSONAGGI
        g2.drawImage(field.getPlayer1().getPngImg(), (int)field.getPlayer1().getPosX(), (int)field.getPlayer1().getPosY(), null);
        g2.drawImage(field.getPlayer2().getPngImg(), (int)field.getPlayer2().getPosX(), (int)field.getPlayer2().getPosY(), null);

        //prove stampa gamba pl1
        g2.setColor(Color.darkGray);
        g2.fill(field.getPlayer1().getShape());

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
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)    //si impone velocità in x nulla se si smette di premere i tasti
            field.getPlayer1().setSpeed(0,0);

        if(e.getKeyCode() == KeyEvent.VK_SPACE)    //se si ha appena calciato
            field.getPlayer1().stopKicking();                       //si riporta la gamba nella posizione originale

        String key = "" + e.getKeyCode();
        currentActiveKeys.remove(key);
    }
}
