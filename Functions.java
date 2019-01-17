import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Functions {
    
    static int getRGB(int r, int g, int b) {
        return (r<<16) + (g<<8) + (b);
    }

    static void arrayAverage(double[] arr, int width, int height, int repeatAmount) {
        double[] avgarr = new double[arr.length];
        int repeat = 0;
        while (repeat < repeatAmount) {
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    double t1 = arr[(x - 1) + (y - 1) * width];
                    double t2 = arr[(x + 0) + (y - 1) * width];
                    double t3 = arr[(x + 1) + (y - 1) * width];
                    double m1 = arr[(x - 1) + (y + 0) * width];
                    double m3 = arr[(x + 1) + (y + 0) * width];
                    double b1 = arr[(x - 1) + (y + 1) * width];
                    double b2 = arr[(x + 0) + (y + 1) * width];
                    double b3 = arr[(x + 1) + (y + 1) * width];
                    double avg = (t1 + t2 + t3 + m1 + m3 + b1 + b2 + b3) / 8;
                    avgarr[x + y * width] = avg;

                }
            }
            repeat++;
            for (int i = 0; i < arr.length; i++) {
                arr[i] = avgarr[i];
            }
        }
    }

    static void exportWorldtoJPG(World w, int tileWidth, int tileHeight) {
        BufferedImage bufferImage = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) bufferImage.getRaster().getDataBuffer()).getData();

        Tile t;
        int type;
        int color;
        for (int x=0; x<tileWidth; x++) 
            for (int y=0; y<tileHeight; y++) {
                t = w.getTileI(x, y);
                type = t.getType();
                color = 0x000000;
                switch (type) {
                    case Tile.AIR:  color=0x0000cc; break;
                    case Tile.DIRT: color=0x00cc00; break;
                    case Tile.STONE:color=0x888888; break;
                }
                pixels[x+y*tileWidth] = color;
            }
        File outputfile = new File("image.jpg");
        try {
            ImageIO.write(bufferImage, "jpg", outputfile);
        } catch (IOException ex) {
            System.out.println("Error");
        }
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(outputfile);
        } catch (IOException ex) {
            System.out.println("Error");
        }
        System.exit(0);
    }

    static void exportArraytoJPG(double[] w, int tileWidth, int tileHeight) {
        BufferedImage bufferImage = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) bufferImage.getRaster().getDataBuffer()).getData();

        int t;
        int type;
        int color;
        for (int x=0; x<tileWidth; x++) 
            for (int y=0; y<tileHeight; y++) {
                t = (int)(255*(0.5+0.5*w[x+y*tileWidth]));
                pixels[x+y*tileWidth] = getRGB(t,t,t);
            }
        File outputfile = new File("image.jpg");
        try {
            ImageIO.write(bufferImage, "jpg", outputfile);
        } catch (IOException ex) {
            System.out.println("Error");
        }
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(outputfile);
        } catch (IOException ex) {
            System.out.println("Error");
        }
        System.exit(0);
    }
    
}
