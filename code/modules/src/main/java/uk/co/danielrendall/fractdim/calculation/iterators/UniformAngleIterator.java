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
