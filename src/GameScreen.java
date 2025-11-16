import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameScreen extends Canvas implements Runnable, KeyListener {

    private static final int TARGET_FPS = 60;

    public ArrayList<Wall> Walls = new ArrayList<Wall>();
    public ArrayList<Paddle> Paddles = new ArrayList<Paddle>();

    private final Paddle paddle;
    private final Ball ball;

    private volatile boolean running = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    private Image backBuffer;
    private Graphics backG;

    public GameScreen(Window window) {
        setBackground(Color.BLACK);
        setSize(window.getWidth(), window.getHeight());

        Wall leftWall = new Wall(5, 0, 10, getHeight() - 10);
        Wall rightWall = new Wall(getWidth() - 15, 9, 10, getHeight() - 10);
        Wall topWall = new Wall(0, getHeight() - 450, getWidth(), 10);
        Wall bottomWall = new Wall(0, getHeight() - 10, getWidth(), 10);

        Walls.add(leftWall);
        Walls.add(rightWall);
        Walls.add(topWall);
        Walls.add(bottomWall);

        paddle = new Paddle(20, 50, 10, 100, 5);
        Paddles.add(paddle);

        ball = new Ball(getWidth() - 100, getHeight() - 30, 10, Color.WHITE, 3, 3);

        addKeyListener(this);
        setFocusable(true);
        requestFocus();
    }

    public void startGameLoop() {
        running = true;
        new Thread(this).start();
    }

    @Override public void update(Graphics g) { paint(g); }

    @Override public void paint(Graphics g) {
        if (backBuffer == null) {
            backBuffer = createImage(getWidth(), getHeight());
            backG = backBuffer.getGraphics();
        }

        backG.setColor(Color.BLACK);
        backG.fillRect(0, 0, getWidth(), getHeight());

        for (Wall wall : Walls) { wall.draw(backG); }
        for (Paddle paddle : Paddles) { paddle.draw(backG); }

        ball.draw(backG);

        FPScounter.render(backG, getWidth());

        g.drawImage(backBuffer, 0, 0, this);
    }

    @Override public void run() {
        while (running) {
            if (upPressed) paddle.moveUp();
            if (downPressed) paddle.moveDown(getHeight());

            FPScounter.update();
            ball.update();
            CollisionDetection.check(ball, Walls, Paddles);
            repaint();

            try { Thread.sleep(1000 / TARGET_FPS); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) upPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) downPressed = true;
    }
    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) upPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) downPressed = false;
    }
    @Override public void keyTyped(KeyEvent e) {}
}
