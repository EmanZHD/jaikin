package app;

import java.awt.*;
import java.util.ArrayList;

public class Chaikin {

    public ArrayList<Point> chaikinCut(ArrayList<Point> points) {
        if (points.size() <= 2) {
            return points;
        }

        ArrayList<Point> newPoints = new ArrayList<>();
        newPoints.add(points.get(0));
        for (int i = 0; i < points.size() - 1; i++) {
            Point p0 = points.get(i);
            Point p1 = points.get(i + 1);

            // Q point: 1/4 along the line from p0 to p1
            int qx = (int) (0.75 * p0.x + 0.25 * p1.x);
            int qy = (int) (0.75 * p0.y + 0.25 * p1.y);
            Point q = new Point(qx, qy);

            // R point: 3/4 along the line from p0 to p1
            int rx = (int) (0.25 * p0.x + 0.75 * p1.x);
            int ry = (int) (0.25 * p0.y + 0.75 * p1.y);
            Point r = new Point(rx, ry);

            newPoints.add(q);
            newPoints.add(r);
        }
        newPoints.add(points.get(points.size() - 1));

        return newPoints;
    }

    public ArrayList<Point> generateStepPoints(ArrayList<Point> controlPoints, int step) {

        if (step <= 0) {
            return new ArrayList<>(controlPoints);
        }
        ArrayList<Point> current = new ArrayList<>(controlPoints);
        System.out.println(step);
        for (int i = 0; i < step && current.size() >= 2; i++) {
            current = chaikinCut(current);
        }

        return (step == 7) ? controlPoints : current;
    }
}
