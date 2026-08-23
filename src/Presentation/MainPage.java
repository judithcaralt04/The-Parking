package Presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Presentation component for the main page screen.
 */
public class MainPage extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    /**
     * Creates a new MainPage instance.
     *
     * @return the requested value
     */
    public MainPage() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        add(mainPanel);
        setTitle("The Parking");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Adds card.
     *
     * @param panel the panel to add to the card layout
     * @param name the card name used by the layout
     */
    public void addCard(JPanel panel, String name) {
        mainPanel.add(panel, name);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Shows card.
     *
     * @param name the card name used by the layout
     */
    public void showCard(String name) {
        cardLayout.show(mainPanel, name);
    }
}
