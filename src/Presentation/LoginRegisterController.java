package Presentation;

import Business.BusinessLogicReservation;
import Business.BusinessLogicUser;
import Business.entity.CancelledNotification;
import Business.entity.User;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller that handles user actions for the login register screen.
 */
public class LoginRegisterController implements ActionListener {

    private final MainController mainController;
    private final LoginRegisterView view;
    private final BusinessLogicUser businessLogicUser;
    private final BusinessLogicReservation businessLogicReservation;
    private boolean registerMode;

    /**
     * Creates a new LoginRegisterController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public LoginRegisterController(MainController mainController, LoginRegisterView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogicUser = new BusinessLogicUser();
        this.businessLogicReservation = new BusinessLogicReservation();
        this.registerMode = false;
        view.registerController(this);
        view.showLoginMode();
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case LoginRegisterView.BTN_LOGIN -> login();
            case LoginRegisterView.BTN_REGISTER -> register();
            case LoginRegisterView.BTN_SWITCHMODE -> switchMode();
        }
    }

    /**
     * Switches mode.
     */
    private void switchMode() {
        registerMode = !registerMode;
        view.clearFields();
        if (registerMode) {
            view.showRegisterMode();
        } else {
            view.showLoginMode();
        }
    }

    /**
     * Authenticates login.
     */
    private void login() {
        String identifier = view.getUsername();
        String password = view.getPassword();

        if (identifier.isEmpty() || password.isEmpty()) {
            view.showError("Rellena ambos campos");
            return;
        }

        User user = businessLogicUser.login(identifier, password);
        if (user != null) {
            view.clearFields();
            showPendingNotifications(user);
            mainController.showMenu(user);
        } else {
            view.showError("Credenciales incorrectas");
        }
    }

    /**
     * Shows pending notifications.
     *
     * @param user the user involved in the operation
     */
    private void showPendingNotifications(User user) {
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            return;
        }

        List<CancelledNotification> pending = businessLogicReservation.getPendingNotifications(user.getUsername());

        if (pending == null || pending.isEmpty()) {
            return;
        }

        String message = "Mientras estabas fuera, el administrador ha cancelado ";
        if (pending.size() == 1) {
            message = message + "esta reserva:\n\n";
        } else {
            message = message + "estas reservas:\n\n";
        }

        for (CancelledNotification n : pending) {
            message = message + "  • Plaza " + n.getOldSpace();

            if ("CANCELLED".equalsIgnoreCase(n.getNewSpace())) {
                message = message + "  →  reserva cancelada sin reasignación";
            } else {
                message = message + "  →  reasignada a la plaza " + n.getNewSpace();
            }
            message = message + "\n";
        }

        JOptionPane.showMessageDialog(view, message, "Reservas modificadas", JOptionPane.INFORMATION_MESSAGE);

        businessLogicReservation.markNotificationsAsShown(pending);
    }

    /**
     * Registers register.
     */
    private void register() {
        if (!validateRegisterFields()) {
            return;
        }

        String username = view.getUsername();
        String email = view.getEmail();
        String password = view.getPassword();

        businessLogicUser.register(username, email, password);

        User user = businessLogicUser.login(username, password);
        view.clearFields();
        mainController.showMenu(user);
    }

    /**
     * Validates register fields.
     *
     * @return true when the condition is met, false otherwise
     */
    private boolean validateRegisterFields() {
        String username = view.getUsername();
        String email = view.getEmail();
        String password = view.getPassword();
        String confirmPassword = view.getConfirmPassword();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            view.showError("Rellena todos los campos");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            view.showError("Las contraseñas no coinciden");
            return false;
        }
        if (!businessLogicUser.validateEmail(email)) {
            view.showError("Formato de email no válido");
            return false;
        }
        if (!businessLogicUser.validatePassword(password)) {
            view.showError("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un dígito y un carácter especial");
            return false;
        }
        if (businessLogicUser.usernameExists(username)) {
            view.showError("El nombre de usuario ya existe");
            return false;
        }
        if (businessLogicUser.emailExists(email)) {
            view.showError("El email ya está registrado");
            return false;
        }
        return true;
    }
}
