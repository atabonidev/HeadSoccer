package it.unibs.pajc.helpers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HelperClass {

    private static final int LXC_RIGHT_DOOR = 1000-80;
    private static final int LYC_RIGHT_DOOR = 386-168;
    private static final int LXC_LEFT_DOOR = 0;
    private static final int LYC_LEFT_DOOR = 386-168;

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
        g2.drawImage(imgs.get(1), LXC_LEFT_DOOR, LYC_LEFT_DOOR, null); //porta sinistra
        g2.drawImage(imgs.get(2), LXC_RIGHT_DOOR, LYC_RIGHT_DOOR, null); //porta destra

        //libera le risorse di g2
        g2.dispose();

        return newImg; //campo completo con porte
    }

    /**
     * Ribalta l'immagine passata come parametro
     * @param image
     * @return
     */
    public static BufferedImage flipVerticallyImage(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }
}
