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
 * Time: 21:26:42
 * To change this template use File | Settings | File Templates.
 */
public class GridCollectionBuilder {
    private final AngleGridCollection angleGridCollection = new AngleGridCollection();
    private final AddGridVisitor addGridVisitor = new AddGridVisitor();

    public GridCollectionBuilder grid(Grid grid) {
        addGridVisitor.setGrid(grid);
        angleGridCollection.accept(addGridVisitor);
        return this;
    }

    public GridCollection build() {
        return new GridCollection(angleGridCollection.freeze());
    }

    private class AddGridVisitor implements CollectionVisitor {

        private Grid grid;

        public void setGrid(Grid grid) {
            this.grid = grid;
        }

        public void visit(AngleGridCollection collection) {
            collection.collectionForAngle(grid.getAngle()).accept(this);
        }

        public void visit(ResolutionGridCollection collection) {
            collection.collectionForResolution(grid.getResolution()).accept(this);
        }

        public void visit(DisplacementGridCollection collection) {
            collection.addGrid(grid);
        }
    }
}
