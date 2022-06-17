package it.unibs.pajc.model;

import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe che rappresenta il model del campo di gioco, nel quale ci si occupa del controllo delle collisioni fra gli
 * oggetti di gioco.
 */
public class GameField extends BaseModel implements Serializable {
    //I player è meglio averli privati in quanto reagiscono agli input dell'utente
    public static final FootballGoal leftFootballGoal = new FootballGoal(true);
    public static final FootballGoal rightFootballGoal = new FootballGoal(false);
    private Player player1;
    private Player player2;
    private Ball ball;
    private Score score;
    private Timer updateTimer;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();  //array contenente tutti gli oggetti coinvolti nel gioco
    private Rectangle2D.Float borders; //bordi dell'area di gioco

    //bisogna creare il player con le posizioni iniziali
    public GameField() {
        this.player1 = new Player(1);
        this.player2 = new Player(2);
        this.ball = new Ball(this);

        //Classe che rappresenta il punteggio
        this.score = new Score();

        gameObjects.add(leftFootballGoal);
        gameObjects.add(rightFootballGoal);
        gameObjects.add(player1);
        gameObjects.add(player2);
        gameObjects.add(ball);

        updateTimer = new Timer(20, e -> this.update());
    }

    public void startGame() {
        updateTimer.start();
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

        checkCollisions();
    }

    public ExchangeDataClass exportData(){
        return new ExchangeDataClass(this);
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
    }

    /* ===================
    GETTERS AND SETTERS
    ====================*/
    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

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

}
