package it.unibs.pajc.model;

import java.awt.*;
import java.util.ArrayList;

public class Player extends DinamicObject {
    public static final double JUMP_STRENGTH = 7;  //potenza del calcio, con quale velocità parte

    private int nPlayer; //Indica il numero del player (1 => sinistra || 2 => destra)

    public Player(double posX, double posY) {
        position[0] = posX;
        position[1] = posY;
        createSkeleton();
    }

    public void kick() {
        /*
            if(player == 1)
                +angolo
            else
                -angolo
         */
    }

    //il delta di cui si sposta la posizione corrisponde alla velocità attuale
    public void move(boolean isMovingRight) {
        if(isMovingRight) {
            position[0] += speed[0];
        } else {
            position[0] -= speed[0];
        }
    }

    public void jump() {
        if(position[1] == 0) {
            accelerateY(JUMP_STRENGTH);
        }
    }

    /**
     * Si nota che il metodo move() viene richiamatosì solo in presenza di un input, altrimenti si muoverebbe a caso
     * anche senza premere tasti.
     */
    @Override
    public void update() {
        if(position[1] == 0 && speed[1] < 0) {
            speed[1] = 0;
        } else {
            position[1] += speed[1];
        }
        accelerateY(-GRAVITY);
    }

    @Override
    public boolean checkCollision(GameObject o) {
        return false;
    }

    @Override
    public void collisionDetected() {

    }

    //metodo che imposta le forme di riferimento che fanno da struttura per il personaggio
    @Override
    public void createSkeleton() {
        //lo disegna prendendo di riferimento il sistema di coordinate che abbiamo messo adesso -> disegna le forme
        //partendo dal loro angolo in basso a sinistra
        Shape legs = new Rectangle((int)position[0]+15, (int)position[1], 15, 30);
        Shape body = new Rectangle((int)position[0], legs.getBounds().y+30, 30, 50);

        objectShape = new ArrayList<>();
        objectShape.add(legs);
        objectShape.add(body);
    }
}
