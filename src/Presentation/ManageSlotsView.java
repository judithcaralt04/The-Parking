package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the manage slots screen.
 */
public class ManageSlotsView extends JPanel {

    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton backButton;

    public static final String BTN_ADD = "BTN_ADD_SLOT";
    public static final String BTN_EDIT = "BTN_EDIT_SLOT";
    public static final String BTN_REMOVE = "BTN_REMOVE_SLOT";
    public static final String BTN_BACK = "BTN_BACK_MENU";

    /**
     * Creates a new ManageSlotsView instance.
     *
     * @return the requested value
     */
    public ManageSlotsView() {
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
        JLabel label = new JLabel("Gestión de plazas", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(label, BorderLayout.NORTH);
    }

    /**
     * Configures botones.
     */
    private void configureBotones() {
        addButton = new JButton("Crear plaza");
        editButton = new JButton("Editar plaza");
        removeButton = new JButton("Eliminar plaza");
        backButton = new JButton("Volver al menú");

        addButton.setActionCommand(BTN_ADD);
        editButton.setActionCommand(BTN_EDIT);
        removeButton.setActionCommand(BTN_REMOVE);
        backButton.setActionCommand(BTN_BACK);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        add(buttonPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 20, 60));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        JButton[] buttons = {addButton, editButton, removeButton, backButton};

        for (JButton btn : buttons) {
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            btn.addActionListener(controller);
        }
    }
}
