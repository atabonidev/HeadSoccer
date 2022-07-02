package it.unibs.pajc.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe che rappresenta lo sfondo del menu iniziale, appena viene avviato il gioco
 */
public class StarterMenuView extends JPanel {

    BufferedImage blurredFieldImage = null;

    public StarterMenuView() {
        InputStream field = this.getClass().getClassLoader().getResourceAsStream("gamefield-blured.png");

        try {
            blurredFieldImage = ImageIO.read(field);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(blurredFieldImage, 0, 0, null);

    }
}
