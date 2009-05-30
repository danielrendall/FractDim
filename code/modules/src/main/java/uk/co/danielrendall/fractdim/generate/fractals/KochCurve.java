package uk.co.danielrendall.fractdim.generate.fractals;

import uk.co.danielrendall.fractdim.geom.Line;
import uk.co.danielrendall.fractdim.geom.Vec;
import uk.co.danielrendall.fractdim.generate.Procedure;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 11:40:10
 */
public class KochCurve implements Procedure {

    public Line[] expand(Line line, int depth) {
        Vec vec = line.getVec();
        Vec third = vec.shrink(3.0);

        Line[] lines = new Line[4];

        lines[0] = new Line(line.getStart(), third);
        lines[1] = new Line(lines[0].getEnd(), third.rotate(Math.PI / 3.0));
        lines[2] = new Line(lines[1].getEnd(), line.getEnd().displace(third.neg()));
        lines[3] = new Line(lines[2].getEnd(), line.getEnd());

        return lines;
    }
}
