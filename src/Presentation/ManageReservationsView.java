package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the manage reservations screen.
 */
public class ManageReservationsView extends JPanel {

    public static final String BTN_BOOK = "BTN_BOOK_RESERVATION";
    public static final String BTN_CANCEL = "BTN_CANCEL_RESERVATION";
    public static final String BTN_BACK = "BTN_BACK_RESERVATIONS";
    public static final String COMBO_TYPE = "COMBO_VEHICLE_TYPE_CHANGED";

    private JTable reservationsTable;
    private DefaultTableModel tableModel;

    private JTextField licensePlateField;
    private JComboBox<String> vehicleTypeCombo;
    private JComboBox<String> spaceCombo;

    private JButton bookButton;
    private JButton cancelButton;
    private JButton backButton;

    /**
     * Creates a new ManageReservationsView instance.
     *
     * @return the requested value
     */
    public ManageReservationsView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        configureTitle();
        configureTable();
        configureForm();
        configureButtons();
    }

    /**
     * Configures title.
     */
    private void configureTitle() {
        JLabel label = new JLabel("Mis reservas", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(label, BorderLayout.NORTH);
    }

    /**
     * Configures table.
     */
    private void configureTable() {
        String[] columns = {"ID Reserva", "Plaza", "Matrícula", "Tipo", "Fecha"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override

            /**
             * Checks whether cell editable.
             *
             * @param row the table row index
             * @param column the table column index
             * @return true when the condition is met, false otherwise
             */
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationsTable = new JTable(tableModel);
        reservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationsTable.setRowHeight(22);
        reservationsTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        scrollPane.setPreferredSize(new Dimension(0, 130));
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(8, 25, 4, 25), "Reservas activas"));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Configures form.
     */
    private void configureForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(4, 25, 4, 25), "Nueva reserva"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        form.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        licensePlateField = new JTextField(15);
        form.add(licensePlateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        form.add(new JLabel("Tipo de vehículo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        vehicleTypeCombo = new JComboBox<>();
        vehicleTypeCombo.setActionCommand(COMBO_TYPE);
        form.add(vehicleTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        form.add(new JLabel("Plaza disponible:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        spaceCombo = new JComboBox<>();
        form.add(spaceCombo, gbc);

        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.setBackground(Color.WHITE);
        southWrapper.add(form, BorderLayout.CENTER);
        southWrapper.add(buildButtonPanel(), BorderLayout.SOUTH);
        add(southWrapper, BorderLayout.SOUTH);
    }

    /**
     * Builds button panel.
     *
     * @return the configured panel
     */
    private JPanel buildButtonPanel() {
        bookButton = new JButton("Reservar plaza");
        cancelButton = new JButton("Cancelar reserva seleccionada");
        backButton = new JButton("Volver al menú");

        bookButton.setActionCommand(BTN_BOOK);
        cancelButton.setActionCommand(BTN_CANCEL);
        backButton.setActionCommand(BTN_BACK);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 60, 20, 60));
        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        JButton[] buttons = {bookButton, cancelButton, backButton};
        for (JButton btn : buttons) {
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            btn.addActionListener(controller);
        }
        for (ActionListener al : vehicleTypeCombo.getActionListeners()) {
            vehicleTypeCombo.removeActionListener(al);
        }
        vehicleTypeCombo.addActionListener(controller);
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
     * Loads reservations.
     *
     * @param rows the table rows to display
     */
    public void loadReservations(Object[][] rows) {
        tableModel.setRowCount(0);

        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    /**
     * Loads available spaces.
     *
     * @param spaces the parking spaces to process
     */
    public void loadAvailableSpaces(String[] spaces) {
        spaceCombo.removeAllItems();

        for (String space : spaces) {
            spaceCombo.addItem(space);
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
    public String getVehicleType() {
        Object selected = vehicleTypeCombo.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    /**
     * Returns selected space code.
     *
     * @return the requested text value
     */
    public String getSelectedSpaceCode() {
        String item = (String) spaceCombo.getSelectedItem();
        if (item == null || item.isBlank()) {
            return null;
        }
        return item.split(" ")[0];
    }

    /**
     * Returns selected reservation id.
     *
     * @return the calculated numeric value
     */
    public int getSelectedReservationId() {
        int row = reservationsTable.getSelectedRow();
        if (row < 0) {
            return -1;
        }
        return (int) tableModel.getValueAt(
                reservationsTable.convertRowIndexToModel(row), 0);
    }

    /**
     * Clears form.
     */
    public void clearForm() {
        licensePlateField.setText("");
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
