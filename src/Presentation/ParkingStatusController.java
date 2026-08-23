package Presentation;

import Business.BusinessLogicParkingSpace;
import Business.ParkingStatusRow;
import Business.BusinessLogicReservation;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controller that handles user actions for the parking status screen.
 */
public class ParkingStatusController implements ActionListener {

    private final MainController mainController;
    private final ParkingStatusView view;
    private final BusinessLogicParkingSpace businessLogicParkingSpace;
    private final BusinessLogicReservation businessLogicReservation;
    private final Timer refreshTimer;
    private boolean adminMode;

    /**
     * Creates a new ParkingStatusController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public ParkingStatusController(MainController mainController, ParkingStatusView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogicParkingSpace = new BusinessLogicParkingSpace();
        this.businessLogicReservation = new BusinessLogicReservation();

        this.refreshTimer = new Timer(2000, new ActionListener() {
            @Override

            /**
             * Handles performed.
             *
             * @param e the event triggered by the user interface
             */
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        view.registerController(this);
        view.registerTableMouseListener(new MouseAdapter() {
            @Override

            /**
             * Handles clicked.
             *
             * @param e the event triggered by the user interface
             */
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 || adminMode) {
                    showSelectedSpaceDetail();
                }
            }
        });
    }

    /**
     * Shows show.
     *
     * @param adminMode true when administrator actions are enabled
     */
    public void show(boolean adminMode) {
        this.adminMode = adminMode;
        refreshTable();
        if (!refreshTimer.isRunning()) {
            refreshTimer.start();
        }
    }

    /**
     * Stops refresh.
     */
    private void stopRefresh() {
        if (refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    /**
     * Refreshes table.
     */
    private void refreshTable() {
        java.util.List<ParkingStatusRow> rows =
                businessLogicParkingSpace.getParkingStatusRows();

        Object[][] tableRows = new Object[rows.size()][6];

        for (int i = 0; i < rows.size(); i++) {
            ParkingStatusRow row = rows.get(i);

            tableRows[i][0] = row.getCode();
            tableRows[i][1] = row.getFloor();
            tableRows[i][2] = row.getOccupationStatus();
            tableRows[i][3] = row.getReservationStatus();
            tableRows[i][4] = row.getParkedPlate();
            tableRows[i][5] = row.getReservedPlate();
        }

        view.setRows(tableRows);
    }

    /**
     * Shows selected space detail.
     */
    private void showSelectedSpaceDetail() {
        String code = view.getSelectedCode();
        if (code == null) {
            return;
        }

        ParkingStatusRow row = businessLogicParkingSpace.getParkingStatusRow(code);
        if (row == null) {
            view.showError("No se ha encontrado la plaza seleccionada.");
            return;
        }

        String message =
                "Codigo: " + row.getCode() + "\n" +
                        "Planta: " + row.getFloor() + "\n" +
                        "Tipo de vehiculo: " + row.getVehicleType() + "\n" +
                        "Estado: " + row.getOccupationStatus() + "\n" +
                        "Reserva: " + row.getReservationStatus() + "\n" +
                        "Matricula aparcada: " + emptyText(row.getParkedPlate()) + "\n" +
                        "Matricula reservada: " + emptyText(row.getReservedPlate());

        boolean hasReservation = !row.getReservedPlate().isBlank();
        if (!adminMode) {
            view.showInfo(message);
            return;
        }

        int option = view.showAdminDetail(message, hasReservation);
        if (option == 0) {
            BusinessLogicReservation.Result result =
                    businessLogicReservation.adminCancelReservation(row.getCode());

            if (result == BusinessLogicReservation.Result.RESERVATION_REASSIGNED) {
                refreshTable();
                view.showInfo("Reserva cancelada y reasignada correctamente.");
            } else if (result == BusinessLogicReservation.Result.RESERVATION_CANCELLED) {
                refreshTable();
                view.showInfo("Reserva cancelada correctamente. La plaza ya no queda reservada.");
            } else if (result == BusinessLogicReservation.Result.VEHICLE_IS_PARKED) {
                view.showError("No se puede cancelar la reserva: el coche del usuario está aparcado en la plaza. El usuario debe salir del parking primero.");
            } else if (result == BusinessLogicReservation.Result.INCONSISTENT_SPACE_STATE) {
                view.showError("Estado incoherente: la plaza está reservada por un vehículo pero ocupada por otro. No se ha modificado nada.");
            } else {
                view.showError("No se ha podido cancelar la reserva: " + result);
            }
        }
    }

    /**
     * Returns empty text.
     *
     * @param text the text to draw
     * @return the requested text value
     */
    private String emptyText(String text) {
        return text == null || text.isBlank() ? "-" : text;
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        if (ParkingStatusView.BTN_BACK.equals(e.getActionCommand())) {
            stopRefresh();
            mainController.showMenu(null);
        }
    }
}
