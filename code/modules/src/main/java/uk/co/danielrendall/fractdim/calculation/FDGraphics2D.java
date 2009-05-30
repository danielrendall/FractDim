package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.ext.awt.g2d.DefaultGraphics2D;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.log4j.Logger;

import java.awt.geom.PathIterator;
import java.awt.*;

import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.geom.Line;
import uk.co.danielrendall.fractdim.geom.BezierQuad;
import uk.co.danielrendall.fractdim.geom.BezierCubic;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 23:27:39
 */
public class FDGraphics2D extends DefaultGraphics2D {
    private static final Logger log = Logger.getLogger(FDGraphics2D.class);

    private Point start = Point.ORIGIN;
    private final double[] pathSegment = new double[6];

    private Grid grid;

    public FDGraphics2D(FDGraphics2D g) {
        super(g);
        this.grid = g.grid;
    }

    public FDGraphics2D(Grid grid) {
        super(true);
        gc = new GraphicContext();
        this.grid = grid;
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
                    setStart(new Point(pathSegment[0], pathSegment[1]));
                    break;
                case PathIterator.SEG_LINETO:
                    drawLine(new Point(pathSegment[0], pathSegment[1]));
                    break;
                case PathIterator.SEG_QUADTO:
                    drawQuadratic(new Point(pathSegment[0], pathSegment[1]),
                            new Point(pathSegment[2], pathSegment[3]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    drawCubic(new Point( pathSegment[0], pathSegment[1]),
                            new Point(pathSegment[2], pathSegment[3]),
                            new Point(pathSegment[4], pathSegment[5]));
                    break;
            }
            pit.next();
        }
    }

    private void setStart(Point start) {
        this.start = start;
    }

    private void drawLine(Point end) {
        grid.add(new Line(start, end));
        setStart(end);
    }

    private void drawQuadratic(Point control, Point end) {
        grid.add(new BezierQuad(start, control, end));
        setStart(end);
    }

    private void drawCubic(Point control1, Point control2, Point end) {
        grid.add(new BezierCubic(start, control1, control2, end));
        setStart(end);
    }

    // ignore for now - treat as draw
    @Override
    public void fill(Shape s) {
        log.debug("Filling shape " + s.toString());
        draw(s);
    }

    @Override
    public void dispose() {
        grid = null;
        gc = null;
    }

    public Graphics create(){
        return new FDGraphics2D(this);
    }

}
