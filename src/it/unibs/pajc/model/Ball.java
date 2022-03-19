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
                speed[1] = Math.abs(speed[1]) - BOUNCING_FRICTION; //Rimbalzo della palla in basso
            }
            if((position[1] + this.getTotalShape().getBounds().height == 386 && speed[1] > 0)) {
                speed[1] = -Math.abs(speed[1]) + BOUNCING_FRICTION; //Rimbalzo della palla in basso

            }
            if((position[0] == -500 && speed[0] < 0)) {
                speed[0] = Math.abs(speed[0]) - BOUNCING_FRICTION; //Rimbalzo della palla sul bordo sinistro
            }
            if((position[0] == 477 && speed[0] > 0)) {
                speed[0] = -Math.abs(speed[0]) + BOUNCING_FRICTION; //Rimbalzo della palla sul bordo destro
            }
        }
        position[0] += speed[0];
        position[1] += speed[1];

        //qui mi sa che serve anche l'attrito con l'aria altrimenti non si ferma più
        accelerateY(-GRAVITY);

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

    @Override
    public void collisionDetected(GameObject o) {
        if(o instanceof Player) {
            Player player = (Player)o;

            Area IntersectionLegBal = new Area(this.getShape());
            IntersectionLegBal.intersect(new Area(player.getSingleShape(0)));    //intersezione gamba palla

            if(player.isKicking() && !(IntersectionLegBal.isEmpty())){
                kicked(player);
            }
            //controlli in X
            else {
                //player e palla: versi opposti (es palla isMOvingRight e la palla no) -> opposto della velocità del giocatore (saranno già oppposto quindi basta sommare)
                if(player.getSpeed(0) > 0 && this.speed[0] < 0)
                    speed[0] = Math.abs(speed[0]) + player.getSpeed(0); //giocatore dx palla sx
                else if(player.getSpeed(0) < 0 && this.speed[0] > 0)
                    speed[0] = - Math.abs(speed[0]) + player.getSpeed(0) ; //giocatore sx palla dx
                else{  //se si muovono nello stesso verso
                    if(speed[0] > 0)
                        speed[0] = -Math.abs(speed[0]);
                    else if(speed[0] < 0)
                        speed[0] = Math.abs(speed[0]);
                    else if (player.getSpeed(1) == 0)    //se il player si sta muovendo solo in orizzontale
                        speed[0] = player.getSpeed(0);   //se la palla è ferma prende la velocità del giocatore
                }

                //stesso discorso per i controlli in y
                //player e palla: versi opposti (es palla isMOvingRight e la palla no) -> opposto della velocità del giocatore (saranno già oppposto quindi basta sommare)
                if(player.getSpeed(1) > 0 && this.speed[1] < 0)
                    speed[1] = Math.abs(speed[1]) + player.getSpeed(1);
                else if(player.getSpeed(1) < 0 && this.speed[1] > 0)
                    speed[1] = - Math.abs(speed[1]) + player.getSpeed(1);
                else{  //se si muovono nello stesso verso
                    if(speed[1] > 0)
                        speed[1] = Math.abs(speed[1]) + player.getSpeed(1);   //entrambi verso l'alto
                    else if (speed[1] < 0)
                        speed[1] = Math.abs(speed[1]);   //entrambi verso il basso (es. palla in testa)
                    /*
                    se il giocatore atterra sulla palla mentre questa è ferma non succede niente
                     */
                }

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
