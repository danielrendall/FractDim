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
