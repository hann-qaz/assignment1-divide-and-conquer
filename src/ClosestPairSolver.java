import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ClosestPairSolver {

    private static int maxDepth;

    public static double closestPair(Point[] points) {
        maxDepth = 0;
        Point[] byX = points.clone();
        Arrays.sort(byX, Comparator.comparingDouble(p -> p.x));
        Point[] byY = byX.clone();
        Arrays.sort(byY, Comparator.comparingDouble(p -> p.y));
        return closestPairRec(byX, byY, 0);
    }

    private static double closestPairRec(Point[] byX, Point[] byY, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        int n = byX.length;

        if (n <= 3) return bruteForce(byX);

        int mid = n / 2;
        double midX = byX[mid].x;

        Point[] leftX = Arrays.copyOfRange(byX, 0, mid);
        Point[] rightX = Arrays.copyOfRange(byX, mid, n);
        Set<Point> leftSet = new HashSet<>(Arrays.asList(leftX));

        Point[] leftY = new Point[mid];
        Point[] rightY = new Point[n - mid];
        int li = 0, ri = 0;
        for (Point p : byY) {
            if (leftSet.contains(p)) leftY[li++] = p;
            else rightY[ri++] = p;
        }

        double dl = closestPairRec(leftX, leftY, depth + 1);
        double dr = closestPairRec(rightX, rightY, depth + 1);
        double d = Math.min(dl, dr);

        Point[] strip = new Point[n];
        int si = 0;
        for (Point p : byY) {
            if (Math.abs(p.x - midX) < d) strip[si++] = p;
        }

        for (int i = 0; i < si; i++) {
            for (int j = i + 1; j < si && (strip[j].y - strip[i].y) < d; j++) {
                d = Math.min(d, strip[i].distanceTo(strip[j]));
            }
        }
        return d;
    }

    public static double bruteForce(Point[] points) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                min = Math.min(min, points[i].distanceTo(points[j]));
            }
        }
        return min;
    }

    public static int getMaxDepth() {
        return maxDepth;
    }
}
