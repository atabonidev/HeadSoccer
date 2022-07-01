package it.unibs.pajc.model;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import javax.swing.Timer;

/**
 * Classe che rappresenta il player.
 */

public class Player extends DinamicObject implements Serializable {

    public static final double DEFAULT_POS_X = 300; //posizione iniziale del giocatore
    public static final double JUMP_STRENGTH = 10;  //potenza del calcio
    public static final double CONST_SPEED_X = 3.0; //velocità costante di movimento del player in x

    private static final int MAX_SCORE = 3;  //punteggio massimo raggiungibile (vittoria)

    private int playerID; //Indica il numero del player (1 => sinistra || 2 => destra)
    private String playerName;
    private boolean kickStatus; //indica se il player sta attualmente calciando
    private boolean isWinner;
    private int currentIMG = 0; //indice dell'immagine da visualizzare
    private Timer timerImageAnimation; //scandisce l'animazione delle immagini
    private int playerScore = 0;

    public Player(int playerID) {
        this.playerID = playerID;
        setDefault();
        createSkeleton();
        calculateCdm();

        this.timerImageAnimation = new Timer(100, e -> {
            setCurrentIMGIndex();
        });
    }

    //Setta la posizione iniziale del player al suo valore di default (usato)
    @Override
    public void setDefault() {
        if(playerID == 1) {
            this.position[0] = -DEFAULT_POS_X;
        } else {
            this.position[0] = DEFAULT_POS_X;
        }
        this.position[1] = 0;
        this.speed[0] = 0;
        this.speed[1] = 0;
    }

    public void startAnimation(){
        timerImageAnimation.start();
    }

    public void stopAnimation() {
        this.timerImageAnimation.stop();
        this.currentIMG = 0;
    }


    //setta l'indice dell'immagine corrente in base allo stato in cui si trova il player
    public void setCurrentIMGIndex(){
        if(this.currentIMG == 0)
            this.currentIMG = 1;
        else
            this.currentIMG = 0;
    }

    //gestione del calcio
    public void kick(boolean isKicking) {
        if(!kickStatus && isKicking) {
            kickStatus = isKicking;  //viene reimpostato a false in keyReleased(), serve per le collisioni nella palla
            legRotation(1);
        }
    }

    //riporta la gamba calciante nello stato iniziale
    public void stopKicking(){
        legRotation( -1);
        kickStatus = false;
        this.currentIMG = 0;
    }

    /**
     * ruota la gamba in base al giocatore che si sta usando:
     *     - se il coefficiente è 1 -> si calcia,
     *     - se è -1 si riporta la gamba alla posizione originale
     */
    private void legRotation(int rotationCoefficient){

        if (rotationCoefficient == 1) {  //se sta calciando
            Shape rotatedLeg;
            AffineTransform tx;

            //se il giocatore è il sinistro -> si ruota la shape[2], ossia la gamba destra
            if(playerID == 1){
                tx = rotateLeg(Math.PI / 4);
                rotatedLeg = tx.createTransformedShape(this.getSingleShape(2));
                this.setSingleShape(2,rotatedLeg);
            }

            else{
                tx = rotateLeg(-Math.PI / 4);
                rotatedLeg = tx.createTransformedShape(this.getSingleShape(1));
                this.setSingleShape(1,rotatedLeg);
            }
        }
        else {   //se ha smesso di calciare

            if(playerID == 1){
                Shape rightLeg = new Rectangle(15, 0, 15, 32);
                this.setSingleShape(2, rightLeg);
            }
            else{
                Shape leftLeg = new Rectangle(0, 0, 15, 32);
                this.setSingleShape(1, leftLeg);
            }
        }
    }

    //incrementa la posizione x del player di un valore pari alla speed
    public void move(boolean isMovingRight) {
        if(isMovingRight) {
            speed[0] = CONST_SPEED_X;
        } else {
            speed[0] = - CONST_SPEED_X;
        }
        position[0] += speed[0];
    }

    public void jump() {
        if(speed[1] == 0) {
            accelerateY(JUMP_STRENGTH);
        }
    }

    @Override
    public void update() {
        if(position[1] == 0 && speed[1] < 0) {
            speed[1] = 0;
        } else{
            position[1] += speed[1];
        }
        gravityApplication();
    }

    @Override
    public void collisionDetected(GameObject o) {
        //================ PALLA E GIOCATORE ================
        if(o instanceof Ball ball){
            //se il giocatore salta sulla palla mentre è ferma
            if(ball.getSpeedY() == 0 && this.speed[1] < 0) {
                this.speed[1] = 0;  //si ferma sulla palla
                this.setPosY(ball.getPosY() + ball.getObjHeight());
            }
        }

        //================ PLAYER E PORTA ================
        if(o instanceof FootballGoal footballGoal) {
            //controlla se il giocatore sta al di sotto della porta
            if(this.getPosY() - speed[1] < footballGoal.getObjHeight()) {
                if(footballGoal.isLeft())
                    this.setPosX(footballGoal.getPosX() + footballGoal.getObjWidth());
                else
                    this.setPosX(footballGoal.getPosX() - this.getObjWidth());
            }
            else{
                this.speed[1] = 0;
                this.setPosY(footballGoal.getObjHeight());
            }
        }

        //================ PLAYER E PLAYER ================
        if(o instanceof Player otherPlayer){
            if(Math.abs(this.getActualCdmY() - otherPlayer.getActualCdmY()) + Math.abs(speed[1]) > this.getObjHeight()) {
                if(this.getActualCdmY() > otherPlayer.getActualCdmY()) {
                    this.position[1] = otherPlayer.getPosY() + otherPlayer.getObjHeight();
                }
                else if(this.getActualCdmY() < otherPlayer.getActualCdmY()) {
                    this.position[1] = this.getPosY();
                }
                this.speed[1] = 0;
            }
            else {
                if(this.getActualCdmX() + this.speed[0] < otherPlayer.getActualCdmX()) {
                    this.position[0] = otherPlayer.position[0] - otherPlayer.getObjWidth();
                }
                else if(this.getActualCdmX() - speed[0] > otherPlayer.getActualCdmX()) {
                    this.position[0] = otherPlayer.position[0] + otherPlayer.getObjWidth();
                }
            }
        }
    }

    @Override
    public void createSkeleton() {
        Shape body = new Rectangle(0, 32 , 30, 48);   //busto (19 = altezza gambe)
        Shape leftLeg = new Rectangle(0, 0, 15, 32);
        Shape rightLeg = new Rectangle(15, 0, 15, 32);

        super.objectShape.add(body);
        super.objectShape.add(leftLeg);
        super.objectShape.add(rightLeg);
    }

    //ruota la singola shape della gamba che sta calciando, in base al particolare player
    private AffineTransform rotateLeg(double radius) {
        AffineTransform rotationTransform;

        if(this.playerID == 1) {
            rotationTransform = AffineTransform.getRotateInstance(radius, this.getSingleShape(2).getBounds().x + this.getSingleShape(2).getBounds().width /2.0,
                    this.getSingleShape(2).getBounds().height);
        }
        else {
            rotationTransform = AffineTransform.getRotateInstance(radius, this.getSingleShape(1).getBounds().x + this.getSingleShape(1).getBounds().width /2.0,
                    this.getSingleShape(1).getBounds().height);
        }

        return rotationTransform;
    }

    /* ===================
    GETTERS AND SETTERS
    ====================*/
    public boolean getKickStatus(){return kickStatus;}
    public int getPlayerID() {
        return playerID;
    }

    //ritorna la singola shape della gamba calciante trasformata rispetto all'attuale posizione del player
    public Shape getKickLegTransformed() {
        AffineTransform t = new AffineTransform(); //inizialmente coincide con la matrice identità
        Shape legTransformedShape;

        if(playerID == 1) {
            t.translate(position[0] + this.getObjWidth() / 2, position[1]); //applicazione della trasformata
            legTransformedShape = t.createTransformedShape(this.getSingleShape(2));
        }
        else {
            t.translate(position[0], position[1]); //applicazione della trasformata
            legTransformedShape = t.createTransformedShape(this.getSingleShape(1));
        }

        return legTransformedShape;
    }

    public boolean isWinner() {
        return this.isWinner;
    }
    public void setSpeed(int speedIndex, double newSpeed){
        speed[speedIndex] = newSpeed;
    }
    public int getCurrentImageIndex() {
        if(kickStatus) {
            this.timerImageAnimation.stop();
            this.currentIMG = 2;
        }

        return this.currentIMG;

    }
    public int getPlayerScore() {
        return playerScore;
    }
    private void setPlayerScore(int score) {
        this.playerScore = score;
        if(score == MAX_SCORE)
            isWinner = true;
    }
    public void incrementPlayerScore() {
        setPlayerScore(this.playerScore + 1);
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}