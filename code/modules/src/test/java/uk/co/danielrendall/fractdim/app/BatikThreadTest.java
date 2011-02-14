/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.fractdim.app;

import org.junit.Test;
import org.junit.Ignore;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.generate.Generator;
import uk.co.danielrendall.fractdim.generate.fractals.KochCurve;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.fractdim.calculation.StatisticsCalculator;
import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.calculation.StatisticsCalculatorBuilder;
import uk.co.danielrendall.fractdim.calculation.FractalMetadataUtil;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 12-Jun-2009
 * Time: 22:24:10
 * To change this template use File | Settings | File Templates.
 */
public class BatikThreadTest {

    @Test @Ignore
    public void testBatikThreading() throws CloneNotSupportedException {
        Generator gen = new Generator();
        final SVGDocument svg = gen.generateFractal(new KochCurve(), new Point(0, 0), new Point(1000, 750), 4);
        FractalController controller = FractalController.fromDocument(svg, "Batik thread test");
        final FractalDocument fractalDocument = controller.getDocument();
//        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
//        final SVGDocument svg2 = (SVGDocument) impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        long before = System.currentTimeMillis();
        final FractalDocument fractalDocument2 = fractalDocument;
        long after = System.currentTimeMillis();

        System.out.println("Cloning took " + (after - before) + " ms");


        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                StatisticsCalculatorBuilder builder = new StatisticsCalculatorBuilder();
                builder.minAngle(StatisticsCalculator.TWO_DEGREES).
                        fractalDocument(fractalDocument);
                StatisticsCalculator sc = builder.build();
                Statistics stats = sc.process();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                StatisticsCalculatorBuilder builder = new StatisticsCalculatorBuilder();
                builder.minAngle(StatisticsCalculator.TWO_DEGREES).
                        fractalDocument(fractalDocument);
                StatisticsCalculator sc = builder.build();
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
