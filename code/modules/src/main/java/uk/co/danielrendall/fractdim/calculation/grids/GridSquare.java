package uk.co.danielrendall.fractdim.calculation.grids;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Daniel Rendall
* @created 30-May-2009 19:25:42
*/
public class GridSquare implements Comparable<GridSquare> {

    static final int NO_TOUCH = -1;
    static final int SAME = 0;
    static final int ABOVE_LEFT = 1;
    static final int ABOVE = 2;
    static final int ABOVE_RIGHT = 3;
    static final int RIGHT = 4;
    static final int BELOW_RIGHT = 5;
    static final int BELOW = 6;
    static final int BELOW_LEFT = 7;
    static final int LEFT = 8;

    static final Map<String, GridSquare> squares = new HashMap<String, GridSquare>();

    // maybe better to have a factory method which returns flyweight squares?
    public static int createCount = 0;

    private final int xIndex, yIndex;
    private final int hashCode;

    private GridSquare(int xIndex, int yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        hashCode = xIndex << 16 | (yIndex & 0xffff);
        createCount++;
    }

    public static GridSquare create(int xIndex, int yIndex) {
        String key = "" + xIndex + "," + yIndex;
        GridSquare square = squares.get(key);
        if (square == null) {
            square = new GridSquare(xIndex, yIndex);
            squares.put(key, square);
        }
        return square;
    }

    public static void resetCount() {
        createCount = 0;
        squares.clear();
    }


    public int x() {
        return xIndex;
    }

    public int y() {
        return yIndex;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridSquare) {
            GridSquare other = (GridSquare) obj;
            return (this.xIndex == other.xIndex && this.yIndex == other.yIndex);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", xIndex, yIndex);
    }

    /**
     * Returns an int indicating how 'another' is related spacially to this pixel. If they are the same, then 0
     * is returned. If they are touching (either adjacent or diagonal), the number is:
     * <p/>
     * 1  2  3
     * 8     4
     * 7  6  5
     * <p/>
     * I.e. '3' means that 'another' is above and to the right of this one.
     * <p/>
     * If they are not touching, -1 is returned
     *
     * @param another
     * @return
     */
    int direction(GridSquare another) {

        int xDiff = another.xIndex - xIndex; // +ve other is to the right, 0 = same, -ve other to the left
        int yDiff = another.yIndex - yIndex; // +ve other is below, 0 = same, -ve = other is above

        switch (yDiff) {
            case -1:
                switch (xDiff) {
                    case -1:
                        return ABOVE_LEFT;
                    case 0:
                        return ABOVE;
                    case 1:
                        return ABOVE_RIGHT;
                    default:
                        return NO_TOUCH;
                }
            case 0:
                switch (xDiff) {
                    case -1:
                        return LEFT;
                    case 0:
                        return SAME;
                    case 1:
                        return RIGHT;
                    default:
                        return NO_TOUCH;
                }
            case 1:
                switch (xDiff) {
                    case -1:
                        return BELOW_LEFT;
                    case 0:
                        return BELOW;
                    case 1:
                        return BELOW_RIGHT;
                    default:
                        return NO_TOUCH;
                }
            default:
                return NO_TOUCH;
        }

    }

    public int compareTo(GridSquare other) {
        int yDiff = yIndex - other.yIndex;
        if (yDiff != 0) return yDiff;
        return xIndex - other.xIndex;
    }

}
