package Presentation;

import Business.BusinessLogicUser;
import Business.entity.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller that handles user actions for the menu screen.
 */
public class MenuController implements ActionListener {

    private final MainController mainController;
    private final MenuView view;
    private final BusinessLogicUser businessLogicUser;
    private User currentUser;

    /**
     * Creates a new MenuController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public MenuController(MainController mainController, MenuView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogicUser = new BusinessLogicUser();
        view.registerController(this);
    }

    /**
     * Sets current user.
     *
     * @param user the user involved in the operation
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;

        if (businessLogicUser.isAdmin(user.getUsername())) {
            view.showAdminMenu(user.getUsername());
        } else {
            view.showUserMenu(user.getUsername());
        }
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case MenuView.BTN_MANAGE_SLOTS    -> mainController.showManageSlots();
            case MenuView.BTN_LOGOUT          -> mainController.showLogoutConfirm();
            case MenuView.BTN_STATUS          -> mainController.showParkingStatus(businessLogicUser.isAdmin(currentUser.getUsername()));
            case MenuView.BTN_OCCUPANCY       -> mainController.showOccupancyChart();
            case MenuView.BTN_ENTRY           -> mainController.showEntryExit();
            case MenuView.BTN_BOOKING         -> mainController.showManageReservations();
            case MenuView.BTN_MANAGE_BOOKINGS -> mainController.showManageBookings();
        }
    }
}