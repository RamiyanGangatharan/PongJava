import java.awt.*;

public class Paddle {
    private int PositionX;
    private int PositionY;
    private int PaddleWidth;
    private int PaddleHeight;
    private int PaddleSpeed;

    public int getPositionX() { return PositionX; }
    public int getPositionY() { return PositionY; }
    public int getPaddleWidth() { return PaddleWidth; }
    public int getPaddleHeight() { return PaddleHeight; }
    public int getPaddleSpeed() { return PaddleSpeed; }

    public void setPositionX(int positionX) { PositionX = positionX; }
    public void setPositionY(int positionY) { PositionY = positionY; }
    public void setPaddleWidth(int paddleWidth) { PaddleWidth = paddleWidth; }
    public void setPaddleHeight(int paddleHeight) { PaddleHeight = paddleHeight; }
    public void setPaddleSpeed(int paddleSpeed) { PaddleSpeed = paddleSpeed; }

    public Paddle(int PositionX, int PositionY, int PaddleWidth, int BallHeight, int PaddleSpeed) {
        this.PositionX = PositionX;
        this.PositionY = PositionY;
        this.PaddleWidth = PaddleWidth;
        this.PaddleHeight = BallHeight;
        this.PaddleSpeed = PaddleSpeed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(PositionX, PositionY, PaddleWidth, PaddleHeight);
    }

    public void moveUp() { if (PositionY > 30) PositionY -= PaddleSpeed; }
    public void moveDown(int screenHeight) { if (PositionY + PaddleHeight < screenHeight) { PositionY += PaddleSpeed; }}
}