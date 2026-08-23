package Business;

import Persistance.dao.*;
import Business.entity.*;
import java.util.List;

/**
 * Business service that manages vehicle entry, exit, and lookup operations.
 */
public class BusinessLogicVehicle {

    private final VehicleDAO vehicleDAO;
    private final ParkingSpaceDAO parkingSpaceDAO;
    private final ReservationDAO reservationDAO;

    /**
     * Creates a new BusinessLogicVehicle instance.
     *
     * @return the requested value
     */
    public BusinessLogicVehicle() {
        this.vehicleDAO = new VehicleDAOSQL();
        this.parkingSpaceDAO = new ParkingSpaceDAOSQL();
        this.reservationDAO = new ReservationDAOSQL();
    }

    /**
     * Enumeration that defines the available result values.
     */
    public enum Result {
        OK,
        VEHICLE_NOT_FOUND,
        VEHICLE_ALREADY_PARKED,
        VEHICLE_NOT_PARKED,
        LICENSE_PLATE_BELONGS_TO_OTHER_USER,
        NO_SPACE_AVAILABLE,
        INVALID_VEHICLE_TYPE,
        VEHICLE_TYPE_MISMATCH
    }

    /**
     * Registers entry.
     *
     * @param licensePlate the vehicle license plate
     * @param vehicleType the vehicle type
     * @param userId the user identifier
     * @return the result of the business operation
     */
    public Result registerEntry(String licensePlate, String vehicleType, int userId) {

        if (vehicleType == null || vehicleType.isBlank()) {
            return Result.INVALID_VEHICLE_TYPE;
        }
        if (!isValidVehicleType(vehicleType)) {
            return Result.INVALID_VEHICLE_TYPE;
        }

        Vehicle vehicle = vehicleDAO.getVehicleByLicensePlate(licensePlate);

        if (vehicle != null) {

            if (!vehicleBelongsToUser(vehicle, userId)) {
                return Result.LICENSE_PLATE_BELONGS_TO_OTHER_USER;
            }
            if (parkingSpaceDAO.getOccupiedSpaceByVehicle(vehicle.getVehicleId()) != null) {
                return Result.VEHICLE_ALREADY_PARKED;
            }

            if (!vehicle.getVehicleType().equalsIgnoreCase(vehicleType.trim())) {
                return Result.VEHICLE_TYPE_MISMATCH;
            }
            Reservation reservation = reservationDAO.getActiveReservationByVehicleId(vehicle.getVehicleId());
            if (reservation != null) {

                parkingSpaceDAO.occupySpace(reservation.getSpaceCode(), vehicle.getVehicleId());
                return Result.OK;
            }
        }

        List<ParkingSpace> available = parkingSpaceDAO.getAvailableSpacesByType(vehicleType.toUpperCase());
        if (available.isEmpty()) {
            return Result.NO_SPACE_AVAILABLE;
        }

        if (vehicle == null) {
            int newId = vehicleDAO.createVehicle(licensePlate, vehicleType.toUpperCase(), false, userId);
            vehicle = vehicleDAO.getVehicleById(newId);
        }
        parkingSpaceDAO.occupySpace(available.get(0).getSpaceCode(), vehicle.getVehicleId());
        return Result.OK;
    }

    /**
     * Returns assigned space.
     *
     * @param licensePlate the vehicle license plate
     * @return the requested text value
     */
    public String getAssignedSpace(String licensePlate) {
        Vehicle vehicle = vehicleDAO.getVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            return null;
        }

        ParkingSpace occupied = parkingSpaceDAO.getOccupiedSpaceByVehicle(vehicle.getVehicleId());
        return occupied == null ? null : occupied.getSpaceCode();
    }

    /**
     * Registers exit.
     *
     * @param licensePlate the vehicle license plate
     * @param userId the user identifier
     * @return the result of the business operation
     */
    public Result registerExit(String licensePlate, int userId) {
        Vehicle vehicle = vehicleDAO.getVehicleByLicensePlate(licensePlate);

        if (vehicle == null) {
            return Result.VEHICLE_NOT_FOUND;
        }

        if (!vehicleBelongsToUser(vehicle, userId)) {
            return Result.LICENSE_PLATE_BELONGS_TO_OTHER_USER;
        }

        ParkingSpace occupiedSpace = parkingSpaceDAO.getOccupiedSpaceByVehicle(vehicle.getVehicleId());
        if (occupiedSpace == null) {
            return Result.VEHICLE_NOT_PARKED;
        }

        parkingSpaceDAO.freeSpace(occupiedSpace.getSpaceCode());
        return Result.OK;
    }

    /**
     * Returns vehicles by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<Vehicle> getVehiclesByUser(String username) {
        return vehicleDAO.getVehiclesByUser(username);
    }

    /**
     * Checks whether the license plate exists.
     *
     * @param licensePlate the vehicle license plate
     * @return true when the condition is met, false otherwise
     */
    public boolean licensePlateExists(String licensePlate) {
        return vehicleDAO.licensePlateExists(licensePlate);
    }

    /**
     * Returns vehicle by license plate.
     *
     * @param licensePlate the vehicle license plate
     * @return the matching vehicle, or null if none is found
     */
    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        return vehicleDAO.getVehicleByLicensePlate(licensePlate);
    }

    /**
     * Creates vehicle for user.
     *
     * @param licensePlate the vehicle license plate
     * @param vehicleType the vehicle type
     * @param userId the user identifier
     * @return the calculated numeric value
     */
    public int createVehicleForUser(String licensePlate, String vehicleType, int userId) {
        return vehicleDAO.createVehicle(licensePlate, vehicleType.toUpperCase(), false, userId);
    }

    /**
     * Returns vehicle belongs to user.
     *
     * @param vehicle the vehicle value
     * @param userId the user identifier
     * @return true when the condition is met, false otherwise
     */
    private boolean vehicleBelongsToUser(Vehicle vehicle, int userId) {
        return vehicle.getUserId() != null && vehicle.getUserId() == userId;
    }

    /**
     * Checks whether valid vehicle type.
     *
     * @param type the vehicle type to validate
     * @return true when the condition is met, false otherwise
     */
    private boolean isValidVehicleType(String type) {
        for (VehicleType vt : VehicleType.values()) {
            if (vt.name().equalsIgnoreCase(type.trim())) {
                return true;
            }
        }
        return false;
    }
}
