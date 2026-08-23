package Persistance.dao;

import Business.entity.CancelledNotification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL DAO implementation for managing cancelled notification persistence.
 */
public class CancelledNotificationDAOSQL implements CancelledNotificationDAO {

    private final Connection conn;

    /**
     * Creates a new CancelledNotificationDAOSQL instance.
     *
     * @return the requested value
     */
    public CancelledNotificationDAOSQL() {
        this.conn = SQLConnector.getInstance().getConnection();
    }

    @Override

    /**
     * Creates notification.
     *
     * @param oldSpace the previous parking space value
     * @param newSpace the new parking space value
     * @param reservationId the reservation identifier
     */
    public void createNotification(String oldSpace, String newSpace, int reservationId) {
        String query = "INSERT INTO cancelled_notification (old_space, new_space, notified, reservation_id) VALUES (?, ?, 0, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, oldSpace);
            ps.setString(2, newSpace);
            ps.setInt(3, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("createNotification error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Returns pending notifications by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<CancelledNotification> getPendingNotificationsByUser(String username) {
        String query = "SELECT cn.* FROM cancelled_notification cn " + "JOIN reservation r ON cn.reservation_id = r.reservation_id " + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id " + "JOIN users u ON v.user_id = u.user_id " + "WHERE u.username = ? AND cn.notified = 0";
        List<CancelledNotification> notifications = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                notifications.add(mapNotification(rs));
            }
        } catch (SQLException e) {
            System.err.println("getPendingNotificationsByUser error: " + e.getMessage());
        }
        return notifications;
    }

    @Override

    /**
     * Marks as shown.
     *
     * @param notifications the notifications to update
     */
    public void markAsShown(List<CancelledNotification> notifications) {
        String query = "UPDATE cancelled_notification SET notified = 1 WHERE notification_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (CancelledNotification n : notifications) {
                ps.setInt(1, n.getNotificationId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("markAsShown error: " + e.getMessage());
        }
    }

    /**
     * Maps notification.
     *
     * @param rs the result set containing the current database row
     * @return the matching notification, or null if none is found
     * @throws SQLException if the operation cannot be completed
     */
    private CancelledNotification mapNotification(ResultSet rs) throws SQLException {
        return new CancelledNotification(rs.getInt("notification_id"), rs.getString("old_space"), rs.getString("new_space"), rs.getBoolean("notified"), rs.getInt("reservation_id"));
    }
}
