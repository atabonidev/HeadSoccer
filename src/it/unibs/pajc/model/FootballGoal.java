package it.unibs.pajc.model;

public class FootballGoal extends GameObject{
    @Override
    public boolean checkCollision(GameObject o) {
        return false;
    }

    @Override
    public void collisionDetected() {

    }

    @Override
    public void createSkeleton() {

    }
}
