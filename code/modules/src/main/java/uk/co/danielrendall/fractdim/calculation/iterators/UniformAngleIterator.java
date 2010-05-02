package uk.co.danielrendall.fractdim.calculation.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:33:53
 * To change this template use File | Settings | File Templates.
 */
public class UniformAngleIterator implements AngleIterator {

    private final int numberOfAngles;
    private final double angleIncrement;
    private int angleNumber = 0;

    public UniformAngleIterator(int numberOfAngles) {
        this.numberOfAngles = numberOfAngles;
        this.angleIncrement = Math.PI / (2.0d * (double) numberOfAngles);
    }

    public boolean hasNext() {
        return angleNumber < numberOfAngles;
    }

    public Double next() {
        double next = angleIncrement * (double) (angleNumber);
        angleNumber++;
        return next;
    }

    public void reset() {
        angleNumber = 0;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
