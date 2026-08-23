package Persistance.dao;

import Business.entity.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL DAO implementation for managing vehicle persistence.
 */
public class VehicleDAOSQL implements VehicleDAO {

    private final Connection conn;

    /**
     * Creates a new VehicleDAOSQL instance.
     *
     * @return the requested value
     */
    public VehicleDAOSQL() {
        this.conn = SQLConnector.getInstance().getConnection();
    }

    @Override

    /**
     * Returns vehicle by id.
     *
     * @param vehicleId the vehicle identifier
     * @return the matching vehicle, or null if none is found
     */
    public Vehicle getVehicleById(int vehicleId) {
        String query = "SELECT * FROM vehicle WHERE vehicle_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapVehicle(rs);
            }
        } catch (SQLException e) {
            System.err.println("getVehicleById error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Returns vehicle by license plate.
     *
     * @param licensePlate the vehicle license plate
     * @return the matching vehicle, or null if none is found
     */
    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        String query = "SELECT * FROM vehicle WHERE license_plate = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, licensePlate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapVehicle(rs);
            }
        } catch (SQLException e) {
            System.err.println("getVehicleByLicensePlate error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Returns vehicles by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<Vehicle> getVehiclesByUser(String username) {
        String query = "SELECT v.* FROM vehicle v " + "JOIN users u ON v.user_id = u.user_id " + "WHERE u.username = ?";
        List<Vehicle> vehicles = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vehicles.add(mapVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("getVehiclesByUser error: " + e.getMessage());
        }
        return vehicles;
    }

    @Override

    /**
     * Returns simulated parked vehicles.
     *
     * @return the requested list of records
     */
    public List<Vehicle> getSimulatedParkedVehicles() {
        String query = "SELECT v.* FROM vehicle v " +
                "JOIN parking_space ps ON ps.parked_vehicle_id = v.vehicle_id " +
                "WHERE v.is_simulated = 1 AND ps.is_occupied = 1";
        List<Vehicle> vehicles = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vehicles.add(mapVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("getSimulatedParkedVehicles error: " + e.getMessage());
        }
        return vehicles;
    }

    @Override

    /**
     * Checks whether the license plate exists.
     *
     * @param licensePlate the vehicle license plate
     * @return true when the condition is met, false otherwise
     */
    public boolean licensePlateExists(String licensePlate) {
        String query = "SELECT COUNT(*) FROM vehicle WHERE license_plate = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, licensePlate);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("licensePlateExists error: " + e.getMessage());
        }
        return false;
    }

    @Override

    /**
     * Creates vehicle.
     *
     * @param licensePlate the vehicle license plate
     * @param vehicleType the vehicle type
     * @param isSimulated true when the vehicle belongs to the simulation
     * @param userId the user identifier
     * @return the calculated numeric value
     */
    public int createVehicle(String licensePlate, String vehicleType, boolean isSimulated, Integer userId) {
        String query = "INSERT INTO vehicle (license_plate, vehicle_type, is_simulated, user_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, licensePlate);
            ps.setString(2, vehicleType.toUpperCase());
            ps.setBoolean(3, isSimulated);

            if (userId == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, userId);
            }
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("createVehicle error: " + e.getMessage());
        }
        return -1;
    }

    @Override

    /**
     * Deletes vehicle.
     *
     * @param licensePlate the vehicle license plate
     */
    public void deleteVehicle(String licensePlate) {
        String query = "DELETE FROM vehicle WHERE license_plate = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, licensePlate);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteVehicle error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Deletes vehicles by user.
     *
     * @param username the username used to identify the user
     */
    public void deleteVehiclesByUser(String username) {
        String query = "DELETE v FROM vehicle v " + "JOIN users u ON v.user_id = u.user_id " + "WHERE u.username = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteVehiclesByUser error: " + e.getMessage());
        }
    }

    /**
     * Maps vehicle.
     *
     * @param rs the result set containing the current database row
     * @return the matching vehicle, or null if none is found
     * @throws SQLException if the operation cannot be completed
     */
    private Vehicle mapVehicle(ResultSet rs) throws SQLException {
        int raw = rs.getInt("user_id");
        Integer userId;

        if (rs.wasNull()) {
            userId = null;
        } else {
            userId = raw;
        }
        return new Vehicle(rs.getInt("vehicle_id"), rs.getString("license_plate"), rs.getString("vehicle_type"), rs.getBoolean("is_simulated"), userId);
    }
}
