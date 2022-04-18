import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Percolation {
    private final int[] states;
    private int numOpenSites;
    private final int n;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final int oneDArraySize;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Outside prescribed range");
        } else {
            numOpenSites = 0;
            this.n = n;
            // 1D array from 2D grid
            oneDArraySize = (n * n) + 2; // Lat 2 are vertical top & bottom sites
            states = new int[oneDArraySize];
            weightedQuickUnionUF = new WeightedQuickUnionUF(oneDArraySize);
            // All sites blocked
            for (int i = 0; i < oneDArraySize; i++) {
                states[i] = 0; // 0-> Closed, 1 -> Open
            }
            // Connect Vertical Top/Bottom sites
            for (int col = 1; col <= n; col++) {
                weightedQuickUnionUF.union(oneDArraySize - 2, xyTo1D(1, col));
                weightedQuickUnionUF.union(oneDArraySize - 1, xyTo1D(n, col));
            }
            // Open the top sites
            states[oneDArraySize - 2] = 1;
            states[oneDArraySize - 1] = 1;
            //if (ENABLE_FAST) System.out.println("States : " + Arrays.toString(states));
        }
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // System.out.println("Open Request for " + row + ", " + col);
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException("Outside prescribed range");
        } else {
            if (!isOpen(row, col)) {
                numOpenSites += 1;
                int cell1 = xyTo1D(row, col);
                states[cell1] = 1;
                String[] openSites = getOpenSitesForCell(row, col);
                // System.out.println("Open sites for " + cell1 + " is " + Arrays.toString(openSites));
                for (String str : openSites) {
                    if (null != str) {
                        int cell2 = xyTo1D(
                                Character.getNumericValue(str.charAt(0)),
                                Character.getNumericValue(str.charAt(0)));
                        // System.out.println("Union of " + cell1 + " & " + cell2);
                        weightedQuickUnionUF.union(cell2, cell1);
                    }
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException("Outside prescribed range");
        } else {
            return states[xyTo1D(row, col)] == 1;
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException("Outside prescribed range");
        } else {
            return states[xyTo1D(row, col)] == 0;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // System.out.println("Percolates Check with " + weightedQuickUnionUF.find(oneDArraySize - 2) + " " + weightedQuickUnionUF.find(oneDArraySize - 1));
        return (weightedQuickUnionUF.find(oneDArraySize - 2) ==
                weightedQuickUnionUF.find(oneDArraySize - 1));
    }

    private boolean validateIndices(int x, int y) {
        return x >= 1 && y >= 1 && x <= n && y <= n;
    }

    private String[] getOpenSitesForCell(int row, int column) {
        String[] sites = new String[4];
        if (validateIndices(row, column - 1)) {
            if (isOpen(row, column - 1)) { // left
                sites[0] = String.valueOf(row) + (column - 1);
            }
        }
        if (validateIndices(row - 1, column)) { // up
            if (isOpen(row - 1, column)) {
                sites[1] = String.valueOf(row - 1) + column;
            }
        }
        if (validateIndices(row, column + 1)) { // right
            if (isOpen(row, column + 1)) {
                sites[2] = String.valueOf(row) + (column + 1);
            }
        }
        if (validateIndices((row + 1), column)) { // down
            if (isOpen(row + 1, column)) {
                sites[3] = String.valueOf(row + 1) + column;
            }
        }
        return sites;
    }

    private int xyTo1D(int x, int y) {
        return ((x - 1) * n) + (y - 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("input6.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            
        } catch (FileNotFoundException exp) {
            System.out.println("File not found");
            exp.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Percolation percolation = new Percolation(6);
        percolation.open(1, 6);
        percolation.open(2, 6);
        percolation.open(3, 6);
        System.out.println(percolation.percolates());
    }
}
