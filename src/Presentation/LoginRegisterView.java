package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the login register screen.
 */
public class LoginRegisterView extends JPanel {

    private JTextField usernameInput;
    private JTextField emailInput;
    private JPasswordField passwordInput;
    private JPasswordField confirmPasswordInput;
    private JButton loginButton;
    private JButton registerButton;
    private JButton switchModeButton;
    private JLabel errorLabel;
    private JLabel emailLabel;
    private JLabel confirmPasswordLabel;

    public static final String BTN_LOGIN = "BTN_LOGIN";
    public static final String BTN_REGISTER = "BTN_REGISTER";
    public static final String BTN_SWITCHMODE = "BTN_SWITCHMODE";

    /**
     * Creates a new LoginRegisterView instance.
     *
     * @return the requested value
     */
    public LoginRegisterView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        addTitle();
        addInputs();
        addButtons();
        showLoginMode();
    }

    /**
     * Adds title.
     */
    private void addTitle() {
        JLabel title = new JLabel("The Parking", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);
    }

    /**
     * Adds inputs.
     */
    private void addInputs() {
        usernameInput = new JTextField();
        emailInput = new JTextField();
        passwordInput = new JPasswordField();
        confirmPasswordInput = new JPasswordField();
        emailLabel = new JLabel("Email:");
        confirmPasswordLabel = new JLabel("Confirmar contraseña:");
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        addRow(form, gbc, 0, new JLabel("Usuario:"),       usernameInput);
        addRow(form, gbc, 1, emailLabel,                   emailInput);
        addRow(form, gbc, 2, new JLabel("Contraseña:"),    passwordInput);
        addRow(form, gbc, 3, confirmPasswordLabel,          confirmPasswordInput);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(errorLabel, gbc);

        add(form, BorderLayout.CENTER);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JComponent input) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; gbc.gridwidth = 1;
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(input, gbc);
    }

    /**
     * Adds buttons.
     */
    private void addButtons() {
        loginButton = new JButton("Iniciar sesión");
        registerButton = new JButton("Registrarse");
        switchModeButton = new JButton("¿No tienes cuenta? Regístrate");

        loginButton.setActionCommand(BTN_LOGIN);
        registerButton.setActionCommand(BTN_REGISTER);
        switchModeButton.setActionCommand(BTN_SWITCHMODE);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 6));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(switchModeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        loginButton.addActionListener(controller);
        registerButton.addActionListener(controller);
        switchModeButton.addActionListener(controller);
    }

    /**
     * Shows login mode.
     */
    public void showLoginMode() {
        emailLabel.setVisible(false);
        emailInput.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        confirmPasswordInput.setVisible(false);
        loginButton.setVisible(true);
        registerButton.setVisible(false);
        switchModeButton.setText("¿No tienes cuenta? Regístrate");
        errorLabel.setText("");
    }

    /**
     * Shows register mode.
     */
    public void showRegisterMode() {
        emailLabel.setVisible(true);
        emailInput.setVisible(true);
        confirmPasswordLabel.setVisible(true);
        confirmPasswordInput.setVisible(true);
        loginButton.setVisible(false);
        registerButton.setVisible(true);
        switchModeButton.setText("¿Ya tienes cuenta? Inicia sesión");
        errorLabel.setText("");
    }

    /**
     * Returns username.
     *
     * @return the requested text value
     */
    public String getUsername() {
        return usernameInput.getText().trim();
    }

    /**
     * Returns email.
     *
     * @return the requested text value
     */
    public String getEmail() {
        return emailInput.getText().trim();
    }

    /**
     * Returns password.
     *
     * @return the requested text value
     */
    public String getPassword() {
        return new String(passwordInput.getPassword());
    }

    /**
     * Returns confirm password.
     *
     * @return the requested text value
     */
    public String getConfirmPassword() {
        return new String(confirmPasswordInput.getPassword());
    }

    /**
     * Shows error.
     *
     * @param msg the message to display
     */
    public void showError(String msg) {
        errorLabel.setForeground(Color.RED);
        errorLabel.setText(msg);
    }

    /**
     * Shows success.
     *
     * @param msg the message to display
     */
    public void showSuccess(String msg) {
        errorLabel.setForeground(new Color(0, 150, 0));
        errorLabel.setText(msg);
    }

    /**
     * Clears fields.
     */
    public void clearFields() {
        usernameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
        errorLabel.setText("");
    }
}
