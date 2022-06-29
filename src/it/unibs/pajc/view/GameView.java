package it.unibs.pajc.view;

import it.unibs.pajc.helpers.*;
import it.unibs.pajc.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
    private Sound sound = new Sound();

    private Timer goalAnimation;
    private int goalStringFontSize;
    private boolean isGoal;

    private int playerId;

    public GameView(FootballGoal leftFootballGoal, FootballGoal rightFootballGoal, int playerId) {
        importGameFieldImg();

        this.scoreView = new ScoreView(0, 0, 1000, 60);

        footballGoalsImport(leftFootballGoal, rightFootballGoal);

        this.playerId = playerId;
    }

    private void importGameFieldImg() {
        fieldBgImage = HelperClass.getCompleteGameField();
    }

    private void footballGoalsImport(FootballGoal leftFootballGoal, FootballGoal rightFootballGoal) {
        this.leftFootballGoal = leftFootballGoal;
        this.rightFootballGoal = rightFootballGoal;
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
            g2.drawImage(HelperClass.getImageFromName("Ball.png"), (int) modelData.getBall().getPosX(), (int) modelData.getBall().getPosY(), null);

            //DISEGNO PERSONAGGI
            BufferedImage leftPlayerImage = null;
            switch (modelData.getPlayer1().getCurrentImageIndex()) {
                case 0 -> leftPlayerImage = HelperClass.getImageFromName("LeftMan.png");
                case 1 -> leftPlayerImage = HelperClass.getImageFromName("WalkingLeftMan.png");
                case 2 -> leftPlayerImage = HelperClass.getImageFromName("KickLeftMan.png");
            }

            g2.drawImage(leftPlayerImage, (int) modelData.getPlayer1().getPosX(), (int) modelData.getPlayer1().getPosY(), null);

            g2.setColor(Color.black);

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
        }

        if(isGoal){
            g2.scale(1, -1);
            g2.translate(0, -386/2); //sistema di riferimento con origine in centro

            g2.setFont(new Font("Helvetica", Font.BOLD, goalStringFontSize));

            //disegno stringa
            int w = g2.getFontMetrics().stringWidth("GOAL");
            int h = g2.getFontMetrics().getHeight();

            int textX = - w / 2;
            int textY = h / 2;    //asse y verso il basso

            //g2.setColor(Color.BLACK);
            //g2.fillRect(textX-2, -textY-2, w+2, h+2);

            g2.setColor(Color.RED);
            g2.drawString("GOAL", textX, textY);
        }

    }

    /* ===================================================================================================
    GESTIONE ANIMAZIONI ED SUONI
    ====================================================================================================*/

    public void setModelData(ExchangeDataClass newModelData, boolean isFirstIteration) {
        this.firstIteration = isFirstIteration;
        checkAnimationsAndSounds(newModelData);
        this.modelData = newModelData;
        this.scoreView.setScore(modelData.getScore());
    }

    private void checkAnimationsAndSounds(ExchangeDataClass newModelData) {
        if(modelData != null && modelData.getScore() != null) {
            SoundClipIdentifier soundClipIdentifier;

            if(playerId == 1)
                soundClipIdentifier = newModelData.getSoundClipIdentifierPl1();
            else {
                soundClipIdentifier = newModelData.getSoundClipIdentifierPl2();
            }



            if(soundClipIdentifier.isClipActive()) {
                if(soundClipIdentifier.getClipNumber() == Sound.KICK_BALL) {
                    this.playSoundEffect(Sound.KICK_BALL);
                    System.out.println(playerId);
                }
                else if(soundClipIdentifier.getClipNumber() == Sound.KICK_OFF) {
                    this.startGoalAnimation(newModelData);
                    this.playSoundEffect(Sound.KICK_OFF);
                    System.out.println(playerId);
                }
            }
        }
    }

    /**
     * Metodo per controllare se c'e stato un goal, e in caso avvia il timer che si occupa dell'animazione
     * @param newModelData
     */
    private void startGoalAnimation(ExchangeDataClass newModelData) {
        isGoal = true;

        goalAnimation = new Timer(15, (e) -> {
            if (goalStringFontSize == 120){
                goalAnimation.stop();
                isGoal = false;
                goalStringFontSize = 0;
            }
            else {
                goalStringFontSize++;
            }
        });

        goalAnimation.start();
    }

    public void playSoundEffect(int i) {
        sound.setFile(i);
        sound.play();
    }


}
