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
        g2.drawImage(leftFootballGoal.getPngImg(), (int)leftFootballGoal.getPosX(), (int)leftFootballGoal.getPosY(), null);
        g2.drawImage(rightFootballGoal.getPngImg(), (int)rightFootballGoal.getPosX(), (int)rightFootballGoal.getPosY(), null);

        //DISEGNO PALLA
        g2.drawImage(modelData.getBall().getPngImg(), (int)modelData.getBall().getPosX(), (int)modelData.getBall().getPosY(), null);

        //DISEGNO PERSONAGGI
        g2.drawImage(modelData.getPlayer1().getPngImg(), (int)modelData.getPlayer1().getPosX(), (int)modelData.getPlayer1().getPosY(), null);
        g2.drawImage(modelData.getPlayer2().getPngImg(), (int)modelData.getPlayer2().getPosX(), (int)modelData.getPlayer2().getPosY(), null);

    }

    /* ===================
    GETTERS AND SETTERS
    ====================*/
    public void setModelData(ExchangeDataClass modelData) {
        this.modelData = modelData;
        this.scoreView.setScore(modelData.getScore());
    }

}
