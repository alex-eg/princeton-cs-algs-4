import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    // construct an empty set of points
    public PointSET() {
        set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (var p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        SET<Point2D> foundPoints = new SET<Point2D>();
        for (var p : set) {
            var x = p.x();
            var y = p.y();

            if (x >= rect.xmin() && x <= rect.xmax() && y >= rect.ymin() && y <= rect.ymax()) {
                foundPoints.add(p);
            }
        }
        return foundPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }

        Point2D found = null;
        for (var p : set) {
            if (found == null || p.distanceTo(target) < found.distanceTo(target)) {
                found = p;
            }
        }
        return found;
    }

    private SET<Point2D> set;

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        var pset = new PointSET();

        pset.insert(new Point2D(0.0, 0.0));
        pset.insert(new Point2D(0.0, 1.0));
        pset.insert(new Point2D(1.0, 1.0));
        pset.insert(new Point2D(1.0, 0.0));
        pset.insert(new Point2D(0.3, 0.5));
        pset.insert(new Point2D(0.7, 0.5));
        pset.insert(new Point2D(0.5, 0.5));

        StdOut.print("Size: ");
        StdOut.println(pset.size());

        StdOut.print("Contains 0, 0?: ");
        StdOut.println(pset.contains(new Point2D(0.0, 0.0)));

        StdOut.print("Contains 2, 2?: ");
        StdOut.println(pset.contains(new Point2D(2.0, 2.0)));

        StdOut.print("Nearest to -1, -1: ");
        StdOut.println(pset.nearest(new Point2D(-1.0, -1.0)));

        StdOut.print("Nearest to 0.41, 0.5: ");
        StdOut.println(pset.nearest(new Point2D(0.41, 0.5)));

        StdOut.print("Nearest to 0.39, 0.5: ");
        StdOut.println(pset.nearest(new Point2D(0.39, 0.5)));

        StdOut.println("Points in rectangle (0, 0) - (0.3, 1.0):");
        for (var p : pset.range(new RectHV(0.0, 0.0, 0.3, 1.0))) {
            StdOut.println(p);
        }

        StdOut.println("Points in rectangle (0, 0.8) - (1, 0.9):");
        for (var p : pset.range(new RectHV(0.0, 0.8, 1.0, 0.9))) {
            StdOut.println(p);
        }
    }
}
