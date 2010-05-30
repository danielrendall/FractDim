package uk.co.danielrendall.fractdim.calculation.iterators;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:57:11
 * To change this template use File | Settings | File Templates.
 */
public class LogarithmicResolutionIterator implements ResolutionIterator {

    private final SortedSet<Double> resolutions;
    private Iterator<Double> iterator;

    public LogarithmicResolutionIterator(double minResolution, double maxResolution, int numberOfResolutionSteps) {
        this.resolutions = new TreeSet<Double>();
        double logMinResolutionReciprocal = Math.log(1.0d / minResolution);
        double logMaxResolutionReciprocal = Math.log(1.0d / maxResolution);
        double resolutionLogIncrement = (logMinResolutionReciprocal - logMaxResolutionReciprocal) / (double) (numberOfResolutionSteps - 1);

        for (int i=0; i < numberOfResolutionSteps; i++ ) {
            double logResolutionReciprocal = (i < (numberOfResolutionSteps - 1)) ? logMaxResolutionReciprocal + (resolutionLogIncrement * (double) i) : logMinResolutionReciprocal;
            resolutions.add(1.0d / Math.exp(logResolutionReciprocal));
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
