package it.unibs.pajc.model;

public class Player extends GameObject {
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
        move(speedX, speedY);
        accelerate(0, -GRAVITY);
        accelerate(0, -FRICTION);
    }

    @Override
    public boolean checkCollision(GameObject o) {
        return false;
    }

    @Override
    public void createSkeleton() {

    }
}
