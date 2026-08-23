package Persistance.dao;

import Business.entity.ParkingSpace;

import java.util.List;

/**
 * Data access contract for managing parking space persistence operations.
 */
public interface ParkingSpaceDAO {

    /**
     * Returns space by code.
     *
     * @param code the parking space code
     * @return the matching parking space, or null if none is found
     */
    ParkingSpace getSpaceByCode(String code);

    /**
     * Returns all spaces.
     *
     * @return the requested list of records
     */
    List<ParkingSpace> getAllSpaces();

    /**
     * Returns space count.
     *
     * @return the calculated numeric value
     */
    int getSpaceCount();

    /**
     * Returns available spaces by type.
     *
     * @param vehicleType the vehicle type
     * @return the requested list of records
     */
    List<ParkingSpace> getAvailableSpacesByType(String vehicleType);

    /**
     * Returns occupied space by vehicle.
     *
     * @param vehicleId the vehicle identifier
     * @return the matching parking space, or null if none is found
     */
    ParkingSpace getOccupiedSpaceByVehicle(int vehicleId);

    /**
     * Checks whether the space exists.
     *
     * @param code the parking space code
     * @return true when the condition is met, false otherwise
     */
    boolean spaceExists(String code);

    /**
     * Creates space.
     *
     * @param code the parking space code
     * @param floor the parking floor number
     * @param vehicleType the vehicle type
     */
    void createSpace(String code, int floor, String vehicleType);

    /**
     * Updates space.
     *
     * @param code the parking space code
     * @param floor the parking floor number
     * @param vehicleType the vehicle type
     */
    void updateSpace(String code, int floor, String vehicleType);

    /**
     * Marks as occupied space.
     *
     * @param code the parking space code
     * @param vehicleId the vehicle identifier
     */
    void occupySpace(String code, int vehicleId);

    /**
     * Frees space.
     *
     * @param code the parking space code
     */
    void freeSpace(String code);

    /**
     * Deletes space.
     *
     * @param code the parking space code
     */
    void deleteSpace(String code);
}
