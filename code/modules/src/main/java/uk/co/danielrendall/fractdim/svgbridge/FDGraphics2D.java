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

package uk.co.danielrendall.fractdim.svgbridge;

import org.apache.batik.ext.awt.g2d.DefaultGraphics2D;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import uk.co.danielrendall.mathlib.geom2d.*;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.fractdim.logging.Log;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 23:27:39
 */
public abstract class FDGraphics2D extends DefaultGraphics2D {

    protected Point start = Point.ORIGIN;
    private final double[] pathSegment = new double[6];
    private final AffineTransform rawTransform = AffineTransform.getScaleInstance(1.0d, 1.0d);

    public FDGraphics2D() {
        super(true);
        gc = new GraphicContext();
    }

    @Override
    public void draw(Shape s) {
        PathIterator pit = s.getPathIterator(rawTransform);
        int count = 0;
        while (!pit.isDone()) {
            count++;
            int type = pit.currentSegment(pathSegment);
            switch (type) {
                case PathIterator.SEG_CLOSE:
                    break;
                case PathIterator.SEG_MOVETO:
                    this.start = new Point(pathSegment[0], pathSegment[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    Point end = new Point(pathSegment[0], pathSegment[1]);
                    handleCurve(new Line(start, end));
                    this.start = end;
                    break;
                case PathIterator.SEG_QUADTO:
                    Point end1 = new Point(pathSegment[2], pathSegment[3]);
                    handleCurve(new BezierQuad(start, new Point(pathSegment[0], pathSegment[1]), end1));
                    this.start = end1;
                    break;
                case PathIterator.SEG_CUBICTO:
                    Point end2 = new Point(pathSegment[4], pathSegment[5]);
                    handleCurve(new BezierCubic(start, new Point( pathSegment[0], pathSegment[1]), new Point(pathSegment[2], pathSegment[3]), end2));
                    this.start = end2;
                    break;
            }
            pit.next();
        }
    }

    public abstract void handleCurve(ParametricCurve curve);

    // ignore for now - treat as draw
    @Override
    public void fill(Shape s) {
        Log.misc.debug("Filling shape " + s.toString());
        draw(s);
    }

    @Override
    public void dispose() {
        gc = null;
    }

    // I have no idea why it's necessary to override this, but if you don't then the method in the superclass
    // returns a new Graphics2D.
    public Graphics create(){
        return this;
    }

}
