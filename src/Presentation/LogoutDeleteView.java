package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the logout delete screen.
 */
public class LogoutDeleteView extends JPanel {

    private JButton logoutButton;
    private JButton deleteButton;

    public static final String BTN_LOGOUT = "BTN_LOGOUT_CONFIRM";
    public static final String BTN_DELETE = "BTN_DELETE_ACCOUNT";

    /**
     * Creates a new LogoutDeleteView instance.
     *
     * @return the requested value
     */
    public LogoutDeleteView() {
        configurePanel();
        configureTitulo();
        configureBotones();
    }

    /**
     * Configures panel.
     */
    private void configurePanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
    }

    /**
     * Configures titulo.
     */
    private void configureTitulo() {
        JLabel label = new JLabel("¿Qué deseas hacer?", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(label, BorderLayout.NORTH);
    }

    /**
     * Configures botones.
     */
    private void configureBotones() {
        logoutButton = new JButton("Cerrar sesión");
        deleteButton = new JButton("Borrar cuenta");

        logoutButton.setActionCommand(BTN_LOGOUT);
        deleteButton.setActionCommand(BTN_DELETE);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        buttonPanel.add(logoutButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        for (ActionListener al : logoutButton.getActionListeners()) {
            logoutButton.removeActionListener(al);
        }

        for (ActionListener al : deleteButton.getActionListeners()) {
            deleteButton.removeActionListener(al);
        }
        logoutButton.addActionListener(controller);
        deleteButton.addActionListener(controller);
    }

    /**
     * Sets delete visible.
     *
     * @param visible true to make the component visible, false otherwise
     */
    public void setDeleteVisible(boolean visible) {
        deleteButton.setVisible(visible);
    }
}
