package it.unibs.pajc.view;

import it.unibs.pajc.helpers.*;
import it.unibs.pajc.model.*;

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

public class GameView extends JPanel {

    private BufferedImage fieldBgImage;

    private FootballGoal leftFootballGoal;
    private FootballGoal rightFootballGoal;

    private ExchangeDataClass modelData;

    private ScoreView scoreView;

    private boolean firstIteration;    //permette di capire se il model è stato aggiornato con dati non nulli

    public GameView(FootballGoal leftFootballGoal, FootballGoal rightFootballGoal) {
        importGameFieldImg();

        this.scoreView = new ScoreView(0, 0, 1000, 60);

        footballGoalsImport(leftFootballGoal, rightFootballGoal);

        focusOn();
    }

    private void importGameFieldImg() {
        fieldBgImage = HelperClass.getCompleteGameField();
    }

    private void footballGoalsImport(FootballGoal leftFootballGoal, FootballGoal rightFootballGoal) {
        this.leftFootballGoal = leftFootballGoal;
        this.rightFootballGoal = rightFootballGoal;
    }

    private void focusOn() {
        this.setFocusable(true);
        this.requestFocusInWindow();
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
        g2.drawImage(HelperClass.getImageFromName("leftDoorRect.jpeg"), (int)leftFootballGoal.getPosX(), (int)leftFootballGoal.getPosY(), null);
        g2.drawImage(HelperClass.getImageFromName("rightDoorRect.jpeg"), (int)rightFootballGoal.getPosX(), (int)rightFootballGoal.getPosY(), null);

        if(!firstIteration) {
            //DISEGNO PALLA
            g2.drawImage(HelperClass.getImageFromName("Ball01.png"), (int) modelData.getBall().getPosX(), (int) modelData.getBall().getPosY(), null);

            //DISEGNO PERSONAGGI
            BufferedImage leftPlayerImage = null;
            switch (modelData.getPlayer1().getCurrentImageIndex()) {
                case 0 -> leftPlayerImage = HelperClass.getImageFromName("LeftMan.png");
                case 1 -> leftPlayerImage = HelperClass.getImageFromName("WalkingLeftMan.png");
                case 2 -> leftPlayerImage = HelperClass.getImageFromName("KickLeftMan.png");
            }

            g2.drawImage(leftPlayerImage, (int) modelData.getPlayer1().getPosX(), (int) modelData.getPlayer1().getPosY(), null);

            g2.setColor(Color.black);

            g2.draw(modelData.getPlayer1().getShape());

            BufferedImage rightPlayerImage = null;
            switch (modelData.getPlayer2().getCurrentImageIndex()) {
                case 0 -> rightPlayerImage = HelperClass.getImageFromName("RightMan.png");
                case 1 -> rightPlayerImage = HelperClass.getImageFromName("WalkingRightMan.png");
                case 2 -> rightPlayerImage = HelperClass.getImageFromName("KickRightMan.png");
            }

            if(!modelData.getPlayer2().getKickStatus()) {
                g2.drawImage(rightPlayerImage, (int) modelData.getPlayer2().getPosX(), (int) modelData.getPlayer2().getPosY(), null);
            }
            else {
                g2.drawImage(rightPlayerImage, (int) modelData.getPlayer2().getPosX() - 12, (int) modelData.getPlayer2().getPosY(), null);
            }

            g2.draw(modelData.getPlayer2().getShape());
        }

    }

    /* ===================
    GETTERS AND SETTERS
    ====================*/
    public void setModelData(ExchangeDataClass modelData, boolean isFirstIteration) {
        this.firstIteration = isFirstIteration;
        this.modelData = modelData;
        this.scoreView.setScore(modelData.getScore());
    }

}
