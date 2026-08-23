package Presentation;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Swing view that renders the occupancy chart screen.
 */
public class OccupancyChartView extends JPanel {

    public static final String BTN_BACK = "BTN_BACK_OCCUPANCY";

    private OccupancyChartPanel chartPanel;
    private JLabel summaryLabel;
    private JButton backButton;

    /**
     * Creates a new OccupancyChartView instance.
     *
     * @return the requested value
     */
    public OccupancyChartView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        configureTitle();
        configureChart();
        configureButtons();
    }

    /**
     * Configures title.
     */
    private void configureTitle() {
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel label = new JLabel("Grafico de ocupacion - ultima hora", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        summaryLabel = new JLabel("Ocupacion actual: 0", SwingConstants.CENTER);
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        titlePanel.add(label);
        titlePanel.add(summaryLabel);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Configures chart.
     */
    private void configureChart() {
        chartPanel = new OccupancyChartPanel();
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Configures buttons.
     */
    private void configureButtons() {
        backButton = new JButton("Volver al menu");
        backButton.setActionCommand(BTN_BACK);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 20, 60));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets data.
     *
     * @param data the occupancy samples to display
     * @param totalSpaces the total number of parking spaces
     */
    public void setData(int[] data, int totalSpaces) {
        int current = data.length == 0 ? 0 : data[data.length - 1];
        summaryLabel.setText("Ocupacion actual: " + current + " / " + totalSpaces);
        chartPanel.setData(data, totalSpaces);
    }

    /**
     * Registers controller.
     *
     * @param controller the action listener that handles view actions
     */
    public void registerController(ActionListener controller) {
        for (ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }
        backButton.addActionListener(controller);
    }
}
