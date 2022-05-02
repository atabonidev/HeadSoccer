package it.unibs.pajc.model;

import it.unibs.pajc.helpers.HelperClass;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Classe che rappresenta il model del campo di gioco, nel quale ci si occupa del controllo delle collisioni fra gli
 * oggetti di gioco.
 */
public class GameField {

    //I player è meglio averli privati in quanto reagiscono agli input dell'utente
    private FootballGoal leftFootballGoal;
    private FootballGoal rightFootballGoal;
    private Player player1;
    private Player player2;
    private Ball ball;
    private Score score;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();  //array contenente tutti gli oggetti coinvolti nel gioco
    private Rectangle2D.Float borders; //bordi dell'area di gioco

    //bisogna creare il player con le posizioni iniziali
    public GameField() {
        InputStream streamLeftFootballGoal = this.getClass().getClassLoader().getResourceAsStream("leftDoorRect.jpeg");
        InputStream streamRightFootballGoal = this.getClass().getClassLoader().getResourceAsStream("rightDoorRect.jpeg");
        InputStream streamPlayer1 = this.getClass().getClassLoader().getResourceAsStream("LeftMan.png");
        InputStream streamPlayer2 = this.getClass().getClassLoader().getResourceAsStream("RightMan.png");
        InputStream streamBall = this.getClass().getClassLoader().getResourceAsStream("Ball01.png");

        BufferedImage pngLeftFootballGoal = null;
        BufferedImage pngRightFootballGoal = null;
        BufferedImage pngBall = null;
        BufferedImage pngPlayer1 = null;
        BufferedImage pngPlayer2 = null;

        try {
            pngLeftFootballGoal = ImageIO.read(streamLeftFootballGoal);
            pngRightFootballGoal = ImageIO.read(streamRightFootballGoal);
            pngPlayer1 = ImageIO.read(streamPlayer1);
            pngPlayer2 = ImageIO.read(streamPlayer2);
            pngBall = ImageIO.read(streamBall);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.leftFootballGoal = new FootballGoal(true, pngLeftFootballGoal);
        this.rightFootballGoal = new FootballGoal(false, pngRightFootballGoal);
        this.player1 = new Player(1, pngPlayer1);
        this.player2 = new Player(2, pngPlayer2);
        this.ball = new Ball(pngBall, this);

        //Classe che rappresenta il punteggio
        this.score = new Score();

        gameObjects.add(leftFootballGoal);
        gameObjects.add(rightFootballGoal);
        gameObjects.add(player1);
        gameObjects.add(player2);
        gameObjects.add(ball);
    }

    /**
     * metodo che richiama gli update dei singoli oggetti di gioco
     */
    public void update(){

        for (GameObject o : gameObjects) {
            if(o instanceof DinamicObject dynamicObject) {
                dynamicObject.update();
                applyCloseField(dynamicObject);
            }
        }

        //System.out.println(score.getScorePl1() + " | " + score.getScorePl2());

        checkCollisions();
    }

    public FootballGoal getLeftFootballGoal() { return leftFootballGoal; }

    public FootballGoal getRightFootballGoal() { return rightFootballGoal; }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public Ball getBall() {
        return this.ball;
    }

    public Score getScore() {
        return score;
    }

    /**
     * reset degli oggetti dinamici di gioco
     */
    public void reset() {
        this.player1.setDefault();
        this.player2.setDefault();
        this.ball.setDefault();
    }

    /**
     * Metodo che si occupa della gestione di un goal
     */
    public void incrementScore(FootballGoal footballGoal) {
        if(footballGoal.isLeft()) {
            score.incrementScore(player2.getPlayerID());
        } else {
            score.incrementScore(player1.getPlayerID());
        }
    }


    /**
     * controlla le collisioni fra le varie coppie di elementi di gioco.
     * (copiato spudoratamente da redmax)
     */
    public void checkCollisions(){
        int nobjs = gameObjects.size();

        for(int i =0 ; i < nobjs-1; i++) {
            for(int j = i+1; j<nobjs; j++) {
                if(gameObjects.get(i).checkCollision(gameObjects.get(j))) {
                    //si informano gli oggetti che si ha avuto un urto
                    gameObjects.get(i).collisionDetected(gameObjects.get(j));
                    gameObjects.get(j).collisionDetected(gameObjects.get(i));
                }
            }

        }
    }

    /**
     * controlla che tutti gli elementi di gioco non escano dall'area di gioco
     */
    private void applyCloseField(DinamicObject o){
        borders = new Rectangle2D.Float(-500, 0, 1000, 386);   //bordi del campo di gioco (secondo il nostro sistema di rif.)
        if(o.getPosY() < borders.getMinY()){
            o.setPosY((float)borders.getMinY());
        }
        if(o.getPosY() > (borders.getMaxY() - o.getTotalShape().getBounds().height)){
            o.setPosY((float)borders.getMaxY() - o.getTotalShape().getBounds().height);
        }

        if(o.getPosX() < borders.getMinX()){
            o.setPosX((float)borders.getMinX());
        }
        if(o.getPosX() > (borders.getMaxX() - o.getTotalShape().getBounds().width)){
            o.setPosX((float)(borders.getMaxX() - o.getTotalShape().getBounds().width));
        }
        //fare applyclosefield per singole entità e chiamarlo qui dentro che diventerebbe quello generale
        //System.out.println(ball.speed[1]);
    }

}
