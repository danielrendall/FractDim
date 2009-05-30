package uk.co.danielrendall.fractdim.calculation;

import org.apache.log4j.Logger;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.geom.Point;

import java.util.*;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 18:05:44
 */
public class PixelGrid {
    private static final Logger log = Logger.getLogger(PixelGrid.class);

    private final double resolution;
    private final PixelStore masterPS;

    public PixelGrid(double resolution) {
        this.resolution = resolution;
        masterPS = new PixelStore();
    }

    public void add(ParametricCurve curve) {
        PixelStore ps = new PixelStore();
        evaluateBetween(curve, ps, 0, 0.0d, curve.evaluate(0.0d), 1.0d, curve.evaluate(1.0d));
    }

    private void evaluateBetween(ParametricCurve curve, PixelStore ps, int depth,
                                 double rangeStart, Point startPoint,
                                 double rangeEnd, Point endPoint) {
        Pixel startPix = getPixel(startPoint);
        Pixel endPix = getPixel(endPoint);
        ps.put(startPix);
        ps.put(endPix);
        masterPS.put(startPix);
        masterPS.put(endPix);
        evaluateBetween(curve, ps, depth,rangeStart, startPoint, startPix, rangeEnd, endPoint, endPix);
    }

    // Note - when this is called, the start and end pixels have both been put in the store
    private void evaluateBetween(ParametricCurve curve, PixelStore ps, int depth,
                                 double rangeStart, Point startPoint, Pixel startPix,
                                 double rangeEnd, Point endPoint, Pixel endPix) {
        final double rangeMid = (rangeStart + rangeEnd) / 2.0d;
        Point midPoint = curve.evaluate(rangeMid);
        Pixel midPix = getPixel(midPoint);
        ps.put(midPix);
        masterPS.put(midPix);
        if (depth <= 1 || !startPix.isSameOrAdjacentTo(midPix) || !midPix.isSameOrAdjacentTo(endPix)) {
            evaluateBetween(curve, ps, depth+1, rangeStart, startPoint, startPix, rangeMid, midPoint, midPix);
            evaluateBetween(curve, ps, depth+1, rangeMid, midPoint, midPix, rangeEnd, endPoint, endPix);
        }
    }

    private Pixel getPixel(Point p) {
        // todo - some serious testing!
        int pixelX = (int) Math.floor( p.x() / resolution );
        int pixelY = (int) Math.floor( p.y() / resolution );
        return new Pixel(pixelX, pixelY);
    }

    public int getPixelCount() {
        return masterPS.count();
    }

    private static class Pixel {
        private int xIndex, yIndex;
        Pixel (int xIndex, int yIndex) {
            this.xIndex = xIndex;
            this.yIndex = yIndex;
        }

        @Override
        public int hashCode() {
            return (xIndex % 10000) * 10000 + (yIndex % 10000);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Pixel) {
                Pixel other = (Pixel) obj;
                return (this.xIndex == other.xIndex && this.yIndex == other.yIndex);
            }
            return false;
        }

        boolean isSameOrAdjacentTo(Pixel another) {
            // 0 = same pixel, 1 = one of the indexes is different, 2 = both different (diagonal)
            return (Math.abs(another.xIndex - xIndex) + Math.abs(another.yIndex - yIndex) < 2);
        }
    }

    private static class PixelStore {
        private final Set<Pixel> pixels;

        public PixelStore() {
            pixels = new HashSet<Pixel>();
        }

        public void put(Pixel pixel) {
            pixels.add(pixel);
        }

        public int count() {
            return pixels.size();
        }
    }
}
