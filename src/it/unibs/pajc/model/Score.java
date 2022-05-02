package it.unibs.pajc.model;

public class Score {

    private int scorePl1;
    private int scorePl2;
    private int winner;
    private boolean isFinished;

    public Score() {
        this.scorePl1 = 0;
        this.scorePl2 = 0;
    }

    public void incrementScore(int playerId) {
        if(playerId == 1) {
            this.setScorePl1(this.scorePl1 + 1);
        }
        else if(playerId == 2) {
            this.setScorePl2(this.scorePl2 + 1);
        }
    }


    //GETTERS AND SETTERS
    public int getScorePl1() {
        return scorePl1;
    }

    public int getScorePl2() {
        return scorePl2;
    }

    public void setScorePl1(int scorePl1) {
        if(scorePl1 < 3) {
            this.scorePl1 = scorePl1;
        } else {
            this.isFinished = true;
            this.winner = 1;
        }
    }

    public void setScorePl2(int scorePl2) {
        if(scorePl1 < 3) {
            this.scorePl2 = scorePl2;
        } else {
            this.isFinished = true;
            this.winner = 2;
        }
    }
}
