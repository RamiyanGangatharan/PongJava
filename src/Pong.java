import java.awt.*;
import java.awt.event.*;

/**
 * This project, Pong (AWT Edition) by Ramiyan Gangatharan, is a low-level Java implementation of Pong using only AWT.
 * Its purpose is to explore manual rendering, window management, and basic game architecture without relying on Swing
 * or external frameworks. The UI is constructed using Panels, Labels, and Buttons, with future plans to implement
 * Canvas-based rendering and a custom game loop.
 */
public class Pong {
    public static void main(String[] args) {
        Window window = new Window("Pong", 640, 480);
        MainMenuScreen mainMenu = new MainMenuScreen(window);
        window.setScreen(mainMenu);
        window.setVisible(true);
    }

    /**
     * Window class wraps an AWT Frame and allows swapping screens easily.
     */
    public static class Window extends Frame {

        public GameScreen gameScreen;
        /**
         * Constructor, builds the window.
         *
         * @param title title of the window
         * @param width width of the window in pixels
         * @param height height of the window in pixels
         */
        public Window(String title, int width, int height) {
            super(title);

            setSize(width, height);
            setResizable(false);
            setBackground(Color.BLACK);
            setLocationRelativeTo(null);
            setLayout(null);

            gameScreen = new GameScreen();

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) { System.exit(0); }
            });
        }

        /**
         * Replaces whatever is in the window with a new component (screen).
         *
         * @param screen the new UI screen to display
         */
        public void setScreen(Component screen) {
            removeAll();
            screen.setBounds(0, 0, getWidth(), getHeight());
            add(screen);
            screen.requestFocus();
            validate();
            repaint();
        }
    }

    // -------------------------- MAIN MENU SCREEN -------------------------------------

    /**
     * This class is responsible for creating the main menu screen where it has code for GUI and functionality
     */
    public static class MainMenuScreen extends Panel {

        private static Pong.Window window = null;

        /**
         * Constructor, this is the starting main menu screen.
         */
        public MainMenuScreen(Pong.Window window) {
            MainMenuScreen.window = window;
            setLayout(null);
            setBackground(Color.BLACK);

            setBounds(0, 0, 640, 480);

            String font = "Helvetica";

            // TITLE
            Label title = new Label("PONG");
            title.setAlignment(Label.CENTER);
            title.setForeground(Color.WHITE);
            title.setFont(new Font(font, Font.BOLD, 50));
            title.setBounds(170, 80, 300, 60);

            // SUBTITLE
            Label subtitle = new Label("Ramiyan Gangatharan");
            subtitle.setAlignment(Label.CENTER);
            subtitle.setForeground(Color.WHITE);
            subtitle.setFont(new Font(font, Font.BOLD, 12));
            subtitle.setBounds(170, 150, 300, 20);

            // PLAY BUTTON
            Button play = getButton(font);

            add(title);
            add(subtitle);
            add(play);
        }

        private static Button getButton(String font) {
            Button play = new Button("Play");
            play.setFont(new Font(font, Font.BOLD, 20));
            play.setBackground(Color.WHITE);
            play.setForeground(Color.BLACK);
            play.setBounds(220, 250, 200, 50);

            play.addActionListener(e -> {
                window.setScreen(window.gameScreen);
                window.gameScreen.requestFocus();
                window.gameScreen.startGameLoop();
            });
            return play;
        }
    }

    // -------------------------- MAIN GAME SCREEN -------------------------------------

    public static class FPScounter {
        private static long lastTime = System.currentTimeMillis();
        private static int frames = 0;
        private static int fps = 0;

        // Call at the start of each frame
        public static void update() {
            frames++;
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 1000) { // 1 second elapsed
                fps = frames;
                frames = 0;
                lastTime = currentTime;
            }
        }

        // Call in your rendering method to draw FPS
        public static void render(Graphics g, int screenWidth) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            String fpsText = "FPS: " + fps;
            int textWidth = g.getFontMetrics().stringWidth(fpsText);
            g.drawString(fpsText, screenWidth - textWidth - 60, 80); // 60 px from right, 80 px from top
        }
    }

    public static class GameScreen extends Canvas implements Runnable, KeyListener {

        private static final int TARGET_FPS = 60;

        private final Paddle paddle;
        private final Ball ball;
        private volatile boolean running = false;

        private boolean upPressed = false;
        private boolean downPressed = false;

        // --- Double Buffering (smoother game movement) ---
        private Image backBuffer;
        private Graphics backG;

        public GameScreen() {
            setBackground(Color.BLACK);
            setSize(640, 480);

            paddle = new Paddle();
            ball = new Ball();

            addKeyListener(this);
            setFocusable(true);
            requestFocus();
        }

        // Start the game loop
        public void startGameLoop() {
            running = true;
            Thread gameThread = new Thread(this);
            gameThread.start();
        }

        // Override update to avoid built-in clear & flicker
        @Override public void update(Graphics g) { paint(g); }

        @Override public void paint(Graphics g) {
            // Initialize buffer once
            if (backBuffer == null) {
                backBuffer = createImage(getWidth(), getHeight());
                backG = backBuffer.getGraphics();
            }

            // ---- Clear frame ----
            backG.setColor(Color.BLACK);
            backG.fillRect(0, 0, getWidth(), getHeight());

            // ---- Draw game elements onto buffer ----
            paddle.draw(backG);
            ball.draw(backG);

            // ---- Draw FPS onto buffer ----
            FPScounter.render(backG, getWidth());

            // ---- Draw buffer to screen ----
            g.drawImage(backBuffer, 0, 0, this);
        }

        // Game Loop
        @Override
        public void run() {
            while (running) {
                updateGame();
                repaint();
                FPScounter.update();

                try { Thread.sleep(1000 / TARGET_FPS); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }

        // Game logic
        public void updateGame() {
            if (upPressed) paddle.moveUp();
            if (downPressed) paddle.moveDown(getHeight());
        }

        // ====================================================
        public static class Paddle {
            int x = 20;
            int y = 50;
            int width = 10;
            int height = 100;
            int speed = 5;

            public void draw(Graphics g) {
                g.setColor(Color.RED);
                g.fillRect(x, y, width, height);
            }

            public void moveUp() {
                y -= speed;
                if (y < 0) y = 0;
            }

            public void moveDown(int screenHeight) {
                if (y + height < screenHeight) y += speed;
            }
        }

        // Key handling
        @Override public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP)    { upPressed = true; }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) { downPressed = true; }
        }

        @Override public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP)    { upPressed = false; }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) { downPressed = false; }
        }

        @Override public void keyTyped(KeyEvent e) {}

        public static class Ball {
            int x = 200;
            int y = 50;
            int width = 10;
            int height = 10;

            public void draw(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillOval(x, y, width, height);
            }
        }
    }
}
