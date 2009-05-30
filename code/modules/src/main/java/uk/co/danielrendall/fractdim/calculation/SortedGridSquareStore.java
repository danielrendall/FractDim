package uk.co.danielrendall.fractdim.calculation;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * @author Daniel Rendall
* @created 30-May-2009 20:14:01
*/
class SortedGridSquareStore implements GridSquareStore {
    private final SortedSet<GridSquare> squares;

    public SortedGridSquareStore() {
        squares = new TreeSet<GridSquare>();
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
