package Presentation;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;

/**
 * Swing panel that renders the occupancy chart component.
 */
public class OccupancyChartPanel extends JPanel {

    private int[] data = new int[60];
    private int totalSpaces = 1;

    /**
     * Creates a new OccupancyChartPanel instance.
     *
     * @return the requested value
     */
    public OccupancyChartPanel() {
        setBackground(Color.WHITE);
    }

    /**
     * Sets data.
     *
     * @param data the occupancy samples to display
     * @param totalSpaces the total number of parking spaces
     */
    public void setData(int[] data, int totalSpaces) {
        this.data = Arrays.copyOf(data, data.length);
        this.totalSpaces = Math.max(1, totalSpaces);
        repaint();
    }

    @Override

    /**
     * Paints component.
     *
     * @param g the graphics context used by Swing
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int left = 75;
        int right = 24;
        int top = 20;
        int bottom = 55;
        int width = getWidth() - left - right;
        int height = getHeight() - top - bottom;

        if (width <= 0 || height <= 0) {
            g2.dispose();
            return;
        }

        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(left, top, left, top + height);
        g2.drawLine(left, top + height, left + width, top + height);

        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        drawYAxisTitle(g2, "Plazas ocupadas", 16, top + height / 2);
        drawCenteredString(g2, "Hace X minutos", left + width / 2, top + height + 42);

        int maxValue = Math.max(1, Math.max(totalSpaces, max(data)));
        int tickStep = calculateTickStep(maxValue);

        for (int value = 0; value <= maxValue; value += tickStep) {
            int y = top + height - (int) Math.round((double) value / maxValue * height);
            g2.setColor(new Color(230, 230, 230));
            g2.drawLine(left, y, left + width, y);
            g2.setColor(Color.DARK_GRAY);
            drawRightAlignedString(g2, String.valueOf(value), left - 8, y + 4);
        }

        if (maxValue % tickStep != 0) {
            g2.setColor(new Color(230, 230, 230));
            g2.drawLine(left, top, left + width, top);
            g2.setColor(Color.DARK_GRAY);
            drawRightAlignedString(g2, String.valueOf(maxValue), left - 8, top + 4);
        }

        int gap = 2;
        int barWidth = Math.max(2, (width - gap * (data.length - 1)) / data.length);

        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            int barHeight = (int) Math.round((double) value / maxValue * height);
            int x = left + i * (barWidth + gap);
            int y = top + height - barHeight;

            g2.setColor(new Color(66, 133, 244));
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(new Color(35, 85, 170));
            g2.drawRect(x, y, barWidth, barHeight);

            if (i == 0 || i == 14 || i == 29 || i == 44 || i == data.length - 1) {
                g2.setColor(Color.DARK_GRAY);
                String label = String.valueOf(data.length - 1 - i);
                drawCenteredString(g2, label, x + barWidth / 2, top + height + 18);
            }
        }

        g2.dispose();
    }

    /**
     * Returns max.
     *
     * @param values the values to inspect
     * @return the calculated numeric value
     */
    private int max(int[] values) {
        int maxValue = 0;
        for (int value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    /**
     * Calculates tick step.
     *
     * @param maxValue the maximum value used to calculate the step
     * @return the calculated numeric value
     */
    private int calculateTickStep(int maxValue) {
        if (maxValue <= 6) {
            return 1;
        }
        if (maxValue <= 12) {
            return 2;
        }
        if (maxValue <= 30) {
            return 5;
        }
        return 10;
    }

    /**
     * Draws centered string.
     *
     * @param g2 the graphics context used for custom drawing
     * @param text the text to draw
     * @param centerX the horizontal center position
     * @param baselineY the text baseline position
     */
    private void drawCenteredString(Graphics2D g2, String text, int centerX, int baselineY) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, centerX - fm.stringWidth(text) / 2, baselineY);
    }

    /**
     * Draws right aligned string.
     *
     * @param g2 the graphics context used for custom drawing
     * @param text the text to draw
     * @param rightX the right-aligned x position
     * @param baselineY the text baseline position
     */
    private void drawRightAlignedString(Graphics2D g2, String text, int rightX, int baselineY) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, rightX - fm.stringWidth(text), baselineY);
    }

    /**
     * Draws yaxis title.
     *
     * @param g2 the graphics context used for custom drawing
     * @param text the text to draw
     * @param x the x coordinate
     * @param centerY the vertical center position
     */
    private void drawYAxisTitle(Graphics2D g2, String text, int x, int centerY) {
        Graphics2D copy = (Graphics2D) g2.create();
        copy.rotate(-Math.PI / 2, x, centerY);
        drawCenteredString(copy, text, x, centerY);
        copy.dispose();
    }
}
