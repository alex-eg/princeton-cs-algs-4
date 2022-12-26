import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    // construct an empty set of points
    public KdTree() {
    }

    // private static double max(double a, double b) {
    //     if (a > b) return a; else return b;
    // }

    private static double min(double a, double b) {
        if (a < b) return a; else return b;
    }

    private class Node {
        public Node(Point2D p_, RectHV rect_) {
            p = p_;
            rect = rect_;
        }

        public Point2D p;
        public RectHV rect;
        public Node left;
        public Node right;
    }

    private Node root;
    private int size;

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            size++;
            return;
        }

        insertRec(root, p, true);
    }

    private boolean checkCorrectness() {
        if (root == null) {
            return true;
        }

        return checkCorrectnessRec(root, true);
    }

    private boolean checkCorrectnessRec(Node node, boolean xDiv) {
        boolean leftCorrect = true;
        boolean rightCorrect = true;
        boolean thisCorrect = true;
        if (xDiv) {
            if (node.left != null) {
                thisCorrect = node.left.p.x() < node.p.x();
                leftCorrect = checkCorrectnessRec(node.left, !xDiv);
            }

            if (node.right != null) {
                thisCorrect = node.right.p.x() >= node.p.x();
                rightCorrect = checkCorrectnessRec(node.right, !xDiv);
            }
        } else {
            if (node.left != null) {
                thisCorrect = node.left.p.y() < node.p.y();
                leftCorrect = checkCorrectnessRec(node.left, !xDiv);
            }

            if (node.right != null) {
                thisCorrect = node.right.p.y() >= node.p.y();
                rightCorrect = checkCorrectnessRec(node.right, !xDiv);
            }
        }
        return rightCorrect && leftCorrect && thisCorrect;
    }

    private void insertRec(Node current, Point2D p, boolean xDiv) {
        if (current.p.equals(p)) {
            return;
        }

        if ((xDiv && p.x() >= current.p.x()) ||
            (!xDiv && p.y() >= current.p.y())) {
            if (current.right == null) {
                // StdOut.print("Inserting ");
                // StdOut.print(p);
                // StdOut.print(" to the right of ");
                // StdOut.println(current.p);
                if (xDiv) {
                    current.right = new Node(p, new RectHV(current.p.x(), current.rect.ymin(), current.rect.xmax(), current.rect.ymax()));
                } else {
                    current.right = new Node(p, new RectHV(current.rect.xmin(), current.p.y(), current.rect.xmax(), current.rect.ymax()));
                }
                // StdOut.print("Inserted right node: ");
                // StdOut.println(current.right.rect);
                size++;
            } else {
                insertRec(current.right, p, !xDiv);
            }
        } else {
            if (current.left == null) {
                // StdOut.print("Inserting ");
                // StdOut.print(p);
                // StdOut.print(" to the left of ");
                // StdOut.println(current.p);
                if (xDiv) {
                    current.left = new Node(p, new RectHV(current.rect.xmin(), current.rect.ymin(), current.p.x(), current.rect.ymax()));
                } else {
                    current.left = new Node(p, new RectHV(current.rect.xmin(), current.rect.ymin(), current.rect.xmax(), current.p.y()));
                }
                // StdOut.print("Inserted left node: ");
                // StdOut.println(current.left.rect);
                size++;
            } else {
                insertRec(current.left, p, !xDiv);
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            return false;
        }

        var current = root;
        boolean xDiv = true;
        while (true) {
            boolean leftRight = false; // false = left, true = right
            if (current.p.equals(p)) {
                return true;
            } else {
                if (xDiv) {
                    if (p.x() >= current.p.x()) {
                        leftRight = true;
                    }
                } else {
                    if (p.y() >= current.p.y()) {
                        leftRight = true;
                    }
                }

                if (leftRight) {
                    if(current.right == null) {
                        return false;
                    } else {
                        current = current.right;
                        xDiv = !xDiv;
                    }
                } else {
                    if (current.left == null) {
                        return false;
                    } else {
                        current = current.left;
                        xDiv = !xDiv;
                    }
                }
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        if (root == null) {
            return;
        }

        drawRec(root, null, false, true);
    }

    private void drawRec(Node current, Node parent, boolean leftRight, boolean xDiv) {
        if (current == null) {
            return;
        }

        // StdOut.print("Drawing: ");
        // StdOut.println(current.p);

        if (parent != null) {
            if (xDiv) {
                StdDraw.setPenColor(StdDraw.RED);
                if (leftRight) { // to the right -- draw from parent to top
                    StdDraw.line(current.p.x(), parent.p.y(), current.p.x(), current.rect.ymax());
                } else { // from bottom to parent
                    StdDraw.line(current.p.x(), current.rect.ymax(), current.p.x(), parent.p.y());
                }
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                if (leftRight) { // to the right -- draw from parent to right border
                    StdDraw.line(parent.p.x(), current.p.y(), current.rect.xmax(), current.p.y());
                } else { // from left to parent
                    StdDraw.line(current.rect.xmin(), current.p.y(), parent.p.x(), current.p.y());
                }
            }
        } else {
            if (xDiv) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(current.p.x(), 0.0, current.p.x(), 1.0);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(0.0, current.p.y(), 1.0, current.p.y());
            }
        }
        drawRec(current.left, current, false, !xDiv);
        drawRec(current.right, current, true, !xDiv);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(current.p.x(), current.p.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        var ret = new SET<Point2D>();
        if (root == null) {
            return ret;
        }

        rangeRec(root, rect, ret, true);

        return ret;
    }

    private void rangeRec(Node current, RectHV rect, SET<Point2D> set, boolean xDiv) {
        if (current == null) {
            return;
        }

        // StdOut.print("Checking if ");
        // StdOut.print(current.p);
        // StdOut.print(" belongs to rect ");
        // StdOut.println(rect);

        if (rect.contains(current.p)) {
            set.add(current.p);
        }

        if ((xDiv && rect.xmin() <= current.p.x()) ||
            (!xDiv && rect.ymin() <= current.p.y())) {
            rangeRec(current.left, rect, set, !xDiv);
        }

        if ((xDiv && rect.xmax() >= current.p.x()) ||
            (!xDiv && rect.ymax() >= current.p.y())) {
            rangeRec(current.right, rect, set, !xDiv);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            return null;
        }

        return nearestRec(root, p, root.p, true);
    }

    private static double abs(double x) {
        if (x < 0.0) {
            return -x;
        } else {
            return x;
        }
    }

    private static double sqrDist(Node node, Point2D target) {
        return node.rect.distanceSquaredTo(target);
    }

    private static double sqrDist(RectHV rect, Point2D target, boolean xDiv) {
        if (xDiv) {
            return min(target.x() - rect.xmin(),
                       target.x() - rect.xmax());
        } else {
            return min(target.y() - rect.ymin(),
                       target.y() - rect.ymax());
        }
    }

    private static boolean trace = false;

    private static Point2D nearestRec(Node current, Point2D target, Point2D nearest, boolean xDiv) {
        if (trace) {
            StdOut.print("Checking distance to ");
            StdOut.print(current.p);
            StdOut.print(": ");
            StdOut.println(target.distanceTo(current.p));
        }

        if (target.distanceSquaredTo(current.p) < target.distanceSquaredTo(nearest)) {
            nearest = current.p;
        }

        boolean checkRight = true;
        if (xDiv) {
            if (target.x() < current.p.x()) {
                if (trace) {
                    StdOut.println("Go left");
                }
                if (current.left != null) {
                    nearest = nearestRec(current.left, target, nearest, !xDiv);
                }
            } else {
                if (trace) {
                    StdOut.println("Go right");
                }
                checkRight = false;
                if (current.right != null) {
                    nearest = nearestRec(current.right, target, nearest, !xDiv);
                }
            }
        } else {
            if (target.y() < current.p.y()) {
                if (trace) {
                    StdOut.println("Go bottom");
                }
                if (current.left != null) {
                    nearest = nearestRec(current.left, target, nearest, !xDiv);
                }
            } else {
                if (trace) {
                    StdOut.println("Go top");
                }
                checkRight = false;
                if (current.right != null) {
                    nearest = nearestRec(current.right, target, nearest, !xDiv);
                }
            }
        }

        if (trace) {
            if (checkRight) {
                StdOut.print("Need to check right subtree of ");
                StdOut.println(current.p);
                StdOut.print("... Subtree is ");
                if (current.right != null) {
                    StdOut.print("not null.\nDistance to nearest from target: ");
                    StdOut.println(target.distanceTo(nearest));
                    StdOut.print(" From right subtree to target: ");
                    StdOut.println(sqrDist(current.right, target));
                    if (target.distanceSquaredTo(nearest) >= sqrDist(current.right, target)) {
                        StdOut.println(" Going into right subtree.");
                    } else {
                        StdOut.println(" Not going into right subtree.");
                    }
                } else {
                    StdOut.println("null, aborting");
                }
            } else {
                StdOut.print("Need to check left subtree of ");
                StdOut.println(current.p);
                StdOut.print(" ... Subtree is ");
                if (current.left != null) {
                    StdOut.print("not null.\nDistance to nearest from target: ");
                    StdOut.println(target.distanceSquaredTo(nearest));
                    StdOut.print(" From right subtree to target: ");
                    StdOut.println(sqrDist(current.left, target));
                    if (target.distanceSquaredTo(nearest) >= sqrDist(current.left, target)) {
                        StdOut.println(" Going into right subtree.");
                    } else {
                        StdOut.println(" Not going into right subtree.");
                    }
                } else {
                    StdOut.println("null, aborting");
                }
            }
        }

        if (checkRight) {
            if (current.right != null && target.distanceSquaredTo(nearest) >= sqrDist(current.right, target)) {
                if (trace) {
                    StdOut.print("Checking right of ");
                    StdOut.println(current.p);
                }
                nearest = nearestRec(current.right, target, nearest, !xDiv);
            }
        } else if (current.left != null && target.distanceSquaredTo(nearest) >= sqrDist(current.left, target)) {
                if (trace) {
                    StdOut.print("Checking left of ");
                    StdOut.println(current.p);
                }
            nearest = nearestRec(current.left, target, nearest, !xDiv);
        }

        return nearest;
    }

    private static void check(KdTree tree) {
        if (tree.checkCorrectness()) {
            StdOut.println("Tree is correct");
        } else {
            StdOut.println("Tree is incorrect");
        }
    }

    private static void print(KdTree tree) {
        print(tree.root, 0);
    }

    private static void print(Node n, int level) {
        if (n == null) {
            StdOut.println("null");
            return;
        }

        StdOut.print(n.p);
        StdOut.print(" ");
        StdOut.println(n.rect);

        for (int i = 0; i < level + 1; i++) {
            StdOut.print("    ");
        }
        StdOut.print("l: ");
        print(n.left, level + 1);

        for (int i = 0; i < level + 1; i++) {
            StdOut.print("    ");
        }
        StdOut.print("r: ");
        print(n.right, level + 1);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        var kdtree = new KdTree();

        var rect = new RectHV(0.0, 0.0, 0.5, 0.7);
        StdOut.print("To rect from 0.7, 0.5 by x: ");
        StdOut.println(sqrDist(rect, new Point2D(0.7, 0.5), true));
        StdOut.print("To rect from 0.7, 0.8 by x: ");
        StdOut.println(sqrDist(rect, new Point2D(0.7, 0.8), true));
        StdOut.print("To rect from 0.5, 0.9 by y: ");
        StdOut.println(sqrDist(rect, new Point2D(0.5, 0.9), false));
        StdOut.print("To rect from 0.0, 0.8 by y: ");
        StdOut.println(sqrDist(rect, new Point2D(0.0, 0.8), false));

        // kdtree.insert(new Point2D(0.3, 0.5));
        // kdtree.insert(new Point2D(0.7, 0.5));
        // kdtree.insert(new Point2D(0.5, 0.3));
        // kdtree.insert(new Point2D(0.1, 0.1));
        // kdtree.insert(new Point2D(0.0, 0.3));
        // kdtree.insert(new Point2D(0.1, 0.9));
        // kdtree.insert(new Point2D(0.9, 0.9));
        // kdtree.insert(new Point2D(0.9, 0.1));
        // kdtree.insert(new Point2D(0.1, 0.9));
        // kdtree.insert(new Point2D(0.1, 0.9));

        kdtree.insert(new Point2D(0.7, 0.2));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        kdtree.insert(new Point2D(0.9, 0.6));
        check(kdtree);

        StdOut.println("Nearest to 0.704, 0.448 (should be (0.5, 0.4)): ");
        StdOut.println(kdtree.nearest(new Point2D(0.704, 0.448)));

        kdtree = new KdTree();
        kdtree.insert(new Point2D(0.372, 0.497));
        kdtree.insert(new Point2D(0.564, 0.413));
        kdtree.insert(new Point2D(0.226, 0.577));
        kdtree.insert(new Point2D(0.144, 0.179));
        kdtree.insert(new Point2D(0.083, 0.51));
        kdtree.insert(new Point2D(0.32, 0.708));
        kdtree.insert(new Point2D(0.417, 0.362));
        kdtree.insert(new Point2D(0.862, 0.825));
        kdtree.insert(new Point2D(0.785, 0.725));
        kdtree.insert(new Point2D(0.499, 0.208));

        check(kdtree);
        // StdDraw.enableDoubleBuffering();
        // StdDraw.setPenRadius(0.01);
        // StdDraw.clear();
        // kdtree.draw();
        // StdDraw.show();

        StdOut.println("Nearest to 0.2, 0.63: ");
        StdOut.println(kdtree.nearest(new Point2D(0.2, 0.63)));

        StdOut.println("Nearest to 0.18, 0.385 (should be (0.083, 0.51)): ");
        StdOut.println(kdtree.nearest(new Point2D(0.18, 0.385)));

        kdtree = new KdTree();
        kdtree.insert(new Point2D(0.25, 0.0));
        kdtree.insert(new Point2D(0.5, 0.75));
        kdtree.insert(new Point2D(1.0, 0.75));
        kdtree.insert(new Point2D(0.5, 0.0));
        kdtree.insert(new Point2D(0.75, 0.75));
        kdtree.insert(new Point2D(0.0, 0.25));
        kdtree.insert(new Point2D(1.0, 0.25));
        kdtree.insert(new Point2D(0.5, 0.5));
        kdtree.insert(new Point2D(1.0, 0.5));
        kdtree.insert(new Point2D(0.0, 0.0));

        check(kdtree);
        StdOut.println("Nearest to 0.75, 1.0: ");
        StdOut.println(kdtree.nearest(new Point2D(0.75, 1.0)));

        kdtree = new KdTree();
        kdtree.insert(new Point2D(0.372, 0.497));
        kdtree.insert(new Point2D(0.564, 0.413));
        kdtree.insert(new Point2D(0.226, 0.577));
        kdtree.insert(new Point2D(0.144, 0.179));
        kdtree.insert(new Point2D(0.083, 0.51));
        kdtree.insert(new Point2D(0.32, 0.708));
        kdtree.insert(new Point2D(0.417, 0.362));
        kdtree.insert(new Point2D(0.862, 0.825));
        kdtree.insert(new Point2D(0.785, 0.725));
        kdtree.insert(new Point2D(0.499, 0.208));

        check(kdtree);
        StdOut.println("Nearest to 0.926, 0.563: ");
        StdOut.println(kdtree.nearest(new Point2D(0.926, 0.563)));

        StdOut.println("Nearest to 0.32, 0.325 (should be (0.417, 0.362)): ");
        StdOut.println(kdtree.nearest(new Point2D(0.32, 0.325)));

        kdtree = new KdTree();
        kdtree.insert(new Point2D(0.3, 0.5));
        kdtree.insert(new Point2D(0.5, 0.3));
        kdtree.insert(new Point2D(0.8, 0.8));
        kdtree.insert(new Point2D(0.7, 0.7));
        check(kdtree);
        print(kdtree);
        KdTree.trace = true;
        StdOut.println("Nearest to 0.9, 0.5 (should be (0.7 0.7)): ");
        StdOut.println(kdtree.nearest(new Point2D(0.9, 0.5)));

        // kdtree = new KdTree();
        // kdtree.insert(new Point2D(0.75, 0.8125));
        // kdtree.insert(new Point2D(0.25, 0.0625));
        // kdtree.insert(new Point2D(0.8125, 1.0));
        // kdtree.insert(new Point2D(0.25, 0.9375));
        // kdtree.insert(new Point2D(0.375, 0.375));
        // kdtree.insert(new Point2D(0.875, 0.875));
        // kdtree.insert(new Point2D(0.0625, 0.3125));
        // kdtree.insert(new Point2D(0.4375, 0.9375));
        // kdtree.insert(new Point2D(0.3125, 0.0));
        // kdtree.insert(new Point2D(0.9375, 1.0));
        // kdtree.insert(new Point2D(0.8125, 0.0625));
        // kdtree.insert(new Point2D(0.375, 0.5));
        // kdtree.insert(new Point2D(0.4375, 0.5625));
        // kdtree.insert(new Point2D(0.4375, 0.1875));
        // kdtree.insert(new Point2D(0.0625, 0.0));
        // kdtree.insert(new Point2D(0.125, 0.875));
        // kdtree.insert(new Point2D(0.9375, 0.5625));
        // kdtree.insert(new Point2D(0.5, 0.25));
        // kdtree.insert(new Point2D(0.875, 0.4375));
        // kdtree.insert(new Point2D(0.0, 0.0));
        //
        // StdOut.println("Nearest to 0.6875, 0.125:");
        // StdOut.println(kdtree.nearest(new Point2D(0.6875, 0.125)));

        // StdOut.println("Nearest to -1, -1: ");
        // StdOut.println(kdtree.nearest(new Point2D(-1.0, -1.0)));
        //
        // StdOut.println("Nearest to 0.51, 0.5: ");
        // StdOut.println(kdtree.nearest(new Point2D(0.51, 0.5)));
        //
        // StdOut.println("Nearest to 0.49, 0.5: ");
        // StdOut.println(kdtree.nearest(new Point2D(0.49, 0.5)));
        //
        // StdOut.println("Points in rectangle (0, 0) - (0.3, 1.0):");
        // for (var p : kdtree.range(new RectHV(0.0, 0.0, 0.3, 1.0))) {
        //     StdOut.println(p);
        // }
        //
        // StdOut.println("Points in rectangle (0, 0.8) - (1, 0.9):");
        // for (var p : kdtree.range(new RectHV(0.0, 0.8, 1.0, 0.9))) {
        //     StdOut.println(p);
        // }
    }
}
