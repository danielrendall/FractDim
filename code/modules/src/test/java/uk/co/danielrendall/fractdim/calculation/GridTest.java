package uk.co.danielrendall.fractdim.calculation;

import org.junit.Test;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.geom.Line;
import uk.co.danielrendall.fractdim.geom.Point;
import static org.junit.Assert.*;
import org.apache.log4j.Logger;

import java.util.Iterator;

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
