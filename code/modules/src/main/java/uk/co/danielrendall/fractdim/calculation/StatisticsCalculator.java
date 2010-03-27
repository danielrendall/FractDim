package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import uk.co.danielrendall.mathlib.geom2d.Line;
import uk.co.danielrendall.mathlib.geom2d.ParametricCurve;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;
import uk.co.danielrendall.fractdim.svgbridge.SVGWithMetadata;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 20:50:55
 */
public class StatisticsCalculator extends AbstractNotifyingGraphics  {

    public static final double TWO_DEGREES = Math.PI / 90.0d;

    private final Set<Set<Line>> curveLines = new HashSet<Set<Line>>();
    private final double minCosine;


    StatisticsCalculator(SVGWithMetadata svgWithMetadata, double minAngle) {
        super(svgWithMetadata);
        minCosine = Math.cos(minAngle);
    }

    public Statistics process() {

        Log.calc.info(String.format("Calculating stats for a shape wih %d curves with a minCosine of %s", numberOfCurves, minCosine));
        TranscoderInput input = new TranscoderInput(svgWithMetadata.getSVGDocument());
        FDTranscoder transcoder = new FDTranscoder(this);
        try {
            transcoder.transcode(input, new TranscoderOutput());
        } catch (TranscoderException e) {
            Log.app.warn("Couldn't transcode at - " + e.getMessage());
        }
        if (curveLines.size() != numberOfCurves) {
            Log.calc.warn(String.format("Discrepancy - was expecting %d curves, but got %d", numberOfCurves, curveLines.size()));
        }
        return Statistics.create(curveLines);
    }

    public void doHandleCurve(ParametricCurve curve) {
        Set<Line> accum = new HashSet<Line>();
        // note - a sequence of methods leading to the real method to minimise duplicating objects
        handleCurve(curve, 0.0, 1.0, accum);
        curveLines.add(accum);
    }

    private void handleCurve(ParametricCurve curve, double rangeStart, double rangeEnd, Set<Line> accum) {
        handleCurve(curve, rangeStart, curve.evaluate(rangeStart), rangeEnd, curve.evaluate(rangeEnd), accum);
    }

    private void handleCurve(ParametricCurve curve, double rangeStart, Point start, double rangeEnd, Point end, Set<Line> accum) {
        handleCurve(curve, rangeStart, start, rangeEnd, end, new Line(start, end), accum);
    }

    /**
     * We're passed the two end points of a curve, the line between them and the set for line accumulation.
     * Calculate the value for the mid point of the curve and look to see how close to collinear the three
     * points are (i.e. is dot products of the normalized start to mid and mid to end vectors very close to
     * 1) If so, add the line to the accumulator, if not subdivide and continue.
     * @param curve
     * @param rangeStart
     * @param start
     * @param rangeEnd
     * @param end
     * @param startToEnd
     * @param accum
     */
    private void handleCurve(ParametricCurve curve, double rangeStart, Point start, double rangeEnd, Point end,
                             Line startToEnd, Set<Line> accum) {
        double rangeMid = (rangeEnd + rangeStart) / 2.0d;
        Point mid = curve.evaluate(rangeMid);
        Line startToMid = new Line(start, mid);
        Line midToEnd = new Line(mid, end);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double dotProduct = startToMid.getVec().normalize().dotProduct(midToEnd.getVec().normalize());
        if (dotProduct >= minCosine) {
            if (dotProduct > 1.0) {
                // this shouldn't happen, of course, but we'll allow for rounding errors
//            Log.calc.warn(String.format("Dot product for %s and %s was %s", startToMid.getVec(), midToEnd.getVec(), dotProduct));
            }
            accum.add(startToEnd);
            return;
        }
        handleCurve(curve, rangeStart, start, rangeMid, mid, startToMid, accum);
        handleCurve(curve, rangeMid, mid, rangeEnd, end, midToEnd, accum);

    }

}
