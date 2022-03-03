package it.unibs.pajc.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class GameObject {

    public static final double FRICTION = 0.01; //attrito viene sottratto senno' la palla non si ferma
    public static final double GRAVITY = 0.01;

    protected int posX;
    protected int posY;
    protected int speedX = 0;
    protected int speedY = 0;

    protected ArrayList<BufferedImage> images; //Immagini di rappresentazione del personaggio
    protected ArrayList<Shape> objectShape; //Shape dell'oggetto data dall'intersezione delle shape dei singoli oggetti

    public void move(int xDelta, int yDelta) {
        posX += xDelta;
        posY += yDelta;
    }

    public void accelerate(double accelerationX, double accelerationY) {
        this.speedX += accelerationX;
        this.speedY += accelerationY;
    }

    public abstract void update();

    public abstract boolean checkCollision(GameObject o);

    public abstract void createSkeleton(); //Crea lo scheletro(shape) dell'oggetto da utilizzare per le collisioni

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}
