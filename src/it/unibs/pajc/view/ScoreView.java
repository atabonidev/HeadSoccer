package it.unibs.pajc.view;

import it.unibs.pajc.model.Score;
import java.awt.*;

public class ScoreView {

    private int x, y, width, height;
    private Score score;
    private ScoreBoard scorePl1;
    private ScoreBoard scorePl2;

    public ScoreView(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initScoreBoard();
    }

    private void initScoreBoard() {
        this.scorePl1 = new ScoreBoard(this.width / 2 - 100, 5, 100, 50, " X " , Color.cyan);
        this.scorePl2 = new ScoreBoard(this.width / 2 + 20, 5, 100, 50, " X " , Color.red);
    }

    public void draw(Graphics2D g) {
        g.fillRect(x, y, width, height);

        if(score != null) {
            scorePl1.setText("" + score.getScorePl1());

            scorePl2.setText("" + score.getScorePl2());
        }else{
            scorePl1.setText(" X ");

            scorePl2.setText(" X ");
        }

        scorePl1.draw(g);

        scorePl2.draw(g);

    }

    public void setScore(Score score) {
        this.score = score;
    }
}
