package it.unibs.pajc.model;

/**
 * classe base per ogni oggetto che si muove all'interno del gioco: palla, giocatore.
 */

public abstract class DinamicObject extends GameObject{

    public static final double GRAVITY = 0.4;

    protected double[] speed = { 3, 0 };

    //Per la gestione della gravità
    public void accelerateY(double accelerationY) {
        this.speed[1] = this.speed[1] + accelerationY;
    }

    public abstract void update();

}
