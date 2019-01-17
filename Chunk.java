
public class Chunk {

    public static int width = 60;
    public static int height = 60; // Chunk size (tile*tile)

    public static int pWidth = width*Tile.size;
    public static int pHeight = height*Tile.size; // Chunk size (px*px)

    private int x;
    private int y; // Index in world (chunks)
    private Tile[] tiles;

    private int[] pixels;

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;
        tiles = new Tile[width * height];
        pixels = new int[pWidth*pHeight];
        for (int xi = 0; xi < width; xi++) 
            for (int yi = 0; yi < height; yi++) 
                tiles[xi+yi*width] = new Tile(x*width+xi,y*height+yi,0,false);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Tile getTileI(int x, int y) {
        return tiles[x+y*width];
    }
    public Tile getTilePX(int xp, int yp) {
        // Convert pixels coords to tile coords
        int xi = xp/Tile.size;
        int yi = yp/Tile.size;
        return getTileI(xi, yi);
    }

    void draw(int[] pixels, int xp, int yp) {
        
        // on the fly rendering
        int hw = Tile.size/2;
        int xo = x*pWidth  - xp + pWidth/2;
        int yo = y*pHeight - yp + pHeight/2;

        for (int y = 0; y < pHeight; y++) {
            for (int x = 0; x < pWidth; x++) {
                Tile c = getTilePX(x, y);
                
                if (x + xo >= 0 & x + xo < pWidth & y + yo >= 0 & y + yo < pHeight) {
                    pixels[(x + xo) + (y + yo) * pWidth] = c.getType() == Tile.AIR ? c.bgcolor : c.color;
                    //pixels[(x + xo) + (y + yo) * pWidth] = (xp * yp) / 5000 * c.getType() * ((x*500) | (y*500));
                }

            }
        }

    }
}
