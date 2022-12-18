import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Board {
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    private int[][] tiles;

    public Board(int[][] _tiles) {
        int n = _tiles.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = _tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String res = String.format("%d\n", dimension());
        int n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res += String.format(" %d", tiles[i][j]);
            }
            res += "\n";
        }
        return res;
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    private int abs(int v) {
        if (v >= 0) {
            return v;
        } else {
            return -v;
        }
    }

    // number of tiles out of place
    public int hamming() {
        int dist = 0;
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }

                if (tiles[i][j] != 1 + n * i + j) {
                    dist++;
                }
            }
        }
        return dist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dist = 0;
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }

                if (tiles[i][j] != 1 + n * i + j) {
                    int target_i = (tiles[i][j] - 1) / n;
                    int target_j = (tiles[i][j] - 1) % n;
                    // StdOut.println(String.format("Num: %d, target: %d %d, actual pos: %d %d, manhattan: %d",
                    //                              tiles[i][j], target_i, target_j, i, j, abs(i - target_i) + abs(j - target_j)));
                    dist += abs(i - target_i) + abs(j - target_j);
                }
            }
        }
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (! (y instanceof Board)) {
            return false;
        }

        Board b = (Board) y;
        if (b.dimension() != dimension()) {
            return false;
        }

        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != b.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private class Neighbors implements Iterable<Board> {
        public Board[] boards;
        public int length;

        public Neighbors(int[][] tiles, int num, int[] inds, int i, int j) {
            boards = new Board[num];
            length = num;
            int boardNum = 0;
            for (int k = 0; k < 4; k++) {
                if (inds[k] == 0) {
                    continue;
                }
                boards[boardNum] = new Board(tiles);
                if (k == 0) { // top
                    boards[boardNum].tiles[i][j] = boards[boardNum].tiles[i - 1][j];
                    boards[boardNum].tiles[i - 1][j] = 0;
                } else if (k == 1) { // right
                    boards[boardNum].tiles[i][j] = boards[boardNum].tiles[i][j + 1];
                    boards[boardNum].tiles[i][j + 1] = 0;
                } else if (k == 2) { // bottom
                    boards[boardNum].tiles[i][j] = boards[boardNum].tiles[i + 1][j];
                    boards[boardNum].tiles[i + 1][j] = 0;
                } else { // left
                    boards[boardNum].tiles[i][j] = boards[boardNum].tiles[i][j - 1];
                    boards[boardNum].tiles[i][j - 1] = 0;
                }
                boardNum++;
            }
        }

        private class BoardIterator implements Iterator<Board> {
            private Board[] boards;
            private int current = 0;

            public BoardIterator(Board[] _boards) {
                boards = _boards;
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

        public Iterator<Board> iterator() {
            return new BoardIterator(boards);
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int n = tiles.length;
        int[] n_inds = { 1, 1, 1, 1 }; // Top, right, bottom, left
        int dim = 4;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    if (i == 0) {
                        n_inds[0] = 0;
                        dim--;
                    } else if (i == n - 1) {
                        n_inds[2] = 0;
                        dim--;
                    }
                    if (j == 0) {
                        n_inds[3] = 0;
                        dim--;
                    } else if (j == n - 1) {
                        n_inds[1] = 0;
                        dim--;
                    }
                    return new Neighbors(tiles, dim, n_inds, i, j);
                }
            }
        }
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int n = tiles.length;
        for (int k = 1; k < n * n; k++) {
            int i1 = k / n;
            int j1 = k % n;
            int i2 = (k - 1) / n;
            int j2 = (k - 1) % n;
            if (tiles[i1][j1] != 0 && tiles[i2][j2] != 0) {
                Board nb = new Board(tiles);
                int s = nb.tiles[i1][j1];
                nb.tiles[i1][j1] = nb.tiles[i2][j2];
                nb.tiles[i2][j2] = s;
                return nb;
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] data1 = { { 0, 1, 3 },
                          { 4, 2, 5 },
                          { 7, 8, 6 } };
        // Manhattan: 1 2 3 4 5 6 7 8
        //            1 1 0 0 1 1 0 0 = 4
        Board b1 = new Board(data1);

        int[][] data2 = { { 0, 1, 3 },
                          { 4, 2, 5 },
                          { 7, 8, 6 } };
        Board b2 = new Board(data2);

        int[][] data3 = { { 1, 2, 3 },
                          { 4, 5, 6 },
                          { 7, 8, 0 } };
        Board b3 = new Board(data3);

        StdOut.println(b1);
        StdOut.print("Is goal? ");
        StdOut.println(b1.isGoal());
        StdOut.print("Hamming: ");
        StdOut.println(b1.hamming());
        StdOut.print("Manhattan: ");
        StdOut.println(b1.manhattan());
        StdOut.print("Equals b2: ");
        StdOut.println(b1.equals(b2));
        StdOut.print("Equals b3: ");
        StdOut.println(b1.equals(b3));
        StdOut.print("Equals other object: ");
        StdOut.println(b1.equals(new Object()));

        StdOut.println(b3);
        StdOut.print("Is goal? ");
        StdOut.println(b3.isGoal());
        StdOut.print("Hamming: ");
        StdOut.println(b3.hamming());
        StdOut.print("Manhattan: ");
        StdOut.println(b3.manhattan());
        StdOut.print("Equals b2: ");
        StdOut.println(b3.equals(b2));
        StdOut.print("Equals b3: ");
        StdOut.println(b3.equals(b3));
        StdOut.println("-------------------------");

        {
            int[][] data = { { 0, 1, 3 },
                             { 4, 2, 5 },
                             { 7, 8, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 1, 0, 3 },
                             { 4, 2, 5 },
                             { 7, 8, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 1, 3, 0 },
                             { 4, 2, 5 },
                             { 7, 8, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 4, 1, 3 },
                             { 0, 2, 5 },
                             { 7, 8, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 2, 1, 3 },
                             { 4, 0, 5 },
                             { 7, 8, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 5, 1, 3 },
                             { 4, 2, 0 },
                             { 7, 8, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 7, 1, 3 },
                             { 4, 2, 5 },
                             { 0, 8, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 8, 1, 3 },
                             { 4, 2, 5 },
                             { 7, 0, 6 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");

        {
            int[][] data = { { 8, 1, 3 },
                             { 4, 2, 5 },
                             { 7, 6, 0 } };
            Board b = new Board(data);
            StdOut.println(b);
            var n = b.neighbors();
            StdOut.print("Neighbors: ");
            StdOut.println(((Neighbors) n).length);

            for (var v : n) {
                StdOut.println(v);
            }

            StdOut.print("Twin: ");
            StdOut.println(b.twin());

        }
        StdOut.println("-------------------------");
    }
}
