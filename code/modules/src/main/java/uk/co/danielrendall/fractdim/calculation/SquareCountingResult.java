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

import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.calculation.grids.CollectionVisitor;
import uk.co.danielrendall.fractdim.calculation.grids.AngleGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.ResolutionGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.DisplacementGridCollection;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.util.*;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 10:28:34
 */
public class SquareCountingResult {

    // maps initial rotation angle to the results for that angle
//    private final SortedMap<Double, FixedAngleSquareCountingResult> results;

    private final AngleGridCollection angleGridCollection;
    // makes sense to have this backed by the grids because we may want to display them
    // in the SVG - contrast the approach with Statistics
    SquareCountingResult(AngleGridCollection angleGridCollection) {
        this.angleGridCollection = angleGridCollection;
    }

    public AngleGridCollection getAngleGridCollection() {
        return angleGridCollection;
    }

}
