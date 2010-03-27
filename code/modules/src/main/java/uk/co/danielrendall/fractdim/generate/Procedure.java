package uk.co.danielrendall.fractdim.generate;

import uk.co.danielrendall.mathlib.geom2d.Line;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 14:07:36
 */
public interface Procedure {

    public Line[] expand(Line line, int depth);
}
