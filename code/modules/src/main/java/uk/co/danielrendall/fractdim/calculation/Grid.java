package uk.co.danielrendall.fractdim.calculation;

import org.apache.log4j.Logger;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.geom.Point;

import java.util.Iterator;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 18:05:44
 */
public class Grid {
    private static final Logger log = Logger.getLogger(Grid.class);

    private final double resolution;
    private final double proximityThreshold;
    private final GridSquareStore masterPS;
    private int maxDepth = 1000;

    public Grid(double resolution) {
        this.resolution = resolution;
        proximityThreshold = resolution / 1000.0d;
        log.debug(String.format("Resolution: %09.9f Proximity Threshold: %09.9f", resolution, proximityThreshold));
        masterPS = new SortedGridSquareStore();
    }

    public void add(ParametricCurve curve) {
        log.debug("Adding: " + curve.toString());
        GridSquareStore ps = new SortedGridSquareStore();
        evaluateBetween(curve, ps, 0, 0.0d, curve.evaluate(0.0d), 1.0d, curve.evaluate(1.0d));

    }

    private void evaluateBetween(ParametricCurve curve, GridSquareStore ps, int depth,
                                 double rangeStart, Point startPoint,
                                 double rangeEnd, Point endPoint) {
        GridSquare startSquare = getSquare(startPoint);
        GridSquare endSquare = getSquare(endPoint);
        ps.put(startSquare);
        ps.put(endSquare);
        masterPS.put(startSquare);
        masterPS.put(endSquare);
        evaluateBetween(curve, ps, depth, rangeStart, startPoint, startSquare, rangeEnd, endPoint, endSquare);
    }

    // Note - when this is called, the start and end Squareels have both been put in the store
    private void evaluateBetween(ParametricCurve curve, GridSquareStore ps, int depth,
                                 double rangeStart, Point startPoint, GridSquare startSquare,
                                 double rangeEnd, Point endPoint, GridSquare endSquare) {

        if (depth > maxDepth) {
            log.warn("Max iteration depth reached - bailing out");
            return;
        }
        final double rangeMid = (rangeStart + rangeEnd) / 2.0d;

        log.debug(String.format("Parameter: Start: %09.9f Mid: %09.9f End: %09.9f", rangeStart, rangeMid, rangeEnd));

        if ((rangeMid == rangeStart) || (rangeMid == rangeEnd)) {
            // this should never happen
            log.warn(String.format("Underflow at depth: %d Resolution: %s RangeStart: %s RangeEnd: %s", depth, resolution, rangeStart, rangeEnd));
            return;
        }
        Point midPoint = curve.evaluate(rangeMid);

        log.debug(String.format("Point Start: (%09.9f, %09.9f) Mid: (%09.9f, %09.9f) End: (%09.9f, %09.9f)",
                startPoint.x(), startPoint.y(), midPoint.x(), midPoint.y(), endPoint.x(), endPoint.y()));


        GridSquare midSquare = getSquare(midPoint);
        ps.put(midSquare);
        masterPS.put(midSquare);

        log.debug(String.format("Square Start: (%d, %d) Mid: (%d, %d) End: (%d, %d)",
                startSquare.xIndex, startSquare.yIndex, midSquare.xIndex, midSquare.yIndex, endSquare.xIndex, endSquare.yIndex));

        int midFromStart = startSquare.direction(midSquare);
        int midFromEnd = endSquare.direction(midSquare);

        log.debug(String.format("Mid from start: %d Mid from end: %d", midFromStart, midFromEnd));

        boolean recurseStartToMid = false;
        boolean recurseMidToEnd = false;

        // if the recursion is not deep, always recurse down both segments
        if (depth <= 2) {
            recurseStartToMid = true;
            recurseMidToEnd = true;
        }


        if (midFromStart == GridSquare.NO_TOUCH) {
            // if the mid Square isn't touching the start Square, recurse down the start to middle segment
            recurseStartToMid = true;
        } else {
            switch (midFromStart) {
                case GridSquare.ABOVE_LEFT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.xIndex))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.yIndex))) > proximityThreshold);
                    break;
                case GridSquare.ABOVE_RIGHT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * midSquare.xIndex)) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.yIndex))) > proximityThreshold);
                    break;
                case GridSquare.BELOW_RIGHT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * midSquare.xIndex)) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.yIndex)) > proximityThreshold);
                    break;
                case GridSquare.BELOW_LEFT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.xIndex))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.yIndex)) > proximityThreshold);
                    break;
                // for SAME, ABOVE, RIGHT, BELOW, LEFT, leave value as false
            }
        }

        if (midFromEnd == GridSquare.NO_TOUCH) {
            // if the mid Square isn't touching the start Square, recurse down the start to middle segment
            recurseMidToEnd = true;
        } else {
            switch (midFromEnd) {
                case GridSquare.ABOVE_LEFT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.xIndex))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.yIndex))) > proximityThreshold);
                    break;
                case GridSquare.ABOVE_RIGHT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * midSquare.xIndex)) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.yIndex))) > proximityThreshold);
                    break;
                case GridSquare.BELOW_RIGHT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * midSquare.xIndex)) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.yIndex)) > proximityThreshold);
                    break;
                case GridSquare.BELOW_LEFT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.xIndex))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.yIndex)) > proximityThreshold);
                    break;
                // for SAME, ABOVE, RIGHT, BELOW, LEFT, leave value as false
            }
        }

        if (recurseStartToMid) {
            evaluateBetween(curve, ps, depth + 1, rangeStart, startPoint, startSquare, rangeMid, midPoint, midSquare);
        }
        if (recurseMidToEnd) {
            evaluateBetween(curve, ps, depth + 1, rangeMid, midPoint, midSquare, rangeEnd, endPoint, endSquare);
        }

    }

    private GridSquare getSquare(Point p) {
        // todo - some serious testing!
        int SquareelX = (int) Math.floor(p.x() / resolution);
        int SquareelY = (int) Math.floor(p.y() / resolution);
        return new GridSquare(SquareelX, SquareelY);
    }

    public int getSquareCount() {
        return masterPS.count();
    }

    public Iterator<GridSquare> squareIterator() {
        return masterPS.squareIterator();
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

}
