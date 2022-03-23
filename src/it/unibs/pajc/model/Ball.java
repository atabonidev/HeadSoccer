package it.unibs.pajc.model;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Ball extends DinamicObject {

    private static final int[] KICK_STRENGHT = {9, 2};
    private static final double BOUNCING_FRICTION = 2.6;
    private static final double AIR_FRICTION = 0.02;

    public Ball(double posX, double posY, double speedX, double speedY){
        this.position[0] = posX;
        this.position[1] = posY; //Parte dall'alto
        this.speed[0] = speedX;
        this.speed[1] = speedY;
        createSkeleton();
    }

    //Controlla se la palla finisce contro uno dei bordi
    private boolean isBordersChecked() {
        if((position[1] == 0 && speed[1] < 0) || (position[0] == -500 && speed[0] < 0) || (position[0] == 477 && speed[0] > 0) || (
                (position[1] + getShape().getBounds().height) == 386  && speed[1] > 0))
            return true;
        return false;
    }

    @Override
    public void update() {
        if(isBordersChecked()) {
            if((position[1] == 0 && speed[1] < 0)) {
                bouncingY(-1, BOUNCING_FRICTION); //Rimbalzo della palla in basso
            }
            if((position[1] + this.getTotalShape().getBounds().height == 386 && speed[1] > 0)) {
                bouncingY(1, BOUNCING_FRICTION); //Rimbalzo della palla in basso
            }
            if((position[0] == -500 && speed[0] < 0)) {
                bouncingX(-1, BOUNCING_FRICTION); //Rimbalzo della palla sul bordo sinistro
            }
            if((position[0] == 477 && speed[0] > 0)) {
                bouncingX(1, BOUNCING_FRICTION); //Rimbalzo della palla sul bordo destro
            }
        }
        position[0] += speed[0];
        position[1] += speed[1];

        gravityApplication();

        //Coefficiente d'attrito aria y
        if(speed[1] > 0) {
            accelerateY(-AIR_FRICTION);
        } else if(speed[1] < 0) {
            accelerateY(+AIR_FRICTION);
        }

        //Coefficiente d'attrito aria x
        if(speed[0] > 0) {
            accelerateX(-AIR_FRICTION);
        } else if(speed[0] < 0) {
            accelerateX(+AIR_FRICTION);
        }
    }

    /**
     * metodo che fa rimbalzare (e fermare la palla)
     * @param acceleration
     * @param accelerationSign   segno dell'accelerazione
     */
    private void bouncingX(int accelerationSign, double acceleration){
        speed[0] = -speed[0];
        accelerateX(accelerationSign * acceleration);
    }

    private void bouncingY(double acceleration, double accelerationSign){
        speed[1] = -speed[1];
        accelerateY(accelerationSign * acceleration);
    }

    @Override
    public void collisionDetected(GameObject o) {
        if(o instanceof Player) {
            Player player = (Player)o;

            Area IntersectionLegBal = new Area(this.getShape());
            IntersectionLegBal.intersect(new Area(player.getSingleShape(0)));    //intersezione gamba palla

            if(player.isKicking() && !(IntersectionLegBal.isEmpty())){
                kicked(player);
            }

            else {
                //controlli in X
                if(speed[0] == 0){ // se la palla è ferma
                    speed[0] = player.getSpeed(0);
                }
                //stesso verso di spostamento
                if(speed[0] > 0 && player.getSpeed(0) > 0) {   //entrambi ->
                    if(position[0]  < player.getPosX()){ //palla da dietro e verso ->
                        speed[0] = -speed[0];
                    }else
                        speed[0] += player.getSpeed(0);
                }
                else if(speed[0] < 0 && player.getSpeed(0) < 0){  //embtrambi <-
                    if(position[0]  > player.getPosX()){ //palla da dietro e verso <-
                        speed[0] = -speed[0];
                    }else
                        speed[0] += player.getSpeed(0);
                }
                else if(!(player.getPosY() > position[1] && player.getPosX() >= position[0] && player.getPosX() <= (position[0] + 23))){  //si cambia velocità solo se la palla non è sotto il player
                    speed[0] = -speed[0] + player.getSpeed(0);   //versi opposti di spostamento
                }

                //stesso discorso per i controlli in y
                if(speed[1] > 0 && player.getSpeed(1) > 0) {   //entrambi su
                    if(position[1]  < player.getPosY()){ //palla da sotto
                        speed[1] = -speed[1];
                    }else
                        speed[1] += player.getSpeed(1);
                }
                else if(speed[1] < 0 && player.getSpeed(1) < 0){  //embtrambi giù
                    if(position[1]  > player.getPosY()){ //palla da sopra
                        speed[1] = -speed[1];
                    }else
                        speed[1] += player.getSpeed(1);
                }
                else speed[1] = -speed[1] + player.getSpeed(1);   //versi opposti di spostamento
            }
        }
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
