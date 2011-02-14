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

package uk.co.danielrendall.fractdim.calculation.grids;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 22:13:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class CollectionVisitorSupport implements CollectionVisitor {

    public void visit(AngleGridCollection collection) {
       for (double d : collection.getAvailableAngles()) {
           collection.collectionForAngle(d).accept(this);
       }
    }

    public void visit(ResolutionGridCollection collection) {
        for (double d : collection.getAvailableResolutions()) {
            collection.collectionForResolution(d).accept(this);
        }
    }

}
