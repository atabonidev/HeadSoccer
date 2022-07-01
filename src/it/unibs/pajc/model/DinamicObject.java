package it.unibs.pajc.model;


/**
 * Classe base per ogni oggetto che si muove all'interno del gioco: palla, giocatore.
 */

public abstract class DinamicObject extends GameObject{

    public static final double GRAVITY = 0.4;

    protected double[] speed = { 0, 0 };

    //applicazione delle accelerazioni subite dall'oggetto in x
    public void accelerateX(double accelerationX){
        if(speed[0] > 0 && accelerationX < 0 && (speed[0] + accelerationX) < 0) {
            speed[0] = 0;
        } else if(speed[0] < 0 && accelerationX > 0 && (speed[0] + accelerationX) > 0) {
            speed[0] = 0;
        } else {
            this.speed[0] = this.speed[0] + accelerationX;
        }
    }

    //applicazione delle accelerazioni subite dall'oggetto in y
    public void accelerateY(double accelerationY) {
        //se la palla tocca il soffitto con una velocità molto prossima a 0 potrebbe rimanere bloccata in alto
        if((speed[1] > 0 && accelerationY < 0 && (speed[1] + accelerationY) < 0)) {
            speed[1] = 0;
        } else {
            this.speed[1] = this.speed[1] + accelerationY;
        }
    }

    //applicazione della gravità all'oggetto
    public void gravityApplication() {
        if(speed[1] != 0 || position[1] != 0) {
            this.speed[1] = this.speed[1] -GRAVITY;
        }
    }

    //setta i parametri ai valori di default
    public abstract void setDefault();

    public abstract void update();

    /* ===================
    GETTERS AND SETTERS
    ====================*/
    public double getSpeedX() {
        return speed[0];
    }
    public double getSpeedY() {
        return speed[1];
    }
}
