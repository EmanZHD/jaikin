package app;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Window extends JFrame {

    public DrawingPanel drawingPanel;
    public ArrayList<Point> controlPoints;
    public boolean isAnimating;
    public boolean isDrowing;
    public int currentStep;
    public Timer animationTimer;
    public Point draggedPoint;

    public final int POINT_RADIUS = 6;
    public final int MAX_STEPS = 7;
    public final int ANIMATION_DELAY = 1200;

    public Window() {
        controlPoints = new ArrayList<>();
        isAnimating = false;
        isDrowing = false;
        currentStep = 0;
        draggedPoint = null;

        setTitle("Chaikin's Algorithm Animator : © izahid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);

        drawingPanel = new DrawingPanel(this);
        add(drawingPanel);
        addListener();
        setFocusable(true);
        setVisible(true);
    }

    private void addListener() {
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> {
                if (controlPoints.size() >= 2) {
                    isDrowing = true;
                    toggleAnimation();
                }
            }
            case KeyEvent.VK_ESCAPE ->
                System.exit(1);
            case KeyEvent.VK_C ->
                clearAll();
            default -> {
            }
        }
    }

    private void handleMousePressed(MouseEvent e) {
        if (isAnimating) {
            return;
        }

        Point clickPoint = e.getPoint();
        // Check if clicking on existing point
        for (Point p : controlPoints) {
            double dist = clickPoint.distance(p);
            if (dist <= POINT_RADIUS + 5) {
                draggedPoint = p;
                return;
            }
        }

        // Add new point
        controlPoints.add(clickPoint);
        repaint();
    }

    private void handleMouseDragged(MouseEvent e) {
        if (draggedPoint != null && !isAnimating) {
            draggedPoint.setLocation(e.getPoint());
            repaint();
        }
    }

    private void toggleAnimation() {
        isAnimating = !isAnimating;

        if (isAnimating) {
            currentStep = 0;
            if (controlPoints.size() > 2) {
                startAnimation();
            }
        } else {
            stopAnimation();
        }

        drawingPanel.repaint();
    }

    private void startAnimation() {
        if (animationTimer != null) {
            animationTimer.cancel();
        }

        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentStep++;
                if (currentStep > MAX_STEPS) {
                    currentStep = 1;
                }
                drawingPanel.repaint();
            }
        }, ANIMATION_DELAY, ANIMATION_DELAY);
    }

    private void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.cancel();
            animationTimer = null;
        }
    }

    private void clearAll() {
        controlPoints.clear();
        isAnimating = false;
        isDrowing = false;
        currentStep = 0;
        stopAnimation();
        drawingPanel.repaint();
    }
}
