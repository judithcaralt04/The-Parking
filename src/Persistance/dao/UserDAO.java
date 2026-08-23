package Persistance.dao;

import Business.entity.User;

/**
 * Data access contract for managing user persistence operations.
 */
public interface UserDAO {

    /**
     * Returns user by username.
     *
     * @param username the username used to identify the user
     * @return the matching user, or null if none is found
     */
    User getUserByUsername(String username);

    /**
     * Returns user by email.
     *
     * @param email the email address used by the user
     * @return the matching user, or null if none is found
     */
    User getUserByEmail(String email);

    /**
     * Creates user.
     *
     * @param username the username used to identify the user
     * @param email the email address used by the user
     * @param password the password provided by the user
     */
    void createUser(String username, String email, String password);

    /**
     * Deletes user.
     *
     * @param username the username used to identify the user
     */
    void deleteUser(String username);

    /**
     * Checks whether the username exists.
     *
     * @param username the username used to identify the user
     * @return true when the condition is met, false otherwise
     */
    boolean usernameExists(String username);

    /**
     * Checks whether the email exists.
     *
     * @param email the email address used by the user
     * @return true when the condition is met, false otherwise
     */
    boolean emailExists(String email);
}
