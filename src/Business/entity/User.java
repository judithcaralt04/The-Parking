package Business.entity;

/**
 * Entity that stores user data used by the parking application.
 */
public class User {

    private int userId;
    private String username;
    private String email;
    private String password;

    /**
     * Creates a new User instance.
     *
     * @param userId the user identifier
     * @param username the username used to identify the user
     * @param email the email address used by the user
     * @param password the password provided by the user
     * @return the requested value
     */
    public User(int userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns user id.
     *
     * @return the calculated numeric value
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns username.
     *
     * @return the requested text value
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns email.
     *
     * @return the requested text value
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns password.
     *
     * @return the requested text value
     */
    public String getPassword() {
        return password;
    }
}
