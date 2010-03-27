package uk.co.danielrendall.fractdim.calculation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.fractdim.calculation.grids.Grid;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 19:10:13
 */
public class GridTest {

    @Test
    public void testResult1() {
        Grid g = new Grid(100.d);
        Point start = new Point(31.0d, 66.0d);
        Point mid = new Point(153.0d, 124.0d);
        Point end = new Point(279.0d, 180.0d);
        g.startEvaluation(start, end);

        boolean[] result = g.notifyNewPoint(start, mid, end);
        assertTrue(result[0]); // points should be in diagonal squares
        assertFalse(result[1]); // points should be in adjacent squares

    }

}
