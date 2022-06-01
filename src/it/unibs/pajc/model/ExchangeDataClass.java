package it.unibs.pajc.model;

import java.io.Serializable;

public class ExchangeDataClass implements Serializable {
    private Player player1;
    private Player player2;
    private Ball ball;
    private Score score;

    public ExchangeDataClass(GameField gameField) {
        this.player1 = gameField.getPlayer1();
        this.player2 = gameField.getPlayer2();
        this.ball = gameField.getBall();
        this.score = gameField.getScore();
    }

    public void updateData(ExchangeDataClass newData) {
        this.player1 = newData.getPlayer1();
        this.player2 = newData.getPlayer2();
        this.ball = newData.getBall();
        this.score = newData.getScore();
    }

    /* ===================
    GETTERS AND SETTERS
    ====================*/

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Ball getBall() {
        return ball;
    }

    public Score getScore() {
        return score;
    }
}
