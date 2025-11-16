import java.awt.*;

public class Ball {
    private double PositionX;
    private double PositionY;
    private double radius;
    private double VelocityX;
    private double VelocityY;
    private Color color;

    public double getPositionX() { return PositionX; }
    public double getPositionY() { return PositionY; }
    public double getVelocityX() { return VelocityX; }
    public double getVelocityY() { return VelocityY; }
    public double getRadius() { return radius; }
    public Color getColor() { return color; }

    public void setPositionX(double positionX) { PositionX = positionX; }
    public void setPositionY(double positionY) { PositionY = positionY; }
    public void setVelocityX(double velocityX) { VelocityX = velocityX; }
    public void setVelocityY(double velocityY) { VelocityY = velocityY; }
    public void setRadius(double radius) { this.radius = radius; }
    public void setColor(Color color) { this.color = color; }

    public Ball(double PositionX, double PositionY, double radius, Color color, double VelocityX, double VelocityY)
    {
        this.PositionX = PositionX;
        this.PositionY = PositionY;
        this.radius = radius;
        this.color = color;
        this.VelocityX = VelocityX;
        this.VelocityY = VelocityY;
    }
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)(PositionX - radius),(int)(PositionY - radius),(int)(2 * radius),(int)(2 * radius));
    }

    public void update() {
        PositionX += VelocityX;
        PositionY += VelocityY;
    }
}