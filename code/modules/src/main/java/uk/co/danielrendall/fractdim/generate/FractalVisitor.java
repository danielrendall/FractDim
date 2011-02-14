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

/**
 * @author Daniel Rendall
 * @created 23-May-2009 12:05:15
 */
public abstract class FractalVisitor {

    protected final int desiredDepth;

    public FractalVisitor(int desiredDepth) {
        this.desiredDepth = desiredDepth;
    }

    public int getDesiredDepth() {
        return desiredDepth;
    }

    public abstract void visit(Fractal aFractal);
}
