package it.unibs.pajc.model;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class GameObject {

    protected double position[];

    protected ArrayList<BufferedImage> images; //Immagini di rappresentazione dell'entità
    protected ArrayList<Shape> objectShape; //Shape dell'oggetto data dall'intersezione delle shape dei singoli oggetti

    /**
     * Metodo che restituisce true se l'oggetto che sta richiamando il metodo e l'oggetto parametro hanno l'intersezione non vuota
     * @param o
     * @return
     */
    public boolean checkCollision(GameObject o) {
        Area myArea = this.getTotalShape();
        myArea.intersect(o.getTotalShape());
        return !myArea.isEmpty();
    }

    /**
     * Metodo che restituisce l'area totale dell'oggetto unendo le diverse shape che lo compongono
     * @return
     */
    public Area getTotalShape() {
        Area totalArea = new Area();

        for (Shape s : objectShape) {
            totalArea.add(new Area(s.getBounds()));
        }

        return totalArea;
    }

    public abstract void collisionDetected(); //metodo che fa qualcosa nel caso in cui l'oggetto abbia rilevato una collisione

    public abstract void createSkeleton(); //Crea lo scheletro(shape) dell'oggetto da utilizzare per le collisioni

    public double getPosX() {
        return position[0];
    }

    public double getPosY() {
        return position[1];
    }

}
