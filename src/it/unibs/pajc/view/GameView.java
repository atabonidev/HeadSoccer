package it.unibs.pajc.view;

import it.unibs.pajc.helpers.HelperClass;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameView extends JPanel {

    private BufferedImage fieldBgImage;

    public GameView() {
        initInputs();
        importGameField();
    }

    private void initInputs() {
        this.setFocusable(true);
        this.requestFocus();
    }

    private void importGameField() {
        fieldBgImage = HelperClass.getFieldImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.fillRect(0,0, 1000, 60);
        g2.translate(0, 60);
        g2.drawImage(fieldBgImage, 0, 0, null);

        /*
        double sx = getWidth() / 1000.;
        double sy = (getHeight() - 60) / 1000.;

        g2.scale(sx, -sy);
        g2.translate(500, -500); //Lasciamo translate perche' comodo per movimento giocatori
        */

        g2.setColor(Color.red);
        g2.fillOval((getWidth()) / 2, (getHeight() - 60) / 2, 10, 10);

    }




}
