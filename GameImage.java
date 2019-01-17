 


import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GameImage {

    int width;
    int height;
    int[] pixels;
    BufferedImage buffer;

    public GameImage(String path) {
        try {
            buffer = ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println("Couldn't load image:" + path);
        }

        width = buffer.getWidth();
        height = buffer.getHeight();

        pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x + y * width] = buffer.getRGB(x, y);
            }
        }

    }
}

