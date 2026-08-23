package Persistance.dao;

import Business.entity.Vehicle;
import java.util.List;

/**
 * Data access contract for managing vehicle persistence operations.
 */
public interface VehicleDAO {

    /**
     * Returns vehicle by id.
     *
     * @param vehicleId the vehicle identifier
     * @return the matching vehicle, or null if none is found
     */
    Vehicle getVehicleById (int vehicleId);

    /**
     * Returns vehicle by license plate.
     *
     * @param licensePlate the vehicle license plate
     * @return the matching vehicle, or null if none is found
     */
    Vehicle getVehicleByLicensePlate (String licensePlate);

    /**
     * Returns vehicles by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    List<Vehicle> getVehiclesByUser (String username);

    /**
     * Returns simulated parked vehicles.
     *
     * @return the requested list of records
     */
    List<Vehicle> getSimulatedParkedVehicles ();

    /**
     * Checks whether the license plate exists.
     *
     * @param licensePlate the vehicle license plate
     * @return true when the condition is met, false otherwise
     */
    boolean licensePlateExists (String licensePlate);

    /**
     * Creates vehicle.
     *
     * @param licensePlate the vehicle license plate
     * @param vehicleType the vehicle type
     * @param isSimulated true when the vehicle belongs to the simulation
     * @param userId the user identifier
     * @return the calculated numeric value
     */
    int createVehicle (String licensePlate, String vehicleType, boolean isSimulated, Integer userId);

    /**
     * Deletes vehicle.
     *
     * @param licensePlate the vehicle license plate
     */
    void deleteVehicle (String licensePlate);

    /**
     * Deletes vehicles by user.
     *
     * @param username the username used to identify the user
     */
    void deleteVehiclesByUser (String username);
}
