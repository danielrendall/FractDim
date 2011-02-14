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
