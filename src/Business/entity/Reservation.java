package Business.entity;

import java.time.LocalDateTime;

/**
 * Entity that stores reservation data used by the parking application.
 */
public class Reservation {

    private int reservationId;
    private LocalDateTime reservationDate;
    private boolean isActive;
    private int vehicleId;
    private String spaceCode;

    public Reservation(int reservationId, LocalDateTime reservationDate, boolean isActive, int vehicleId, String spaceCode) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.isActive = isActive;
        this.vehicleId = vehicleId;
        this.spaceCode = spaceCode;
    }

    /**
     * Returns reservation id.
     *
     * @return the calculated numeric value
     */
    public int getReservationId() {
        return reservationId;
    }

    /**
     * Returns reservation date.
     *
     * @return the requested date and time value
     */
    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    /**
     * Checks whether is active.
     *
     * @return true when the condition is met, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Returns vehicle id.
     *
     * @return the calculated numeric value
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Returns space code.
     *
     * @return the requested text value
     */
    public String getSpaceCode() {
        return spaceCode;
    }
}
