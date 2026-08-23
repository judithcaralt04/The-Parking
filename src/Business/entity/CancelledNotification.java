package Business.entity;

/**
 * Entity that stores cancelled notification data used by the parking application.
 */
public class CancelledNotification {

    private int notificationId;
    private String oldSpace;
    private String newSpace;
    private boolean notified;
    private int reservationId;

    /**
     * Creates a new CancelledNotification instance.
     *
     * @param notificationId the notification id value
     * @param oldSpace the previous parking space value
     * @param newSpace the new parking space value
     * @param notified the notified value
     * @param reservationId the reservation identifier
     * @return the requested value
     */
    public CancelledNotification(int notificationId, String oldSpace, String newSpace, boolean notified, int reservationId) {
        this.notificationId = notificationId;
        this.oldSpace = oldSpace;
        this.newSpace = newSpace;
        this.notified = notified;
        this.reservationId = reservationId;
    }

    /**
     * Returns notification id.
     *
     * @return the calculated numeric value
     */
    public int getNotificationId() {
        return notificationId;
    }

    /**
     * Returns old space.
     *
     * @return the requested text value
     */
    public String getOldSpace() {
        return oldSpace;
    }

    /**
     * Returns new space.
     *
     * @return the requested text value
     */
    public String getNewSpace() {
        return newSpace;
    }

    /**
     * Checks whether notified.
     *
     * @return true when the condition is met, false otherwise
     */
    public boolean isNotified() {
        return notified;
    }

    /**
     * Returns reservation id.
     *
     * @return the calculated numeric value
     */
    public int getReservationId() {
        return reservationId;
    }
}
