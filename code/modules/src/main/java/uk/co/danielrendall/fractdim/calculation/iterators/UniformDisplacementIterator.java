package uk.co.danielrendall.fractdim.calculation.iterators;

import uk.co.danielrendall.fractdim.geom.Vec;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:44:22
 * To change this template use File | Settings | File Templates.
 */
public class UniformDisplacementIterator implements DisplacementIterator {

    private final int numberOfDisplacementSteps;
    private final double displacementIncrement;
    private int dispX;
    private int dispY;

    public UniformDisplacementIterator(int numberOfDisplacementSteps) {
        this.numberOfDisplacementSteps = numberOfDisplacementSteps;
        this.displacementIncrement = 1.0d / (double) numberOfDisplacementSteps;
        dispX = 0;
        dispY = 0;
    }

    public boolean hasNext() {
        return dispY < numberOfDisplacementSteps && dispX < numberOfDisplacementSteps;
    }

    public Vec next() {
        Vec result = new Vec(displacementIncrement * (double) (dispX++), displacementIncrement * (double) dispY);
        if (dispX == numberOfDisplacementSteps) {
            dispX = 0;
            dispY++;
        }

        return result;
    }

    public void reset() {
        dispX = 0;
        dispY = 0;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
