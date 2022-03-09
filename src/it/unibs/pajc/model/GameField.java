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

    //bisogna creare il player con le posizioni iniziali
    public GameField(){
        this.player1 = new Player(-100, 0);
    }

    /**
     * metodo che richiama gli update dei singoli oggetti di gioco
     */
    public void update(){
        player1.update();
        applyCloseField();
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
        borders = new Rectangle2D.Float(-500, 0, 1000, 386);   //bordi del campo di gioco (secondo il nostro sistema di rif.)
        if(player1.getPosY() < borders.y){
            player1.setPosY(borders.y);
        }
        if(player1.getPosX() < borders.x){
            player1.setPosX(borders.x);
        }
        if(player1.getPosX() > -borders.x){
            player1.setPosX(-borders.x);
        }
    }

}
