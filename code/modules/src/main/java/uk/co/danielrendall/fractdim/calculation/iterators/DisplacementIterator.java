package uk.co.danielrendall.fractdim.calculation.iterators;

import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 17:33:00
 * To change this template use File | Settings | File Templates.
 */
public interface DisplacementIterator extends Iterator<Vec> {

    void reset();
    
}
