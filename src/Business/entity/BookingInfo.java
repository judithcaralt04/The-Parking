package Business.entity;

import java.time.LocalDateTime;

/**
 * Entity that stores booking info data used by the parking application.
 */
public class BookingInfo {

    private final int reservationId;
    private final String spaceCode;
    private final int floor;
    private final String vehicleType;
    private final String licensePlate;
    private final String username;
    private final String email;
    private final LocalDateTime reservationDate;

    /**
     * Creates a new BookingInfo instance.
     *
     * @param reservationId the reservation identifier
     * @param spaceCode the parking space code
     * @param floor the parking floor number
     * @param vehicleType the vehicle type
     * @param licensePlate the vehicle license plate
     * @param username the username used to identify the user
     * @param email the email address used by the user
     * @param reservationDate the reservation date value
     * @return the requested value
     */
    public BookingInfo(int reservationId, String spaceCode, int floor, String vehicleType, String licensePlate, String username, String email, LocalDateTime reservationDate) {
        this.reservationId = reservationId;
        this.spaceCode = spaceCode;
        this.floor = floor;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.username = username;
        this.email = email;
        this.reservationDate = reservationDate;
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
     * Returns space code.
     *
     * @return the requested text value
     */
    public String getSpaceCode() {
        return spaceCode;
    }

    /**
     * Returns floor.
     *
     * @return the calculated numeric value
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Returns vehicle type.
     *
     * @return the requested text value
     */
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * Returns license plate.
     *
     * @return the requested text value
     */
    public String getLicensePlate() {
        return licensePlate;
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
     * Returns reservation date.
     *
     * @return the requested date and time value
     */
    public LocalDateTime getReservationDate() {
        return reservationDate;
    }
}
