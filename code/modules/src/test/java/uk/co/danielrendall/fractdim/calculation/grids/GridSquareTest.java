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

package uk.co.danielrendall.fractdim.calculation.grids;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import uk.co.danielrendall.fractdim.calculation.grids.GridSquare;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 19:26:43
 */
public class GridSquareTest {


    @Test
    public void testDirection() {
        GridSquare one = GridSquare.create(5, 8);
        GridSquare two = GridSquare.create(1, 2);
        GridSquare three = GridSquare.create(6, 8);
        GridSquare four = GridSquare.create(2, 3);
        GridSquare five = GridSquare.create(5, 8);
        GridSquare six = GridSquare.create(5, 7);
        GridSquare seven = GridSquare.create(6, 6);

        assertEquals(GridSquare.NO_TOUCH, one.direction(two));
        assertEquals(GridSquare.SAME, five.direction(one));

        assertEquals(GridSquare.ABOVE_LEFT, four.direction(two));
        assertEquals(GridSquare.BELOW_RIGHT, two.direction(four));

        assertEquals(GridSquare.RIGHT, five.direction(three));
        assertEquals(GridSquare.LEFT, three.direction(five));

        assertEquals(GridSquare.BELOW, six.direction(five));
        assertEquals(GridSquare.ABOVE, five.direction(six));

        assertEquals(GridSquare.ABOVE_RIGHT, six.direction(seven));
        assertEquals(GridSquare.BELOW_LEFT, seven.direction(six));


    }

}
