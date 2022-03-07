package it.unibs.pajc.helpers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HelperClass {
    /**
     * metodo che ritorna l'array contenente le immagini utili per formare il campo completo con porte
     * @return
     */
    public static ArrayList<BufferedImage> getImagesForField() {
        ArrayList<BufferedImage> imgs = new ArrayList<>();
        InputStream field = HelperClass.class.getClassLoader().getResourceAsStream("gameField1.jpeg");
        InputStream leftDoor = HelperClass.class.getClassLoader().getResourceAsStream("leftDoorRect.jpeg");
        InputStream rightDoor = HelperClass.class.getClassLoader().getResourceAsStream("rightDoorRect.jpeg");

        try {
            imgs.add(ImageIO.read(field));
            imgs.add(ImageIO.read(leftDoor));
            imgs.add(ImageIO.read(rightDoor));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgs;
    }

    /**
     * metodo che ritorna la singola buffered image, fatta di field e porte
     */
    public static BufferedImage getCompleteGameField(ArrayList<BufferedImage> imgs){
        int fieldWidth = imgs.get(0).getWidth();
        int fieldHeight = imgs.get(0).getHeight();

        //Creates a Graphics2D, which can be used to draw into this BufferedImage.
        BufferedImage newImg = new BufferedImage(fieldWidth, fieldHeight, imgs.get(0).getType());
        Graphics2D g2 = newImg.createGraphics();

        //disegno delle porte
        g2.drawImage(imgs.get(0), 0, 0, null);
        g2.drawImage(imgs.get(1), 0, 386-168, null); //porta sinistra
        g2.drawImage(imgs.get(2), 1000-80, 386-168, null); //porta destra

        //libera le risorse di g2
        g2.dispose();

        return newImg; //campo completo con porte
    }
}
