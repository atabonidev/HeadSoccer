package it.unibs.pajc.view;

import it.unibs.pajc.helpers.HelperClass;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameView extends JPanel {

    private BufferedImage fieldBgImage;
    private BufferedImage leftDoor;

    public GameView() {
        initInputs();
        importGameField();
    }

    private void initInputs() {
        this.setFocusable(true);
        this.requestFocus();
    }

    private void importGameField() {

        fieldBgImage = HelperClass.getCompleteGameField(HelperClass.getImagesForField());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.fillRect(0,0, 1000, 60);
        g2.translate(0, 60);
        //disegno campo di gioco con porte
        g2.drawImage(fieldBgImage, 0, 0, null);

        //sistema di rif spostato al centro all'altezza del campo da gioco.
        g2.translate(500, 386); //sistema di riferimento con origine in centro
        g2.scale(1,-1);

        g2.setColor(Color.red);
        g2.fillOval(0, 0, 5, 5);
        g2.fillRect(100, 0, 40, 60);

    }

}
