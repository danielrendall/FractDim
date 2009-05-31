package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.logging.Log;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 18:05:44
 */
public class Grid {

    private final double resolution;
    private final double proximityThreshold;
    private final GridSquareStore masterStore;
    private final GridSquareStore temporaryStore;

    private final static boolean[] NO_RECURSE = new boolean[] {false, false};

    private final Map<Point, GridSquare> squaresMet;

    public Grid(double resolution) {
        this.resolution = resolution;
        proximityThreshold = resolution / 1000.0d;
        Log.points.debug(String.format("Resolution: %09.9f Proximity Threshold: %09.9f", resolution, proximityThreshold));
        masterStore = new GridSquareStore(new TreeSet<GridSquare>()); // sorted
        temporaryStore = new GridSquareStore(); // use the default hashset
        squaresMet = new HashMap<Point, GridSquare>();
    }

    Point transformPoint(Point p) {
        return p;
    }

    void startEvaluation(Point startPoint, Point endPoint) {
        temporaryStore.clear();

        // apply transformation
        startPoint = transformPoint(startPoint);
        endPoint = transformPoint(endPoint);



        GridSquare currentStartSquare = getSquare(startPoint);
        GridSquare currentEndSquare = getSquare(endPoint);
        temporaryStore.put(currentStartSquare);
        temporaryStore.put(currentEndSquare);
        squaresMet.put(startPoint, currentStartSquare);
        squaresMet.put(endPoint, currentEndSquare);
    }

    void endEvaluation() {
        masterStore.addAll(temporaryStore);
    }

    boolean[] notifyNewPoint(Point startPoint, Point midPoint, Point endPoint) {

        startPoint = transformPoint(startPoint);
        midPoint = transformPoint(midPoint);
        endPoint = transformPoint(endPoint);

        GridSquare midSquare = getSquare(midPoint);
        temporaryStore.put(midSquare);
        squaresMet.put(midPoint, midSquare);

        GridSquare startSquare = squaresMet.get(startPoint);
        GridSquare endSquare = squaresMet.get(endPoint);

        if ((startSquare == null) || (endSquare == null)) {
            Log.points.warn("Encountered a null start or end square - this should never happen!");
            return NO_RECURSE;
        }

        Log.points.debug(String.format("Square Start: (%d, %d) Mid: (%d, %d) End: (%d, %d)",
                startSquare.xIndex, startSquare.yIndex, midSquare.xIndex, midSquare.yIndex, endSquare.xIndex, endSquare.yIndex));

        int midFromStart = startSquare.direction(midSquare);
        int midFromEnd = endSquare.direction(midSquare);

        Log.points.debug(String.format("Mid from start: %d Mid from end: %d", midFromStart, midFromEnd));

        boolean recurseStartToMid = false;
        boolean recurseMidToEnd = false;

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
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.xIndex))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.yIndex))) > proximityThreshold);
                    break;
                case GridSquare.ABOVE_RIGHT:
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * midSquare.xIndex)) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.yIndex))) > proximityThreshold);
                    break;
                case GridSquare.BELOW_RIGHT:
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * midSquare.xIndex)) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.yIndex)) > proximityThreshold);
                    break;
                case GridSquare.BELOW_LEFT:
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.xIndex))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.yIndex)) > proximityThreshold);
                    break;
                // for SAME, ABOVE, RIGHT, BELOW, LEFT, leave value as false
            }
        }

        return new boolean[] {recurseStartToMid, recurseMidToEnd};
    }

    private GridSquare getSquare(Point p) {
        // todo - some serious testing!
        int SquareelX = (int) Math.floor(p.x() / resolution);
        int SquareelY = (int) Math.floor(p.y() / resolution);
        return new GridSquare(SquareelX, SquareelY);
    }

    public int getSquareCount() {
        return masterStore.count();
    }

    public Iterator<GridSquare> squareIterator() {
        return masterStore.squareIterator();
    }

}
