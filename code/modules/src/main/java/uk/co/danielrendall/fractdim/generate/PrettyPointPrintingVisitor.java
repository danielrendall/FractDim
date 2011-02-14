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

import uk.co.danielrendall.mathlib.geom2d.Point;

import java.util.Iterator;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 12:08:17
 */
public class PrettyPointPrintingVisitor extends FractalVisitor {


    public PrettyPointPrintingVisitor(int desiredDepth) {
        super(desiredDepth);
    }

    public void visit(Fractal aFractal) {
        int depth = aFractal.getDepth();
        String indent = "               ".substring(0, depth);
        if (depth == desiredDepth) {
            Point endPoint = aFractal.getEndPoint();
            System.out.println(indent + " " + endPoint);
        } else {
            for (Iterator<Fractal> it = aFractal.childIterator(); it.hasNext();) {
                it.next().accept(this);
            }
        }
    }
}
