package Presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller that handles user actions for the manage slots screen.
 */
public class ManageSlotsController implements ActionListener {

    private final MainController mainController;
    private final ManageSlotsView view;

    /**
     * Creates a new ManageSlotsController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public ManageSlotsController(MainController mainController, ManageSlotsView view) {
        this.mainController = mainController;
        this.view = view;
        view.registerController(this);
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ManageSlotsView.BTN_ADD -> mainController.showAddSlot();
            case ManageSlotsView.BTN_EDIT -> mainController.showEditSlot();
            case ManageSlotsView.BTN_REMOVE -> mainController.showRemoveSlot();
            case ManageSlotsView.BTN_BACK -> mainController.showMenu(null);
        }
    }
}
