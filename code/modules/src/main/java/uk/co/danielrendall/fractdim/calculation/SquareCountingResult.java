package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.calculation.grids.CollectionVisitor;
import uk.co.danielrendall.fractdim.calculation.grids.AngleGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.ResolutionGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.DisplacementGridCollection;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.util.*;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 10:28:34
 */
public class SquareCountingResult {

    // maps initial rotation angle to the results for that angle
//    private final SortedMap<Double, FixedAngleSquareCountingResult> results;

    private final AngleGridCollection angleGridCollection;
    // makes sense to have this backed by the grids because we may want to display them
    // in the SVG - contrast the approach with Statistics
    SquareCountingResult(AngleGridCollection angleGridCollection) {
        this.angleGridCollection = angleGridCollection;
    }

    public AngleGridCollection getAngleGridCollection() {
        return angleGridCollection;
    }

}
