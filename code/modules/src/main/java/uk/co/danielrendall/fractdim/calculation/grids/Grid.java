/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

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

    private final Vec displacement;

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

    Point inverseTransformPoint(Point p) {
        if (doDisplace) {
            if (doRotate) {
                return p.rotate(-angle).displace(displacement.neg());
            } else {
                return p.displace(displacement.neg());
            }
        } else {
            if (doRotate) {
                return p.rotate(-angle);
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
        squaresMet.clear();
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
                startSquare.x(), startSquare.y(), midSquare.x(), midSquare.y(), midPoint, endSquare.x(), endSquare.y()));
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
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.x()))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.y()))) > proximityThreshold);
                    break;
                case GridSquare.ABOVE_RIGHT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * midSquare.x())) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.y()))) > proximityThreshold);
                    break;
                case GridSquare.BELOW_RIGHT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * midSquare.x())) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.y())) > proximityThreshold);
                    break;
                case GridSquare.BELOW_LEFT:
                    recurseStartToMid = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.x()))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.y())) > proximityThreshold);
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
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.x()))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.y()))) > proximityThreshold);
                    break;
                case GridSquare.ABOVE_RIGHT:
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * midSquare.x())) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * (1.0d + midSquare.y()))) > proximityThreshold);
                    break;
                case GridSquare.BELOW_RIGHT:
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * midSquare.x())) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.y())) > proximityThreshold);
                    break;
                case GridSquare.BELOW_LEFT:
                    recurseMidToEnd = (Math.abs(midPoint.x() - (resolution * (1.0d + midSquare.x()))) > proximityThreshold)
                        || (Math.abs(midPoint.y() - (resolution * midSquare.y())) > proximityThreshold);
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
     * to create any elements it requires, covering at least the supplied bounding box. Returns the actual bounding
     * box of the resulting grid (for large squares, this could overshoot the supplied bounding box considerably)
     * @param rootGroup
     * @param creator
     * @param boundingBox
     * @param colour
     * @return The bounding box occupied by enough of the grid to cover the supplied bounding box.
     */
    public BoundingBox writeToSVG(Element rootGroup, SVGElementCreator creator, BoundingBox boundingBox, String colour) {
        double originX = boundingBox.getMinX();
        double originY = boundingBox.getMinY();

        // Recall that in SVG, the origin is at the top-left and so y increases downwards

        int squaresToTheLeft = (int) Math.ceil((originX - boundingBox.getMinX()) / resolution);
        int squaresToTheRight = (int) Math.ceil((boundingBox.getMaxX() - originX) / resolution);
        int squaresToTheTop = (int) Math.ceil((originY - boundingBox.getMinY()) / resolution);
        int squaresToTheBottom = (int) Math.ceil((boundingBox.getMaxY() - originY) / resolution);

        double left = originX - ((double) squaresToTheLeft  + 1.0d) * resolution;
        double right = originX + ((double) squaresToTheRight + 1.0d) * resolution;
        double top = originY - ((double) squaresToTheTop + 1.0d) * resolution;
        double bottom = originY + ((double) squaresToTheBottom + 1.0d) * resolution;

        if (left > right) { throw new RuntimeException("Left should be less than right");}
        if (top > bottom) { throw new RuntimeException("Top should be less than bottom");}

        Element horizLines = creator.createGroup();

        for (double y = top; y <= bottom; y += resolution) {
            Element path = creator.createPath(colour);
            path.setAttributeNS(null, "d", String.format("M %s,%s L %s,%s", left, y, right, y));
            horizLines.appendChild(path);
        }

        Element vertLines = creator.createGroup();

        for (double x = left; x <= right; x += resolution) {
            Element path = creator.createPath(colour);
            path.setAttributeNS(null, "d", String.format("M %s,%s L %s,%s", x, top, x, bottom));
            vertLines.appendChild(path);
        }

        rootGroup.appendChild(horizLines);
        rootGroup.appendChild(vertLines);
        return new BoundingBox(left, right, top, bottom);
    }

    public BoundingBox writeFilledToSVG(Element rootGroup, SVGElementCreator creator, BoundingBox boundingBox, String colour) {

        BoundingBox bb = BoundingBox.empty();

        for (Iterator<GridSquare> it = masterStore.squareIterator(); it.hasNext();) {
            GridSquare next = it.next();
            double xLeft = next.x() * resolution;
            double yBottom = next.y() * resolution;
            double xRight = xLeft + resolution;
            double yTop = yBottom + resolution;
            Point p1 = inverseTransformPoint(new Point(xLeft, yBottom));
            Point p2 = inverseTransformPoint(new Point(xRight, yBottom));
            Point p3 = inverseTransformPoint(new Point(xRight, yTop));
            Point p4 = inverseTransformPoint(new Point(xLeft, yTop));
            Element path = creator.createFilledPath("#ff0000", "#ffcccc");
            path.setAttributeNS(null, "d", String.format("M %s,%s L %s,%s L %s,%s L %s,%s Z", p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), p4.x(), p4.y()));
            rootGroup.appendChild(path);
            bb = bb.expandToInclude(BoundingBox.containing(p1, p2, p3, p4));
        }
        return bb;
    }
}
