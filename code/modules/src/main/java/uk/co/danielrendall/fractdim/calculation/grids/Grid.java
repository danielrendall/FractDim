package uk.co.danielrendall.fractdim.calculation.grids;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGSVGElement;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.svg.SVGElementCreator;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.mathlib.geom2d.Vec;
import uk.co.danielrendall.fractdim.logging.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 18:05:44
 */
public class Grid {

    private final double angle;
    private final double resolution;
    private final Vec fractionalDisplacement;

    final Vec displacement;

    private final GridSquareStore masterStore;
    private final GridSquareStore temporaryStore;

    private final static boolean[] NO_RECURSE = new boolean[] {false, false};
    private final static boolean[] START_TO_MID = new boolean[] {true, false};
    private final static boolean[] MID_TO_END = new boolean[] {false, true};
    private final static boolean[] RECURSE_BOTH = new boolean[] {true, true};

    private final Map<Point, GridSquare> squaresMet;
    private final double proximityThreshold;

    private final boolean doDisplace, doRotate;

    public Grid(double resolution) {
        this(0.0d, resolution, new Vec(Point.ORIGIN));
    }

    public Grid(double resolution, Vec fractionalDisplacement) {
        this(0.0d, resolution, fractionalDisplacement);
    }

    public Grid(double angle, double resolution) {
        this(angle, resolution, new Vec(Point.ORIGIN));
    }

    public Grid(double angle, double resolution, Vec fractionalDisplacement) {
        this.angle = angle;
        this.resolution = resolution;
        this.fractionalDisplacement = fractionalDisplacement;

        double fractionalXDisp = fractionalDisplacement.x();
        double fractionalYDisp = fractionalDisplacement.y();

        proximityThreshold = resolution / 1000.0d;
        Log.points.debug(String.format("Resolution: %09.9f Proximity Threshold: %09.9f", resolution, proximityThreshold));
        masterStore = new GridSquareStore(new TreeSet<GridSquare>()); // sorted
        temporaryStore = new GridSquareStore(); // use the default hashset
        squaresMet = new HashMap<Point, GridSquare>();
        displacement = new Vec(new Point(fractionalXDisp * resolution, fractionalYDisp * resolution));
        doDisplace = (fractionalXDisp != 0.0d || fractionalYDisp != 0.0d);
        doRotate = (angle != 0.0d);
    }

    public double getAngle() {
        return angle;
    }

    public double getResolution() {
        return resolution;
    }

    public Vec getFractionalDisplacement() {
        return fractionalDisplacement;
    }

    Point transformPoint(Point p) {
        if (doDisplace) {
            if (doRotate) {
                return p.displace(displacement).rotate(angle);
            } else {
                return p.displace(displacement);
            }
        } else {
            if (doRotate) {
                return p.rotate(angle);
            } else {
                return p;
            }
        }
    }

    public void startEvaluation(Point startPoint, Point endPoint) {
        temporaryStore.clear();

        // apply transformation
        startPoint = transformPoint(startPoint);
        endPoint = transformPoint(endPoint);



        GridSquare startSquare = getSquare(startPoint);
        GridSquare endSquare = getSquare(endPoint);

        if (Log.squares.isDebugEnabled()) {
            Log.squares.debug(String.format("Start point: %s Start square: %s End point: %s End square: %s", startPoint, startSquare, endPoint, endSquare));
        }

        temporaryStore.put(startSquare);
        temporaryStore.put(endSquare);
        squaresMet.put(startPoint, startSquare);
        squaresMet.put(endPoint, endSquare);
    }

    public void endEvaluation() {
        masterStore.addAll(temporaryStore);
    }

    public boolean[] notifyNewPoint(Point startPoint, Point midPoint, Point endPoint) {

        startPoint = transformPoint(startPoint);
        midPoint = transformPoint(midPoint);
        endPoint = transformPoint(endPoint);

        GridSquare midSquare = getSquare(midPoint);
        temporaryStore.put(midSquare);
        squaresMet.put(midPoint, midSquare);

        GridSquare startSquare = squaresMet.get(startPoint);
        GridSquare endSquare = squaresMet.get(endPoint);

        if ((startSquare == null) || (endSquare == null)) {
            Log.app.warn("Encountered a null start or end square - this should never happen!");

            if (Log.app.isDebugEnabled()) {
                Log.app.debug(String.format("Transformed points - start: %s, mid: %s, end: %s", startPoint, midPoint, endPoint));
                for (Point next : squaresMet.keySet()) {
                    Log.app.debug(String.format("Stored point: %s", next));
                }

            }

            return NO_RECURSE;
        }

        if (Log.squares.isDebugEnabled()) {
            Log.squares.debug(String.format("Square Start: (%d, %d) Mid: (%d, %d) [%s] End: (%d, %d)",
                startSquare.xIndex, startSquare.yIndex, midSquare.xIndex, midSquare.yIndex, midPoint, endSquare.xIndex, endSquare.yIndex));
        }
        
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
        return recurseStartToMid ?
                recurseMidToEnd ?
                        RECURSE_BOTH : START_TO_MID :
                recurseMidToEnd ?
                        MID_TO_END : NO_RECURSE;

    }

    private GridSquare getSquare(Point p) {
        // todo - some serious testing!
        int SquareelX = (int) Math.floor(p.x() / resolution);
        int SquareelY = (int) Math.floor(p.y() / resolution);
        return GridSquare.create(SquareelX, SquareelY);
    }

    public int getSquareCount() {
        return masterStore.count();
    }

    public Iterator<GridSquare> squareIterator() {
        return masterStore.squareIterator();
    }

    /**
     * Called to ask the grid to write something suitable to the root element provided, using the creator
     * to create any elements it requires, fitting within the specified boundingbox.
     * @param rootGroup
     * @param creator
     * @param boundingBox
     */
    public void writeToSVG(Element rootGroup, SVGElementCreator creator, BoundingBox boundingBox) {
        double centreX = (boundingBox.getMaxX() + boundingBox.getMinX()) / 2.0d;
        double centreY = (boundingBox.getMaxY() + boundingBox.getMinY()) / 2.0d;
        int squaresToTheLeft = (int) Math.ceil((centreX - boundingBox.getMinX()) / resolution);
        int squaresToTheRight = (int) Math.ceil((boundingBox.getMaxX() - centreX) / resolution);
        int squaresToTheBottom = (int) Math.ceil((centreY - boundingBox.getMinY()) / resolution);
        int squaresToTheTop = (int) Math.ceil((boundingBox.getMaxY() - centreY) / resolution);

        double left = centreX - ((double) squaresToTheLeft) * resolution;
        double right = centreX + ((double) squaresToTheRight) * resolution;
        double bottom = centreY - ((double) squaresToTheBottom) * resolution;
        double top = centreY + ((double) squaresToTheTop) * resolution;

        Element horizLines = creator.createGroup();

        for (double y = bottom; y <= top; y += resolution) {
            Element path = creator.createPath();
            path.setAttributeNS(null, "d", String.format("M %s,%s L %s,%s", left, y, right, y));
            horizLines.appendChild(path);
        }

        Element vertLines = creator.createGroup();

        for (double x = left; x <= right; x += resolution) {
            Element path = creator.createPath();
            path.setAttributeNS(null, "d", String.format("M %s,%s L %s,%s", x, bottom, x, top));
            vertLines.appendChild(path);
        }

        rootGroup.appendChild(horizLines);
        rootGroup.appendChild(vertLines);
    }
}
