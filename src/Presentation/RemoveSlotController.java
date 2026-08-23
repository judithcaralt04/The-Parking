package Presentation;

import Business.BusinessLogicParkingSpace;
import Business.BusinessLogicParkingSpace.Result;
import Business.entity.ParkingSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller that handles user actions for the remove slot screen.
 */
public class RemoveSlotController implements ActionListener {

    private final MainController mainController;
    private final RemoveSlotView view;
    private final BusinessLogicParkingSpace businessLogic;

    /**
     * Creates a new RemoveSlotController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public RemoveSlotController(MainController mainController, RemoveSlotView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogic = new BusinessLogicParkingSpace();
        view.registerController(this);
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
        showSelectedInfo();
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case RemoveSlotView.SLOT_SELECTED -> showSelectedInfo();
            case RemoveSlotView.BTN_REMOVE -> removeSlot();
            case RemoveSlotView.BTN_BACK -> mainController.showManageSlots();
        }
    }

    /**
     * Shows selected info.
     */
    private void showSelectedInfo() {
        String code = view.getSelectedCode();

        if (code == null) {
            view.showSlotInfo(null);
            return;
        }

        ParkingSpace slot = businessLogic.getSpaceByCode(code);

        if (slot == null) {
            view.showSlotInfo(null);
            return;
        }

        String estado = slot.isOccupied() ? "Ocupada" : "Libre";

        view.showSlotInfo("Planta: " + slot.getFloor() + " | Tipo: " + slot.getVehicleType() + " | Estado: " + estado);
    }

    /**
     * Removes slot.
     */
    private void removeSlot() {
        String code = view.getSelectedCode();

        if (code == null) {
            view.showError("No hay ninguna plaza seleccionada.");
            return;
        }

        if (!view.confirmRemove(code)) {
            return;
        }

        Result result = businessLogic.deleteSpace(code);

        if (result == Result.OK) {
            view.showSuccess("Plaza " + code + " eliminada correctamente.");
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
            case SPACE_NOT_FOUND -> "La plaza no existe.";
            case SPACE_IS_OCCUPIED -> "No se puede eliminar una plaza ocupada.";
            case NO_EQUIVALENT_SPACE -> "No hay plazas equivalentes para reasignar la reserva.";
            default -> "Error desconocido.";
        };
    }
}
