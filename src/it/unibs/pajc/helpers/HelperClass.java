package it.unibs.pajc.helpers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class HelperClass {

    public static BufferedImage getFieldImage() {
        BufferedImage field = null;
        InputStream is = HelperClass.class.getClassLoader().getResourceAsStream("gameField1.jpeg");

        try {
            assert is != null;
            field = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return field;
    }

}
