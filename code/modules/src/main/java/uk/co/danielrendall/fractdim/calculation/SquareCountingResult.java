package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.calculation.grids.CollectionVisitor;
import uk.co.danielrendall.fractdim.calculation.grids.AngleGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.ResolutionGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.DisplacementGridCollection;

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
    SquareCountingResult(SquareCounter squareCounter) {
        results = new TreeMap<Double, FixedAngleSquareCountingResult>();
        squareCounter.accept(new CollectionVisitor() {

            private FixedAngleSquareCountingResult angleResult;
            private double currentResolution = 0.0d;

            public void visit(AngleGridCollection collection) {
                for(double angle : collection.getAvailableAngles()) {
                    angleResult = new FixedAngleSquareCountingResult();
                    collection.collectionForAngle(angle).accept(this);
                    results.put(angle, angleResult);
                    Log.gui.info(String.format("Fractal dimension is %s", angleResult.getFractalDimension()));
                }
            }

            public void visit(ResolutionGridCollection collection) {
                for (double resolution: collection.getAvailableResolutions()) {
                    currentResolution = resolution;
                    collection.collectionForResolution(resolution).accept(this);
                }
            }

            public void visit(DisplacementGridCollection collection) {
                angleResult.add(currentResolution, collection.getAverageSquareCount());
            }
        });

    }

    // using SortedMap means that the iterator returns these in order
    public List<Double> getAvailableAngles() {
        return Collections.unmodifiableList(new ArrayList<Double>(results.keySet()));
    }

    // TODO - handle the case where there are no such statistics
    public FixedAngleSquareCountingResult getResultsForAngle(double angle) {
        return results.get(angle);
    }


    public static class FixedAngleSquareCountingResult {
        private final SortedMap<Double, Double> results;

        public List<Double> getAvailableResolutions() {
            return Collections.unmodifiableList(new ArrayList<Double>(results.keySet()));
        }

        public FixedAngleSquareCountingResult() {
            this.results = new TreeMap<Double, Double>();
        }

        public void add(double resolution, Double squareCounts) {
            results.put(resolution, squareCounts);
        }

        public Double getCountsForResolution(double resolution) {
            return results.get(resolution);
        }

        public double getFractalDimension() {
            // http://en.wikipedia.org/wiki/Simple_linear_regression
            
            double logSquareCountsSum = 0.0d; // Y
            double logReciprocalResolutionsSum = 0.0d; // X

            double sumOfXTimesY = 0.0d;
            double sumOfXSquared = 0.0d; // logReciprocalResolution

            int i=0;
            for (Map.Entry<Double, Double> entry : results.entrySet()) {
                double resolution = entry.getKey();
                double squareCounts = entry.getValue();
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
