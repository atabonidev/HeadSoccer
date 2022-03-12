package it.unibs.pajc.model;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball extends DinamicObject{

    public static final int[] KICK_STRENGHT = {9, 2};

    public Ball(){
        this.position[0] = 0;
        this.position[1] = 0;
    }

    @Override
    public void update() {
        position[0] += speed[0];
        position[1] += speed[1];

        //qui mi sa che serve anche l'attrito con l'aria altrimenti non si ferma più
        accelerateY(-GRAVITY);
    }

    @Override
    public void collisionDetected() {
        //si controlla se la palla ha fatto collisione con la gamba del giocatore e si richiama kicked()

        /*
        if(gameObj.class == Player)
           if : palla intercetta piede
                if : isKicking
                    kicked();
            else
                accelerateY(speedY giocatore)
                accelerateX(speedX giocatore)
         */
    }

    @Override
    public void createSkeleton() {
        Shape ballShape = new Ellipse2D.Double(0, 0, 23, 23);
        super.objectShape.add(ballShape);
    }

    public void kicked(Player player){
        accelerateX(player.getSpeed(0) + KICK_STRENGHT[0]);
        accelerateY(player.getSpeed(1) + KICK_STRENGHT[1]);
    }

}
