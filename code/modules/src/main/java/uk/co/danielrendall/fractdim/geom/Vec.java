package uk.co.danielrendall.fractdim.geom;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 10:51:28
 */
public class Vec {

    private final Complex rep;

    private Vec(Complex c) {
        rep = c;
    }

    public Vec(XY to) {
        rep = new Complex(to.x(), to.y());
    }

    public Vec(XY from, XY to) {
        rep = new Complex(to.x() - from.x(), to.y() - from.y());
    }

    public double x() {
        return rep.x();
    }

    public double y() {
        return rep.y();
    }

    public double length() {
        return rep.mod();
    }

    public Vec add(Vec other) {
        return new Vec(rep.add(other.rep));
    }

    public Vec sub(Vec other) {
        return new Vec(rep.sub(other.rep));
    }

    public Vec neg() {
        return new Vec(rep.neg());
    }

    public Vec rotate(double angle) {
        return new Vec(rep.rotate(angle));
    }

    public Vec scale(double factor) {
        return new Vec(rep.times(factor));
    }

    public Vec shrink(double factor) {
        // todo - handle zero
        return scale(1.0d / factor);
    }

    public Vec scaleAndRotate(double factor, double angle) {
        return new Vec(rep.times(Complex.modArg(factor, angle)));
    }

    public Vec shrinkAndRotate(double factor, double angle) {
        return scaleAndRotate(1.0d / factor, angle);
    }

    public String toString() {
        return rep.toString();
    }
}
