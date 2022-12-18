import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] segments;

    private int size = 0;
    private int capacity = 1;

    public FastCollinearPoints(Point[] pointsIn) {
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
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        segments = new LineSegment[capacity];

        for (var i = 0; i < n; i++) {
            Point[] curPts = new Point[n - 1];
            var sub = 0;
            for (var j = 0; j < n; j++) {
                if (j == i) {
                    sub = 1;
                    continue;
                }
                curPts[j - sub] = points[j];
            }

            java.util.Arrays.sort(curPts, points[i].slopeOrder());
            // StdOut.println("--Sorting by slope----------");
            // StdOut.println(points[i]);
            // StdOut.println("----------------------------");
            for (var j = 0; j < curPts.length; j++) {
                // StdOut.print(j);
                // StdOut.print(": ");
                // StdOut.print(curPts[j]);
                // StdOut.print(" ");
                // StdOut.println(points[i].slopeTo(curPts[j]));
            }

            int numOfSame = 2;
            for (var j = 0; j < curPts.length - 1; j++) {
                if (points[i].slopeTo(curPts[j]) == points[i].slopeTo(curPts[j + 1])) {
                    numOfSame++;
                    if (j != curPts.length - 2) {
                        continue;
                    } else {
                        j++;
                    }
                }
                // StdOut.print("Same nums on step ");
                // StdOut.print(j);
                // StdOut.print(": ");
                // StdOut.println(numOfSame);
                if (numOfSame < 4) {
                    numOfSame = 2;
                    continue;
                }

                // StdOut.print("Collect points. j = ");
                // StdOut.print(j);
                // StdOut.print(", numOfSame = ");
                // StdOut.print(numOfSame);
                // StdOut.print(". Collecting indexes from ");
                // StdOut.print(j - numOfSame + 2);
                // StdOut.print(" to ");
                // StdOut.println(j - numOfSame + numOfSame);
                Point[] toSort = new Point[numOfSame];
                toSort[0] = points[i];
                for (var k = 0; k < numOfSame - 1; k++) {
                    toSort[k + 1] = curPts[j - (numOfSame - 2) + k];
                }

                java.util.Arrays.sort(toSort);
                // StdOut.println("Sorted points: ");
                // for (var p : toSort) {
                //     StdOut.print(p);
                //     StdOut.print(" ");
                // }
                // StdOut.println("---------------------");
                // StdOut.print("Ref pt: ");
                // StdOut.print(points[i]);
                // StdOut.print(" sorted[0]: ");
                // StdOut.println(toSort[0]);
                if (points[i].compareTo(toSort[0]) == 0) {
                    if (size == capacity) {
                        resize(2 * capacity);
                    }
                    var s = new LineSegment(toSort[0], toSort[numOfSame - 1]);
                   // StdOut.print("Adding new segment: ");
                   // StdOut.println(s);
                    segments[size++] = s;
                }
                numOfSame = 2;
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

    public int numberOfSegments() {
        return size;
    }

    public LineSegment[] segments() {
        return java.util.Arrays.copyOf(segments, size);
    }

    public static void main(String[] args) {
        Point[] p = new Point[5];
        // p[0] = new Point(0, 1);
        // p[1] = new Point(0, 2);
        // p[2] = new Point(0, 3);
        // p[3] = new Point(0, 4);

        // p[4] = new Point(1, 0);
        // p[5] = new Point(2, 0);
        // p[6] = new Point(3, 0);
        // p[7] = new Point(4, 0);

        p[0] = new Point(0, 0);
        p[1] = new Point(0, 1);
        p[2] = new Point(0, 2);
        p[3] = new Point(0, 3);
        p[4] = new Point(0, 4);
        // p[5] = new Point(1, 0);
        // p[6] = new Point(1, 1);
        // p[7] = new Point(1, 2);
        // p[8] = new Point(1, 3);
        // p[9] = new Point(1, 4);
        // p[10] = new Point(2, 0);
        // p[11] = new Point(2, 1);
        // p[12] = new Point(2, 2);
        // p[13] = new Point(2, 3);
        // p[14] = new Point(2, 4);
        // p[15] = new Point(3, 0);
        // p[16] = new Point(3, 1);
        // p[17] = new Point(3, 2);
        // p[18] = new Point(3, 3);
        // p[19] = new Point(3, 4);
        // p[20] = new Point(4, 0);
        // p[21] = new Point(4, 1);
        // p[22] = new Point(4, 2);
        // p[23] = new Point(4, 3);
        // p[24] = new Point(4, 4);

        var fp = new FastCollinearPoints(p);
        StdOut.println(fp.numberOfSegments());
        for (var s : fp.segments()) {
            StdOut.println(s);
        }
    }
}
