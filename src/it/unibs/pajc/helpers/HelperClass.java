package it.unibs.pajc.helpers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HelperClass {

    public static BufferedImage getFieldImage() {
        BufferedImage field = null;
        InputStream is = HelperClass.class.getClassLoader().getResourceAsStream("gameFieldDoors.jpeg");

        try {
            assert is != null;
            field = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return field;
    }

    public static BufferedImage getDoorImage() {
        BufferedImage field = null;
        InputStream portaSx = HelperClass.class.getClassLoader().getResourceAsStream("leftDoor.jpeg");
        try {
            assert portaSx != null;
            field = ImageIO.read(portaSx);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return field;
    }

}
