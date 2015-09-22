
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean [] statusMatrix;
    private int count;
    private WeightedQuickUnionUF quickunion,auxGrid;
    private int N;
    public Percolation(int N) // create N-by-N grid, with all sites blocked
    {
            int countSite = N * N;
            this.N = N;
            statusMatrix = new boolean[countSite + 2];
            quickunion = new WeightedQuickUnionUF(countSite + 2);
            auxGrid = new WeightedQuickUnionUF(countSite + 1);

            // Initialize all sites to be blocked.
            for (int i = 0; i < countSite; i++) {
                statusMatrix[i] = false;
            }
             // Initialize virtual top and bottom site with open state
            statusMatrix[0] = true;
            statusMatrix[countSite+1] = true;
    }

    // return array index of given row i and column j
    private int xyToIndex(int i, int j) {
        // Attention: i and j are of range 1 ~ N, NOT 0 ~ N-1.
        // Throw IndexOutOfBoundsException if i or j is not valid
        if (i <= 0 || i > N)
            throw new IndexOutOfBoundsException("row i out of bound");
        if (j <= 0 || j > N)
            throw new IndexOutOfBoundsException("column j out of bound");

        return (i - 1) * N + j;
    }

    private boolean isTopSite(int index) {
        return index <= N;
    }
    private boolean isBottomSite(int index) {
        return index >= (N - 1) * N + 1;
    }

    public void open(int i, int j) {   // open site (row i, column j) if it is not open already

        int idx = xyToIndex(i, j);
        statusMatrix[idx] = true;

        // Traverse surrounding sites, connect all open ones.
        // Make sure we do not index sites out of bounds.
        if (i != 1 && isOpen(i-1, j)) {
            quickunion.union(idx, xyToIndex(i-1, j));
            auxGrid.union(idx, xyToIndex(i-1, j));
        }
        if (i != N && isOpen(i+1, j)) {
            quickunion.union(idx, xyToIndex(i+1, j));
            auxGrid.union(idx, xyToIndex(i+1, j));
        }
        if (j != 1 && isOpen(i, j-1)) {
            quickunion.union(idx, xyToIndex(i, j-1));
            auxGrid.union(idx, xyToIndex(i, j-1));
        }
        if (j != N && isOpen(i, j+1)) {
            quickunion.union(idx, xyToIndex(i, j+1));
            auxGrid.union(idx, xyToIndex(i, j+1));
        }
        // if site is on top or bottom, connect to corresponding virtual site.
        if (isTopSite(idx)) {
            quickunion.union(0, idx);
            auxGrid.union(0, idx);
        }
        if (isBottomSite(idx))  quickunion.union(statusMatrix.length-1, idx);


    }

    public boolean isOpen(int i, int j) {  // is site (row i, column j) open?
        int idx = xyToIndex(i, j);
        return statusMatrix[idx];
    }

    public boolean isFull(int i, int j) {  // is site (row i, column j) full?
        int idx = xyToIndex(i, j);
        return quickunion.connected(0, idx) && auxGrid.connected(0, idx);
    }

    public boolean percolates() {   // does the system percolate?
        return quickunion.connected(0, statusMatrix.length-1);
    }
}