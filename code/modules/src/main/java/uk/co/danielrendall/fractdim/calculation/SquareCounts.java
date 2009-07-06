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

    private final double squareCount;

    public SquareCounts(Set<Grid> grids) {
        double c = 0.0d;
        int i=0;
        for (Grid grid : grids) {
            c += (double)grid.getSquareCount();
            i++;
        }
        squareCount = c / (double) i;
    }

    public double getNumberOfSquares() {
        return squareCount;
    }

}
