package uk.co.danielrendall.fractdim.calculation.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 16-May-2010
 * Time: 20:06:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class ResolutionIteratorFactory {

    public final static ResolutionIteratorFactory[] factories = {
            new ResolutionIteratorFactory() {
                public ResolutionIterator create(double minimumSquareSize, double maximumSquareSize, int numberOfResolutionSteps) {
                    return new UniformResolutionIterator(minimumSquareSize, maximumSquareSize, numberOfResolutionSteps);
                }

                public String getName() {
                    return "Uniform";
                }
            },
            new ResolutionIteratorFactory() {
                public ResolutionIterator create(double minimumSquareSize, double maximumSquareSize, int numberOfResolutionSteps) {
                    return new LogarithmicResolutionIterator(minimumSquareSize, maximumSquareSize, numberOfResolutionSteps);
                }

                public String getName() {
                    return "Logarithmic";
                }
            },
            new ResolutionIteratorFactory() {
                public ResolutionIterator create(double minimumSquareSize, double maximumSquareSize, int numberOfResolutionSteps) {
                    return new DoublingResolutionIterator(minimumSquareSize, maximumSquareSize);
                }

                public String getName() {
                    return "Doubling";
                }
            },
    };


    public abstract ResolutionIterator create(double minimumSquareSize, double maximumSquareSize, int numberOfResolutionSteps);

    public String toString() {
        return getName();
    }

    protected abstract String getName();
}
