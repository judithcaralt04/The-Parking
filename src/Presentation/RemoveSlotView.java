package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the remove slot screen.
 */
public class RemoveSlotView extends JPanel {

    public static final String BTN_REMOVE = "BTN_DO_REMOVE_SLOT";
    public static final String BTN_BACK = "BTN_BACK_REMOVE_SLOT";
    public static final String SLOT_SELECTED = "SLOT_SELECTED";

    private JComboBox<String> slotSelector;
    private JLabel infoLabel;
    private JButton removeButton;
    private JButton backButton;

    /**
     * Creates a new RemoveSlotView instance.
     *
     * @return the requested value
     */
    public RemoveSlotView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        configureTitle();
        configureContent();
        configureButtons();
    }

    /**
     * Configures title.
     */
    private void configureTitle() {
        JLabel label = new JLabel("Eliminar plaza", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(label, BorderLayout.NORTH);
    }

    /**
     * Configures content.
     */
    private void configureContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        content.add(new JLabel("Plaza a eliminar:"), gbc);
        gbc.gridx = 1;
        slotSelector = new JComboBox<>();
        slotSelector.setActionCommand(SLOT_SELECTED);
        content.add(slotSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        infoLabel = new JLabel(" ");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        content.add(infoLabel, gbc);

        add(content, BorderLayout.CENTER);
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
        removeButton = new JButton("Eliminar plaza");
        backButton = new JButton("Volver");

        removeButton.setActionCommand(BTN_REMOVE);
        backButton.setActionCommand(BTN_BACK);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60));
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        for (ActionListener al : removeButton.getActionListeners()) {
            removeButton.removeActionListener(al);
        }

        for (ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }
        for (ActionListener al : slotSelector.getActionListeners()) {
            slotSelector.removeActionListener(al);
        }
        removeButton.addActionListener(controller);
        backButton.addActionListener(controller);
        slotSelector.addActionListener(controller);
    }

    /**
     * Loads slots.
     *
     * @param slots the parking space codes to display
     */
    public void loadSlots(String[] slots) {
        slotSelector.removeAllItems();

        for (String slot : slots) {
            slotSelector.addItem(slot);
        }
    }

    /**
     * Shows slot info.
     *
     * @param text the text to draw
     */
    public void showSlotInfo(String text) {
        infoLabel.setText(text == null ? " " : text);
    }

    /**
     * Returns selected code.
     *
     * @return the requested text value
     */
    public String getSelectedCode() {
        return (String) slotSelector.getSelectedItem();
    }

    /**
     * Returns confirm remove.
     *
     * @param code the parking space code
     * @return true when the condition is met, false otherwise
     */
    public boolean confirmRemove(String code) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar la plaza " + code + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return confirm == JOptionPane.YES_OPTION;
    }

    /**
     * Shows error.
     *
     * @param message the message to display
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows success.
     *
     * @param message the message to display
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
