import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] percolationThresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Outside prescribed range");
        } else {
            Percolation percolation;
            for (int i = 0; i < trials; i++) {
                percolation = new Percolation(n);
                percolationThresholds = new double[trials];
                while (!percolation.percolates()) {
                    // Open a site randomly
                    int row = StdRandom.uniform(1, n + 1);
                    int column = StdRandom.uniform(1, n + 1);
                    // System.out.println("Randomly selected row, col " + row + "," + column);
                    if (percolation.isOpen(row, column)) {
                        continue;
                    }
                    percolation.open(row, column);
                }
                // System.out.println("Open sites " + percolation.numberOfOpenSites());
                percolationThresholds[i] = (1.0 * percolation.numberOfOpenSites()) / (n * n);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return 1.0;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return 1.0;
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(20, 1);
//        PercolationStats percolationStats = new PercolationStats(
//                Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean: " + percolationStats.mean());
        System.out.println("stddev: " + percolationStats.stddev());
        System.out.println("95% confidence interval: ["
                + percolationStats.confidenceLo() + "," +
                percolationStats.confidenceHi() + "]");
    }

}
