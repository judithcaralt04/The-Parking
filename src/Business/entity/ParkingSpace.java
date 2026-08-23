package Business.entity;

/**
 * Entity that stores parking space data used by the parking application.
 */
public class ParkingSpace {

    private final String spaceCode;
    private final int floor;
    private final String vehicleType;
    private final boolean isOccupied;
    private final Integer parkedVehicleId;

    public ParkingSpace(String spaceCode, int floor, String vehicleType, boolean isOccupied, Integer parkedVehicleId) {
        this.spaceCode = spaceCode;
        this.floor = floor;
        this.vehicleType = vehicleType;
        this.isOccupied = isOccupied;
        this.parkedVehicleId = parkedVehicleId;
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
     * Checks whether occupied.
     *
     * @return true when the condition is met, false otherwise
     */
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * Returns parked vehicle id.
     *
     * @return the requested numeric value
     */
    public Integer getParkedVehicleId() {
        return parkedVehicleId;
    }
}
