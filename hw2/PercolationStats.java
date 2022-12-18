import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private Percolation perc;
    private double[] results;
    private int t;

    private double trial(int n) {
        int step = 0;
        perc = new Percolation(n);
        while(!perc.percolates()) {
            int row = StdRandom.uniformInt(n) + 1;
            int col = StdRandom.uniformInt(n) + 1;
            perc.open(row, col);
            step++;
        }
        return (double)perc.numberOfOpenSites() / (n * n);
    }

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        t = trials;
        results = new double[trials];
        for (int i = 0; i < trials; i++) {
            results[i] = trial(n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(t);
    }

   // test client (see below)
   public static void main(String[] args) {
       int n = Integer.parseInt(args[0]);
       int trials = Integer.parseInt(args[1]);

       if (n <= 0 || trials <= 0) {
           throw new IllegalArgumentException("n or trials is <= 0");
       }

       var ps = new PercolationStats(n, trials);

       StdOut.println("mean			= " + ps.mean());
       StdOut.println("stddev			= " + ps.stddev());
       StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
   }
}
