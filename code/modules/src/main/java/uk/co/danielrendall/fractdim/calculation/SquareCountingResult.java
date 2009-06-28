package uk.co.danielrendall.fractdim.calculation;

import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 10:28:34
 */
public class SquareCountingResult {

    // maps initial rotation angle to the results for that angle
    private final SortedMap<Double, SortedMap<Double, SquareCounts>> results;
    private final SquareCounter grids;

    // makes sense to have this backed by the grids because we may want to display them
    // in the SVG - contrast the approach with Statistics
    SquareCountingResult(SquareCounter grids) {
        results = new TreeMap<Double, SortedMap<Double, SquareCounts>>();
        this.grids = grids;
    }

    // using SortedMap means that the iterator returns these in order
    public Set<Double> getAvailableAngles() {
        return Collections.unmodifiableSet(grids.getAvailableAngles());
    }

    public Set<Double> getAvailableResolutions(double angle) {
        return Collections.unmodifiableSet(grids.getAvailableResolutions(angle));
    }

    // TODO - handle the case where there are no such statistics
    public SquareCounts getStatistics(double angle, double resolution) {
        return new SquareCounts(grids.setForResolution(resolution, grids.mapForAngle(angle)));
    }

    public String getFractalDimension() {
        return "42";
    }


}
