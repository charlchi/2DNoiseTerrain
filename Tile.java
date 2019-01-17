
import java.util.Random;

public class Tile {

    public final static int AIR = 0;
    public final static int DIRT = 1;
    public final static int STONE = 2;

    public static int size= 8; // Tile is 8*8 pixels

    private int x;
    private int y; // Index in world (tiles)
    private int type;
    private boolean solid;

    public int color;
    public int bgcolor;

    public Tile(int x, int y, int type, boolean solid) {
        this.x = x;
        this.y = y;
        this.type = type;
        setSolid(solid);
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        if (type == DIRT) {
            int rand = new Random().nextInt(6);
            setColor(Functions.getRGB(30 + rand, 70 + rand, 0));
        } else if (type == STONE) {
            int rand = new Random().nextInt(6);
            setColor(Functions.getRGB(90 + rand, 90 + rand, 90 + rand));
        }
    }

    public void setColor(int c){
        this.color = c;
    }
    public void setBgColor(int c){
        this.bgcolor = c;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
