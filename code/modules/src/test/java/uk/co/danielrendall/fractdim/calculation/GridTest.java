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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.fractdim.calculation.grids.Grid;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 19:10:13
 */
public class GridTest {

    @Test
    public void testResult1() {
        Grid g = new Grid(100.d);
        Point start = new Point(31.0d, 66.0d);
        Point mid = new Point(153.0d, 124.0d);
        Point end = new Point(279.0d, 180.0d);
        g.startEvaluation(start, end);

        boolean[] result = g.notifyNewPoint(start, mid, end);
        assertTrue(result[0]); // points should be in diagonal squares
        assertFalse(result[1]); // points should be in adjacent squares

    }

}
