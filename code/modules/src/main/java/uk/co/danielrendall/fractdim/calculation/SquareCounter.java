package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;
import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.app.workers.SquareCountingWorker;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.geom.Point;

import java.util.Set;
import java.util.SortedMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 11:00:39
 */
public class SquareCounter extends AbstractNotifyingGraphics {

    private final Set<Grid> grids;
    private final int maxDepth;

    private final SortedMap<Double, SortedMap<Double, Set<Grid>>> classifiedGrids;

    public SquareCounter(int maxDepth) {
        this.maxDepth = maxDepth;
        grids = new HashSet<Grid>();
        classifiedGrids = new TreeMap<Double, SortedMap<Double, Set<Grid>>>();
    }

    public void addGrid(double angle, double resolution, double fractionalXDisplacement, double fractionalYDisplacement) {
        Grid aGrid = new Grid(angle, resolution, fractionalXDisplacement, fractionalYDisplacement);
        setForAngleAndResolution(angle, resolution).add(aGrid);
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

    Set<Grid> setForAngleAndResolution(double angle, double resolution) {
        SortedMap<Double, Set<Grid>> angleMap = mapForAngle(angle);
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

    public void doHandleCurve(ParametricCurve curve) {
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

    public static SquareCounter createSquareCounter(int maxDepth, double minResolution, double maxResolution, int numberOfResolutionSteps,
                                                  int numberOfAngles, int numberOfDisplacementPoints) {
        SquareCounter collection = new SquareCounter(maxDepth);
        if (numberOfAngles < 1) throw new IllegalArgumentException("Number of angles must be >= 1");
        if (numberOfDisplacementPoints < 1) throw new IllegalArgumentException("Number of displacement points must be >= 1");
        if (minResolution > maxResolution) throw new IllegalArgumentException("Minimum resolution must be less than maximum resolution");

        double angleIncrement = Math.PI / (2.0d * (double) numberOfAngles);
        double fractionalDisplacementIncrement = 1.0d / (double) numberOfDisplacementPoints;

        double logMinResolutionReciprocal = Math.log(1.0d / minResolution);
        double logMaxResolutionReciprocal = Math.log(1.0d / maxResolution);

        double resolutionIncrement = ((logMinResolutionReciprocal - logMaxResolutionReciprocal) / (double) numberOfResolutionSteps);

        for (int angleCount = 0; angleCount < numberOfAngles; angleCount++) {
            double angle = angleIncrement * (double) angleCount;
            for (int resolutionCount = 0; resolutionCount <= numberOfResolutionSteps; resolutionCount++) {
                double logResolutionReciprocal = logMaxResolutionReciprocal + (resolutionIncrement * (double) resolutionCount);
                double resolution = 1.0d / Math.exp(logResolutionReciprocal);
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

    public SquareCountingResult process(SVGDocument svgDoc) {
        initCurveCount(svgDoc);

        TranscoderInput input = new TranscoderInput(svgDoc);

        FDTranscoder transcoder = new FDTranscoder(this);


        try {
            transcoder.transcode(input, new TranscoderOutput());
        } catch (TranscoderException e) {
            Log.app.warn("Couldn't transcode at - " + e.getMessage());
        }
        Log.misc.info("There were " + GridSquare.createCount + " squares created");
        return new SquareCountingResult(this);
    }

}
