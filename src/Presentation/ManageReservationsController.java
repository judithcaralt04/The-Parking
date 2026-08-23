package Presentation;

import Business.BusinessLogicReservation;
import Business.BusinessLogicReservation.Result;
import Business.VehicleType;
import Business.entity.BookingInfo;
import Business.entity.ParkingSpace;
import Business.entity.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 * Controller that handles user actions for the manage reservations screen.
 */
public class ManageReservationsController implements ActionListener {

    private final MainController mainController;
    private final ManageReservationsView view;
    private final BusinessLogicReservation businessLogicReservation;
    private User currentUser;

    /**
     * Creates a new ManageReservationsController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public ManageReservationsController(MainController mainController, ManageReservationsView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogicReservation = new BusinessLogicReservation();

        view.setVehicleTypes(getVehicleTypes());
        view.registerController(this);
    }

    /**
     * Returns vehicle types.
     *
     * @return the requested array of values
     */
    private String[] getVehicleTypes() {
        VehicleType[] values = VehicleType.values();
        String[] types = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            types[i] = values[i].name();
        }

        return types;
    }

    /**
     * Sets current user.
     *
     * @param user the user involved in the operation
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        refresh();
    }

    /**
     * Refreshes refresh.
     */
    private void refresh() {
        refreshReservations();
        refreshAvailableSpaces();
    }

    /**
     * Refreshes reservations.
     */
    private void refreshReservations() {
        List<BookingInfo> reservations =
                businessLogicReservation.getBookingInfosByUser(currentUser.getUsername());

        Object[][] rows = new Object[reservations.size()][5];

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (int i = 0; i < reservations.size(); i++) {
            BookingInfo r = reservations.get(i);

            rows[i][0] = r.getReservationId();
            rows[i][1] = r.getSpaceCode();
            rows[i][2] = r.getLicensePlate();
            rows[i][3] = r.getVehicleType();
            rows[i][4] = r.getReservationDate().format(fmt);
        }

        view.loadReservations(rows);
    }

    /**
     * Refreshes available spaces.
     */
    private void refreshAvailableSpaces() {
        String vehicleType = view.getVehicleType();
        List<ParkingSpace> spaces = businessLogicReservation.getAvailableSpaces(vehicleType);

        String[] items = new String[spaces.size()];

        for (int i = 0; i < spaces.size(); i++) {
            ParkingSpace s = spaces.get(i);
            items[i] = s.getSpaceCode() + " (Planta " + s.getFloor() + ")";
        }

        view.loadAvailableSpaces(items);
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ManageReservationsView.BTN_BOOK -> makeReservation();
            case ManageReservationsView.BTN_CANCEL -> cancelReservation();
            case ManageReservationsView.COMBO_TYPE -> refreshAvailableSpaces();
            case ManageReservationsView.BTN_BACK -> mainController.showMenu(null);
        }
    }

    /**
     * Creates reservation.
     */
    private void makeReservation() {
        String licensePlate = view.getLicensePlate();
        String vehicleType = view.getVehicleType();
        String spaceCode = view.getSelectedSpaceCode();

        if (licensePlate.isEmpty()) {
            view.showError("Introduce la matrícula del vehículo.");
            return;
        }
        if (spaceCode == null) {
            view.showError("No hay plazas disponibles para ese tipo de vehículo.");
            return;
        }

        Result result = businessLogicReservation.makeReservation(licensePlate, vehicleType, spaceCode, currentUser.getUserId());

        if (result == Result.OK) {
            view.showSuccess("Reserva realizada correctamente en la plaza " + spaceCode + ".");
            view.clearForm();
            refresh();
        } else {
            view.showError(getErrorMessage(result));
        }
    }

    /**
     * Cancels reservation.
     */
    private void cancelReservation() {
        int reservationId = view.getSelectedReservationId();

        if (reservationId < 0) {
            view.showError("Selecciona una reserva de la tabla para cancelarla.");
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(view,
                "¿Estás seguro de que quieres cancelar esta reserva?",
                "Confirmar cancelación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            Result result = businessLogicReservation.cancelReservation(reservationId);
            if (result == Result.OK) {
                view.showSuccess("Reserva cancelada correctamente.");
                refresh();
            } else {
                view.showError(getErrorMessage(result));
            }
        }
    }

    /**
     * Returns error message.
     *
     * @param result the result value
     * @return the requested text value
     */
    private String getErrorMessage(Result result) {
        return switch (result) {
            case SPACE_NOT_FOUND -> "La plaza seleccionada no existe.";
            case SPACE_ALREADY_RESERVED -> "Esa plaza ya está reservada.";
            case SPACE_IS_OCCUPIED -> "Esa plaza está actualmente ocupada.";
            case VEHICLE_TYPE_MISMATCH -> "El tipo de vehículo no coincide con el de la plaza.";
            case INVALID_VEHICLE_TYPE -> "Tipo de vehículo no válido.";
            case LICENSE_PLATE_BELONGS_TO_OTHER_USER -> "Esta matrícula pertenece a otro usuario.";
            case RESERVATION_NOT_FOUND -> "La reserva ya no existe. Refresca la lista.";
            case VEHICLE_IS_PARKED -> "No puedes cancelar la reserva mientras el vehículo está aparcado. Sal del parking primero.";
            default -> "Error desconocido.";
        };
    }
}
