package it.unibs.pajc.model;

public class Player extends DinamicObject {
    public static final double JUMP_STRENGTH = 5;
    public static final double SPEED = 1;

    private int nPlayer; //Indica il numero del player (1 => sinistra || 2 => destra)

    public Player() {
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

    public void jump() {

    }

    @Override
    public void update() {
        move(speed[0], speed[1]);
        accelerate(0, -GRAVITY);
        accelerate(0, -FRICTION);
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

    }
}
