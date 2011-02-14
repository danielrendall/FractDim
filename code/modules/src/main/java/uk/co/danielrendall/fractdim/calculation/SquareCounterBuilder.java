/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

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
        GridCollection collection = builder.build();
        return new SquareCounter(fractalDocument, collection, maxDepth);
    }

}
