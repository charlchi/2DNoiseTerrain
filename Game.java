

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Game extends Canvas {

    // Game
    private Player p;
    private World world;

    // Size of screen (px*px)
    private int width = Chunk.pWidth;
    private int height =Chunk.pHeight;

    private int scale = 2; // Scale up screen pixels

    // Graphics
    private JFrame frame;
    private BufferedImage bgbufferImage;
    private int[] bgpixels;
    private BufferedImage fgbufferImage;
    private int[] fgpixels;

    // Input
    private static int mX = 0;
    private static int mY = 0;
    private static int mButton = 0;
    private static boolean mPressed;
    private static boolean[] keys = new boolean[255];
    public void processEvent(AWTEvent e) {
        switch (e.getID()) {
            case WindowEvent.WINDOW_CLOSING:
                System.exit(0);
            case MouseEvent.MOUSE_RELEASED:
                mButton = 0;
                break;
            case MouseEvent.MOUSE_PRESSED:
                mButton = ((MouseEvent) e).getButton();
            case MouseEvent.MOUSE_MOVED:
            case MouseEvent.MOUSE_DRAGGED:
                mX = (int) (((MouseEvent) e).getX()/scale);
                mY = (int) (((MouseEvent) e).getY()/scale);
                break;
            case KeyEvent.KEY_PRESSED:
            case KeyEvent.KEY_RELEASED:
                keys[((KeyEvent) e).getKeyCode()] = e.getID() == KeyEvent.KEY_PRESSED;
        }
    }

    public static void main(String[] args) throws Throwable {
        Game game = new Game();
    }

    public Game() {
        initGUI();
        initGraphics();
        initWorld();
        initPlayer();
        runGame();
    }

    public void runGame() {
        frame.setVisible(true);

        System.out.println("Game loop running...");
        long now = System.currentTimeMillis();
        keys[32] = false;

        // Run the game at 30 updates
        while (!keys[KeyEvent.VK_ESCAPE]) {
            if (keys[KeyEvent.VK_G]) {
                world = new World();
            }
            if (System.currentTimeMillis() > now + (1000 / 30)) {
                update();
                now = System.currentTimeMillis();
            }
            render();
            displayGraphics();
        }

        System.exit(0);
    }

    // draws the graphics
    private void render() {
        // clear pixels
        for (int i = 0; i < bgpixels.length; i++) {
            bgpixels[i] = 0x22222222;
        }
        for (int i = 0; i < fgpixels.length; i++) {
            fgpixels[i] = 0x22222222;
        }

        // Draw Chunks
        Chunk tlC = world.getChunkPX(((int)p.getCX() - width / 2), ((int)p.getCY() - height / 2));
        Chunk blC = world.getChunkPX(((int)p.getCX() - width / 2), ((int)p.getCY() + height / 2));
        Chunk brC = world.getChunkPX(((int)p.getCX() + width / 2), ((int)p.getCY() + height / 2));
        Chunk trC = world.getChunkPX(((int)p.getCX() + width / 2), ((int)p.getCY() - height / 2));

        tlC.draw(bgpixels, (int)p.getCX(), (int)p.getCY());
        trC.draw(bgpixels, (int)p.getCX(), (int)p.getCY());
        blC.draw(bgpixels, (int)p.getCX(), (int)p.getCY());
        brC.draw(bgpixels, (int)p.getCX(), (int)p.getCY());

        // Draw Player
        p.draw(width, height, fgpixels);



    }

    private void displayGraphics() {
        // buffering
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g =  bs.getDrawGraphics();
        g.drawImage(bgbufferImage, 0, 0, width * scale, height * scale, null);
        g.drawImage(fgbufferImage, 0, 0, width * scale, height * scale, null);
        g.dispose();
        bs.show();
    }

    private void update() {

        // Draw Tiles based on mouse input
        handleMouseInput();
        p.update(world, keys);
    }

    public void handleMouseInput() {


        int hw = width / 2;
        int hh = height / 2;
        int mX = getMouseX();
        int mY = getMouseY();
        Chunk mChunk = world.getChunkPX(mX , mY);

        if (mButton == 3) {
            for (int x=-2; x<3;x++) {
                for (int y=-2; y<3;y++) {
                    Tile mTile = world.getTilePX(mX+(8*x), mY+(8*y));
                    mTile.setType(Tile.AIR);
                    mTile.setSolid(false);
                }
            }
        }
        if (mButton == 1) {
            for (int x=-2; x<3;x++) {
                for (int y=-2; y<3;y++) {
                    Tile mTile = world.getTilePX(mX+(8*x), mY+(8*y));
                    mTile.setType(Tile.STONE);
                    mTile.setSolid(true);
                }
            }
        }

    }

    public int getMouseX(){
        return ((int)p.getCX()+(mX)-width / 2);
    }
    public int getMouseY(){
        return ((int)p.getCY()+(mY)-height / 2);
    }

    private void initPlayer() {
        p = new Player(500, 500, 0, 0);
        Tile footTile = world.getTilePX(500, 500);
        footTile.setType(Tile.AIR);
        footTile.setSolid(false);
    }
    
    private void initGUI() {
        setPreferredSize(new Dimension(width * scale, height * scale));
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("TerrariaCopy");
        frame.add(this);
        frame.pack();
        enableEvents(KeyEvent.KEY_PRESSED | KeyEvent.KEY_RELEASED | MouseEvent.MOUSE_PRESSED | MouseEvent.MOUSE_RELEASED | MouseEvent.MOUSE_DRAGGED | MouseEvent.MOUSE_MOVED);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        requestFocus();
    }
    private void initGraphics() {
        bgbufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bgpixels = ((DataBufferInt) bgbufferImage.getRaster().getDataBuffer()).getData();

        fgbufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fgpixels = ((DataBufferInt) bgbufferImage.getRaster().getDataBuffer()).getData();
    }
    private void initWorld() {
        world = new World();
    }
}
