package Presentation;

import Business.BusinessLogicVehicle;
import Business.BusinessLogicVehicle.Result;
import Business.VehicleType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller that handles user actions for the parking entry exit screen.
 */
public class ParkingEntryExitController implements ActionListener {

    private final MainController mainController;
    private final ParkingEntryExitView view;
    private final BusinessLogicVehicle businessLogicVehicle;

    /**
     * Creates a new ParkingEntryExitController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public ParkingEntryExitController(MainController mainController, ParkingEntryExitView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogicVehicle = new BusinessLogicVehicle();

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

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ParkingEntryExitView.BTN_ENTRY -> registerEntry();
            case ParkingEntryExitView.BTN_EXIT -> registerExit();
            case ParkingEntryExitView.BTN_BACK -> mainController.showMenu(null);
        }
    }

    /**
     * Registers entry.
     */
    private void registerEntry() {
        String licensePlate = view.getLicensePlate();
        String vehicleType = view.getVehicleType();

        if (licensePlate.isEmpty()) {
            view.showError("Introduce la matrícula del vehículo.");
            return;
        }

        Result result = businessLogicVehicle.registerEntry(licensePlate, vehicleType, mainController.getCurrentUser().getUserId());

        if (result == Result.OK) {

            String assignedSpace = businessLogicVehicle.getAssignedSpace(licensePlate);
            view.showAssignedSpace(assignedSpace);
            view.showSuccess("Entrada registrada correctamente.\nPlaza asignada: " + assignedSpace);
            view.clearFields();
        } else {
            view.showError(getErrorMessage(result));
        }
    }

    /**
     * Registers exit.
     */
    private void registerExit() {
        String licensePlate = view.getLicensePlate();

        if (licensePlate.isEmpty()) {
            view.showError("Introduce la matrícula del vehículo.");
            return;
        }

        Result result = businessLogicVehicle.registerExit(licensePlate, mainController.getCurrentUser().getUserId());

        if (result == Result.OK) {
            view.showSuccess("Salida registrada correctamente.");
            view.clearFields();
        } else {
            view.showError(getErrorMessage(result));
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
            case VEHICLE_NOT_FOUND -> "No se ha encontrado ningún vehículo con esa matrícula.";
            case VEHICLE_ALREADY_PARKED -> "Este vehículo ya está aparcado en el parking.";
            case VEHICLE_NOT_PARKED -> "Este vehículo no está aparcado actualmente.";
            case LICENSE_PLATE_BELONGS_TO_OTHER_USER -> "Esta matrícula pertenece a otro usuario.";
            case NO_SPACE_AVAILABLE -> "No hay plazas libres disponibles para ese tipo de vehículo.";
            case INVALID_VEHICLE_TYPE -> "Tipo de vehículo no válido.";
            case VEHICLE_TYPE_MISMATCH -> "Esta matrícula está registrada con otro tipo de vehículo.";
            default -> "Error desconocido.";
        };
    }
}
