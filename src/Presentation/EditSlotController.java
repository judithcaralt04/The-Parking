package Presentation;

import Business.BusinessLogicParkingSpace;
import Business.BusinessLogicParkingSpace.Result;
import Business.VehicleType;
import Business.entity.ParkingSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller that handles user actions for the edit slot screen.
 */
public class EditSlotController implements ActionListener {

    private final MainController mainController;
    private final EditSlotView view;
    private final BusinessLogicParkingSpace businessLogic;

    /**
     * Creates a new EditSlotController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public EditSlotController(MainController mainController, EditSlotView view) {
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

    /**
     * Refreshes slots.
     */
    public void refreshSlots() {
        List<ParkingSpace> slots = businessLogic.getAllSpaces();

        if (slots.isEmpty()) {
            view.showError("No hay ninguna plaza creada. Crea una primero.");
            mainController.showManageSlots();
            return;
        }

        String[] codes = new String[slots.size()];

        for (int i = 0; i < slots.size(); i++) {
            codes[i] = slots.get(i).getSpaceCode();
        }

        view.loadSlots(codes);
        loadSelectedSlotIntoFields();
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case EditSlotView.SLOT_SELECTED -> loadSelectedSlotIntoFields();
            case EditSlotView.BTN_SAVE -> saveSlot();
            case EditSlotView.BTN_BACK -> mainController.showManageSlots();
        }
    }

    /**
     * Loads selected slot into fields.
     */
    private void loadSelectedSlotIntoFields() {
        String code = view.getSelectedCode();
        if (code == null) {
            return;
        }
        ParkingSpace slot = businessLogic.getSpaceByCode(code);
        if (slot != null) {
            view.fillFields(String.valueOf(slot.getFloor()), slot.getVehicleType());
        }
    }

    /**
     * Executes save slot.
     */
    private void saveSlot() {
        String code = view.getSelectedCode();
        String floor = view.getFloor();
        String vehicleType = view.getVehicleType();

        if (code == null) {
            view.showError("No hay ninguna plaza seleccionada.");
            return;
        }

        Result result = businessLogic.editSpace(code, floor, vehicleType);

        if (result == Result.OK) {
            view.showSuccess("Plaza " + code + " actualizada correctamente.");
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
            case INVALID_FLOOR -> "La planta debe ser un número entero positivo.";
            case INVALID_VEHICLE_TYPE -> "Tipo de vehículo no válido.";
            case SPACE_NOT_FOUND -> "La plaza no existe.";
            case SPACE_IS_OCCUPIED -> "No se puede editar una plaza que está ocupada.";
            case SPACE_ALREADY_RESERVED -> "No se puede cambiar el tipo de una plaza reservada.";
            default -> "Error desconocido.";
        };
    }
}
