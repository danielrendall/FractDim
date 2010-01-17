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
