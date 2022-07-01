package it.unibs.pajc.helpers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Classe che si occupa di importare le immagini del gioco
 */
public class HelperClass {

    private static String[] imagesNames = new String[]{
            "leftDoorRect.jpeg",
            "rightDoorRect.jpeg",
            "LeftMan.png",
            "RightMan.png",
            "KickLeftMan.png",
            "KickRightMan.png",
            "WalkingLeftMan.png",
            "WalkingRightMan.png",
            "Ball.png"
    };

    public static HashMap<String, BufferedImage> gameImages = new HashMap<>();

    /**
     * metodo che ritorna l'array contenente le immagini utili per formare il campo completo con porte
     * @return
     */
    public static BufferedImage getCompleteGameField() {
        InputStream field = HelperClass.class.getClassLoader().getResourceAsStream("gameField.jpeg");

        try {
            return ImageIO.read(field);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

    public static void importImages() throws IOException {
        for (String imageName : imagesNames) {
            gameImages.put(imageName, ImageIO.read(HelperClass.class.getClassLoader().getResourceAsStream(imageName)));
        }
    }

    public static BufferedImage getImageFromName(String nameImage) {
        return flipVerticallyImage(gameImages.get(nameImage));
    }


}
