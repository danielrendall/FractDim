package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.calculation.iterators.*;
import uk.co.danielrendall.fractdim.calculation.grids.Grid;
import uk.co.danielrendall.fractdim.calculation.grids.GridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.GridCollectionBuilder;
import uk.co.danielrendall.mathlib.geom2d.Vec;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 16-Jan-2010
 * Time: 18:55:08
 * To change this template use File | Settings | File Templates.
 */
public class SquareCounterBuilder {

    private int maxDepth = 1;
    private AngleIterator angleIterator = new UniformAngleIterator(1);
    private ResolutionIterator resolutionIterator = new UniformResolutionIterator(1.0d, 1000.0d, 1);
    private DisplacementIterator displacementIterator = new UniformDisplacementIterator(1);
    private FractalDocument fractalDocument = null;

    public SquareCounterBuilder maxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    public SquareCounterBuilder angleIterator(AngleIterator angleIterator) {
        this.angleIterator = angleIterator;
        return this;
    }

    public SquareCounterBuilder resolutionIterator(ResolutionIterator resolutionIterator) {
        this.resolutionIterator = resolutionIterator;
        return this;
    }

    public SquareCounterBuilder displacementIterator(DisplacementIterator displacementIterator) {
        this.displacementIterator = displacementIterator;
        return this;
    }

    public SquareCounterBuilder fractalDocument(FractalDocument fractalDocument) {
        this.fractalDocument = fractalDocument;
        return this;
    }

    public SquareCounter build() {
        GridCollectionBuilder builder = new GridCollectionBuilder();
//        if (svgWithMetadata == null) throw new IllegalStateException("Can't build - no svgWithMetadata");
        for (angleIterator.reset(); angleIterator.hasNext();) {
            double angle = angleIterator.next();
            for (resolutionIterator.reset(); resolutionIterator.hasNext();) {
                double resolution = resolutionIterator.next();
                for (displacementIterator.reset(); displacementIterator.hasNext();) {
                    Vec fractionalDisplacement = displacementIterator.next();
                    builder.grid(new Grid(angle, resolution,  fractionalDisplacement));
                }
            }
        }
        return new SquareCounter(fractalDocument, builder.build(), maxDepth);
    }

}
