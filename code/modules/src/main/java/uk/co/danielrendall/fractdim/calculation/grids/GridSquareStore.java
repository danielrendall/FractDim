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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 20:15:13
 */
public class GridSquareStore {

    private final Set<GridSquare> squares;

    public GridSquareStore() {
        this(new HashSet<GridSquare>());
    }

    public GridSquareStore(Set<GridSquare> squares) {
        this.squares = squares;
    }

    public void put(GridSquare square) {
        squares.add(square);
    }

    public int count() {
        return squares.size();
    }

    public Iterator<GridSquare> squareIterator() {
        return squares.iterator();
    }

    public void clear() {
        squares.clear();
    }

    public void addAll(GridSquareStore other) {
        squares.addAll(other.squares);
    }
}
