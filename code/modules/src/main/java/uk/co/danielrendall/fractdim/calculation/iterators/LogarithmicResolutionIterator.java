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

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:57:11
 * To change this template use File | Settings | File Templates.
 */
public class LogarithmicResolutionIterator implements ResolutionIterator {

    private final SortedSet<Double> resolutions;
    private Iterator<Double> iterator;

    public LogarithmicResolutionIterator(double minResolution, double maxResolution, int numberOfResolutionSteps) {
        this.resolutions = new TreeSet<Double>();
        double logMinResolutionReciprocal = Math.log(1.0d / minResolution);
        double logMaxResolutionReciprocal = Math.log(1.0d / maxResolution);
        double resolutionLogIncrement = (logMinResolutionReciprocal - logMaxResolutionReciprocal) / (double) (numberOfResolutionSteps - 1);

        for (int i=0; i < numberOfResolutionSteps; i++ ) {
            double logResolutionReciprocal = (i < (numberOfResolutionSteps - 1)) ? logMaxResolutionReciprocal + (resolutionLogIncrement * (double) i) : logMinResolutionReciprocal;
            resolutions.add(1.0d / Math.exp(logResolutionReciprocal));
        }
        iterator = resolutions.iterator();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Double next() {
        return iterator.next();
    }

    public void reset() {
        iterator = resolutions.iterator();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
