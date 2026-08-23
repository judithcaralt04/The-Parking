package Persistance.dao;

import Business.entity.CancelledNotification;
import java.util.List;

/**
 * Data access contract for managing cancelled notification persistence operations.
 */
public interface CancelledNotificationDAO {

    /**
     * Creates a notification.
     *
     * @param oldSpace the previous parking space value
     * @param newSpace the new parking space value
     * @param reservationId the reservation identifier
     */
    void createNotification (String oldSpace, String newSpace, int reservationId);

    /**
     * Returns pending notifications by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    List<CancelledNotification> getPendingNotificationsByUser (String username);

    /**
     * Marks as shown.
     *
     * @param notifications the notifications to update
     */
    void markAsShown (List<CancelledNotification> notifications);
}
