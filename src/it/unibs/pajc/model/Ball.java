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

    private boolean isBordersChecked() {
        if((position[1] == 0 && speed[1] < 0) || (position[0] == -500 && speed[0] < 0) || (position[0] == 477 && speed[0] > 0))
            return true;
        return false;
    }

    @Override
    public void update() {
        System.out.println(position[0] + " : " + position[1]);
        System.out.println("Speed: " + speed[0] + " : " + speed[1]);

        if(isBordersChecked()) {
            if((position[1] == 0 && speed[1] < 0)) {
                speed[1] = Math.abs(speed[1]) - BOUNCING_FRICTION; //Rimbalzo della palla in basso
            }
            if((position[0] == -500 && speed[0] < 0)) {
                speed[0] = Math.abs(speed[0]) - BOUNCING_FRICTION; //Rimbalzo della palla sul bordo sinistro
            }
            if((position[0] == 477 && speed[0] > 0)) {
                speed[0] = -Math.abs(speed[0]) + BOUNCING_FRICTION; //Rimbalzo della palla sul bordo destro
            }
        }
        else {
            position[0] += speed[0];
            position[1] += speed[1];
        }

        //qui mi sa che serve anche l'attrito con l'aria altrimenti non si ferma più
        accelerateY(-GRAVITY);

        //Coefficiente d'attrito aria x
        if(speed[0] > 0) {
            accelerateX(-AIR_FRICTION);
        } else if(speed[0] < 0) {
            accelerateX(+AIR_FRICTION);
        }

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
                //player e palla: versi opposti (es palla isMOvingRight e la palla no) -> opposto della velocità del giocatore (saranno già oppposto quindi basta sommare)
                if(player.getSpeed(0) > 0 && this.speed[0] < 0)
                    speed[0] = Math.abs(speed[0]) + player.getSpeed(0) - BOUNCING_FRICTION; //giocatore dx palla sx
                else if(player.getSpeed(0) < 0 && this.speed[0] > 0)
                    speed[0] = - Math.abs(speed[0]) - player.getSpeed(0) + BOUNCING_FRICTION; //giocatore sx palla dx
                else{  //se si muovono nello stesso verso
                    if(speed[0] > 0)
                        speed[0] = -Math.abs(speed[0]) + BOUNCING_FRICTION;
                    else
                        speed[0] = Math.abs(speed[0]) - BOUNCING_FRICTION;
                }

                //stesso discorso per i controlli in y
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
