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

package uk.co.danielrendall.fractdim.generate;

import uk.co.danielrendall.mathlib.geom2d.Line;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 11:37:56
 */
public class Fractal {

    private final Line line;
    private final int depth;
    private final List<Fractal> children;
    private final Procedure procedure;

    public Fractal(Line line, Procedure procedure) {
        this(0, line, procedure);
    }

    private Fractal(int depth, Line line, Procedure procedure) {
        this.depth = depth;
        this.line = line;
        children = new ArrayList<Fractal>();
        this.procedure = procedure;
    }

    public Point getStart() {
        return line.getStart();
    }

    public void accept(FractalVisitor fv) {
        Line[] lines = procedure.expand(line, depth);
        for (Line newLine : lines) {
            children.add(new Fractal(depth + 1, newLine, procedure));
        }
        fv.visit(this);
        children.clear();
    }

    public Point getEndPoint() {
        return line.getEnd();
    }

    public Iterator<Fractal> childIterator() {
        return Collections.unmodifiableList(children).iterator();
    }

    public int getDepth() {
        return depth;
    }
}
