package it.unibs.pajc.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Classe che rappresenta il model del campo di gioco, nel quale ci si occupa del controllo delle collisioni fra gli
 * oggetti di gioco.
 */
public class GameField {

    //I player è meglio averli privati in quanto reagiscono agli input dell'utente
    private Player player1;
    private Player player2;

    private ArrayList<GameObject> gameObjects;  //array contenente tutti gli oggetti coinvolti nel gioco
    private Rectangle2D.Float borders; //bordi dell'area di gioco

    //non saprei esattamente come costruirlo per adesso
    public GameField(){

    }

    /**
     * metodo che richiama gli update dei singoli oggetti di gioco
     */
    public void update(){
        player1.update();
    }

    public Player getPlayer1() {
        return this.player1;
    }

    /**
     * controlla le collisioni fra le varie coppie di elementi di gioco.
     * (copiato spudoratamente da redmax)
     */
    public void checkCollisions(){
        int nobjs = gameObjects.size();

        for(int i=0; i<nobjs; i++) {
            for(int j = 0; j<nobjs; j++) {
                if(gameObjects.get(i).checkCollision(gameObjects.get(j))) {
                    //si informano gli oggetti che si ha avuto un urto
                    gameObjects.get(i).collisionDetected();
                    gameObjects.get(j).collisionDetected();
                }
            }
        }
    }

    /**
     * controlla che tutti gli elementi di gioco non escano dall'area di gioco
     */
    public void applyCloseField(){

    }

}
