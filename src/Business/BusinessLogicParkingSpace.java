package Business;

import Persistance.dao.CancelledNotificationDAO;
import Persistance.dao.CancelledNotificationDAOSQL;
import Persistance.dao.ParkingSpaceDAO;
import Persistance.dao.ParkingSpaceDAOSQL;
import Persistance.dao.ReservationDAO;
import Persistance.dao.ReservationDAOSQL;
import Persistance.dao.VehicleDAO;
import Persistance.dao.VehicleDAOSQL;
import Business.entity.ParkingSpace;
import Business.entity.Reservation;
import Business.entity.Vehicle;
import java.util.List;
import java.util.ArrayList;

/**
 * Business service that manages parking space creation, updates, deletion, and status queries.
 */
public class BusinessLogicParkingSpace {

    private final ParkingSpaceDAO parkingSpaceDAO;
    private final ReservationDAO reservationDAO;
    private final VehicleDAO vehicleDAO;
    private final CancelledNotificationDAO cancelledNotificationDAO;

    /**
     * Creates a new BusinessLogicParkingSpace instance.
     *
     * @return the requested value
     */
    public BusinessLogicParkingSpace() {
        this.parkingSpaceDAO = new ParkingSpaceDAOSQL();
        this.reservationDAO = new ReservationDAOSQL();
        this.vehicleDAO = new VehicleDAOSQL();
        this.cancelledNotificationDAO = new CancelledNotificationDAOSQL();
    }

    /**
     * Enumeration that defines the available result values.
     */
    public enum Result {
        OK,
        CODE_ALREADY_EXISTS,
        INVALID_FLOOR,
        INVALID_VEHICLE_TYPE,
        SPACE_NOT_FOUND,
        SPACE_IS_OCCUPIED,
        SPACE_ALREADY_RESERVED,
        NO_EQUIVALENT_SPACE
    }

    /**
     * Creates space.
     *
     * @param code the parking space code
     * @param floorStr the parking floor value as text
     * @param vehicleType the vehicle type
     * @return the result of the business operation
     */
    public Result createSpace(String code, String floorStr, String vehicleType) {
        Result validation = validateFields(code, floorStr, vehicleType, true);
        if (validation != Result.OK) {
            return validation;
        }
        parkingSpaceDAO.createSpace(code.trim(), Integer.parseInt(floorStr.trim()), vehicleType.trim().toUpperCase());
        return Result.OK;
    }

    /**
     * Returns edit space.
     *
     * @param code the parking space code
     * @param floorStr the parking floor value as text
     * @param vehicleType the vehicle type
     * @return the result of the business operation
     */
    public Result editSpace(String code, String floorStr, String vehicleType) {
        Result validation = validateFields(code, floorStr, vehicleType, false);

        if (validation != Result.OK) {
            return validation;
        }

        ParkingSpace space = parkingSpaceDAO.getSpaceByCode(code.trim());

        if (space != null && space.isOccupied()) {
            return Result.SPACE_IS_OCCUPIED;
        }

        Reservation reservation = reservationDAO.getReservationBySpaceCode(code.trim());

        if (reservation != null && !space.getVehicleType().equalsIgnoreCase(vehicleType.trim())) {
            return Result.SPACE_ALREADY_RESERVED;
        }

        parkingSpaceDAO.updateSpace(code.trim(), Integer.parseInt(floorStr.trim()), vehicleType.trim().toUpperCase());

        return Result.OK;
    }

    /**
     * Deletes space.
     *
     * @param code the parking space code
     * @return the result of the business operation
     */
    public Result deleteSpace(String code) {
        ParkingSpace space = parkingSpaceDAO.getSpaceByCode(code.trim());

        if (space == null) {
            return Result.SPACE_NOT_FOUND;
        }
        if (space.isOccupied()) {
            return Result.SPACE_IS_OCCUPIED;
        }

        Reservation reservation = reservationDAO.getReservationBySpaceCode(code.trim());

        if (reservation != null) {
            ParkingSpace newSpace = findEquivalentAvailableSpace(space.getVehicleType(), code.trim());

            if (newSpace == null) {
                return Result.NO_EQUIVALENT_SPACE;
            }

            reservationDAO.updateSpaceCode(reservation.getReservationId(), newSpace.getSpaceCode());
            cancelledNotificationDAO.createNotification(code.trim(), newSpace.getSpaceCode(), reservation.getReservationId());
        }

        parkingSpaceDAO.deleteSpace(code.trim());
        return Result.OK;
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
     * Returns all spaces.
     *
     * @return the requested list of records
     */
    public List<ParkingSpace> getAllSpaces() {
        return parkingSpaceDAO.getAllSpaces();
    }

    /**
     * Returns space count.
     *
     * @return the calculated numeric value
     */
    public int getSpaceCount() {
        return parkingSpaceDAO.getSpaceCount();
    }

    /**
     * Returns space by code.
     *
     * @param c the parking space code
     * @return the matching parking space, or null if none is found
     */
    public ParkingSpace getSpaceByCode(String c) {
        return parkingSpaceDAO.getSpaceByCode(c);
    }

    /**
     * Counts occupied.
     *
     * @return the calculated numeric value
     */
    public long countOccupied() {
        long count = 0;

        for (ParkingSpace space : getAllSpaces()) {
            if (space.isOccupied()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns parking status rows.
     *
     * @return the requested list of records
     */
    public List<ParkingStatusRow> getParkingStatusRows() {
        List<ParkingStatusRow> rows = new ArrayList<>();

        for (ParkingSpace space : getAllSpaces()) {
            Vehicle parkedVehicle = null;

            if (space.getParkedVehicleId() != null) {
                parkedVehicle = vehicleDAO.getVehicleById(space.getParkedVehicleId());
            }

            Reservation reservation = reservationDAO.getReservationBySpaceCode(space.getSpaceCode());
            Vehicle reservedVehicle = null;

            if (reservation != null) {
                reservedVehicle = vehicleDAO.getVehicleById(reservation.getVehicleId());
            }

            rows.add(new ParkingStatusRow(space.getSpaceCode(), space.getFloor(),
                    space.getVehicleType(),
                    space.isOccupied() ? "Ocupada" : "Libre",
                    reservation == null ? "Disponible" : "Reservada",
                    parkedVehicle == null ? "" : parkedVehicle.getLicensePlate(),
                    reservedVehicle == null ? "" : reservedVehicle.getLicensePlate()
            ));
        }

        return rows;
    }

    /**
     * Returns parking status row.
     *
     * @param code the parking space code
     * @return the requested value
     */
    public ParkingStatusRow getParkingStatusRow(String code) {
        for (ParkingStatusRow row : getParkingStatusRows()) {
            if (row.getCode().equals(code)) {
                return row;
            }
        }
        return null;
    }

    /**
     * Validates fields.
     *
     * @param code the parking space code
     * @param floorStr the parking floor value as text
     * @param vehicleType the vehicle type
     * @param checkCodeUnique true when the parking space code must be unique
     * @return the result of the business operation
     */
    private Result validateFields(String code, String floorStr, String vehicleType, boolean checkCodeUnique) {
        if (code == null || code.isBlank()) {
            return Result.SPACE_NOT_FOUND;
        }

        if (checkCodeUnique) {
            if (parkingSpaceDAO.spaceExists(code.trim())) {
                return Result.CODE_ALREADY_EXISTS;
            }
        } else {
            if (!parkingSpaceDAO.spaceExists(code.trim())) {
                return Result.SPACE_NOT_FOUND;
            }
        }

        try {
            if (Integer.parseInt(floorStr.trim()) < 0) {
                return Result.INVALID_FLOOR;
            }
        } catch (NumberFormatException e) {
            return Result.INVALID_FLOOR;
        }

        if (!isValidVehicleType(vehicleType)) {
            return Result.INVALID_VEHICLE_TYPE;
        }
        return Result.OK;
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
