package uk.co.danielrendall.fractdim.geom;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 11:08:18
 */
public class Point extends XY {

    public final static Point ORIGIN = new Point(0.0d, 0.0d);

    public Point(double x, double y) {
        super(x, y);
    }

    public Point displace(Vec vec) {
        return new Point(x + vec.x(), y + vec.y());
    }

    public Line line(Point other) {
        return new Line(this, other);
    }

    public Line line(Vec vec) {
        return new Line(this, vec);
    }

}
