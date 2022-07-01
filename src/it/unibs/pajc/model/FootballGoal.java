package it.unibs.pajc.model;

import java.awt.*;

/**
 * Classe che rappresenta la porta
 */

public class FootballGoal extends GameObject {
    private boolean isLeft;

    public FootballGoal(boolean isLeft) {
        this.isLeft = isLeft;
        setFootballGoalPosition(isLeft);
        createSkeleton();
    }

    private void setFootballGoalPosition(boolean isLeft) {
        this.setPosY(0);
        if(isLeft) {
            this.setPosX(-500);
        } else {
            this.setPosX(500 - this.getObjWidth());
        }
    }

    //Vuoto perché la porta è un oggetto statico (non reagisce a collisioni)
    @Override
    public void collisionDetected(GameObject o) {

    }

    @Override
    public void createSkeleton() {
        Shape crossBar = new Rectangle(0, 157, 80, 11);
        Shape net = new Rectangle(0, 0, 80, 157);

        super.objectShape.add(crossBar);
        super.objectShape.add(net);
    }

    /* ===================
    GETTERS AND SETTERS
    ====================*/
    public double getCrossBarY() {
        return this.getSingleShape(0).getBounds().y;
    }
    public double getCrossBarHeight() {
        return this.getSingleShape(0).getBounds().height;
    }
    public boolean isLeft() {
        return isLeft;
    }
}
