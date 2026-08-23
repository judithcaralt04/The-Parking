package Persistance.dao;

import Business.entity.BookingInfo;
import Business.entity.Reservation;
import java.util.List;

/**
 * Data access contract for managing reservation persistence operations.
 */
public interface ReservationDAO {

    /**
     * Returns reservation by id.
     *
     * @param reservationId the reservation identifier
     * @return the matching reservation, or null if none is found
     */
    Reservation getReservationById (int reservationId);

    /**
     * Returns reservation by space code.
     *
     * @param spaceCode the parking space code
     * @return the matching reservation, or null if none is found
     */
    Reservation getReservationBySpaceCode (String spaceCode);

    /**
     * Returns active reservation by vehicle id.
     *
     * @param vehicleId the vehicle identifier
     * @return the matching reservation, or null if none is found
     */
    Reservation getActiveReservationByVehicleId (int vehicleId);

    /**
     * Returns active reservations by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    List<Reservation> getActiveReservationsByUser (String username);

    /**
     * Checks whether active reservation.
     *
     * @param spaceCode the parking space code
     * @return true when the condition is met, false otherwise
     */
    boolean hasActiveReservation (String spaceCode);

    /**
     * Creates reservation.
     *
     * @param vehicleId the vehicle identifier
     * @param spaceCode the parking space code
     */
    void createReservation (int vehicleId, String spaceCode);

    /**
     * Cancels reservation.
     *
     * @param reservationId the reservation identifier
     */
    void cancelReservation (int reservationId);

    /**
     * Updates space code.
     *
     * @param reservationId the reservation identifier
     * @param newSpaceCode the new parking space code assigned to the reservation
     */
    void updateSpaceCode (int reservationId, String newSpaceCode);

    /**
     * Returns all active bookings.
     *
     * @return the requested list of records
     */
    List<BookingInfo> getAllActiveBookings ();

    /**
     * Returns active bookings by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    List<BookingInfo> getActiveBookingsByUser(String username);
}
