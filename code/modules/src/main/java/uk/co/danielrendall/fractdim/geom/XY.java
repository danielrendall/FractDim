package uk.co.danielrendall.fractdim.geom;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 10:02:49
 */

class XY {

    protected final double x, y;

    public XY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public String toString() {
        return (String.format("(%s, %s)", x, y));
    }
    
}
