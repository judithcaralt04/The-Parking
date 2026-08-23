package Presentation;

import Business.BusinessLogicReservation;
import Business.BusinessLogicReservation.Result;
import Business.entity.BookingInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 * Controller that handles user actions for the manage bookings screen.
 */
public class ManageBookingsController implements ActionListener {

    private final MainController mainController;
    private final ManageBookingsView view;
    private final BusinessLogicReservation businessLogicReservation;

    /**
     * Creates a new ManageBookingsController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public ManageBookingsController(MainController mainController, ManageBookingsView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogicReservation = new BusinessLogicReservation();
        view.registerController(this);
    }

    /**
     * Shows show.
     */
    public void show() {
        refresh();
    }

    /**
     * Refreshes refresh.
     */
    private void refresh() {
        List<BookingInfo> bookings = businessLogicReservation.getAllBookings();

        Object[][] rows = new Object[bookings.size()][7];
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (int i = 0; i < bookings.size(); i++) {
            BookingInfo b = bookings.get(i);

            rows[i][0] = b.getReservationId();
            rows[i][1] = b.getSpaceCode();
            rows[i][2] = b.getFloor();
            rows[i][3] = b.getVehicleType();
            rows[i][4] = b.getLicensePlate();
            rows[i][5] = b.getUsername();
            rows[i][6] = b.getReservationDate() == null ? "-" : b.getReservationDate().format(fmt);
        }

        view.loadBookings(rows);
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ManageBookingsView.BTN_REFRESH -> refresh();
            case ManageBookingsView.BTN_USER_INFO -> showUserInfo();
            case ManageBookingsView.BTN_CANCEL -> cancelSelectedBooking();
            case ManageBookingsView.BTN_BACK -> mainController.showMenu(null);
        }
    }

    /**
     * Shows user info.
     */
    private void showUserInfo() {
        int reservationId = view.getSelectedReservationId();
        if (reservationId < 0) {
            view.showError("Selecciona una reserva de la tabla.");
            return;
        }

        BookingInfo info = findBooking(reservationId);
        if (info == null) {
            view.showError("La reserva ya no existe. Refresca la lista.");
            refresh();
            return;
        }

        String message =
                "Información de la reserva\n" +
                        "─────────────────────────\n" +
                        "Plaza:          " + info.getSpaceCode() + " (Planta " + info.getFloor() + ")\n" +
                        "Tipo vehículo:  " + info.getVehicleType() + "\n" +
                        "Matrícula:      " + info.getLicensePlate() + "\n" +
                        "\n" +
                        "Usuario que ha reservado\n" +
                        "─────────────────────────\n" +
                        "Nombre usuario: " + info.getUsername() + "\n" +
                        "Email:          " + info.getEmail();

        view.showInfo(message);
    }

    /**
     * Cancels selected booking.
     */
    private void cancelSelectedBooking() {
        String spaceCode = view.getSelectedSpaceCode();
        if (spaceCode == null) {
            view.showError("Selecciona una reserva de la tabla para cancelarla.");
            return;
        }

        boolean confirmed = view.confirm(
                "¿Seguro que quieres cancelar la reserva de la plaza " + spaceCode + "?\n" +
                        "Si la plaza está libre, se intentará reasignar a una plaza equivalente.");

        if (!confirmed) {
            return;
        }

        Result result = businessLogicReservation.adminCancelReservation(spaceCode);

        if (result == Result.RESERVATION_REASSIGNED) {
            view.showSuccess("Reserva cancelada y transferida correctamente.");
            refresh();
        } else if (result == Result.RESERVATION_CANCELLED) {
            view.showSuccess("Reserva cancelada correctamente. No se ha creado una nueva reserva.");
            refresh();
        } else {
            view.showError(getErrorMessage(result));
        }
    }

    /**
     * Finds booking.
     *
     * @param reservationId the reservation identifier
     * @return the requested value
     */
    private BookingInfo findBooking(int reservationId) {
        for (BookingInfo b : businessLogicReservation.getAllBookings()) {
            if (b.getReservationId() == reservationId) {
                return b;
            }
        }
        return null;
    }

    /**
     * Returns error message.
     *
     * @param result the result value
     * @return the requested text value
     */
    private String getErrorMessage(Result result) {
        return switch (result) {
            case RESERVATION_NOT_FOUND  -> "No se ha encontrado la reserva.";
            case SPACE_NOT_FOUND -> "No se ha encontrado la plaza.";
            case VEHICLE_IS_PARKED -> "No se puede cancelar la reserva: el coche del usuario está aparcado en la plaza. El usuario debe salir del parking primero.";
            case INCONSISTENT_SPACE_STATE -> "Estado incoherente: la plaza está reservada por un vehículo pero ocupada por otro.";
            default -> "Error desconocido al cancelar la reserva.";
        };
    }
}
