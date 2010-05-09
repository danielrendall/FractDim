package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;
import uk.co.danielrendall.mathlib.geom2d.ParametricCurve;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.mathlib.geom2d.Vec;
import uk.co.danielrendall.fractdim.calculation.grids.*;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 11:00:39
 */
public class SquareCounter extends AbstractNotifyingGraphics {

    private final GridCollection gridCollection;
    private final Set<Grid> grids = new HashSet<Grid>();
    private final int maxDepth;


    public SquareCounter(FractalDocument fractalDocument, GridCollection gridCollection, int maxDepth) {
        super(fractalDocument);
        this.gridCollection = gridCollection;
        this.maxDepth = maxDepth;

        gridCollection.accept(new CollectionVisitorSupport() {

            public void visit(DisplacementGridCollection collection) {
                for (Vec v : collection.getAvailableDisplacements()) {
                    grids.add(collection.gridForDisplacement(v));
                }
            }
        });
    }

    public void accept(CollectionVisitor visitor) {
        gridCollection.accept(visitor);
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

    public int count() {
        return grids.size();
    }

    public SquareCountingResult process() {

        TranscoderInput input = new TranscoderInput(fractalDocument.getSvgDoc());

        FDTranscoder transcoder = new FDTranscoder(this);

        try {
            transcoder.transcode(input, new TranscoderOutput());
        } catch (TranscoderException e) {
            Log.app.warn("Couldn't transcode at - " + e.getMessage());
        }
        Log.misc.info("There were " + GridSquare.createCount + " squares created");
        return new SquareCountingResult(this.gridCollection.getCollection());
    }

}
