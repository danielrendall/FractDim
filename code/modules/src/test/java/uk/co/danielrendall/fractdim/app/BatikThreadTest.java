package uk.co.danielrendall.fractdim.app;

import org.junit.Test;
import org.junit.Ignore;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.DOMImplementation;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import uk.co.danielrendall.fractdim.generate.Generator;
import uk.co.danielrendall.fractdim.generate.fractals.KochCurve;
import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.calculation.StatisticsCalculator;
import uk.co.danielrendall.fractdim.calculation.Statistics;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 12-Jun-2009
 * Time: 22:24:10
 * To change this template use File | Settings | File Templates.
 */
public class BatikThreadTest {

    @Test @Ignore
    public void testBatikThreading() {
        Generator gen = new Generator();
        final SVGDocument svg = gen.generateFractal(new KochCurve(), new Point(0, 0), new Point(1000, 750), 4);

//        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
//        final SVGDocument svg2 = (SVGDocument) impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        long before = System.currentTimeMillis();
        final SVGDocument svg2 = (SVGDocument) (svg.cloneNode(false));
        long after = System.currentTimeMillis();

        System.out.println("Cloning took " + (after - before) + " ms");

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                StatisticsCalculator sc = new StatisticsCalculator(svg, Math.PI / 90.0d);
                Statistics stats = sc.process();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                StatisticsCalculator sc = new StatisticsCalculator(svg2, Math.PI / 90.0d);
                Statistics stats = sc.process();
            }
        });

        thread1.start();
        thread2.start();

//        StatisticsCalculator sc = new StatisticsCalculator(svg, Math.PI / 90.0d);
//        Statistics stats = sc.process();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
