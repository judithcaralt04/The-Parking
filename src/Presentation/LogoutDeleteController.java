package Presentation;

import Business.BusinessLogicUser;
import Business.entity.User;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller that handles user actions for the logout delete screen.
 */
public class LogoutDeleteController implements ActionListener {

    private final MainController mainController;
    private final LogoutDeleteView view;
    private final BusinessLogicUser businessLogicUser;
    private User currentUser;

    /**
     * Creates a new LogoutDeleteController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public LogoutDeleteController(MainController mainController, LogoutDeleteView view) {
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
        boolean isAdmin = businessLogicUser.isAdmin(user.getUsername());
        view.setDeleteVisible(!isAdmin);
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case LogoutDeleteView.BTN_LOGOUT -> logout();
            case LogoutDeleteView.BTN_DELETE -> deleteAccount();
        }
    }

    /**
     * Logs out logout.
     */
    private void logout() {
        businessLogicUser.logout(currentUser);
        currentUser = null;
        mainController.showLogin();
    }

    /**
     * Deletes account.
     */
    private void deleteAccount() {
        if (businessLogicUser.isAdmin(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(view,
                    "La cuenta de administrador no se puede eliminar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Estás seguro de que quieres borrar tu cuenta?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            businessLogicUser.deleteAccount(currentUser);
            currentUser = null;
            mainController.showLogin();
        }
    }
}
