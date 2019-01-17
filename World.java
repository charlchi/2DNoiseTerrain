
public class World {

    // Dimensions
    public static int width = 16;
    public static int height = 16; // World size (chunk*chunk)

    public static int tileWidth = width*Chunk.width;
    public static int tileHeight = height*Chunk.height; // World size (tile*tile)

    public Chunk[] chunks;

    public World() {

        chunks = new Chunk[width * height];
        for (int x = 0; x < width; x++) 
            for (int y = 0; y < height; y++) 
                chunks[x + y * width] = new Chunk(x, y);

        TerrainGenerator.initWorld(this, tileWidth, tileHeight);

        //Functions.exportWorldtoJPG(this, tileWidth, tileHeight);
    }

    public Tile getTileI(int x, int y) {
        return getChunk(x/Chunk.width, y/Chunk.height).getTileI(x%Chunk.width ,y%Chunk.height);
    }

    public Tile getTilePX(int xp, int yp) {
        // Convert pixels coords to tile coords
        int xi = xp/Tile.size;
        int yi = yp/Tile.size;
        return getTileI(xi, yi);
    }

    public Chunk getChunk(int x, int y) {
        return chunks[x+y*width];
    }

    public Chunk getChunkPX(int xp, int yp) {
        // Convert pixels coords to chunk coords
        int xi = xp/Chunk.width/Tile.size;
        int yi = yp/Chunk.height/Tile.size;
        return getChunk(xi, yi);
    }

}
