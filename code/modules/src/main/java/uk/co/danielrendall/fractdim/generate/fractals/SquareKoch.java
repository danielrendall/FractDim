package uk.co.danielrendall.fractdim.generate.fractals;

import uk.co.danielrendall.fractdim.generate.Procedure;
import uk.co.danielrendall.fractdim.geom.Line;
import uk.co.danielrendall.fractdim.geom.Vec;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 15:31:02
 */
public class SquareKoch implements Procedure {

    public Line[] expand(Line line, int depth) {
        Vec vec = line.getVec();
        Vec third = vec.shrink(3.0);

        Vec perp = (depth % 2 == 0) ? third.rotate(Math.PI / 2.0) : third.rotate(-Math.PI / 2.0);


        Line[] lines = new Line[5];

        lines[0] = new Line(line.getStart(), third);
        lines[1] = new Line(lines[0].getEnd(), perp);
        lines[2] = new Line(lines[1].getEnd(), third);
        lines[3] = new Line(lines[2].getEnd(), line.getEnd().displace(third.neg()));
        lines[4] = new Line(lines[3].getEnd(), line.getEnd());

        return lines;
    }

}
