package app;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class DrawingPanel extends JPanel {

    private final Chaikin chaikin;
    private final Window w;

    public DrawingPanel(Window w) {
        this.w = w;
        this.chaikin = new Chaikin();
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (w.controlPoints.isEmpty()) {
            drawHintMessage(g2d);
            return;
        }

        if (w.isAnimating && w.controlPoints.size() >= 2) {
            drawAnimatedState(g2d);
        } else {
            drawStaticState(g2d);
        }

        drawInstructions(g2d);
    }

    private void drawHintMessage(Graphics2D g2d) {
        g2d.setColor(new Color(148, 163, 184));
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        FontMetrics fm = g2d.getFontMetrics();
        String message = "Click to add control points, then press Enter to animate";
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(message, x, y);
    }

    private void drawAnimatedState(Graphics2D g2d) {
        ArrayList<Point> currentPoints = chaikin.generateStepPoints(w.controlPoints, w.currentStep);

        if (currentPoints.size() >= 2) {
            g2d.setColor(new Color(59, 130, 246));
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < currentPoints.size() - 1; i++) {
                Point p1 = currentPoints.get(i);
                Point p2 = currentPoints.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        drawControlPoints(g2d);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Step: " + w.currentStep + " / " + w.MAX_STEPS, 20, 30);
    }

    private void drawStaticState(Graphics2D g2d) {
        if (w.controlPoints.size() >= 2 && w.isDrowing) {
            g2d.setColor(new Color(148, 163, 184));
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < w.controlPoints.size() - 1; i++) {
                Point p1 = w.controlPoints.get(i);
                Point p2 = w.controlPoints.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            w.isDrowing = false;
        }

        drawControlPoints(g2d);
    }

    private void drawControlPoints(Graphics2D g2d) {
        for (Point p : w.controlPoints) {
            // Fill
            g2d.setColor(new Color(239, 68, 68));
            g2d.fillOval(p.x - w.POINT_RADIUS, p.y - w.POINT_RADIUS,
                    w.POINT_RADIUS * 2, w.POINT_RADIUS * 2);

            // Border
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(p.x - w.POINT_RADIUS, p.y - w.POINT_RADIUS,
                    w.POINT_RADIUS * 2, w.POINT_RADIUS * 2);
        }
    }

    private void drawInstructions(Graphics2D g2d) {
        g2d.setColor(new Color(100, 116, 139));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        int y = getHeight() - 80;
        g2d.drawString("Controls:", 20, y);
        g2d.drawString("• Click - Add control point", 20, y + 15);
        g2d.drawString("• Drag - Move control point", 20, y + 30);
        g2d.drawString("• Enter - Start/Stop animation", 20, y + 45);
        g2d.drawString("• Esc - Clear all points", 20, y + 60);

        int x = getWidth() - 250;
        g2d.drawString("Status:", x, y);
        g2d.drawString("• Control Points: " + w.controlPoints.size(), x, y + 15);
        g2d.drawString("• Animation: " + (w.isAnimating ? "Running" : "Stopped"), x, y + 30);
        g2d.drawString("• Current Step: " + w.currentStep + " / " + w.MAX_STEPS, x, y + 45);
    }
}
