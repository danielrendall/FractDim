package uk.co.danielrendall.fractdim.calculation.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:57:11
 * To change this template use File | Settings | File Templates.
 */
public class LogarithmicResolutionIterator implements ResolutionIterator {

    private final double logMinResolutionReciprocal;
    private final double logMaxResolutionReciprocal;
    private final int numberOfResolutionSteps;
    private final double resolutionLogIncrement;
    private int resolutionStep;

    public LogarithmicResolutionIterator(double minResolution, double maxResolution, int numberOfResolutionSteps) {
        this.logMinResolutionReciprocal = Math.log(1.0d / minResolution);
        this.logMaxResolutionReciprocal = Math.log(1.0d / maxResolution);
        this.numberOfResolutionSteps = numberOfResolutionSteps;
        this.resolutionLogIncrement = (logMinResolutionReciprocal - logMaxResolutionReciprocal) / (double)(numberOfResolutionSteps);

    }

    public boolean hasNext() {
        return resolutionStep <= numberOfResolutionSteps;
    }

    public Double next() {
        double logResolutionReciprocal = logMaxResolutionReciprocal + (resolutionLogIncrement * (double) (resolutionStep++));
        double resolution = 1.0d / Math.exp(logResolutionReciprocal);
        return (resolutionStep == numberOfResolutionSteps) ? 1.0d / Math.exp(logMinResolutionReciprocal) : resolution;
    }

    public void reset() {
        resolutionStep = 0;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
