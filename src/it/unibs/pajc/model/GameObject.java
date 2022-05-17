package it.unibs.pajc.model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class GameObject implements Serializable {

    protected double[] position = { 0, 0 };
    protected double[] cdm = {0, 0};  //centro di massa dell'oggetto

    protected ArrayList<BufferedImage> images = new ArrayList<>(); //Immagini di rappresentazione dell'entità
    protected ArrayList<Shape> objectShape = new ArrayList<>(); //Shape dell'oggetto data dall'intersezione delle shape dei singoli oggetti

    /**
     * Metodo che restituisce l'area totale dell'oggetto unendo le diverse shape che lo compongono
     * @return
     */
    public Area getTotalShape() {
        Area totalArea = new Area();
        for (Shape s : objectShape) {
            totalArea.add(new Area(s));
        }

        return totalArea;
    }

    public Shape getShape() {
        AffineTransform t = new AffineTransform(); //inizialmente coincide con la matrice identità
        t.translate(position[0], position[1]); //applicazione della trasformata
        return t.createTransformedShape(getTotalShape());
    }

    /**
     * Metodo che restituisce true se l'oggetto che sta richiamando il metodo e l'oggetto parametro hanno l'intersezione non vuota
     * @param o
     * @return
     */
    public boolean checkCollision(GameObject o) {
        Area myArea = new Area(this.getShape());
        myArea.intersect(new Area(o.getShape()));
        return !myArea.isEmpty();
    }

    //metodo che dalla shape toatale calcola le coordinate del cdm
    protected void calculateCdm(){
        Area totalArea = this.getTotalShape();

        cdm[0] = totalArea.getBounds().width / 2.0;
        cdm[1] = totalArea.getBounds().height / 2.0;

    }

    public abstract void collisionDetected(GameObject o); //deve ricevere come parametro l'oggetto con cui ha avuto la collisione

    public abstract void createSkeleton(); //Crea lo scheletro(shape) dell'oggetto da utilizzare per le collisioni

    //getters e setters

    public double getPosX() {
        return position[0];
    }

    public double getPosY() {
        return position[1];
    }

    public void setPosY(double newPosY){
        this.position[1] = newPosY;
    }

    public void setPosX(double newPosX){
        this.position[0] = newPosX;
    }

    public Shape getSingleShape(int shapeIndex){
        return objectShape.get(shapeIndex);
    }

    public void setSingleShape(int shapeIndex, Shape newShape){
        this.objectShape.set(shapeIndex, newShape);
    }

    public double getActualCdmX(){
        return position[0] + cdm[0];
    }

    public double getActualCdmY(){
        return position[1] + cdm[1];
    }

    public double getObjWidth(){
        return this.getTotalShape().getBounds().width;
    }

    public double getObjHeight(){
        return this.getTotalShape().getBounds().height;
    }

    public ArrayList<BufferedImage> getImages(){
        return this.images;
    }

}
