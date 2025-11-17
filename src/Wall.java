import java.awt.*;

public class Wall {
    private int PositionX;
    private int PositionY;
    private int WallWidth;
    private int WallHeight;

    private boolean ignoreCollision;

    public boolean isIgnoreCollision() { return ignoreCollision; }
    public void setIgnoreCollision(boolean ignoreCollision) { this.ignoreCollision = ignoreCollision; }

    public int getPositionX() { return PositionX; }
    public int getPositionY() { return PositionY; }
    public int getWallWidth() { return WallWidth; }
    public int getWallHeight() { return WallHeight; }

    public void setPositionX(int positionX) { PositionX = positionX; }
    public void setPositionY(int positionY) { PositionY = positionY; }
    public void setWallWidth(int wallWidth) { WallWidth = wallWidth; }
    public void setWallHeight(int wallHeight) { WallHeight = wallHeight; }

    public Wall(int positionX, int positionY, int wallWidth, int wallHeight) {
        this.PositionX = positionX;
        this.PositionY = positionY;
        this.WallWidth = wallWidth;
        this.WallHeight = wallHeight;
    }


    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(PositionX, PositionY, WallWidth, WallHeight);
    }
}