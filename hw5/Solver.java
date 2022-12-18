import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {
    private boolean is_solvable = false;
    private int num_moves = -1;

    private Node finalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<Node> main_pq = new MinPQ<Node>(new ManhattanPriority());
        MinPQ<Node> twin_pq = new MinPQ<Node>(new ManhattanPriority());

        var twin = initial.twin();

        main_pq.insert(new Node(initial, 0, null));
        twin_pq.insert(new Node(twin, 0, null));

        Node cur = null;
        Node cur_twin = null;

        while (true) {
            cur = main_pq.delMin();
            cur_twin = twin_pq.delMin();

            if (cur.board.isGoal() || cur_twin.board.isGoal()) {
                break;
            }

            for (var n : cur.board.neighbors()) {
                if (cur.prev == null || cur.prev.board.equals(n) == false) {
                    main_pq.insert(new Node(n, cur.distance + 1, cur));
                }
            }

            for (var n : cur_twin.board.neighbors()) {
                if (cur_twin.prev == null || cur_twin.prev.board.equals(n) == false) {
                    twin_pq.insert(new Node(n, cur_twin.distance + 1, cur_twin));
                }
            }
        }

        if (cur.board.isGoal()) {
            is_solvable = true;
            num_moves = cur.distance;
            finalNode = cur;
        }
    }

    private class Node {
        public Board board;
        public int distance;
        public Node prev;
        public int priorityDistance = -1;

        public Node(Board b, int _distance, Node previous) {
            board = b;
            distance = _distance;
            prev = previous;
        }
    }

    private class HammingPriority implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            var s1 = n1.distance + n1.board.hamming();
            var s2 = n2.distance + n2.board.hamming();
            if (s1 > s2) {
                return 1;
            } else if (s1 == s2) {
                return 0;
            }
            return -1;
        }
    }

    private class ManhattanPriority implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            if (n1.priorityDistance == -1) {
                n1.priorityDistance = n1.board.manhattan();
            }
            if (n2.priorityDistance == -1) {
                n2.priorityDistance = n2.board.manhattan();
            }
            var s1 = n1.distance + n1.priorityDistance;
            var s2 = n2.distance + n2.priorityDistance;
            if (s1 > s2) {
                return 1;
            } else if (s1 == s2) {
                return 0;
            }
            return -1;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return is_solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return num_moves;
    }

    private class NodeIterable implements Iterable<Board> {
        private class NodeIterator implements Iterator<Board> {
            public Board[] boards;
            public int current = 0;

            public NodeIterator(Node node) {
                int num = 0;
                var cur = node;
                while (cur != null) {
                    num++;
                    cur = cur.prev;
                }

                boards = new Board[num];
                cur = node;
                int i = num - 1;
                while (cur != null) {
                    boards[i] = cur.board;
                    i--;
                    cur = cur.prev;
                }
            }

            public boolean hasNext() {
                return current < boards.length;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public Board next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return boards[current++];
            }
        }

        private Node node;

        public NodeIterable(Node node_) {
            node = node_;
        }

        public Iterator<Board> iterator() {
            return new NodeIterator(node);
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (finalNode != null) {
            return new NodeIterable(finalNode);
        } else {
            return null;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
