package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.ext.awt.g2d.DefaultGraphics2D;
import org.apache.batik.ext.awt.g2d.GraphicContext;

import java.awt.geom.PathIterator;
import java.awt.*;

import uk.co.danielrendall.fractdim.geom.*;
import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.logging.Log;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 23:27:39
 */
public class FDGraphics2D extends DefaultGraphics2D {

    private Point start = Point.ORIGIN;
    private final double[] pathSegment = new double[6];

    private GridCollection grids;

    public FDGraphics2D(FDGraphics2D g) {
        super(g);
        this.grids = g.grids;
    }

    public FDGraphics2D(GridCollection grids) {
        super(true);
        gc = new GraphicContext();
        this.grids = grids;
    }

    @Override
    public void draw(Shape s) {
        PathIterator pit = s.getPathIterator(gc.getTransform());
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
                    grids.handleCurve(new Line(start, end));
                    this.start = end;
                    break;
                case PathIterator.SEG_QUADTO:
                    Point end1 = new Point(pathSegment[2], pathSegment[3]);
                    grids.handleCurve(new BezierQuad(start, new Point(pathSegment[0], pathSegment[1]), end1));
                    this.start = end1;
                    break;
                case PathIterator.SEG_CUBICTO:
                    Point end2 = new Point(pathSegment[4], pathSegment[5]);
                    grids.handleCurve(new BezierCubic(start, new Point( pathSegment[0], pathSegment[1]), new Point(pathSegment[2], pathSegment[3]), end2));
                    this.start = end2;
                    break;
            }
            pit.next();
        }
    }

    // ignore for now - treat as draw
    @Override
    public void fill(Shape s) {
        Log.misc.debug("Filling shape " + s.toString());
        draw(s);
    }

    @Override
    public void dispose() {
        grids = null;
        gc = null;
    }

    public Graphics create(){
        return new FDGraphics2D(this);
    }

}
