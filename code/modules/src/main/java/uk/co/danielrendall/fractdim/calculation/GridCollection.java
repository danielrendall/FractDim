package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.geom.*;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;

import java.util.*;

/**
 * @author Daniel Rendall
 * @created 31-May-2009 09:34:20
 */
public class GridCollection extends FDGraphics2D {

    private final Set<Grid> grids;
    private final int maxDepth;

    private final SortedMap<Double, SortedMap<Double, Set<Grid>>> classifiedGrids;

    public GridCollection(int maxDepth) {
        this.maxDepth = maxDepth;
        grids = new HashSet<Grid>();
        classifiedGrids = new TreeMap<Double, SortedMap<Double, Set<Grid>>>();
    }

    public void addGrid(double angle, double resolution, double fractionalXDisplacement, double fractionalYDisplacement) {
        Grid aGrid = new Grid(angle, resolution, fractionalXDisplacement, fractionalYDisplacement);
        setForResolution(resolution, mapForAngle(angle)).add(aGrid);
        grids.add(aGrid);
    }

    Set<Double> getAvailableAngles() {
        return classifiedGrids.keySet();
    }

    SortedMap<Double, Set<Grid>> mapForAngle(double angle) {
        SortedMap<Double, Set<Grid>> theMap = classifiedGrids.get(angle);
        if (theMap == null) {
            theMap = new TreeMap<Double, Set<Grid>>();
            classifiedGrids.put(angle, theMap);
        }
        return theMap;
    }

    Set<Double> getAvailableResolutions(double angle) {
        return mapForAngle(angle).keySet();
    }


    Set<Grid> setForResolution(double resolution, SortedMap<Double, Set<Grid>> angleMap) {
        Set<Grid> theSet = angleMap.get(resolution);
        if (theSet == null) {
            theSet = new HashSet<Grid>();
            angleMap.put(resolution,theSet);
        }
        return theSet;
    }
    // Intended for testing with single grids
    @Deprecated
    void addGrid(Grid grid) {
        grids.add(grid);
    }

    public void handleCurve(ParametricCurve curve) {
        evaluateBetween(curve, 0, 0.0d, 1.0d);
    }

    private void evaluateBetween(ParametricCurve curve, int depth, double rangeStart, double rangeEnd) {
        Point start = curve.evaluate(rangeStart);
        Point end = curve.evaluate(rangeEnd);
        for (Grid grid : grids) {
            grid.startEvaluation(start, end);
        }
        Set<Grid> temporarySet = new HashSet<Grid>();
        temporarySet.addAll(grids);

        evaluateBetween(curve, temporarySet, depth, rangeStart, start, rangeEnd, end);

        for (Grid grid : grids) {
            grid.endEvaluation();
        }
    }

    // Note - when this is called, the start and end Squareels have both been put in the store
    private void evaluateBetween(ParametricCurve curve, Set<Grid> gridsToNotify, int depth,
                                 double rangeStart, Point startPoint,
                                 double rangeEnd, Point endPoint) {

        if (depth > maxDepth) {
            Log.app.warn("Max iteration depth reached - bailing out");
            return;
        }
        final double rangeMid = (rangeStart + rangeEnd) / 2.0d;

        if (Log.points.isDebugEnabled()) {
            Log.points.debug(String.format("Parameter: Start: %09.9f Mid: %09.9f End: %09.9f", rangeStart, rangeMid, rangeEnd));
        }

        if ((rangeMid == rangeStart) || (rangeMid == rangeEnd)) {
            // this should never happen
            Log.app.warn(String.format("Underflow at depth: %d RangeStart: %s RangeEnd: %s", depth, rangeStart, rangeEnd));
            return;
        }
        Point midPoint = curve.evaluate(rangeMid);

        if (Log.points.isDebugEnabled()) {
            Log.points.debug(String.format("Point Start: (%09.9f, %09.9f) Mid: (%09.9f, %09.9f) End: (%09.9f, %09.9f)",
                startPoint.x(), startPoint.y(), midPoint.x(), midPoint.y(), endPoint.x(), endPoint.y()));
        }
        // notify all of the grids of the midsquare and see whether they're interested in recursing further

        Set<Grid> gridsToRecurseStartMid = new HashSet<Grid>();
        Set<Grid> gridsToRecurseMidEnd = new HashSet<Grid>();

        for(Grid grid: gridsToNotify) {
            boolean[] toRecurse = grid.notifyNewPoint(startPoint, midPoint, endPoint);
            if (toRecurse[0] || depth <=2) gridsToRecurseStartMid.add(grid);
            if (toRecurse[1] || depth <=2) gridsToRecurseMidEnd.add(grid);
        }

        if (Log.recursion.isDebugEnabled()) {
            Log.recursion.debug(String.format("At depth %d, there were %d grids, %d want startToMid and %d want midToEnd", depth,
                    gridsToNotify.size(), gridsToRecurseStartMid.size(), gridsToRecurseMidEnd.size()));
        }

        if (gridsToRecurseStartMid.size() > 0) {
            evaluateBetween(curve, gridsToRecurseStartMid, depth + 1, rangeStart, startPoint, rangeMid, midPoint);
        }
        if (gridsToRecurseMidEnd.size() > 0) {
            evaluateBetween(curve, gridsToRecurseMidEnd, depth + 1, rangeMid, midPoint, rangeEnd, endPoint);
        }

    }

    public static GridCollection createCollection(int maxDepth, double minResolution, double maxResolution, double resolutionIncrement,
                                                  int numberOfAngles, int numberOfDisplacementPoints) {
        GridCollection collection = new GridCollection(maxDepth);
        if (numberOfAngles < 1) throw new IllegalArgumentException("Number of angles must be >= 1");
        if (numberOfDisplacementPoints < 1) throw new IllegalArgumentException("Number of displacement points must be >= 1");
        if (minResolution > maxResolution) throw new IllegalArgumentException("Minimum resolution must be less than maximum resolution");

        double angleIncrement = Math.PI / (2.0d * (double) numberOfAngles);
        double fractionalDisplacementIncrement = 1.0d / (double) numberOfDisplacementPoints;
        int numberOfResolutionSteps = (int) Math.ceil((maxResolution - minResolution) / resolutionIncrement);

        for (int angleCount = 0; angleCount < numberOfAngles; angleCount++) {
            double angle = angleIncrement * (double) angleCount;
            for (int resolutionCount = 0; resolutionCount <= numberOfResolutionSteps; resolutionCount++) {
                double resolution = minResolution + (resolutionIncrement * (double) resolutionCount);
                for (int displacementXCount = 0; displacementXCount < numberOfDisplacementPoints; displacementXCount++) {
                    double fractionalXDisplacement = fractionalDisplacementIncrement * (double) displacementXCount;
                    for (int displacementYCount = 0; displacementYCount < numberOfDisplacementPoints; displacementYCount++) {
                        double fractionalYDisplacement = fractionalDisplacementIncrement * (double) displacementYCount;
                        collection.addGrid(angle, resolution, fractionalXDisplacement, fractionalYDisplacement);
                    }
                }
            }
        }
        return collection;
    }

    public int count() {
        return grids.size();
    }
}
