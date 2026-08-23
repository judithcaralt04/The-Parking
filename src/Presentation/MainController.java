package Presentation;

import Business.entity.User;

/**
 * Controller that handles user actions for the main screen.
 */
public class MainController {

    private final MainPage mainPage;

    private LoginRegisterView loginRegisterView;
    private MenuView menuView;
    private LogoutDeleteView logoutDeleteView;
    private ManageSlotsView manageSlotsView;
    private AddSlotView addSlotView;
    private EditSlotView editSlotView;
    private RemoveSlotView removeSlotView;
    private ParkingStatusView parkingStatusView;
    private OccupancyChartView occupancyChartView;
    private ParkingEntryExitView parkingEntryExitView;
    private ManageReservationsView manageReservationsView;
    private ManageBookingsView manageBookingsView;

    private MenuController menuController;
    private LogoutDeleteController logoutDeleteController;
    private EditSlotController editSlotController;
    private RemoveSlotController removeSlotController;
    private ParkingStatusController parkingStatusController;
    private OccupancyChartController occupancyChartController;
    private ManageReservationsController manageReservationsController;
    private ManageBookingsController manageBookingsController;

    private User currentUser;

    /**
     * Creates a new MainController instance.
     *
     * @return the requested value
     */
    public MainController() {
        mainPage = new MainPage();
        showLogin();
    }

    /**
     * Shows login.
     */
    public void showLogin() {
        if (loginRegisterView == null) {
            loginRegisterView = new LoginRegisterView();
            new LoginRegisterController(this, loginRegisterView);
            mainPage.addCard(loginRegisterView, "LOGIN");
        }
        currentUser = null;
        mainPage.showCard("LOGIN");
    }

    /**
     * Shows menu.
     *
     * @param user the user involved in the operation
     */
    public void showMenu(User user) {
        if (user != null) {
            this.currentUser = user;
        }
        if (menuView == null) {
            menuView = new MenuView();
            menuController = new MenuController(this, menuView);
            mainPage.addCard(menuView, "MENU");
        }
        menuController.setCurrentUser(currentUser);
        mainPage.showCard("MENU");
    }

    /**
     * Shows logout confirm.
     */
    public void showLogoutConfirm() {
        if (logoutDeleteView == null) {
            logoutDeleteView = new LogoutDeleteView();
            logoutDeleteController = new LogoutDeleteController(this, logoutDeleteView);
            mainPage.addCard(logoutDeleteView, "LOGOUT");
        }
        logoutDeleteController.setCurrentUser(currentUser);
        mainPage.showCard("LOGOUT");
    }

    /**
     * Shows manage slots.
     */
    public void showManageSlots() {
        if (manageSlotsView == null) {
            manageSlotsView = new ManageSlotsView();
            new ManageSlotsController(this, manageSlotsView);
            mainPage.addCard(manageSlotsView, "MANAGE_SLOTS");
        }
        mainPage.showCard("MANAGE_SLOTS");
    }

    /**
     * Shows add slot.
     */
    public void showAddSlot() {
        if (addSlotView == null) {
            addSlotView = new AddSlotView();
            new AddSlotController(this, addSlotView);
            mainPage.addCard(addSlotView, "ADD_SLOT");
        }
        mainPage.showCard("ADD_SLOT");
    }

    /**
     * Shows edit slot.
     */
    public void showEditSlot() {
        if (editSlotView == null) {
            editSlotView = new EditSlotView();
            editSlotController = new EditSlotController(this, editSlotView);
            mainPage.addCard(editSlotView, "EDIT_SLOT");
        }
        mainPage.showCard("EDIT_SLOT");
        editSlotController.refreshSlots();
    }

    /**
     * Shows remove slot.
     */
    public void showRemoveSlot() {
        if (removeSlotView == null) {
            removeSlotView = new RemoveSlotView();
            removeSlotController = new RemoveSlotController(this, removeSlotView);
            mainPage.addCard(removeSlotView, "REMOVE_SLOT");
        }
        mainPage.showCard("REMOVE_SLOT");
        removeSlotController.refreshSlots();
    }

    /**
     * Shows parking status.
     *
     * @param adminMode true when administrator actions are enabled
     */
    public void showParkingStatus(boolean adminMode) {
        if (parkingStatusView == null) {
            parkingStatusView = new ParkingStatusView();
            parkingStatusController = new ParkingStatusController(this, parkingStatusView);
            mainPage.addCard(parkingStatusView, "PARKING_STATUS");
        }
        parkingStatusController.show(adminMode);
        mainPage.showCard("PARKING_STATUS");
    }

    /**
     * Shows occupancy chart.
     */
    public void showOccupancyChart() {
        if (occupancyChartView == null) {
            occupancyChartView = new OccupancyChartView();
            occupancyChartController = new OccupancyChartController(this, occupancyChartView);
            mainPage.addCard(occupancyChartView, "OCCUPANCY_CHART");
        }
        occupancyChartController.show();
        mainPage.showCard("OCCUPANCY_CHART");
    }

    /**
     * Shows entry exit.
     */
    public void showEntryExit() {
        if (parkingEntryExitView == null) {
            parkingEntryExitView = new ParkingEntryExitView();
            new ParkingEntryExitController(this, parkingEntryExitView);
            mainPage.addCard(parkingEntryExitView, "ENTRY_EXIT");
        }
        mainPage.showCard("ENTRY_EXIT");
    }

    /**
     * Shows manage reservations.
     */
    public void showManageReservations() {
        if (manageReservationsView == null) {
            manageReservationsView = new ManageReservationsView();
            manageReservationsController = new ManageReservationsController(this, manageReservationsView);
            mainPage.addCard(manageReservationsView, "MANAGE_RESERVATIONS");
        }
        manageReservationsController.setCurrentUser(currentUser);
        mainPage.showCard("MANAGE_RESERVATIONS");
    }

    /**
     * Shows manage bookings.
     */
    public void showManageBookings() {
        if (manageBookingsView == null) {
            manageBookingsView = new ManageBookingsView();
            manageBookingsController = new ManageBookingsController(this, manageBookingsView);
            mainPage.addCard(manageBookingsView, "MANAGE_BOOKINGS");
        }
        manageBookingsController.show();
        mainPage.showCard("MANAGE_BOOKINGS");
    }

    /**
     * Returns current user.
     *
     * @return the matching user, or null if none is found
     */
    public User getCurrentUser() {
        return currentUser;
    }
}
