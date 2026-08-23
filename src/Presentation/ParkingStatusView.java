package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * Swing view that renders the parking status screen.
 */
public class ParkingStatusView extends JPanel {

    public static final String BTN_BACK = "BTN_BACK_STATUS";

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton backButton;

    /**
     * Creates a new ParkingStatusView instance.
     *
     * @return the requested value
     */
    public ParkingStatusView() {
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
        JLabel label = new JLabel("Estado actual del parking", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(label, BorderLayout.NORTH);
    }

    /**
     * Configures table.
     */
    private void configureTable() {
        String[] columns = {"Codigo", "Planta", "Estado", "Reserva", "Matricula aparcada", "Matricula reservada"};
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

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
        backButton = new JButton("Volver al menu");
        backButton.setActionCommand(BTN_BACK);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 20, 60));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets rows.
     *
     * @param rows the table rows to display
     */
    public void setRows(Object[][] rows) {
        tableModel.setRowCount(0);

        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    /**
     * Returns selected code.
     *
     * @return the requested text value
     */
    public String getSelectedCode() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            return null;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        return tableModel.getValueAt(modelRow, 0).toString();
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        for (ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }
        backButton.addActionListener(controller);
    }

    /**
     * Registers table mouse listener.
     *
     * @param listener the mouse listener that handles table actions
     */
    public void registerTableMouseListener(MouseListener listener) {
        table.addMouseListener(listener);
    }

    /**
     * Shows info.
     *
     * @param message the message to display
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Detalle de plaza", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows admin detail.
     *
     * @param message the message to display
     * @param hasReservation the has reservation value
     * @return the calculated numeric value
     */
    public int showAdminDetail(String message, boolean hasReservation) {
        if (!hasReservation) {
            showInfo(message);
            return JOptionPane.CLOSED_OPTION;
        }
        Object[] options = {"Cancelar reserva", "Cerrar"};
        return JOptionPane.showOptionDialog(this, message, "Detalle de plaza", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
    }

    /**
     * Shows error.
     *
     * @param message the message to display
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
