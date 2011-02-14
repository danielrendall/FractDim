/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.fractdim.generate.fractals;

import uk.co.danielrendall.fractdim.generate.Procedure;
import uk.co.danielrendall.mathlib.geom2d.Line;
import uk.co.danielrendall.mathlib.geom2d.Vec;

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
