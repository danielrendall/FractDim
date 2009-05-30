package uk.co.danielrendall.fractdim.calculation;

import java.util.*;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 10:28:34
 */
public class CalculationResult {

    // maps initial rotation angle to the results for that angle
    private final SortedMap<Double, SortedMap<Double, Statistics>> results;

    CalculationResult() {
        results = new TreeMap<Double, SortedMap<Double, Statistics>>();
    }

    // using SortedMap means that the iterator returns these in order
    public Set<Double> getAvailableAngles() {
        return Collections.unmodifiableSet(results.keySet());
    }

    public Set<Double> getAvailableResolutions(double angle) {
        return Collections.unmodifiableSet(_getMapForAngle(angle).keySet());
    }

    public SortedMap<Double, Statistics> getMapForAngle(double angle) {
        return Collections.unmodifiableSortedMap(_getMapForAngle(angle));
    }

    // TODO - handle the case where there are no such statistics
    public Statistics getStatistics(double angle, double resolution) {
        return _getMapForAngle(angle).get(resolution);
    }

    void addResult(double angle, double resolution, int numberOfSquares) {
        SortedMap<Double, Statistics> map = _getMapForAngle(angle);
        map.put(resolution, new Statistics(numberOfSquares));
    }

    private SortedMap<Double, Statistics> _getMapForAngle(double angle) {
        SortedMap<Double, Statistics> map = results.get(angle);
        if (map == null) {
            map = new TreeMap<Double, Statistics>();
            results.put(angle, map);
        }
        return map;
    }

    public String getFractalDimension() {
        return "42";
    }


    public static class Statistics {

        private final int numberOfSquares;

        public Statistics(int numberOfSquares) {
            this.numberOfSquares = numberOfSquares;
        }

        public int getNumberOfSquares() {
            return numberOfSquares;
        }
    }

}
