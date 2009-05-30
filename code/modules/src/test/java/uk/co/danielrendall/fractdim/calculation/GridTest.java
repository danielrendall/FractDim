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
    private static final Logger log = Logger.getLogger(GridTest.class);


    @Test
    public void testStraightLine() {
        Point start = new Point(5.0, 3.0);
        Point end = new Point(25.0, 9.0);

        Grid pg = new Grid(10.0d);
        ParametricCurve pc = new Line(start, end);
        pg.add(pc);
        assertEquals(3, pg.getSquareCount());
    }

    @Test
    public void testSimpleKoch100() {

        Point start = new Point(0.0, 0.0);
        Point p1 = new Point(333.3333333333333, 250.0);
        Point p2 = new Point(283.49364905389035, 663.6751345948129);
        Point p3 = new Point(666.6666666666667, 500.0);
        Point end = new Point(999.999999999, 749.9999999999); // no spill over

        Grid pg = new Grid(100.0d);
        pg.add(new Line(start, p1));
        pg.add(new Line(p1, p2));
        pg.add(new Line(p2, p3));
        pg.add(new Line(p3, end));

        for (Iterator it1 = pg.squareIterator(); it1.hasNext();) {
            log.debug(it1.next());
        }

        Iterator<GridSquare> it = pg.squareIterator();
        assertTrue(new GridSquare(0, 0).equals(it.next()));
        assertTrue(new GridSquare(1, 0).equals(it.next()));
        assertTrue(new GridSquare(1, 1).equals(it.next()));
        assertTrue(new GridSquare(2, 1).equals(it.next()));
        assertTrue(new GridSquare(2, 2).equals(it.next()));
        assertTrue(new GridSquare(3, 2).equals(it.next()));
        assertTrue(new GridSquare(3, 3).equals(it.next()));
        assertTrue(new GridSquare(3, 4).equals(it.next()));
        assertTrue(new GridSquare(2, 5).equals(it.next()));
        assertTrue(new GridSquare(3, 5).equals(it.next()));
        assertTrue(new GridSquare(4, 5).equals(it.next()));
        assertTrue(new GridSquare(5, 5).equals(it.next()));
        assertTrue(new GridSquare(6, 5).equals(it.next()));
        assertTrue(new GridSquare(7, 5).equals(it.next()));
        assertTrue(new GridSquare(2, 6).equals(it.next()));
        assertTrue(new GridSquare(3, 6).equals(it.next()));
        assertTrue(new GridSquare(4, 6).equals(it.next()));
        assertTrue(new GridSquare(8, 6).equals(it.next()));
        assertTrue(new GridSquare(9, 6).equals(it.next()));
        assertTrue(new GridSquare(9, 7).equals(it.next()));
    }

    @Test
    public void testSimpleKoch69() {

        Point start = new Point(0.0, 0.0);
        Point p1 = new Point(333.3333333333333, 250.0);
        Point p2 = new Point(283.49364905389035, 663.6751345948129);
        Point p3 = new Point(666.6666666666667, 500.0);
        Point end = new Point(999.999999999, 749.9999999999); // no spill over

        Grid pg = new Grid(69.0d);
        pg.add(new Line(start, p1));
        pg.add(new Line(p1, p2));
        pg.add(new Line(p2, p3));
        pg.add(new Line(p3, end));

        for (Iterator it1 = pg.squareIterator(); it1.hasNext();) {
            log.debug(it1.next());
        }

        Iterator<GridSquare> it = pg.squareIterator();
        assertTrue(new GridSquare(0, 0).equals(it.next()));
        assertTrue(new GridSquare(1, 0).equals(it.next()));
        assertTrue(new GridSquare(1, 1).equals(it.next()));
        assertTrue(new GridSquare(2, 1).equals(it.next()));
        assertTrue(new GridSquare(2, 2).equals(it.next()));
        assertTrue(new GridSquare(3, 2).equals(it.next()));
        assertTrue(new GridSquare(4, 3).equals(it.next()));
        assertTrue(new GridSquare(4, 4).equals(it.next()));
        assertTrue(new GridSquare(4, 5).equals(it.next()));
        assertTrue(new GridSquare(4, 6).equals(it.next()));
        assertTrue(new GridSquare(4, 7).equals(it.next()));
        assertTrue(new GridSquare(7, 7).equals(it.next()));
        assertTrue(new GridSquare(8, 7).equals(it.next()));
        assertTrue(new GridSquare(9, 7).equals(it.next()));
        assertTrue(new GridSquare(10, 7).equals(it.next()));
        assertTrue(new GridSquare(4, 8).equals(it.next()));
        assertTrue(new GridSquare(5, 8).equals(it.next()));
        assertTrue(new GridSquare(6, 8).equals(it.next()));
        assertTrue(new GridSquare(7, 8).equals(it.next()));
        assertTrue(new GridSquare(10, 8).equals(it.next()));
        assertTrue(new GridSquare(11, 8).equals(it.next()));
        assertTrue(new GridSquare(4, 9).equals(it.next()));
        assertTrue(new GridSquare(5, 9).equals(it.next()));
        assertTrue(new GridSquare(12, 9).equals(it.next()));
        assertTrue(new GridSquare(13, 9).equals(it.next()));
        assertTrue(new GridSquare(13, 10).equals(it.next()));
        assertTrue(new GridSquare(14, 10).equals(it.next()));
    }
}
