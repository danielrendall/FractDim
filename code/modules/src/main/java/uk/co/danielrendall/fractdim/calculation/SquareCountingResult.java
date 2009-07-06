package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.logging.Log;

import java.util.*;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 10:28:34
 */
public class SquareCountingResult {

    // maps initial rotation angle to the results for that angle
    private final SortedMap<Double, FixedAngleSquareCountingResult> results;

    // makes sense to have this backed by the grids because we may want to display them
    // in the SVG - contrast the approach with Statistics
    SquareCountingResult(SquareCounter grids) {
        results = new TreeMap<Double, FixedAngleSquareCountingResult>();
        for (double angle : grids.getAvailableAngles()) {
            FixedAngleSquareCountingResult angleResult = new FixedAngleSquareCountingResult();
            for (double resolution : grids.getAvailableResolutions(angle)) {
                angleResult.add(resolution, new SquareCounts(grids.setForAngleAndResolution(angle, resolution)));
            }
            results.put(angle, angleResult);
            Log.gui.info(String.format("Fractal dimension is %s", angleResult.getFractalDimension()));
        }
    }

    // using SortedMap means that the iterator returns these in order
    public Set<Double> getAvailableAngles() {
        return Collections.unmodifiableSet(results.keySet());
    }

    // TODO - handle the case where there are no such statistics
    public FixedAngleSquareCountingResult getResultsForAngle(double angle) {
        return results.get(angle);
    }


    public static class FixedAngleSquareCountingResult {
        private final SortedMap<Double, SquareCounts> results;

        public Set<Double> getAvailableResolutions() {
            return Collections.unmodifiableSet(results.keySet());
        }

        public FixedAngleSquareCountingResult() {
            this.results = new TreeMap<Double, SquareCounts>();
        }

        public void add(double resolution, SquareCounts squareCounts) {
            results.put(resolution, squareCounts);
        }

        public SquareCounts getCountsForResolution(double resolution) {
            return results.get(resolution);
        }

        public double getFractalDimension() {
            // http://en.wikipedia.org/wiki/Simple_linear_regression
            
            double logSquareCountsSum = 0.0d; // Y
            double logReciprocalResolutionsSum = 0.0d; // X

            double sumOfXTimesY = 0.0d;
            double sumOfXSquared = 0.0d; // logReciprocalResolution

            int i=0;
            for (Map.Entry<Double, SquareCounts> entry : results.entrySet()) {
                double resolution = entry.getKey();
                double squareCounts = entry.getValue().getNumberOfSquares();
                // assume no pesky divide by zero errors...
                double logSquareCount = Math.log(squareCounts);
                double logReciprocalResolution = Math.log(1.0d/resolution);

                sumOfXTimesY += (logReciprocalResolution * logSquareCount);
                sumOfXSquared += (logReciprocalResolution * logReciprocalResolution);

                logSquareCountsSum += logSquareCount;
                logReciprocalResolutionsSum += logReciprocalResolution;
                i++;
            }

            double logSquareCountMean = logSquareCountsSum / (double) i;
            double logReciprocalResolutionMean = logReciprocalResolutionsSum / (double) i;

            return ((sumOfXTimesY - ((double) i * logSquareCountMean * logReciprocalResolutionMean)) /
                    (sumOfXSquared - ((double) i * logReciprocalResolutionMean * logReciprocalResolutionMean)));            
        }
    }
}
