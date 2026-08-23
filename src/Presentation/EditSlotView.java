package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the edit slot screen.
 */
public class EditSlotView extends JPanel {

    public static final String BTN_SAVE = "BTN_SAVE_SLOT";
    public static final String BTN_BACK = "BTN_BACK_EDIT_SLOT";
    public static final String SLOT_SELECTED = "SLOT_SELECTED";

    private JComboBox<String> slotSelector;
    private JTextField floorField;
    private JComboBox<String> vehicleTypeCombo;

    private JButton saveButton;
    private JButton backButton;

    /**
     * Creates a new EditSlotView instance.
     *
     * @return the requested value
     */
    public EditSlotView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        configureTitle();
        configureForm();
        configureButtons();
    }

    /**
     * Configures title.
     */
    private void configureTitle() {
        JLabel label = new JLabel("Editar plaza", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(label, BorderLayout.NORTH);
    }

    /**
     * Configures form.
     */
    private void configureForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Plaza a editar:"), gbc);
        gbc.gridx = 1;
        slotSelector = new JComboBox<>();
        slotSelector.setActionCommand(SLOT_SELECTED);
        form.add(slotSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Nueva planta:"), gbc);
        gbc.gridx = 1;
        floorField = new JTextField(15);
        form.add(floorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Nuevo tipo:"), gbc);
        gbc.gridx = 1;
        vehicleTypeCombo = new JComboBox<>();
        form.add(vehicleTypeCombo, gbc);

        add(form, BorderLayout.CENTER);
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
        saveButton = new JButton("Guardar cambios");
        backButton = new JButton("Volver");

        saveButton.setActionCommand(BTN_SAVE);
        backButton.setActionCommand(BTN_BACK);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60));
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        for (ActionListener al : saveButton.getActionListeners()) {
            saveButton.removeActionListener(al);
        }
        for (ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }
        for (ActionListener al : slotSelector.getActionListeners()) {
            slotSelector.removeActionListener(al);
        }
        saveButton.addActionListener(controller);
        backButton.addActionListener(controller);
        slotSelector.addActionListener(controller);
    }

    /**
     * Sets vehicle types.
     *
     * @param vehicleTypes the vehicle types value
     */
    public void setVehicleTypes(String[] vehicleTypes) {
        vehicleTypeCombo.removeAllItems();

        for (String type : vehicleTypes) {
            vehicleTypeCombo.addItem(type);
        }

        if (vehicleTypeCombo.getItemCount() > 0) {
            vehicleTypeCombo.setSelectedIndex(0);
        }
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
     * Fills fields.
     *
     * @param floor the parking floor number
     * @param vehicleType the vehicle type
     */
    public void fillFields(String floor, String vehicleType) {
        floorField.setText(floor);

        if (vehicleType != null) {
            vehicleTypeCombo.setSelectedItem(vehicleType.toUpperCase());
        }
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
     * Returns floor.
     *
     * @return the requested text value
     */
    public String getFloor() {
        return floorField.getText().trim();
    }

    /**
     * Returns vehicle type.
     *
     * @return the requested text value
     */
    public String getVehicleType()  {
        Object selected = vehicleTypeCombo.getSelectedItem();
        return selected == null ? "" : selected.toString();
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
