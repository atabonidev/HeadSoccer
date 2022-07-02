package it.unibs.pajc.view;

import it.unibs.pajc.model.BaseModel;
import it.unibs.pajc.model.Score;
import java.awt.*;

/**
 * Classe che rappresenta la barra superiore del gioco, in cui sono visibili i nomi dei player e il punteggio
 */
public class ScoreView extends BaseModel {

    private int x, y, width, height;
    private ScoreLabel namePl1;
    private ScoreLabel namePl2;
    private ScoreLabel scorePl1;
    private ScoreLabel scorePl2;

    public ScoreView(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initScoreBoard();
    }

    private void initScoreBoard() {
        this.namePl1 = new ScoreLabel(10, 10, 322, 40, "Player1", new Color(2,171,255), 2, 16);
        this.namePl2 = new ScoreLabel(668, 10, 322, 40, "Player2", Color.RED, 2, 16);
        this.scorePl1 = new ScoreLabel(380, 4, 110, 52, "0" , Color.LIGHT_GRAY, 0, 28);
        this.scorePl2 = new ScoreLabel(510, 4, 110, 52, "0" , Color.LIGHT_GRAY, 0, 28);
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);

        namePl1.draw(g);
        namePl2.draw(g);
        scorePl1.draw(g);
        scorePl2.draw(g);
    }

    public void setScore(Score score) {
        if(score != null) {
            if(!score.getPlayer1().getPlayerName().equals(namePl1.getText())) {
                namePl1.setText(score.getPlayer1().getPlayerName());
                namePl2.setText(score.getPlayer2().getPlayerName());
            }

            scorePl1.setText(""+score.getScorePl1());
            scorePl2.setText(""+score.getScorePl2());
        }
    }
}
