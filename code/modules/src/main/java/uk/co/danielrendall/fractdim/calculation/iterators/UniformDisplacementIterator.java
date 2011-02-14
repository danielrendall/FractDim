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

package uk.co.danielrendall.fractdim.calculation.iterators;

import uk.co.danielrendall.mathlib.geom2d.Vec;

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
