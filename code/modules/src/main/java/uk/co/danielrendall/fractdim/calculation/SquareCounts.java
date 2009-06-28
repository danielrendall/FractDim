package uk.co.danielrendall.fractdim.calculation;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
* User: daniel
* Date: 28-Jun-2009
* Time: 17:45:10
* To change this template use File | Settings | File Templates.
*/
public class SquareCounts {

    private final int[] squareCounts;

    public SquareCounts(Set<Grid> grids) {
        squareCounts = new int[grids.size()];
        int i=0;
        for (Grid grid : grids) {
            squareCounts[i++] = grid.getSquareCount();
        }
    }

    public double getNumberOfSquares() {
        double d=0.0;
        for (int squareCount : squareCounts) {
            d += (double) squareCount;
        }
        return d / (double) squareCounts.length;
    }
}
