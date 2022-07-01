package it.unibs.pajc.model;

import it.unibs.pajc.helpers.Sound;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class Ball extends DinamicObject implements Serializable {
    private static final int DEFAULT_POS_Y = 356;
    private static final int[] KICK_STRENGHT = {10, 20};
    private static final double BOUNCING_FRICTION = 2.6;
    private static final double AIR_FRICTION = 0.02;


    private GameField gameField;

    public Ball(GameField gameField){
        setDefault();

        this.gameField = gameField;

        createSkeleton();
        calculateCdm();
    }

    /**
     * Metodo che reimposta la palla alla posizione di default
     */
    public void setDefault() {
        this.position[0] = -this.getTotalShape().getBounds().width / 2.0;
        this.position[1] = DEFAULT_POS_Y;
        this.speed[0] = 0;
        this.speed[1] = -10;
    }

    //Controlla se la palla finisce contro uno dei bordi
    private boolean isBordersChecked() {
        return (position[1] == 0 || position[0] == -500 || (position[0] + this.getObjWidth()) == 500 || (position[1] + this.getObjHeight()) == 386);
    }

    /**
     * gestione fisica della palla
     */
    @Override
    public void update() {
        if(isBordersChecked()) {
            if((position[1] == 0 && speed[1] < 0)) {
                bouncingY(-1, BOUNCING_FRICTION); //Rimbalzo della palla in basso
            }
            if((position[1] + this.getObjHeight() == 386 && speed[1] > 0)) {
                bouncingY(0, 0); //Rimbalzo della palla in alto
            }
            if((position[0] == -500 && speed[0] < 0)) {
                bouncingX(-1, BOUNCING_FRICTION); //Rimbalzo della palla sul bordo sinistro
            }
            if((position[0] + this.getObjWidth() == 500 && speed[0] > 0)) {
                bouncingX(1, BOUNCING_FRICTION); //Rimbalzo della palla sul bordo destro
            }
        }

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

        gravityApplication();

        position[0] += speed[0];
        position[1] += speed[1];
    }

    /**
     * metodo che fa rimbalzare (e fermare la palla)
     * @param acceleration
     * @param accelerationSign segno dell'accelerazione
     */
    private void bouncingX(int accelerationSign, double acceleration){
        speed[0] = -speed[0];
        accelerateX(accelerationSign * acceleration);
    }

    private void bouncingY(double accelerationSign, double acceleration){
        speed[1] = -speed[1];
        accelerateY(accelerationSign * acceleration);
    }

    /**
     * Gestione delle collisioni tra la palla e gli altri oggetti presenti nel gioco
     * @param o
     */
    @Override
    public void collisionDetected(GameObject o) {
        //================ PALLA E GIOCATORE ================
        if(o instanceof Player player) {
            Area IntersectionLegBal = new Area(this.getShape()); //prende la shape gia' trasformata della palla
            IntersectionLegBal.intersect(new Area(player.getKickLegTransformed())); //intersezione gamba palla

            //Se il player sta calciando e l'intersezione tra palla e gamba calciante del player non e'vuota
            if(player.getKickStatus() && !(IntersectionLegBal.isEmpty())){
                this.kicked(player.getPlayerID());
            }
            else {
                //______________ CONTROLLI VELOCITA' IN X ______________
                //Se non sei sopra la palla
                if(!isPlayerOverBall(player)) {
                    if (speed[0] == 0 && (player.getSpeedX() > 0 || player.getSpeedX() < 0)) { // se la palla è ferma
                        speed[0] = player.getSpeedX();
                    }
                    //stesso verso di spostamento: entrambi verso destra (->)
                    else if (speed[0] > 0 && player.getSpeedX() > 0) {

                        //posizione palla minore della posizione player
                        if (position[0] < player.getPosX())
                            speed[0] = -speed[0];
                        else
                            speed[0] += player.getSpeedX();

                    }
                    //stesso verso di spostamento: entrambi verso sinistra (<-)
                    else if (speed[0] < 0 && player.getSpeedX() < 0) {

                        //posizione palla maggiore della posizione player
                        if (position[0] > player.getPosX())
                            speed[0] = -speed[0];
                        else
                            speed[0] += player.getSpeedX();

                    }
                    else
                        speed[0] = -speed[0] + player.getSpeed(0);   //versi opposti di spostamento
                }


                //______________ CONTROLLI VELOCITA' IN Y ______________
                //stesso verso di spostamento: entrambi verso l'alto
                if (speed[1] > 0 && player.getSpeedY() > 0) {

                    //posizione palla minore della posizione player: palla sotto il player
                    if (position[1] < player.getPosY()) {
                        speed[1] = -speed[1];
                    } else
                        speed[1] += player.getSpeedY();

                }
                //stesso verso di spostamento: entrambi verso il basso
                else if (speed[1] < 0 && player.getSpeedY() < 0) {

                    //posizione palla maggiore della posizione player: palla sopra il player
                    if (position[1] > player.getPosY()) {
                        speed[1] = -speed[1];
                    } else
                        speed[1] += player.getSpeedY();

                }
                else
                    speed[1] = -speed[1] + player.getSpeedY();


                //______________ CUT OFF PALLA FERMA SOPRA PLAYER FERMA ______________
                //palla rimbalza su giocatore fermo in y -> cut off per impedire che la palla entri nel giocatore quando è quasi ferma
                if (Math.abs(speed[1]) <= 5.28 && speed[0] == 0 && this.getActualCdmY() >= player.getActualCdmY()) {
                    speed[1] = 0;
                    this.setPosY(player.getObjHeight());
                }
            }
        }

        //================ PALLA E PORTA ================
        else if(o instanceof  FootballGoal footballGoal){
            //lato superiore della traversa
            if(this.getPosY() - speed[1] >= footballGoal.getCrossBarY() + footballGoal.getCrossBarHeight()){
                if(this.speed[1] == -DinamicObject.GRAVITY && speed[0] == 0) {
                    this.gameField.reset();
                } else {
                    this.setPosY(footballGoal.getObjHeight());
                    bouncingY(-1, BOUNCING_FRICTION);
                }
            }
            //lato della traversa (dx o sx)
            else if(this.getPosY() + this.getObjHeight() - speed[1] >= footballGoal.getCrossBarY() && this.getPosY() - speed[1] <= footballGoal.getCrossBarY() + footballGoal.getCrossBarHeight()){
                bouncingX(0, 0);
            }
            //controllo nella rete
            else {
                this.gameField.reset();
                this.gameField.incrementScore(footballGoal);
            }
        }
    }


    //Metodo che restituisce true se il player e' sopra la palla
    private boolean isPlayerOverBall(Player player) {
        return (player.getPosY() > position[1] && player.getPosX() > (position[0] - player.getObjWidth()) && player.getPosX() < (position[0] + this.getObjWidth() + player.getObjWidth()));
    }


    @Override
    public void createSkeleton() {
        Shape ballShape = new Ellipse2D.Double(0, 0, 23, 23);
        super.objectShape.add(ballShape);
    }

    public void kicked(int playerID){
        if(playerID == 1) {
            speed[0] = KICK_STRENGHT[0];
            speed[1] = KICK_STRENGHT[1];
        } else {
            speed[0] = -KICK_STRENGHT[0];
            speed[1] = KICK_STRENGHT[1];
        }

        //Attivazione suono calcio palla
        gameField.setClipNumberPl1(Sound.KICK_BALL);
        gameField.setClipActivePl1(true);
        gameField.setClipNumberPl2(Sound.KICK_BALL);
        gameField.setClipActivePl2(true);
    }

}