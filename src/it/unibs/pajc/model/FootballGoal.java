package it.unibs.pajc.model;

import it.unibs.pajc.helpers.HelperClass;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FootballGoal extends GameObject {
    private boolean isLeft;

    public FootballGoal(boolean isLeft ,BufferedImage image) {
        this.images.add(HelperClass.flipVerticallyImage(image));
        this.isLeft = isLeft;
        setFootballGoalPosition(isLeft);
        createSkeleton();
    }

    private void setFootballGoalPosition(boolean isLeft) {
        this.setPosY(0);
        if(isLeft) {
            this.setPosX(-500);
        } else {
            this.setPosX(500-80);
        }
    }

    public BufferedImage getPngImg() {
        return this.images.get(0);
    }

    //Vuoto perchè la porta è un oggetto statico (non reagisce a collisioni)
    @Override
    public void collisionDetected(GameObject o) {

    }

    @Override
    public void createSkeleton() {
        Shape crossBar = new Rectangle(0, 157, 80, 11);
        Shape net = new Rectangle(0, 0, 80, 157);

        super.objectShape.add(net);
        super.objectShape.add(crossBar);
    }

    public boolean isLeft() {
        return isLeft;
    }
}
