import java.awt.*;

public class MainMenuScreen extends Panel {
    private static Window window;

    public MainMenuScreen(Window w) {
        window = w;
        setLayout(null);
        setBackground(Color.BLACK);
        setBounds(0, 0, 640, 480);

        String font = "Helvetica";

        Label title = new Label("PONG");
        title.setAlignment(Label.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font(font, Font.BOLD, 50));
        title.setBounds(170, 80, 300, 60);

        Label subtitle = new Label("Ramiyan Gangatharan");
        subtitle.setAlignment(Label.CENTER);
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font(font, Font.BOLD, 12));
        subtitle.setBounds(170, 150, 300, 20);

        Button play = new Button("Play Singleplayer");
        play.setFont(new Font(font, Font.BOLD, 20));
        play.setBackground(Color.WHITE);
        play.setForeground(Color.BLACK);
        play.setBounds(220, 250, 200, 50);

        play.addActionListener(e -> {
            window.setScreen(window.getGameScreen());
            window.getGameScreen().requestFocus();
            window.getGameScreen().startGameLoop();
        });

        add(title);
        add(subtitle);
        add(play);
    }
}
