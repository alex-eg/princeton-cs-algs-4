import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF union_find;
    private int grid_n;
    private int grid_size;
    private int opened;
    private int[][] grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        opened = 0;
        grid_n = n;
        grid_size = n * n;
        grid = new int[n][n];
        for (int i = 0; i < grid_size; i++) {
            grid[i / grid_n][i % grid_n] = 0;
        }
        union_find = new WeightedQuickUnionUF(grid_size + 2); // 2 for virtual top&bottom
    }

    private int toUfIndex(int row, int col) {
        return (row - 1) * grid_n + col - 1 + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > grid_n || col > grid_n) {
            throw new IllegalArgumentException();
        }

        if (isOpen(row, col)) {
            return;
        }

        grid[row - 1][col - 1] = 1;
        if (row == 1) {
            union_find.union(0, toUfIndex(row, col));
        }
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                union_find.union(toUfIndex(row, col), toUfIndex(row - 1, col));
            }
        }

        if (row < grid_n) {
            if (isOpen(row + 1, col)) {
                union_find.union(toUfIndex(row, col), toUfIndex(row + 1, col));
            }
        }

        if (col > 1) {
            if (isOpen(row, col - 1)) {
                union_find.union(toUfIndex(row, col), toUfIndex(row, col - 1));
            }
        }

        if (col < grid_n) {
            if (isOpen(row, col + 1)) {
                union_find.union(toUfIndex(row, col), toUfIndex(row, col + 1));
            }
        }

        if (row == grid_n) {
            union_find.union(grid_size + 1, toUfIndex(row, col));
        }

        opened++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > grid_n || col > grid_n) {
            throw new IllegalArgumentException();
        }

        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > grid_n || col > grid_n) {
            throw new IllegalArgumentException();
        }

        return union_find.find(0) == union_find.find(toUfIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opened;
    }

    // does the system percolate?
    public boolean percolates() {
        return union_find.find(0) == union_find.find(grid_size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
