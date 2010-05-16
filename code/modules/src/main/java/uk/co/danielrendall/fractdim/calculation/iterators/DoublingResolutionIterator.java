package uk.co.danielrendall.fractdim.calculation.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 16-May-2010
 * Time: 20:12:11
 * To change this template use File | Settings | File Templates.
 */
public class DoublingResolutionIterator implements ResolutionIterator {

    private final double minResolution;
    private final double maxResolution;
    private double currentResolution;

    public DoublingResolutionIterator(double minResolution, double maxResolution) {
        this.minResolution = minResolution;
        this.maxResolution = maxResolution;
        this.currentResolution = minResolution;
    }

    public boolean hasNext() {
        return currentResolution <= maxResolution;
    }

    public Double next() {

        Double next = currentResolution;
        currentResolution *= 2.0d;
        return next;
    }

    public void reset() {
        this.currentResolution = minResolution;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
