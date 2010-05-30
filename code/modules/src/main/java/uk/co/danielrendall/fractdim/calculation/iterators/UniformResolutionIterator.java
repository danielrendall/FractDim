package uk.co.danielrendall.fractdim.calculation.iterators;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:38:55
 * To change this template use File | Settings | File Templates.
 */
public class UniformResolutionIterator implements ResolutionIterator {

    private final SortedSet<Double> resolutions;
    private Iterator<Double> iterator;

    public UniformResolutionIterator(double minResolution, double maxResolution, int numberOfResolutionSteps) {
        this.resolutions = new TreeSet<Double>();
        double resolutionIncrement = (maxResolution - minResolution) / (double)(numberOfResolutionSteps - 1);
        for (int i=0; i < numberOfResolutionSteps; i++ ) {
            double resolution = (i < (numberOfResolutionSteps - 1)) ? minResolution + (resolutionIncrement * (double) i) : maxResolution;
            resolutions.add(resolution);
        }
        iterator = resolutions.iterator();

    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Double next() {
        return iterator.next();
    }

    public void reset() {
        iterator = resolutions.iterator();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
