package it.unibs.pajc.model;

/**
 * classe base per ogni oggetto che si muove all'interno del gioco: palla, giocatore.
 */

public abstract class DinamicObject extends GameObject{

    public static final double FRICTION = 0.01; //attrito viene sottratto senno' la palla non si ferma
    public static final double GRAVITY = 0.01;

    protected int[] speed;

    //il delta di cui si sposta la posizione corrisponde alla velocità attuale
    public void move(int xDelta, int yDelta) {
        position[0] += xDelta;
        position[1] += yDelta;
    }

    public void accelerate(double accelerationX, double accelerationY) {
        this.speed[0] += accelerationX;
        this.speed[1] += accelerationY;
    }

    public abstract void update();

}
