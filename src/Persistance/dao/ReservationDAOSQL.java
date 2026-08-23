package Persistance.dao;

import Business.entity.BookingInfo;
import Business.entity.Reservation;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL DAO implementation for managing reservation persistence.
 */
public class ReservationDAOSQL implements ReservationDAO {

    private final Connection conn;

    /**
     * Creates a new ReservationDAOSQL instance.
     *
     * @return the requested value
     */
    public ReservationDAOSQL() {
        this.conn = SQLConnector.getInstance().getConnection();
    }

    @Override

    /**
     * Returns reservation by id.
     *
     * @param reservationId the reservation identifier
     * @return the matching reservation, or null if none is found
     */
    public Reservation getReservationById(int reservationId) {
        String query = "SELECT * FROM reservation WHERE reservation_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapReservation(rs);
            }
        } catch (SQLException e) {
            System.err.println("getReservationById error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Returns reservation by space code.
     *
     * @param spaceCode the parking space code
     * @return the matching reservation, or null if none is found
     */
    public Reservation getReservationBySpaceCode(String spaceCode) {
        String query = "SELECT * FROM reservation WHERE space_code = ? AND is_active = 1";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, spaceCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapReservation(rs);
            }
        } catch (SQLException e) {
            System.err.println("getReservationBySpaceCode error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Returns active reservation by vehicle id.
     *
     * @param vehicleId the vehicle identifier
     * @return the matching reservation, or null if none is found
     */
    public Reservation getActiveReservationByVehicleId(int vehicleId) {
        String query = "SELECT * FROM reservation WHERE vehicle_id = ? AND is_active = 1";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapReservation(rs);
            }
        } catch (SQLException e) {
            System.err.println("getActiveReservationByVehicleId error: " + e.getMessage());
        }
        return null;
    }

    @Override

    /**
     * Returns active reservations by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<Reservation> getActiveReservationsByUser(String username) {
        String query = "SELECT r.* FROM reservation r " + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id " + "JOIN users u ON v.user_id = u.user_id " + "WHERE u.username = ? AND r.is_active = 1";
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
        } catch (SQLException e) {
            System.err.println("getActiveReservationsByUser error: " + e.getMessage());
        }
        return reservations;
    }

    @Override

    /**
     * Checks whether active reservation.
     *
     * @param spaceCode the parking space code
     * @return true when the condition is met, false otherwise
     */
    public boolean hasActiveReservation(String spaceCode) {
        String query = "SELECT COUNT(*) FROM reservation WHERE space_code = ? AND is_active = 1";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, spaceCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("hasActiveReservation error: " + e.getMessage());
        }
        return false;
    }

    @Override

    /**
     * Creates reservation.
     *
     * @param vehicleId the vehicle identifier
     * @param spaceCode the parking space code
     */
    public void createReservation(int vehicleId, String spaceCode) {
        String query = "INSERT INTO reservation (reservation_date, is_active, vehicle_id, space_code) VALUES (?, 1, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, vehicleId);
            ps.setString(3, spaceCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("createReservation error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Cancels reservation.
     *
     * @param reservationId the reservation identifier
     */
    public void cancelReservation(int reservationId) {
        String query = "UPDATE reservation SET is_active = 0 WHERE reservation_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("cancelReservation error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Updates space code.
     *
     * @param reservationId the reservation identifier
     * @param newSpaceCode the new parking space code assigned to the reservation
     */
    public void updateSpaceCode(int reservationId, String newSpaceCode) {
        String query = "UPDATE reservation SET space_code = ? WHERE reservation_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newSpaceCode);
            ps.setInt(2, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateSpaceCode error: " + e.getMessage());
        }
    }

    @Override

    /**
     * Returns all active bookings.
     *
     * @return the requested list of records
     */
    public List<BookingInfo> getAllActiveBookings() {
        List<BookingInfo> list = new ArrayList<>();
        String query =
                "SELECT r.reservation_id, r.reservation_date, " +
                        "ps.space_code, ps.floor, ps.vehicle_type, " +
                        "v.license_plate, u.username, u.email " +
                        "FROM reservation r " +
                        "JOIN vehicle v ON r.vehicle_id = v.vehicle_id " +
                        "JOIN users u ON v.user_id = u.user_id " +
                        "JOIN parking_space ps ON r.space_code = ps.space_code " +
                        "WHERE r.is_active = 1 " +
                        "ORDER BY r.reservation_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BookingInfo(
                        rs.getInt("reservation_id"),
                        rs.getString("space_code"),
                        rs.getInt("floor"),
                        rs.getString("vehicle_type"),
                        rs.getString("license_plate"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getTimestamp("reservation_date").toLocalDateTime()));
            }
        } catch (SQLException e) {
            System.err.println("getAllActiveBookings error: " + e.getMessage());
        }
        return list;
    }

    @Override

    /**
     * Returns active bookings by user.
     *
     * @param username the username used to identify the user
     * @return the requested list of records
     */
    public List<BookingInfo> getActiveBookingsByUser(String username) {
        List<BookingInfo> list = new ArrayList<>();
        String query =
                "SELECT r.reservation_id, r.reservation_date, " +
                        "ps.space_code, ps.floor, ps.vehicle_type, " +
                        "v.license_plate, u.username, u.email " +
                        "FROM reservation r " +
                        "JOIN vehicle v ON r.vehicle_id = v.vehicle_id " +
                        "JOIN users u ON v.user_id = u.user_id " +
                        "JOIN parking_space ps ON r.space_code = ps.space_code " +
                        "WHERE r.is_active = 1 AND u.username = ? " +
                        "ORDER BY r.reservation_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BookingInfo(
                        rs.getInt("reservation_id"),
                        rs.getString("space_code"),
                        rs.getInt("floor"),
                        rs.getString("vehicle_type"),
                        rs.getString("license_plate"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getTimestamp("reservation_date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("getActiveBookingsByUser error: " + e.getMessage());
        }

        return list;
    }

    /**
     * Maps reservation.
     *
     * @param rs the result set containing the current database row
     * @return the matching reservation, or null if none is found
     * @throws SQLException if the operation cannot be completed
     */
    private Reservation mapReservation(ResultSet rs) throws SQLException {
        return new Reservation(rs.getInt("reservation_id"), rs.getTimestamp("reservation_date").toLocalDateTime(), rs.getBoolean("is_active"), rs.getInt("vehicle_id"), rs.getString("space_code"));
    }
}
