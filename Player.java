import java.lang.Math;
import java.awt.event.KeyEvent;

public class Player {

	private boolean grounded;
    private double x;
    private double y;
    private double cx;
    private double cy;
    private int xDir;
    private double footDrawOffset;
    private double xv;
    private double yv;
    private GameImage sprite = new GameImage("images/playersprite.png");
    private GameImage jumpsprite = new GameImage("images/playerspritejump.png");

    public Player(double x, double y, double xv, double yv) {
        this.x = x;
        this.cx = x;
        this.y = y;
        this.cy = y;
        this.xv = xv;
        this.yv = yv;
        footDrawOffset = 0;
    }

    public void update(World world, boolean[] keys) {


		grounded = world.getTilePX((int)x, (int)y + 1).isSolid();
        // keyboard input
        if (keys[KeyEvent.VK_SPACE] & grounded) {
            yv = -6;
        }
        if (keys[KeyEvent.VK_A]) {
            footDrawOffset+=0.4;
            footDrawOffset%=3.0;
            if (xv > -3) xv -= 1;
        }
        if (keys[KeyEvent.VK_D]) {
            footDrawOffset+=0.4;
            footDrawOffset%=3.0;
            if (xv < 3) xv += 1;
        }
        if (!keys[KeyEvent.VK_A] & !keys[KeyEvent.VK_D]) xv = 0;

        x += xv;
        y += yv;

        // hit detection
        boolean midright = world.getTilePX((int)x+4, (int)y - 9).isSolid();
        boolean topright = world.getTilePX((int)x+4, (int)y - sprite.height).isSolid();
        boolean midleft = world.getTilePX((int)x-4, (int)y - 9).isSolid();
        boolean topleft = world.getTilePX((int)x-4, (int)y - sprite.height).isSolid();
        if (topright || midright) {
            xv = 0;
            x--;
        }
        if (topleft || midleft) {
            xv = 0;
            x++;
        }


        while (world.getTilePX((int)x, (int)y-1).isSolid()) {
            y--;
            yv=0;
        }
        while (world.getTilePX((int)x, (int)y-sprite.height).isSolid()) {
            y++;
            yv=0;
        }

        if (!world.getTilePX((int)x, (int)y + 1).isSolid()) {
            if (yv < 8) {
                yv += 1;
            }
        }




        double dx = x-cx;
        double dy = y-cy;
        cx += dx * 0.1;
        cy += dy * 0.1;

        int speed = 30;
        if (keys[KeyEvent.VK_UP]) cy -= speed;
        if (keys[KeyEvent.VK_DOWN]) cy += speed;
        if (keys[KeyEvent.VK_LEFT]) cx -= speed;
        if (keys[KeyEvent.VK_RIGHT]) cx += speed;
        if (keys[KeyEvent.VK_UP]) y -= speed;
        if (keys[KeyEvent.VK_DOWN]) y += speed;
        if (keys[KeyEvent.VK_LEFT]) x -= speed;
        if (keys[KeyEvent.VK_RIGHT]) x += speed;

    }

    public void draw(int width, int height, int[] fgpixels){
        
		if (xv<0) xDir = -1;
        if (xv>0) xDir = 1;

        double xO = -getDrawOffsetX();
        double yO = -getDrawOffsetY();
        if (grounded) {
			int i = 0;
        	for (int y = -sprite.height; y < 0; y++) {
            	for (int x = -sprite.width/2; x < sprite.width/2; x++) {
                	double xp = ((width / 2)+(x*xDir)+xO);
                	double yp = ((height / 2)+y+yO);
                	if (y > -3) xp = ((width / 2)+((x-1+(int)footDrawOffset)*xDir)+xO);
                	if (sprite.pixels[i]<0) fgpixels[(int)(xp) + (int)(yp) * width] = sprite.pixels[i];
                	i++;
            	}
        	}
		}else {
			int i = 0;
        	for (int y = -jumpsprite.height; y < 0; y++) {
            	for (int x = -jumpsprite.width/2; x < jumpsprite.width/2; x++) {
                	double xp = ((width / 2)+(x*xDir)+xO);
                	double yp = ((height / 2)+y+yO);
                	if (jumpsprite.pixels[i]<0) fgpixels[(int)(xp) + (int)(yp) * width] = jumpsprite.pixels[i];
                	i++;
            	}
        	}
		}
    }

    public double getDrawOffsetX() {
        return cx-x;
    }

    public double getDrawOffsetY() {
        return cy-y;
    }

    public GameImage getSprite() {
        return sprite;
    }

    public double getX() {
        return x;
    }

    public double getXv() {
        return xv;
    }

    public double getY() {
        return y;
    }

    public double getYv() {
        return yv;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setXv(double xv) {
        this.xv = xv;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setYv(double yv) {
        this.yv = yv;
    }

    public double getCX() {
        return cx;
    }

    public double getCY() {
        return cy;
    }
}
