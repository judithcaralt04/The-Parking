package Business;

/**
 * Data row used to display parking status information.
 */
public class ParkingStatusRow {

    private final String code;
    private final int floor;
    private final String vehicleType;
    private final String occupationStatus;
    private final String reservationStatus;
    private final String parkedPlate;
    private final String reservedPlate;

    public ParkingStatusRow(String code, int floor, String vehicleType, String occupationStatus, String reservationStatus, String parkedPlate, String reservedPlate) {
        this.code = code;
        this.floor = floor;
        this.vehicleType = vehicleType;
        this.occupationStatus = occupationStatus;
        this.reservationStatus = reservationStatus;
        this.parkedPlate = parkedPlate;
        this.reservedPlate = reservedPlate;
    }

    /**
     * Returns code.
     *
     * @return the requested text value
     */
    public String getCode() {
        return code;
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
     * Returns occupation status.
     *
     * @return the requested text value
     */
    public String getOccupationStatus() {
        return occupationStatus;
    }

    /**
     * Returns reservation status.
     *
     * @return the requested text value
     */
    public String getReservationStatus() {
        return reservationStatus;
    }

    /**
     * Returns parked plate.
     *
     * @return the requested text value
     */
    public String getParkedPlate() {
        return parkedPlate;
    }

    /**
     * Returns reserved plate.
     *
     * @return the requested text value
     */
    public String getReservedPlate() {
        return reservedPlate;
    }
}
