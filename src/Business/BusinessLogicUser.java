package Business;

import Persistance.dao.ConfigLoader;
import Persistance.dao.UserDAO;
import Persistance.dao.UserDAOSQL;
import Persistance.dao.VehicleDAO;
import Persistance.dao.VehicleDAOSQL;
import Persistance.dao.ParkingSpaceDAO;
import Persistance.dao.ParkingSpaceDAOSQL;
import Business.entity.User;
import Business.entity.Vehicle;
import Business.entity.ParkingSpace;

import java.util.List;

/**
 * Business service that manages user authentication and account operations.
 */
public class BusinessLogicUser {

    private final UserDAO userDAO;
    private final VehicleDAO vehicleDAO;
    private final ParkingSpaceDAO parkingSpaceDAO;

    /**
     * Creates a new BusinessLogicUser instance.
     *
     * @return the requested value
     */
    public BusinessLogicUser() {
        this.userDAO = new UserDAOSQL();
        this.vehicleDAO = new VehicleDAOSQL();
        this.parkingSpaceDAO = new ParkingSpaceDAOSQL();
    }

    /**
     * Authenticates login.
     *
     * @param identifier the username or email used to log in
     * @param password the password provided by the user
     * @return the matching user, or null if none is found
     */
    public User login(String identifier, String password) {

        String adminPassword = ConfigLoader.getInstance().getAdminPassword();

        if ("admin".equals(identifier) && adminPassword.equals(password)) {
            return new User(0, "admin", "admin@parking.com", adminPassword);
        }

        User user = userDAO.getUserByUsername(identifier);

        if (user == null) {
            user = userDAO.getUserByEmail(identifier);
        }

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    /**
     * Registers register.
     *
     * @param username the username used to identify the user
     * @param email the email address used by the user
     * @param password the password provided by the user
     */
    public void register(String username, String email, String password) {
        userDAO.createUser(username, email, password);
    }

    /**
     * Logs out logout.
     *
     * @param user the user involved in the operation
     */
    public void logout(User user) {
        System.out.println("User logged out: " + user.getUsername());
    }

    /**
     * Deletes account.
     *
     * @param user the user involved in the operation
     * @return true when the condition is met, false otherwise
     */
    public boolean deleteAccount(User user) {
        if (isAdmin(user.getUsername())) {
            return false;
        }

        List<Vehicle> vehicles = vehicleDAO.getVehiclesByUser(user.getUsername());

        for (Vehicle vehicle : vehicles) {
            ParkingSpace occupiedSpace = parkingSpaceDAO.getOccupiedSpaceByVehicle(vehicle.getVehicleId());

            if (occupiedSpace != null) {
                parkingSpaceDAO.freeSpace(occupiedSpace.getSpaceCode());
            }
        }

        userDAO.deleteUser(user.getUsername());
        return true;
    }

    /**
     * Validates password.
     *
     * @param password the password provided by the user
     * @return true when the condition is met, false otherwise
     */
    public boolean validatePassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Checks whether the username exists.
     *
     * @param username the username used to identify the user
     * @return true when the condition is met, false otherwise
     */
    public boolean usernameExists(String username) {
        return userDAO.usernameExists(username);
    }

    /**
     * Checks whether the email exists.
     *
     * @param email the email address used by the user
     * @return true when the condition is met, false otherwise
     */
    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }

    /**
     * Checks whether admin.
     *
     * @param username the username used to identify the user
     * @return true when the condition is met, false otherwise
     */
    public boolean isAdmin(String username) {
        return "admin".equals(username);
    }

    /**
     * Validates email.
     *
     * @param email the email address used by the user
     * @return true when the condition is met, false otherwise
     */
    public boolean validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return false;
        }

        if (email.indexOf('@', atIndex + 1) != -1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1);

        if (localPart.isEmpty() || domainPart.isEmpty()) {
            return false;
        }

        int lastDot = domainPart.lastIndexOf('.');
        if (lastDot <= 0) {
            return false;
        }

        String tld = domainPart.substring(lastDot + 1);
        if (tld.length() < 2) {
            return false;
        }

        return true;
    }
}