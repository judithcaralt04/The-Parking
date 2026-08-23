package Business;

import Persistance.dao.*;
import Business.entity.*;
import java.util.List;

/**
 * Business service that manages reservation creation, cancellation, and booking queries.
 */
public class BusinessLogicReservation {

    private final ReservationDAO reservationDAO;
    private final ParkingSpaceDAO parkingSpaceDAO;
    private final VehicleDAO vehicleDAO;
    private final CancelledNotificationDAO cancelledNotificationDAO;

    /**
     * Creates a new BusinessLogicReservation instance.
     *
     * @return the requested value
     */
    public BusinessLogicReservation() {
        this.reservationDAO = new ReservationDAOSQL();
        this.parkingSpaceDAO = new ParkingSpaceDAOSQL();
        this.vehicleDAO = new VehicleDAOSQL();
        this.cancelledNotificationDAO = new CancelledNotificationDAOSQL();
    }

    /**
     * Enumeration that defines the available result values.
     */
    public enum Result {
        OK,
        RESERVATION_REASSIGNED,
        RESERVATION_CANCELLED,
        SPACE_NOT_FOUND,
        SPACE_ALREADY_RESERVED,
        SPACE_IS_OCCUPIED,
        VEHICLE_TYPE_MISMATCH,
        RESERVATION_NOT_FOUND,
        NO_EQUIVALENT_SPACE,
        INVALID_VEHICLE_TYPE,
        LICENSE_PLATE_BELONGS_TO_OTHER_USER,
        INCONSISTENT_SPACE_STATE,
        VEHICLE_IS_PARKED
    }

    /**
     * Creates reservation.
     *
     * @param licensePlate the vehicle license plate
     * @param vehicleType the vehicle type
     * @param spaceCode the parking space code
     * @param userId the user identifier
     * @return the result of the business operation
     */
    public Result makeReservation(String licensePlate, String vehicleType, String spaceCode, int userId) {

        if (!isValidVehicleType(vehicleType)) {
            return Result.INVALID_VEHICLE_TYPE;
        }

        ParkingSpace space = parkingSpaceDAO.getSpaceByCode(spaceCode);
        if (space == null) {
            return Result.SPACE_NOT_FOUND;
        }
        if (space.isOccupied()) {
            return Result.SPACE_IS_OCCUPIED;
        }
        if (!space.getVehicleType().equalsIgnoreCase(vehicleType)) {
            return Result.VEHICLE_TYPE_MISMATCH;
        }
        if (reservationDAO.hasActiveReservation(spaceCode)) {
            return Result.SPACE_ALREADY_RESERVED;
        }

        Vehicle vehicle = vehicleDAO.getVehicleByLicensePlate(licensePlate);

        int vehicleId;

        if (vehicle == null) {
            vehicleId = vehicleDAO.createVehicle(licensePlate, vehicleType.toUpperCase(), false, userId);
        } else {
            if (vehicle.getUserId() == null || vehicle.getUserId() != userId || vehicle.isSimulated()) {
                return Result.LICENSE_PLATE_BELONGS_TO_OTHER_USER;
            }
            if (!vehicle.getVehicleType().equalsIgnoreCase(vehicleType.trim())) {
                return Result.VEHICLE_TYPE_MISMATCH;
            }
            vehicleId = vehicle.getVehicleId();
        }

        reservationDAO.createReservation(vehicleId, spaceCode);
        return Result.OK;
    }

    /**
     * Returns reservations by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<Reservation> getReservationsByUser(String username) {
        return reservationDAO.getActiveReservationsByUser(username);
    }

    /**
     * Returns booking infos by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<BookingInfo> getBookingInfosByUser(String username) {
        return reservationDAO.getActiveBookingsByUser(username);
    }

    /**
     * Returns available spaces.
     *
     * @param vehicleType the vehicle type
     * @return the requested list of records
     */
    public List<ParkingSpace> getAvailableSpaces(String vehicleType) {
        return parkingSpaceDAO.getAvailableSpacesByType(vehicleType.toUpperCase());
    }

    /**
     * Cancels reservation.
     *
     * @param reservationId the reservation identifier
     * @return the result of the business operation
     */
    public Result cancelReservation(int reservationId) {
        Reservation reservation = reservationDAO.getReservationById(reservationId);

        if (reservation == null) {
            return Result.RESERVATION_NOT_FOUND;
        }

        ParkingSpace space = parkingSpaceDAO.getSpaceByCode(reservation.getSpaceCode());

        if (space != null
                && space.isOccupied()
                && space.getParkedVehicleId() != null
                && space.getParkedVehicleId() == reservation.getVehicleId()) {
            return Result.VEHICLE_IS_PARKED;
        }

        reservationDAO.cancelReservation(reservationId);
        return Result.OK;
    }

    /**
     * Returns admin cancel reservation.
     *
     * @param spaceCode the parking space code
     * @return the result of the business operation
     */
    public Result adminCancelReservation(String spaceCode) {
        Reservation reservation = reservationDAO.getReservationBySpaceCode(spaceCode);

        if (reservation == null) {
            return Result.RESERVATION_NOT_FOUND;
        }

        ParkingSpace space = parkingSpaceDAO.getSpaceByCode(spaceCode);
        if (space == null) {
            return Result.SPACE_NOT_FOUND;
        }

        if (space.isOccupied()) {
            Integer parkedVehicleId = space.getParkedVehicleId();

            if (parkedVehicleId != null && parkedVehicleId.equals(reservation.getVehicleId())) {
                return Result.VEHICLE_IS_PARKED;
            }
            return Result.INCONSISTENT_SPACE_STATE;
        }

        ParkingSpace newSpace = findEquivalentAvailableSpace(space.getVehicleType(), spaceCode);

        if (newSpace == null) {
            reservationDAO.cancelReservation(reservation.getReservationId());
            cancelledNotificationDAO.createNotification(spaceCode, "CANCELLED", reservation.getReservationId());
            return Result.RESERVATION_CANCELLED;
        }

        reservationDAO.updateSpaceCode(reservation.getReservationId(), newSpace.getSpaceCode());
        cancelledNotificationDAO.createNotification(spaceCode, newSpace.getSpaceCode(), reservation.getReservationId());

        return Result.RESERVATION_REASSIGNED;
    }

    /**
     * Finds equivalent available space.
     *
     * @param vehicleType the vehicle type
     * @param excludeCode the parking space code to exclude from the search
     * @return the matching parking space, or null if none is found
     */
    private ParkingSpace findEquivalentAvailableSpace(String vehicleType, String excludeCode) {
        List<ParkingSpace> candidates = parkingSpaceDAO.getAvailableSpacesByType(vehicleType);

        for (ParkingSpace candidate : candidates) {
            if (!candidate.getSpaceCode().equals(excludeCode)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Returns pending notifications.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<CancelledNotification> getPendingNotifications(String username) {
        return cancelledNotificationDAO.getPendingNotificationsByUser(username);
    }

    /**
     * Marks notifications as shown.
     *
     * @param notifications the notifications to update
     */
    public void markNotificationsAsShown(List<CancelledNotification> notifications) {
        cancelledNotificationDAO.markAsShown(notifications);
    }

    /**
     * Returns all bookings.
     *
     * @return the requested list of records
     */
    public List<BookingInfo> getAllBookings() {
        return reservationDAO.getAllActiveBookings();
    }

    /**
     * Checks whether valid vehicle type.
     *
     * @param type the vehicle type to validate
     * @return true when the condition is met, false otherwise
     */
    private boolean isValidVehicleType(String type) {
        if (type == null) {
            return false;
        }

        for (VehicleType vt : VehicleType.values()) {
            if (vt.name().equalsIgnoreCase(type.trim())) {
                return true;
            }
        }
        return false;
    }
}
