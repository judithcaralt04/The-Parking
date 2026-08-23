package Business;

import Persistance.dao.*;
import Business.entity.ParkingSpace;
import Business.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Background service that simulates vehicle entries and exits.
 */
public class BusinessLogicSimulation implements Runnable {

    private final ParkingSpaceDAO parkingSpaceDAO;
    private final VehicleDAO vehicleDAO;
    private final ReservationDAO reservationDAO;
    private final int maxDelay;

    private final Random random = new Random();
    private Thread worker;
    private volatile boolean running = false;

    /**
     * Creates a new BusinessLogicSimulation instance.
     *
     * @return the requested value
     */
    public BusinessLogicSimulation() {
        this.parkingSpaceDAO = new ParkingSpaceDAOSQL();
        this.vehicleDAO = new VehicleDAOSQL();
        this.reservationDAO = new ReservationDAOSQL();
        this.maxDelay = ConfigLoader.getInstance().getSimulatedVehicleDelay();
    }

    /**
     * Starts start.
     */
    public void start() {
        if (running) {
            return;
        }
        running = true;

        worker = new Thread(this, "traffic-simulation");
        worker.setDaemon(true);
        worker.start();
        System.out.println("Traffic simulation started (maxDelay=" + maxDelay + "s)");
    }

    /**
     * Stops stop.
     */
    public void stop() {
        running = false;
        if (worker != null) {
            worker.interrupt();
        }
    }

    @Override

    /**
     * Runs run.
     */
    public void run() {
        while (running) {
            try {
                int delaySeconds = 1 + random.nextInt(Math.max(1, maxDelay));
                Thread.sleep(delaySeconds * 1000L);
                tick();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Simulation tick error: " + e.getMessage());
            }
        }
    }

    /**
     * Executes tick.
     */
    private void tick() {
        List<ParkingSpace> unreserved = getUnreservedSpaces();
        List<ParkingSpace> vacantUnreserved = filterVacant(unreserved);
        List<Vehicle> simParked = vehicleDAO.getSimulatedParkedVehicles();

        if (vacantUnreserved.isEmpty()) {
            if (!simParked.isEmpty()) {
                simulateExit(simParked);
            }
            return;
        }

        if (simParked.isEmpty()) {
            simulateEntry(vacantUnreserved);
            return;
        }

        double probabilidadEntrada = (double) vacantUnreserved.size() / unreserved.size();
        if (random.nextDouble() < probabilidadEntrada) {
            simulateEntry(vacantUnreserved);
        } else {
            simulateExit(simParked);
        }
    }

    /**
     * Simulates entry.
     *
     * @param vacantUnreserved the vacant parking spaces without active reservations
     */
    private void simulateEntry(List<ParkingSpace> vacantUnreserved) {
        ParkingSpace target = vacantUnreserved.get(random.nextInt(vacantUnreserved.size()));

        String plate = generatePlate();
        int vehicleId = vehicleDAO.createVehicle(plate, target.getVehicleType(), true, null);

        parkingSpaceDAO.occupySpace(target.getSpaceCode(), vehicleId);
        System.out.println("[SIM] ENTRY  " + plate + " -> " + target.getSpaceCode());
    }

    /**
     * Simulates exit.
     *
     * @param simulatedParked the simulated vehicles currently parked
     */
    private void simulateExit(List<Vehicle> simulatedParked) {
        Vehicle v = simulatedParked.get(random.nextInt(simulatedParked.size()));

        ParkingSpace space = parkingSpaceDAO.getOccupiedSpaceByVehicle(v.getVehicleId());

        if (space == null) {
            return;
        }

        parkingSpaceDAO.freeSpace(space.getSpaceCode());
        vehicleDAO.deleteVehicle(v.getLicensePlate());
        System.out.println("[SIM] EXIT   " + v.getLicensePlate() + " <- " + space.getSpaceCode());
    }

    /**
     * Returns unreserved spaces.
     *
     * @return the requested list of records
     */
    private List<ParkingSpace> getUnreservedSpaces() {
        List<ParkingSpace> result = new ArrayList<>();

        for (ParkingSpace s : parkingSpaceDAO.getAllSpaces()) {
            if (!reservationDAO.hasActiveReservation(s.getSpaceCode())) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Filters vacant.
     *
     * @param spaces the parking spaces to process
     * @return the requested list of records
     */
    private List<ParkingSpace> filterVacant(List<ParkingSpace> spaces) {
        List<ParkingSpace> result = new ArrayList<>();

        for (ParkingSpace s : spaces) {
            if (!s.isOccupied()) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Generates plate.
     *
     * @return the requested text value
     */
    private String generatePlate() {
        String plate = "";

        for (int i = 0; i < 4; i++) {
            plate = plate + random.nextInt(10);
        }

        for (int i = 0; i < 3; i++) {
            char letter = (char) ('A' + random.nextInt(26));
            plate = plate + letter;
        }

        return plate;
    }
}
