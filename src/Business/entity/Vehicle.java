package Business.entity;

/**
 * Entity that stores vehicle data used by the parking application.
 */
public class Vehicle {

    private int vehicleId;
    private String licensePlate;
    private String vehicleType;
    private boolean isSimulated;
    private Integer userId;

    public Vehicle(int vehicleId, String licensePlate, String vehicleType, boolean isSimulated, Integer userId) {
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.isSimulated = isSimulated;
        this.userId = userId;
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
     * Returns license plate.
     *
     * @return the requested text value
     */
    public String getLicensePlate() {
        return licensePlate;
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
     * Checks whether simulated.
     *
     * @return true when the condition is met, false otherwise
     */
    public boolean isSimulated() {
        return isSimulated;
    }

    /**
     * Returns user id.
     *
     * @return the requested numeric value
     */
    public Integer getUserId() {
        return userId;
    }
}
