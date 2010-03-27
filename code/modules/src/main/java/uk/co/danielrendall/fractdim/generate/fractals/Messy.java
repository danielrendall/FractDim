package uk.co.danielrendall.fractdim.generate.fractals;

import uk.co.danielrendall.fractdim.generate.Procedure;
import uk.co.danielrendall.mathlib.geom2d.Line;
import uk.co.danielrendall.mathlib.geom2d.Vec;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 15:42:55
 */
public class Messy implements Procedure {

    public Line[] expand(Line line, int depth) {
        Vec vec = line.getVec();
        Vec half = vec.shrink(2.0);

        Vec rotated = half.rotate(2.0 * Math.PI / 5.0);

        Line[] lines = new Line[3];

        lines[0] = new Line(line.getStart(), half.add(rotated));
        lines[1] = new Line(lines[0].getEnd(), line.getEnd().displace(half.neg().add(rotated.neg())));
        lines[2] = new Line(lines[1].getEnd(), line.getEnd());

        return lines;
    }

}
