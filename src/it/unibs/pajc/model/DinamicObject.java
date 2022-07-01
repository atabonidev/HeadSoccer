package it.unibs.pajc.model;


/**
 * classe base per ogni oggetto che si muove all'interno del gioco: palla, giocatore.
 */

public abstract class DinamicObject extends GameObject{

    public static final double GRAVITY = 0.4;

    protected double[] speed = { 0, 0 };

    //Per la gestione della gravità
    public void accelerateY(double accelerationY) {
        if((speed[1] > 0 && accelerationY < 0 && (speed[1] + accelerationY) < 0)) {
            speed[1] = 0;
        } else {
            this.speed[1] = this.speed[1] + accelerationY;
        }
    }

    public void gravityApplication() {
        if(speed[1] != 0 || position[1] != 0) {
            this.speed[1] = this.speed[1] -GRAVITY;
        }
    }

    public void accelerateX(double accelerationX){
        if(speed[0] > 0 && accelerationX < 0 && (speed[0] + accelerationX) < 0) {
            speed[0] = 0;
        } else if(speed[0] < 0 && accelerationX > 0 && (speed[0] + accelerationX) > 0) {
            speed[0] = 0;
        } else {
            this.speed[0] = this.speed[0] + accelerationX;
        }
    }

    public abstract void update();

    //getters e setters
    public double getSpeedX() {
        return speed[0];
    }

    public double getSpeedY() {
        return speed[1];
    }

    public double getSpeed(int speedComponent){
        return speed[speedComponent];
    }

}
