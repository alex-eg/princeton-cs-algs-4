import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    private int size = 0;
    private int capacity = 1;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] pointsIn) {
        if (pointsIn == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < pointsIn.length; ++i) {
            if (pointsIn[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        var n = pointsIn.length;
        Point[] points = new Point[n];
        for (var i = 0; i < n; i++) {
            points[i] = pointsIn[i];
        }
        java.util.Arrays.sort(points);
        for (int i = 1; i < n; ++i) {
            if (points[i].compareTo(points[i - 1]) == 0)  {
                throw new IllegalArgumentException();
            }
        }

        segments = new LineSegment[1];

        for (var i = 0; i < n; i++) {
            for (var j = i + 1; j < n; j++) {
                for (var k = j + 1; k < n; k++) {
                    for (var u = k + 1; u < n; u++) {
                        double[] slopes = new double[3];
                        slopes[0] = points[i].slopeTo(points[j]);
                        slopes[1] = points[i].slopeTo(points[k]);
                        slopes[2] = points[i].slopeTo(points[u]);

                        Point[] pts = new Point[4];
                        pts[0] = points[i];
                        pts[1] = points[j];
                        pts[2] = points[k];
                        pts[3] = points[u];
                        java.util.Arrays.sort(pts);
                        // StdOut.print(pts[0]);
                        // StdOut.print(" ");
                        // StdOut.print(pts[1]);
                        // StdOut.print(" ");
                        // StdOut.print(pts[2]);
                        // StdOut.print(" ");
                        // StdOut.print(pts[3]);
                        // StdOut.println();

                        if (slopes[0] == slopes[1] && slopes[1] == slopes[2]) {
                            if (size == capacity) {
                                resize(2 * capacity);
                            }
                            // StdOut.println("Found 4 points!");
                            // StdOut.print(slopes[0]);
                            // StdOut.print(" ");
                            // StdOut.print(slopes[1]);
                            // StdOut.print(" ");
                            // StdOut.print(slopes[2]);
                            // StdOut.println();
                            //
                            // StdOut.println(pts[0].slopeTo(pts[3]));

                            segments[size++] = new LineSegment(pts[0], pts[3]);
                        }
                    }
                }
            }
        }
    }

    private void resize(int newCap) {
        LineSegment[] copy = new LineSegment[newCap];
        for (int i = 0; i < size; i++) {
            copy[i] = segments[i];
        }
        capacity = newCap;
        segments = copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return size;
    }

    // the line segments
    public LineSegment[] segments() {
        return java.util.Arrays.copyOf(segments, size);
    }

    // *
    // *
    // *
    // *
    //
    //
    //    * * * *
    public static void main(String[] args) {
        // Point[] p = new Point[8];
        // p[0] = new Point(0, 1);
        // p[1] = new Point(0, 2);
        // p[2] = new Point(0, 3);
        // p[3] = new Point(0, 4);
        //
        // p[4] = new Point(1, 0);
        // p[5] = new Point(2, 0);
        // p[6] = new Point(3, 0);
        // p[7] = new Point(4, 0);

        Point[] p = new Point[20];

        p[0] = new Point(10820, 3702);
        p[1] = new Point(16044, 16317);
        p[2] = new Point(8305, 3702);
        p[3] = new Point(16437, 4068);
        p[4] = new Point(6465, 3702);
        p[5] = new Point(1028, 4068);
        p[6] = new Point(5235, 8953);
        p[7] = new Point(6705, 11812);
        p[8] = new Point(19628, 16317);
        p[9] = new Point(10087, 4068);
        p[10] = new Point(14652, 8953);
        p[11] = new Point(14327, 4068);
        p[12] = new Point(8446, 16317);
        p[13] = new Point(6564, 11812);
        p[14] = new Point(2250, 16317);
        p[15] = new Point(7058, 8953);
        p[16] = new Point(11985, 11812);
        p[17] = new Point(12301, 8953);
        p[18] = new Point(8624, 3702);
        // p[19] = new Point(8305, 3702);
        p[19] = new Point(19073, 11812);

        var bf = new BruteCollinearPoints(p);
        StdOut.println(bf.numberOfSegments());
        for (var s : bf.segments()) {
            StdOut.println(s);
        }
    }
}
