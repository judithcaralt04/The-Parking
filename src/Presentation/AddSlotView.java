package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the add slot screen.
 */
public class AddSlotView extends JPanel {

    public static final String BTN_CREATE = "BTN_CREATE_SLOT";
    public static final String BTN_BACK = "BTN_BACK_ADD_SLOT";

    private JTextField codeField;
    private JTextField floorField;
    private JComboBox<String> vehicleTypeCombo;

    private JButton createButton;
    private JButton backButton;

    /**
     * Creates a new AddSlotView instance.
     *
     * @return the requested value
     */
    public AddSlotView() {
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
        JLabel label = new JLabel("Crear nueva plaza", SwingConstants.CENTER);
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
        form.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        codeField = new JTextField(15);
        form.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Planta:"), gbc);
        gbc.gridx = 1;
        floorField = new JTextField(15);
        form.add(floorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Tipo de vehículo:"), gbc);
        gbc.gridx = 1;
        vehicleTypeCombo = new JComboBox<>();
        form.add(vehicleTypeCombo, gbc);

        add(form, BorderLayout.CENTER);
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
        createButton = new JButton("Crear plaza");
        backButton = new JButton("Volver");

        createButton.setActionCommand(BTN_CREATE);
        backButton.setActionCommand(BTN_BACK);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60));
        buttonPanel.add(createButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        JButton[] buttons = {createButton, backButton};

        for (JButton btn : buttons) {
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            btn.addActionListener(controller);
        }
    }

    /**
     * Returns code.
     *
     * @return the requested text value
     */
    public String getCode() {
        return codeField.getText().trim();
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
    public String getVehicleType() {
        Object selected = vehicleTypeCombo.getSelectedItem();
        return selected == null ? "" : selected.toString();
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
     * Clears fields.
     */
    public void clearFields() {
        codeField.setText("");
        floorField.setText("");
        vehicleTypeCombo.setSelectedIndex(0);
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
