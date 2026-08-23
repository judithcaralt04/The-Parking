package Persistance.dao;

import Business.entity.ParkingSpace;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL DAO implementation for managing parking space persistence.
 */
public class ParkingSpaceDAOSQL implements ParkingSpaceDAO {

    private final Connection conn;

    /**
     * Creates a new ParkingSpaceDAOSQL instance.
     *
     * @return the requested value
     */
    public ParkingSpaceDAOSQL() {
        this.conn = SQLConnector.getInstance().getConnection();
    }

    @Override

    /**
     * Returns space by code.
     *
     * @param code the parking space code
     * @return the matching parking space, or null if none is found
     */
    public ParkingSpace getSpaceByCode(String code) {
        String query = "SELECT * FROM parking_space WHERE space_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapSpace(rs);
            }
        } catch (SQLException e) {
            System.err.println("getSpaceByCode error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Returns all spaces.
     *
     * @return the requested list of records
     */
    public List<ParkingSpace> getAllSpaces() {
        String query = "SELECT * FROM parking_space";
        List<ParkingSpace> spaces = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                spaces.add(mapSpace(rs));
            }
        } catch (SQLException e) {
            System.err.println("getAllSpaces error: " + e.getMessage());
        }
        return spaces;
    }

    @Override

    /**
     * Returns space count.
     *
     * @return the calculated numeric value
     */
    public int getSpaceCount() {
        String query = "SELECT COUNT(*) FROM parking_space";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("getSpaceCount error: " + e.getMessage());
        }
        return 0;
    }

    @Override

    /**
     * Returns available spaces by type.
     *
     * @param vehicleType the vehicle type
     * @return the requested list of records
     */
    public List<ParkingSpace> getAvailableSpacesByType(String vehicleType) {
        String query = "SELECT ps.* FROM parking_space ps " + "LEFT JOIN reservation r ON ps.space_code = r.space_code AND r.is_active = 1 " + "WHERE ps.vehicle_type = ? AND ps.is_occupied = 0 AND r.space_code IS NULL";
        List<ParkingSpace> spaces = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, vehicleType.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                spaces.add(mapSpace(rs));
            }
        } catch (SQLException e) {
            System.err.println("getAvailableSpacesByType error: " + e.getMessage());
        }
        return spaces;
    }

    @Override

    /**
     * Returns occupied space by vehicle.
     *
     * @param vehicleId the vehicle identifier
     * @return the matching parking space, or null if none is found
     */
    public ParkingSpace getOccupiedSpaceByVehicle(int vehicleId) {
        String query = "SELECT * FROM parking_space WHERE parked_vehicle_id = ? AND is_occupied = 1";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapSpace(rs);
            }
        } catch (SQLException e) {
            System.err.println("getOccupiedSpaceByVehicle error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Checks whether the space exists.
     *
     * @param code the parking space code
     * @return true when the condition is met, false otherwise
     */
    public boolean spaceExists(String code) {
        String query = "SELECT COUNT(*) FROM parking_space WHERE space_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("spaceExists error: " + e.getMessage());
        }
        return false;
    }

    @Override

    /**
     * Creates space.
     *
     * @param code the parking space code
     * @param floor the parking floor number
     * @param vehicleType the vehicle type
     */
    public void createSpace(String code, int floor, String vehicleType) {
        String query = "INSERT INTO parking_space (space_code, floor, vehicle_type, is_occupied) VALUES (?, ?, ?, 0)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, code);
            ps.setInt(2, floor);
            ps.setString(3, vehicleType.toUpperCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("createSpace error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Updates space.
     *
     * @param code the parking space code
     * @param floor the parking floor number
     * @param vehicleType the vehicle type
     */
    public void updateSpace(String code, int floor, String vehicleType) {
        String query = "UPDATE parking_space SET floor = ?, vehicle_type = ? WHERE space_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, floor);
            ps.setString(2, vehicleType.toUpperCase());
            ps.setString(3, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateSpace error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Marks as occupied space.
     *
     * @param code the parking space code
     * @param vehicleId the vehicle identifier
     */
    public void occupySpace(String code, int vehicleId) {
        String query = "UPDATE parking_space SET is_occupied = 1, parked_vehicle_id = ? WHERE space_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            ps.setString(2, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("occupySpace error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Frees space.
     *
     * @param code the parking space code
     */
    public void freeSpace(String code) {
        String query = "UPDATE parking_space SET is_occupied = 0, parked_vehicle_id = NULL WHERE space_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("freeSpace error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Deletes space.
     *
     * @param code the parking space code
     */
    public void deleteSpace(String code) {
        String query = "DELETE FROM parking_space WHERE space_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteSpace error: " + e.getMessage());
        }
    }

    /**
     * Maps space.
     *
     * @param rs the result set containing the current database row
     * @return the matching parking space, or null if none is found
     * @throws SQLException if the operation cannot be completed
     */
    private ParkingSpace mapSpace(ResultSet rs) throws SQLException {
        int raw = rs.getInt("parked_vehicle_id");
        Integer parkedVehicleId = rs.wasNull() ? null : raw;
        return new ParkingSpace(rs.getString("space_code"), rs.getInt("floor"), rs.getString("vehicle_type"), rs.getBoolean("is_occupied"), parkedVehicleId);
    }
}
