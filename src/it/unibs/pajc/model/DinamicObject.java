package it.unibs.pajc.model;

/**
 * classe base per ogni oggetto che si muove all'interno del gioco: palla, giocatore.
 */

public abstract class DinamicObject extends GameObject{

    public static final double GRAVITY = 0.4;

    protected double[] speed = { 0, 0 };

    //Per la gestione della gravità
    public void accelerateY(double accelerationY) {
        this.speed[1] = this.speed[1] + accelerationY;
    }

    public void accelerateX(double accelerationX){
        this.speed[0] = this.speed[0] + accelerationX;
    }

    public abstract void update();

    //getters e setters
    public double getSpeed(int speedDirection){
        return speed[speedDirection];
    }

}
