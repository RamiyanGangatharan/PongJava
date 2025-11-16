import java.awt.*;

public class FPScounter {
    private static long lastTime = System.currentTimeMillis();
    private static int frames = 0;
    private static int fps = 0;

    public static void update() {
        frames++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= 1000) {
            fps = frames;
            frames = 0;
            lastTime = currentTime;
        }
    }

    public static void render(Graphics g, int screenWidth) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String fpsText = "FPS: " + fps;
        int textWidth = g.getFontMetrics().stringWidth(fpsText);
        g.drawString(fpsText, screenWidth - textWidth - 10, 20);
    }
}
