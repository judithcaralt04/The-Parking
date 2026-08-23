package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the menu screen.
 */
public class MenuView extends JPanel {

    public static final String BTN_STATUS           = "BTN_STATUS";
    public static final String BTN_OCCUPANCY        = "BTN_OCCUPANCY";
    public static final String BTN_LOGOUT           = "BTN_LOGOUT";
    public static final String BTN_ENTRY            = "BTN_ENTRY";
    public static final String BTN_BOOKING          = "BTN_BOOKING";
    public static final String BTN_MANAGE_SLOTS     = "BTN_MANAGE_SLOTS";
    public static final String BTN_MANAGE_BOOKINGS  = "BTN_MANAGE_BOOKINGS";

    private JLabel welcomeLabel;
    private JButton statusButton;
    private JButton occupancyButton;
    private JButton logoutButton;
    private JButton entryButton;
    private JButton bookingButton;
    private JButton manageSlotsButton;
    private JButton manageBookingsButton;

    /**
     * Creates a new MenuView instance.
     *
     * @return the requested value
     */
    public MenuView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        addTitle();
        addButtons();
    }

    /**
     * Adds title.
     */
    private void addTitle() {
        welcomeLabel = new JLabel("Bienvenido", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(welcomeLabel, BorderLayout.NORTH);
    }

    /**
     * Adds buttons.
     */
    private void addButtons() {
        statusButton = new JButton("Estado del parking");
        occupancyButton = new JButton("Gráfico de ocupación");
        logoutButton = new JButton("Cerrar sesión");
        entryButton = new JButton("Entrar / Salir parking");
        bookingButton = new JButton("Mis reservas");
        manageSlotsButton = new JButton("Gestionar plazas");
        manageBookingsButton = new JButton("Gestionar reservas");

        statusButton.setActionCommand(BTN_STATUS);
        occupancyButton.setActionCommand(BTN_OCCUPANCY);
        logoutButton.setActionCommand(BTN_LOGOUT);
        entryButton.setActionCommand(BTN_ENTRY);
        bookingButton.setActionCommand(BTN_BOOKING);
        manageSlotsButton.setActionCommand(BTN_MANAGE_SLOTS);
        manageBookingsButton.setActionCommand(BTN_MANAGE_BOOKINGS);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));
        buttonPanel.add(entryButton);
        buttonPanel.add(bookingButton);
        buttonPanel.add(manageSlotsButton);
        buttonPanel.add(manageBookingsButton);
        buttonPanel.add(statusButton);
        buttonPanel.add(occupancyButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        JButton[] buttons = {statusButton, occupancyButton, logoutButton, entryButton, bookingButton, manageSlotsButton, manageBookingsButton};
        for (JButton btn : buttons) {
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            btn.addActionListener(controller);
        }
    }

    /**
     * Shows user menu.
     *
     * @param username the username used to identify the user
     */
    public void showUserMenu(String username) {
        welcomeLabel.setText("Bienvenido, " + username);
        entryButton.setVisible(true);
        bookingButton.setVisible(true);
        manageSlotsButton.setVisible(false);
        manageBookingsButton.setVisible(false);
    }

    /**
     * Shows admin menu.
     *
     * @param username the username used to identify the user
     */
    public void showAdminMenu(String username) {
        welcomeLabel.setText("Bienvenido, Admin " + username);
        entryButton.setVisible(false);
        bookingButton.setVisible(false);
        manageSlotsButton.setVisible(true);
        manageBookingsButton.setVisible(true);
    }
}