package uk.co.danielrendall.fractdim.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.w3c.dom.svg.SVGDocument;
import org.kohsuke.args4j.CmdLineException;
import uk.co.danielrendall.fractdim.geom.BezierCubic;
import uk.co.danielrendall.fractdim.geom.Line;
import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.generate.fractals.KochCurve;
import uk.co.danielrendall.fractdim.generate.Generator;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 * @author Daniel Rendall
 * @created 31-May-2009 11:20:00
 */
public class SquareCounterTest {

    @Test
    public void testStraightLine() {
        Point start = new Point(50.0, 30.0);
        Point end = new Point(250.0, 90.0);

        SquareCounter gc = new SquareCounter(1000);
        Grid pg = new Grid(100.0d);
        gc.addGrid(pg);
        gc.handleCurve(new Line(start, end));
        assertEquals(3, pg.getSquareCount());
    }

    @Test
    public void testSimpleKoch100() {

        Point start = new Point(0.0, 0.0);
        Point p1 = new Point(333.3333333333333, 250.0);
        Point p2 = new Point(283.49364905389035, 663.6751345948129);
        Point p3 = new Point(666.6666666666667, 500.0);
        Point end = new Point(999.999999999, 749.9999999999); // no spill over

        SquareCounter gc = new SquareCounter(1000);
        Grid pg = new Grid(100.0d);
        gc.addGrid(pg);

        gc.handleCurve(new Line(start, p1));
        gc.handleCurve(new Line(p1, p2));
        gc.handleCurve(new Line(p2, p3));
        gc.handleCurve(new Line(p3, end));

        for (Iterator it1 = pg.squareIterator(); it1.hasNext();) {
            Log.test.debug(it1.next());
        }

        Iterator<GridSquare> it = pg.squareIterator();
        assertTrue(GridSquare.create(0, 0).equals(it.next()));
        assertTrue(GridSquare.create(1, 0).equals(it.next()));
        assertTrue(GridSquare.create(1, 1).equals(it.next()));
        assertTrue(GridSquare.create(2, 1).equals(it.next()));
        assertTrue(GridSquare.create(2, 2).equals(it.next()));
        assertTrue(GridSquare.create(3, 2).equals(it.next()));
        assertTrue(GridSquare.create(3, 3).equals(it.next()));
        assertTrue(GridSquare.create(3, 4).equals(it.next()));
        assertTrue(GridSquare.create(2, 5).equals(it.next()));
        assertTrue(GridSquare.create(3, 5).equals(it.next()));
        assertTrue(GridSquare.create(4, 5).equals(it.next()));
        assertTrue(GridSquare.create(5, 5).equals(it.next()));
        assertTrue(GridSquare.create(6, 5).equals(it.next()));
        assertTrue(GridSquare.create(7, 5).equals(it.next()));
        assertTrue(GridSquare.create(2, 6).equals(it.next()));
        assertTrue(GridSquare.create(3, 6).equals(it.next()));
        assertTrue(GridSquare.create(4, 6).equals(it.next()));
        assertTrue(GridSquare.create(8, 6).equals(it.next()));
        assertTrue(GridSquare.create(9, 6).equals(it.next()));
        assertTrue(GridSquare.create(9, 7).equals(it.next()));
    }

    @Test
    public void testSimpleKoch69() {

        Point start = new Point(0.0, 0.0);
        Point p1 = new Point(333.3333333333333, 250.0);
        Point p2 = new Point(283.49364905389035, 663.6751345948129);
        Point p3 = new Point(666.6666666666667, 500.0);
        Point end = new Point(999.999999999, 749.9999999999); // no spill over

        SquareCounter gc = new SquareCounter(1000);
        Grid pg = new Grid(69.0d);
        gc.addGrid(pg);

        gc.handleCurve(new Line(start, p1));
        gc.handleCurve(new Line(p1, p2));
        gc.handleCurve(new Line(p2, p3));
        gc.handleCurve(new Line(p3, end));

        for (Iterator it1 = pg.squareIterator(); it1.hasNext();) {
            Log.test.debug(it1.next());
        }

        Iterator<GridSquare> it = pg.squareIterator();
        assertTrue(GridSquare.create(0, 0).equals(it.next()));
        assertTrue(GridSquare.create(1, 0).equals(it.next()));
        assertTrue(GridSquare.create(1, 1).equals(it.next()));
        assertTrue(GridSquare.create(2, 1).equals(it.next()));
        assertTrue(GridSquare.create(2, 2).equals(it.next()));
        assertTrue(GridSquare.create(3, 2).equals(it.next()));
        assertTrue(GridSquare.create(4, 3).equals(it.next()));
        assertTrue(GridSquare.create(4, 4).equals(it.next()));
        assertTrue(GridSquare.create(4, 5).equals(it.next()));
        assertTrue(GridSquare.create(4, 6).equals(it.next()));
        assertTrue(GridSquare.create(4, 7).equals(it.next()));
        assertTrue(GridSquare.create(7, 7).equals(it.next()));
        assertTrue(GridSquare.create(8, 7).equals(it.next()));
        assertTrue(GridSquare.create(9, 7).equals(it.next()));
        assertTrue(GridSquare.create(10, 7).equals(it.next()));
        assertTrue(GridSquare.create(4, 8).equals(it.next()));
        assertTrue(GridSquare.create(5, 8).equals(it.next()));
        assertTrue(GridSquare.create(6, 8).equals(it.next()));
        assertTrue(GridSquare.create(7, 8).equals(it.next()));
        assertTrue(GridSquare.create(10, 8).equals(it.next()));
        assertTrue(GridSquare.create(11, 8).equals(it.next()));
        assertTrue(GridSquare.create(4, 9).equals(it.next()));
        assertTrue(GridSquare.create(5, 9).equals(it.next()));
        assertTrue(GridSquare.create(12, 9).equals(it.next()));
        assertTrue(GridSquare.create(13, 9).equals(it.next()));
        assertTrue(GridSquare.create(13, 10).equals(it.next()));
        assertTrue(GridSquare.create(14, 10).equals(it.next()));
    }

    @Test
    public void testMultipleGridsWithLine() {
        Point start = new Point(50.001, 30.001);
        Point end = new Point(249.999, 89.999);

        SquareCounter gc = new SquareCounter(1000);
        Grid pg100 = new Grid(100.0d);
        Grid pg80 = new Grid(80);
        Grid pg60 = new Grid(60);
        Grid pg40 = new Grid(40);
        Grid pg20 = new Grid(20);
        Grid pg10 = new Grid(10);
        gc.addGrid(pg100);
        gc.addGrid(pg80);
        gc.addGrid(pg60);
        gc.addGrid(pg40);
        gc.addGrid(pg20);
        gc.addGrid(pg10);
        gc.handleCurve(new Line(start, end));
        assertEquals(3, pg100.getSquareCount());
        assertEquals(5, pg80.getSquareCount());
        assertEquals(6, pg60.getSquareCount());
        assertEquals(8, pg40.getSquareCount());
        assertEquals(14, pg20.getSquareCount());
        assertEquals(24, pg10.getSquareCount());
    }

    @Test
    public void testMultipleGridsWithBezier() {
        Point start = new Point(50.001, 30.001);
        Point control1 = new Point(100.0, 150.0);
        Point control2 = new Point(210.0, 160.0);
        Point end = new Point(249.999, 90.001);

        SquareCounter gc = new SquareCounter(1000);
        Grid pg100 = new Grid(100.0d);
        Grid pg80 = new Grid(80);
        Grid pg60 = new Grid(60);
        Grid pg40 = new Grid(40);
        Grid pg20 = new Grid(20);
        Grid pg10 = new Grid(10);
        gc.addGrid(pg100);
        gc.addGrid(pg80);
        gc.addGrid(pg60);
        gc.addGrid(pg40);
        gc.addGrid(pg20);
        gc.addGrid(pg10);
        gc.handleCurve(new BezierCubic(start, control1, control2, end));
        assertEquals(5, pg100.getSquareCount());
        assertEquals(5, pg80.getSquareCount());
        assertEquals(8, pg60.getSquareCount());
        assertEquals(10, pg40.getSquareCount());
        assertEquals(18, pg20.getSquareCount());
        assertEquals(34, pg10.getSquareCount());
    }

    @Test
    public void testCreateCollection() {
        SquareCounter collection = SquareCounter.createSquareCounter(1000, 1.0d, 10.0d, 9, 1, 1);
        assertEquals(10, collection.count());

        // four angles
        collection = SquareCounter.createSquareCounter(1000, 1.0d, 10.0d, 9, 4, 1);
        assertEquals(40, collection.count());

        // four angles, 3 displacement points (= 9 displacement vectors)
        collection = SquareCounter.createSquareCounter(1000, 1.0d, 10.0d, 9, 4, 3);
        assertEquals(360, collection.count());
    }

    @Test
    public void testGridDisplacements() {
        Point start = new Point(29.198, 29.198);
        Point end = new Point(286.171, 88.963);

        SquareCounter gc = new SquareCounter(1000);
        Grid pg100 = new Grid(100.0d);

        Grid pg100_x1 = new Grid(100.0d, 0.5d, 0.0d);

        Grid pg100_y1 = new Grid(100.0d, 0.0d, 0.5d);

        Grid pg100_x1_y1 = new Grid(100.0d, 0.5d, 0.5d);

        gc.addGrid(pg100);
        gc.addGrid(pg100_x1);
        gc.addGrid(pg100_y1);
        gc.addGrid(pg100_x1_y1);
        gc.handleCurve(new Line(start, end));

        assertEquals(3, pg100.getSquareCount());
        assertEquals(4, pg100_x1.getSquareCount());
        assertEquals(4, pg100_y1.getSquareCount());
        assertEquals(5, pg100_x1_y1.getSquareCount());

    }

    @Test
    public void testGridRotation() {
        Point start = new Point(50.001, 30.001);
        Point control1 = new Point(100.0, 150.0);
        Point control2 = new Point(210.0, 160.0);
        Point end = new Point(249.999, 90.001);

        SquareCounter gc = new SquareCounter(1000);
        Grid pg100 = new Grid(100.0d);
        Grid pg100_10 = new Grid(Math.PI / 18.0d, 100.0d);
        Grid pg100_20 = new Grid(2.0d * Math.PI / 18.0d, 100.0d);
        Grid pg100_30 = new Grid(3.0d * Math.PI / 18.0d, 100.0d);
        Grid pg100_40 = new Grid(4.0d * Math.PI / 18.0d, 100.0d);
        Grid pg100_50 = new Grid(5.0d * Math.PI / 18.0d, 100.0d);
        Grid pg100_60 = new Grid(6.0d * Math.PI / 18.0d, 100.0d);
        Grid pg100_70 = new Grid(7.0d * Math.PI / 18.0d, 100.0d);
        Grid pg100_80 = new Grid(8.0d * Math.PI / 18.0d, 100.0d);
        Grid pg100_90 = new Grid(9.0d * Math.PI / 18.0d, 100.0d);
        gc.addGrid(pg100);
        gc.addGrid(pg100_10);
        gc.addGrid(pg100_20);
        gc.addGrid(pg100_30);
        gc.addGrid(pg100_40);
        gc.addGrid(pg100_50);
        gc.addGrid(pg100_60);
        gc.addGrid(pg100_70);
        gc.addGrid(pg100_80);
        gc.addGrid(pg100_90);
        gc.handleCurve(new BezierCubic(start, control1, control2, end));
        assertEquals(5, pg100.getSquareCount());
        assertEquals(4, pg100_10.getSquareCount());
        assertEquals(4, pg100_20.getSquareCount());
        assertEquals(4, pg100_30.getSquareCount());
        assertEquals(4, pg100_40.getSquareCount());
        assertEquals(5, pg100_50.getSquareCount());
        assertEquals(4, pg100_60.getSquareCount());
        assertEquals(4, pg100_70.getSquareCount());
        assertEquals(5, pg100_80.getSquareCount());
        assertEquals(5, pg100_90.getSquareCount());

    }

    @Test
    public void testGridRotationAndDisplacement() {
        Point start = new Point(50.001, 30.001);
        Point control1 = new Point(100.0, 150.0);
        Point control2 = new Point(210.0, 160.0);
        Point end = new Point(249.999, 90.001);

        SquareCounter gc = new SquareCounter(1000);
        Grid pg60 = new Grid(60.0d);
        Grid pg60_30 = new Grid(3.0d * Math.PI / 18.0d, 60.0d);
        Grid pg60_70 = new Grid(7.0d * Math.PI / 18.0d, 60.0d);
        Grid pg60_x1 = new Grid(60.0d, 0.5d, 0.0d);
        Grid pg60_30_x1 = new Grid(3.0d * Math.PI / 18.0d, 60.0d, 0.5d, 0.0d);
        Grid pg60_70_x1 = new Grid(7.0d * Math.PI / 18.0d, 60.0d, 0.5d, 0.0d);
        Grid pg60_y1 = new Grid(60.0d, 0.0d, 0.5d);
        Grid pg60_30_y1 = new Grid(3.0d * Math.PI / 18.0d, 60.0d, 0.0d, 0.5d);
        Grid pg60_70_y1 = new Grid(7.0d * Math.PI / 18.0d, 60.0d, 0.0d, 0.5d);
        Grid pg60_x1_y1 = new Grid(60.0d, 0.5d, 0.5d);
        Grid pg60_30_x1_y1 = new Grid(3.0d * Math.PI / 18.0d, 60.0d, 0.5d, 0.5d);
        Grid pg60_70_x1_y1 = new Grid(7.0d * Math.PI / 18.0d, 60.0d, 0.5d, 0.5d);
        gc.addGrid(pg60);
        gc.addGrid(pg60_30);
        gc.addGrid(pg60_70);
        gc.addGrid(pg60_x1);
        gc.addGrid(pg60_30_x1);
        gc.addGrid(pg60_70_x1);
        gc.addGrid(pg60_y1);
        gc.addGrid(pg60_30_y1);
        gc.addGrid(pg60_70_y1);
        gc.addGrid(pg60_x1_y1);
        gc.addGrid(pg60_30_x1_y1);
        gc.addGrid(pg60_70_x1_y1);
        gc.handleCurve(new BezierCubic(start, control1, control2, end));
        assertEquals(8, pg60.getSquareCount());
        assertEquals(6, pg60_30.getSquareCount());
        assertEquals(8, pg60_70.getSquareCount());
        assertEquals(7, pg60_x1.getSquareCount());
        assertEquals(6, pg60_30_x1.getSquareCount());
        assertEquals(6, pg60_70_x1.getSquareCount());
        assertEquals(6, pg60_y1.getSquareCount());
        assertEquals(7, pg60_30_y1.getSquareCount());
        assertEquals(6, pg60_70_y1.getSquareCount());
        assertEquals(5, pg60_x1_y1.getSquareCount());
        assertEquals(7, pg60_30_x1_y1.getSquareCount());
        assertEquals(7, pg60_70_x1_y1.getSquareCount());

    }

    @Test
    public void testCalculateFractalDimension() {

        for (int i=0; i< 4; i++) {
            Generator gen = new Generator();

            double angle = (Math.PI / 2.0) * (double) i;

            double startX = 0.1d * Math.cos(angle);
            double startY = 0.1d * Math.sin(angle);

            double endX = 1001.0d * Math.cos(angle);
            double endY = 1001.0d * Math.sin(angle);

            SVGDocument svg = gen.generateFractal(new KochCurve(), new Point(startX, startY).rotate(Math.PI / 90d), new Point(endX, endY).rotate(Math.PI / 90d), 3);


            try {
                // Prepare the DOM document for writing
                Source source = new DOMSource(svg);

                // Prepare the output file
                File file = new File("/tmp/mem/koch" + i + ".svg");

                OutputStream fos = new FileOutputStream(file);

                Result result = new StreamResult(fos);

                // Write the DOM document to the file
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            StatisticsCalculator sc = new StatisticsCalculator(Math.PI / 90.0d);

            Statistics stats = sc.process(svg);

            Log.test.info(stats.toString());

            CalculationSettings settings = new CalculationSettings(stats);

            Log.test.info(settings.toString());

            SquareCounter counter = SquareCounter.createSquareCounter(30,
                    settings.getMinimumSquareSize(),
                    settings.getMaximumSquareSize(),
                    settings.getNumberOfResolutions(),
                    settings.getNumberOfAngles(),
                    3);

            SquareCountingResult result = counter.process(svg);
            GridSquare.resetCount();
        }
    }
}
