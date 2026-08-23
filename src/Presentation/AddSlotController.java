package Presentation;

import Business.BusinessLogicParkingSpace;
import Business.BusinessLogicParkingSpace.Result;
import Business.VehicleType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller that handles user actions for the add slot screen.
 */
public class AddSlotController implements ActionListener {

    private final MainController mainController;
    private final AddSlotView view;
    private final BusinessLogicParkingSpace businessLogic;

    /**
     * Creates a new AddSlotController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public AddSlotController(MainController mainController, AddSlotView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogic = new BusinessLogicParkingSpace();

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
            case AddSlotView.BTN_CREATE -> createSlot();
            case AddSlotView.BTN_BACK -> mainController.showManageSlots();
        }
    }

    /**
     * Creates slot.
     */
    private void createSlot() {
        String code = view.getCode();
        String floor = view.getFloor();
        String vehicleType = view.getVehicleType();

        Result result = businessLogic.createSpace(code, floor, vehicleType);

        if (result == Result.OK) {
            view.showSuccess("Plaza " + code + " creada correctamente.");
            view.clearFields();
            mainController.showManageSlots();
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
            case CODE_ALREADY_EXISTS -> "Ya existe una plaza con ese código.";
            case INVALID_FLOOR -> "La planta debe ser un número entero positivo.";
            case INVALID_VEHICLE_TYPE -> "Tipo de vehículo no válido.";
            case SPACE_NOT_FOUND -> "El código de la plaza no es válido.";
            default -> "Error desconocido.";
        };
    }
}
