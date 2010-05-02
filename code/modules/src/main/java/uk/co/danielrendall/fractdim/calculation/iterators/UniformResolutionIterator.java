package uk.co.danielrendall.fractdim.calculation.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:38:55
 * To change this template use File | Settings | File Templates.
 */
public class UniformResolutionIterator implements ResolutionIterator {
    private final double minResolution;
    private final double maxResolution;
    private final int numberOfResolutionSteps;
    private final double resolutionIncrement;
    private int resolutionStep;

    public UniformResolutionIterator(double minResolution, double maxResolution, int numberOfResolutionSteps) {
        this.minResolution = minResolution;
        this.maxResolution = maxResolution;
        this.numberOfResolutionSteps = numberOfResolutionSteps;
        this.resolutionIncrement = (maxResolution - minResolution) / (double)(numberOfResolutionSteps);
        //        double resolutionIncrement = ((logMinResolutionReciprocal - logMaxResolutionReciprocal) / (double) numberOfResolutionSteps);

    }

    public boolean hasNext() {
        return resolutionStep <= numberOfResolutionSteps;
    }

    public Double next() {

        Double next = (resolutionStep == numberOfResolutionSteps) ? maxResolution : minResolution + resolutionIncrement * (double)(resolutionStep);
        resolutionStep++;
        return next;
    }

    public void reset() {
        resolutionStep = 0;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
