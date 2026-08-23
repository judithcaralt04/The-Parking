package Persistance.dao;

import Business.entity.User;
import java.sql.*;

/**
 * SQL DAO implementation for managing user persistence.
 */
public class UserDAOSQL implements UserDAO {

    private final Connection conn;

    /**
     * Creates a new UserDAOSQL instance.
     *
     * @return the requested value
     */
    public UserDAOSQL() {
        this.conn = SQLConnector.getInstance().getConnection();
    }

    @Override

    /**
     * Returns user by username.
     *
     * @param username the username used to identify the user
     * @return the matching user, or null if none is found
     */
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("getUserByUsername error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Returns user by email.
     *
     * @param email the email address used by the user
     * @return the matching user, or null if none is found
     */
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("getUserByEmail error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Creates user.
     *
     * @param username the username used to identify the user
     * @param email the email address used by the user
     * @param password the password provided by the user
     */
    public void createUser(String username, String email, String password) {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            System.out.println("User created: " + username);
        } catch (SQLException e) {
            System.err.println("createUser error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Deletes user.
     *
     * @param username the username used to identify the user
     */
    public void deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.executeUpdate();
            System.out.println("User deleted: " + username);
        } catch (SQLException e) {
            System.err.println("deleteUser error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Checks whether the username exists.
     *
     * @param username the username used to identify the user
     * @return true when the condition is met, false otherwise
     */
    public boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("usernameExists error: " + e.getMessage());
        }
        return false;
    }

    @Override

    /**
     * Checks whether the email exists.
     *
     * @param email the email address used by the user
     * @return true when the condition is met, false otherwise
     */
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("emailExists error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Maps user.
     *
     * @param rs the result set containing the current database row
     * @return the matching user, or null if none is found
     * @throws SQLException if the operation cannot be completed
     */
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
    }
}
