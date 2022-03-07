package it.unibs.pajc.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class GameObject {

    protected int position[];

    protected ArrayList<BufferedImage> images; //Immagini di rappresentazione dell'entità
    protected ArrayList<Shape> objectShape; //Shape dell'oggetto data dall'intersezione delle shape dei singoli oggetti

    public abstract boolean checkCollision(GameObject o);
    public abstract void collisionDetected(); //metodo che fa qualcosa nel caso in cui l'oggetto abbia rilevato una collisione

    public abstract void createSkeleton(); //Crea lo scheletro(shape) dell'oggetto da utilizzare per le collisioni

    public int getPosX() {
        return position[0];
    }

    public int getPosY() {
        return position[1];
    }

}
