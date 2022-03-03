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
        g.fillRect(0,0, 1000, 60);
        g.drawImage(fieldBgImage, 0, 60, null);
    }




}
