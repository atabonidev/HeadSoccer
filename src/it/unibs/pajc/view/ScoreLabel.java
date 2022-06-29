package it.unibs.pajc.view;

import java.awt.*;

public class ScoreLabel {
    public int x, y, width, height;
    private String text;
    private Color color;
    private float borderSize;
    private int fontSize;

    public ScoreLabel(int x, int y, int width, int height, String text, Color color, float borderSize, int fontSize) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;
        this.borderSize = borderSize;
        this.fontSize = fontSize;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);

        if(borderSize != 0) {
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(borderSize));
            g.drawRect(x, y, width, height);
        }

        drawText(g);
    }

    private void drawText(Graphics g) {
        g.setFont(new Font("Lucida Sans", Font.BOLD, fontSize));

        int w = g.getFontMetrics().stringWidth(text);
        int h = g.getFontMetrics().getHeight();

        int textX = x - w / 2 + width / 2;
        int textY = y + ((height - h) / 2) + g.getFontMetrics().getAscent();

        g.setColor(Color.white);
        g.drawString(this.text, textX, textY);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
