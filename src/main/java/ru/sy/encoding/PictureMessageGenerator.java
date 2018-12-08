package ru.sy.encoding;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PictureMessageGenerator implements MessageGenerator {

    public static int toNormalRGB(int rgb) {
        Color color = new Color(rgb);
        return color.getRed() + color.getBlue() + color.getGreen();
    }

    public static int quantization(int val) {
        return val / 20 * 20 + (val % 20 > 0 ? 20 : 0);
    }


    @Override
    public int[] generateMessageByFile(File file) throws Exception {
        BufferedImage image = ImageIO.read(file);
        int line = image.getHeight() / 2;
        int[] message = new int[image.getWidth()];
        for (int i = 0; i < image.getWidth(); i++) {
            message[i] = quantization(toNormalRGB(image.getRGB(i, line)));
        }
        return message;
    }
}
