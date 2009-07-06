package uk.co.danielrendall.fractdim.calculation;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

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
