import java.awt.*;
import java.awt.event.*;

public class Window extends Frame {
    private final GameScreen gameScreen;

    public Window(String title, int width, int height) {
        super(title);

        setSize(width, height);
        setResizable(false);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setLayout(null);

        gameScreen = new GameScreen(this); // Pass the window itself to GameScreen

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }

    public void setScreen(Component screen) {
        removeAll();
        screen.setBounds(0, 0, getWidth(), getHeight());
        add(screen);
        screen.requestFocus();
        validate();
        repaint();
    }

    public GameScreen getGameScreen() { return gameScreen; }
    public MainMenuScreen getMainMenuScreen() { return new MainMenuScreen(this); }
}
