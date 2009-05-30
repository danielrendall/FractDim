package uk.co.danielrendall.fractdim.geom;

import org.apache.log4j.Logger;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 10:05:23
 */
public class Complex extends XY {
    private final static Logger log = Logger.getLogger(Complex.class);

    private final static double EPSILON = Double.MIN_VALUE * 100;

    private final static double HALF_PI = Math.PI / 2.0d;

    private final boolean xIsZero;
    private final boolean yIsZero;

    public Complex(double x, double y) {
        super(x, y);
        xIsZero = Math.abs(x) < EPSILON;
        yIsZero = Math.abs(y) < EPSILON;
    }

    public static Complex unit() {
        return unit(1.0d);
    }

    public static Complex unit(double arg) {
        return modArg(1.0d, arg);
    }

    public static Complex modArg(double mod, double arg) {
        return new Complex (mod * Math.cos(arg), mod * Math.sin(arg));
    }

    public Complex neg() {
        return new Complex(0.0d - x, 0.0d - y);
    }

    public Complex add(Complex other) {
        return new Complex(x + other.x, y + other.y);
    }

    public Complex sub(Complex other) {
        return add(other.neg());
    }

    public double mod() {
        return xIsZero && yIsZero ? 0.0 : Math.sqrt((x * x) + (y * y));
    }

    public double arg() {
        if (xIsZero) {
            // on the y axis
            if (yIsZero) {
                // the zero vector
                log.warn("Zero vector asked for argument");
                return 0.0;
            } else {
                // either straight up or straight down
                return (y > 0.0d) ? HALF_PI : -HALF_PI;
            }
        } else {
            if (yIsZero) {
                // on the x axis; either right or left
                return (x > 0) ? 0.0d : -Math.PI;
            } else {
                if (x >= 0.0d) {
                    return Math.atan(y/x);
                } else {
                    if (y >= 0.0d) {
                        // upper left quadrant
                        return Math.atan(y/x) + Math.PI;
                    } else {
                        // lower left quadrant
                        return Math.atan(y/x) - Math.PI;
                    }
                }
            }
        }
    }

    public Complex times(double m) {
        return new Complex(x * m, y * m);
    }

    public Complex times(Complex c) {
        // (x + iy)(a+ib) = xa - yb + (xb + ya)i
        return new Complex(x * c.x - y * c.y, x * c.y + y * c.x);
    }

    public Complex rotate(double angle) {
        return times(Complex.unit(angle));
    }


}
