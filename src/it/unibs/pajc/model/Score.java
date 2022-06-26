package it.unibs.pajc.model;

import java.io.Serializable;

public class Score implements Serializable {

    private static final int MAX_SCORE = 3;

    private Player player1;
    private Player player2;
    private boolean isFinished;
    private boolean isGoal;

    public Score(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void incrementScore(int playerId) {
        if(playerId == 1)
            this.player1.incrementPlayerScore();
        else
            this.player2.incrementPlayerScore();

        isGoal = true;
    }

    //GETTERS AND SETTERS
    public int getScorePl1() {
        return player1.getPlayerScore();
    }

    public int getScorePl2() {
        return player2.getPlayerScore();
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setIsGoal(boolean goal) {
        this.isGoal = goal;
    }
}
