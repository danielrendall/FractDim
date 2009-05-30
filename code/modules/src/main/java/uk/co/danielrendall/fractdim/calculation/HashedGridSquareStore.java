package uk.co.danielrendall.fractdim.calculation;

import java.util.*;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 20:15:28
 */
public class HashedGridSquareStore implements GridSquareStore {
    private final Set<GridSquare> squares;

    public HashedGridSquareStore() {
        squares = new HashSet<GridSquare>();
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

}
