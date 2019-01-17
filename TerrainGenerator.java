

public class TerrainGenerator {

    static void initWorld(World w, int width, int height) {
    
        System.out.println("Generating heightmap noise");

        double[] heightMap = new double[width*height];
        addNoise(heightMap, width, height, height*2, 0.5);
        addGradient(heightMap, width, height, 1.0);

        System.out.println("Generating cavemap noise");

        double[] caveMap = new double[width*height];
        addNoise(caveMap, width, height, 1000, 0.4);
        addNoise(caveMap, width, height, 100, 1.0);
        addGradient(caveMap, width, height, 0.5);

        Functions.arrayAverage(caveMap, width, height, 3);

        Tile t;
        int rand;
        int r,g,b;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                t = w.getTileI(x, y);
                r = y>70?70:(int)((1.0-(double)y/(double)height)*70.0);
                g = y>70?70:(int)((1.0-(double)y/(double)height)*70.0);
                b = (int)((1.0-(double)y/(double)height)*255.0);
                t.setBgColor(Functions.getRGB(r, g, b));
                // Add terrain
                if (heightMap[x+y*width] > 0.5) {         
                    t.setSolid(true);
                    t.setType(Tile.STONE);
                } else {
                    t.setSolid(false);
                    t.setType(Tile.AIR);
                }
                if (heightMap[x+y*width] > 0.48 && heightMap[x+y*width] <= 0.5) {
                    t.setSolid(true);
                    t.setType(Tile.DIRT);
                }
                if (heightMap[x+y*width] > 0.5 && caveMap[x+y*width] > 0.55){
                    t.setSolid(false);
                    t.setType(Tile.AIR);
                }
            }
        }

    }

    static public void addNoise(double[] map, int width, int height, int largestFeature, double weight){
        SimplexNoise simplexNoise=new SimplexNoise(largestFeature, 0.5, (int)(Math.random()*500));
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++)
                map[i+j*width] = (simplexNoise.getNoise(i,j)) * weight;
    }

    static public void addGradient(double[] map, int width, int height, double weight) {
        for (int y=0; y<height; y++) 
            for (int x=0; x<width; x++) 
                map[x+y*width] += ((double)y/(double)height) * weight;
    }
}