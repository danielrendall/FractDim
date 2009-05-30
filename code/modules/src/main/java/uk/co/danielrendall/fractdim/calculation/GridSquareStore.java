package uk.co.danielrendall.fractdim.calculation;

import java.util.Iterator;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 20:15:13
 */
public interface GridSquareStore {
    void put(GridSquare square);

    int count();

    Iterator<GridSquare> squareIterator();
}
