package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the manage bookings screen.
 */
public class ManageBookingsView extends JPanel {

    public static final String BTN_REFRESH = "BTN_REFRESH_BOOKINGS";
    public static final String BTN_USER_INFO = "BTN_USER_INFO";
    public static final String BTN_CANCEL = "BTN_CANCEL_BOOKING_ADMIN";
    public static final String BTN_BACK = "BTN_BACK_MANAGE_BOOKINGS";

    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JLabel summaryLabel;

    private JButton refreshButton;
    private JButton userInfoButton;
    private JButton cancelButton;
    private JButton backButton;

    /**
     * Creates a new ManageBookingsView instance.
     *
     * @return the requested value
     */
    public ManageBookingsView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        configureTitle();
        configureTable();
        configureButtons();
    }

    /**
     * Configures title.
     */
    private void configureTitle() {
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel label = new JLabel("Gestión de reservas", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        summaryLabel = new JLabel("0 reservas activas", SwingConstants.CENTER);
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        titlePanel.add(label);
        titlePanel.add(summaryLabel);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Configures table.
     */
    private void configureTable() {
        String[] columns = {"ID", "Plaza", "Planta", "Tipo", "Matrícula", "Usuario", "Fecha reserva"};
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

        bookingsTable = new JTable(tableModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.setRowHeight(24);
        bookingsTable.getTableHeader().setReorderingAllowed(false);

        bookingsTable.getColumnModel().getColumn(0).setMinWidth(0);
        bookingsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        bookingsTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
        refreshButton = new JButton("Refrescar");
        userInfoButton = new JButton("Ver información del usuario");
        cancelButton = new JButton("Cancelar reserva seleccionada");
        backButton = new JButton("Volver al menú");

        refreshButton.setActionCommand(BTN_REFRESH);
        userInfoButton.setActionCommand(BTN_USER_INFO);
        cancelButton.setActionCommand(BTN_CANCEL);
        backButton.setActionCommand(BTN_BACK);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 8));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 20, 60));
        buttonPanel.add(refreshButton);
        buttonPanel.add(userInfoButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        JButton[] buttons = {refreshButton, userInfoButton, cancelButton, backButton};

        for (JButton btn : buttons) {
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            btn.addActionListener(controller);
        }
    }

    /**
     * Loads bookings.
     *
     * @param rows the table rows to display
     */
    public void loadBookings(Object[][] rows) {
        tableModel.setRowCount(0);

        for (Object[] row : rows) {
            tableModel.addRow(row);
        }

        summaryLabel.setText(rows.length + " reservas activas");
    }

    /**
     * Returns selected reservation id.
     *
     * @return the calculated numeric value
     */
    public int getSelectedReservationId() {
        int row = bookingsTable.getSelectedRow();
        if (row < 0) {
            return -1;
        }
        int modelRow = bookingsTable.convertRowIndexToModel(row);
        return (int) tableModel.getValueAt(modelRow, 0);
    }

    /**
     * Returns selected space code.
     *
     * @return the requested text value
     */
    public String getSelectedSpaceCode() {
        int row = bookingsTable.getSelectedRow();
        if (row < 0) {
            return null;
        }
        int modelRow = bookingsTable.convertRowIndexToModel(row);
        return (String) tableModel.getValueAt(modelRow, 1);
    }

    /**
     * Shows info.
     *
     * @param message the message to display
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
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

    /**
     * Returns confirm.
     *
     * @param message the message to display
     * @return true when the condition is met, false otherwise
     */
    public boolean confirm(String message) {
        int opt = JOptionPane.showConfirmDialog(this, message, "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return opt == JOptionPane.YES_OPTION;
    }
}
