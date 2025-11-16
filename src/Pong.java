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
}
