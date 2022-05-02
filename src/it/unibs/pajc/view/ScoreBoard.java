package it.unibs.pajc.view;

import java.awt.*;

public class ScoreBoard {
    public int x, y, width, height;
    private String text;
    private Color color;

    public ScoreBoard(int x, int y, int width, int height, String text, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);

        g.setColor(Color.gray);
        g.setStroke(new BasicStroke(3f));
        g.drawRect(x, y, width, height);

        drawText(g);
    }

    private void drawText(Graphics g) {
        g.setFont(new Font("TimesRoman", Font.BOLD, 22));

        int w = g.getFontMetrics().stringWidth(text);
        int h = g.getFontMetrics().getHeight();

        int textX = x - w / 2 + width / 2;
        int textY = y + ((height - h) / 2) + g.getFontMetrics().getAscent();

        g.setColor(Color.white);
        g.drawString(this.text, textX, textY);
    }

    public void setText(String text) {
        this.text = text;
    }
}
