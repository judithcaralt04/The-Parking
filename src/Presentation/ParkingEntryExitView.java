package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the parking entry exit screen.
 */
public class ParkingEntryExitView extends JPanel {

    public static final String BTN_ENTRY = "BTN_ENTRY_VEHICLE";
    public static final String BTN_EXIT = "BTN_EXIT_VEHICLE";
    public static final String BTN_BACK = "BTN_BACK_ENTRY_EXIT";

    private JTextField licensePlateField;
    private JComboBox<String> vehicleTypeCombo;
    private JLabel vehicleTypeLabel;
    private JLabel assignedSpaceLabel;
    private JButton entryButton;
    private JButton exitButton;
    private JButton backButton;

    /**
     * Creates a new ParkingEntryExitView instance.
     *
     * @return the requested value
     */
    public ParkingEntryExitView() {
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
        JLabel label = new JLabel("Entrar / Salir del parking", SwingConstants.CENTER);
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
        form.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1;
        licensePlateField = new JTextField(15);
        form.add(licensePlateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        vehicleTypeLabel = new JLabel("Tipo de vehículo:");
        form.add(vehicleTypeLabel, gbc);
        gbc.gridx = 1;
        vehicleTypeCombo = new JComboBox<>();
        form.add(vehicleTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        assignedSpaceLabel = new JLabel(" ");
        assignedSpaceLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        assignedSpaceLabel.setForeground(new Color(0, 120, 0));
        assignedSpaceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        form.add(assignedSpaceLabel, gbc);

        add(form, BorderLayout.CENTER);
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
        entryButton = new JButton("Registrar entrada");
        exitButton = new JButton("Registrar salida");
        backButton = new JButton("Volver al menú");

        entryButton.setActionCommand(BTN_ENTRY);
        exitButton.setActionCommand(BTN_EXIT);
        backButton.setActionCommand(BTN_BACK);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60));
        buttonPanel.add(entryButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        JButton[] buttons = {entryButton, exitButton, backButton};
        for (JButton btn : buttons) {
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            btn.addActionListener(controller);
        }
    }

    /**
     * Returns license plate.
     *
     * @return the requested text value
     */
    public String getLicensePlate() {
        return licensePlateField.getText().trim().toUpperCase();
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
     * Shows assigned space.
     *
     * @param spaceCode the parking space code
     */
    public void showAssignedSpace(String spaceCode) {
        if (spaceCode != null) {
            assignedSpaceLabel.setText("Plaza asignada: " + spaceCode);
        } else {
            assignedSpaceLabel.setText(" ");
        }
    }

    /**
     * Clears fields.
     */
    public void clearFields() {
        licensePlateField.setText("");
        vehicleTypeCombo.setSelectedIndex(0);
        assignedSpaceLabel.setText(" ");
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
