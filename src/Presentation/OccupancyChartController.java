package Presentation;

import Business.BusinessLogicParkingSpace;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Controller that handles user actions for the occupancy chart screen.
 */
public class OccupancyChartController implements ActionListener {

    private final MainController mainController;
    private final OccupancyChartView view;
    private final BusinessLogicParkingSpace businessLogicParkingSpace;
    private final Timer refreshTimer;
    private final int[] samples = new int[60];
    private long lastMinute;

    /**
     * Creates a new OccupancyChartController instance.
     *
     * @param mainController the main controller used to navigate between screens
     * @param view the view controlled by this controller
     * @return the requested value
     */
    public OccupancyChartController(MainController mainController, OccupancyChartView view) {
        this.mainController = mainController;
        this.view = view;
        this.businessLogicParkingSpace = new BusinessLogicParkingSpace();

        this.refreshTimer = new Timer(1000, new ActionListener() {
            @Override

            /**
             * Handles performed.
             *
             * @param e the event triggered by the user interface
             */
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        this.lastMinute = System.currentTimeMillis() / 60000L;
        view.registerController(this);
        initializeSamples();
    }

    /**
     * Shows show.
     */
    public void show() {
        refreshData();
        if (!refreshTimer.isRunning()) {
            refreshTimer.start();
        }
    }

    /**
     * Stops refresh.
     */
    private void stopRefresh() {
        if (refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    /**
     * Executes initialize samples.
     */
    private void initializeSamples() {
        int occupied = (int) businessLogicParkingSpace.countOccupied();
        Arrays.fill(samples, occupied);
    }

    /**
     * Refreshes data.
     */
    private void refreshData() {
        int occupied = (int) businessLogicParkingSpace.countOccupied();
        long currentMinute = System.currentTimeMillis() / 60000L;

        if (currentMinute > lastMinute) {
            long minutesPassed = Math.min(samples.length, currentMinute - lastMinute);

            for (int i = 0; i < minutesPassed; i++) {
                shiftLeft(occupied);
            }
            lastMinute = currentMinute;
        } else {
            samples[samples.length - 1] = occupied;
        }

        view.setData(samples, businessLogicParkingSpace.getSpaceCount());
    }

    /**
     * Executes shift left.
     *
     * @param newValue the new value value
     */
    private void shiftLeft(int newValue) {
        System.arraycopy(samples, 1, samples, 0, samples.length - 1);
        samples[samples.length - 1] = newValue;
    }

    @Override

    /**
     * Handles performed.
     *
     * @param e the event triggered by the user interface
     */
    public void actionPerformed(ActionEvent e) {
        if (OccupancyChartView.BTN_BACK.equals(e.getActionCommand())) {
            stopRefresh();
            mainController.showMenu(null);
        }
    }
}
