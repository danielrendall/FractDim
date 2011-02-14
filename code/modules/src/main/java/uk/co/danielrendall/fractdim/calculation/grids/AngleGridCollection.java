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

import java.util.*;

/**
 * Created by IntelliJ IDEA.
* User: daniel
* Date: 17-Jan-2010
* Time: 21:25:14
* To change this template use File | Settings | File Templates.
*/
public class AngleGridCollection {
    private final SortedMap<Double, ResolutionGridCollection> gridMap;
    private final boolean isBuilding;

    private boolean averageFractalDimensionCalculated = false;
    private double averageFractalDimension = 0.0d;

    AngleGridCollection() {
        this.gridMap = new TreeMap<Double, ResolutionGridCollection>();
        isBuilding = true;
    }

    private AngleGridCollection(SortedMap<Double, ResolutionGridCollection> gridMap) {
        this.gridMap = Collections.unmodifiableSortedMap(gridMap);
        isBuilding = false;
    }

    public Set<Double> getAvailableAngles() {
        return gridMap.keySet();
    }

    public ResolutionGridCollection collectionForAngle(double angle) {
        if (isBuilding && !gridMap.containsKey(angle)) {
            gridMap.put(angle, new ResolutionGridCollection());
        }
        return gridMap.get(angle);
    }

    public void accept (CollectionVisitor visitor) {
        visitor.visit(this);
    }

    AngleGridCollection freeze() {
        for (Map.Entry<Double, ResolutionGridCollection> next : gridMap.entrySet()) {
            next.setValue(next.getValue().freeze());
        }
        return new AngleGridCollection(gridMap);
    }

    public synchronized double getAverageFractalDimension() {
        if (!averageFractalDimensionCalculated) {
            double tot = 0;
            Collection<ResolutionGridCollection> grids = gridMap.values();
            for (ResolutionGridCollection rgc : grids) {
                tot += (double)rgc.getFractalDimension();
            }
            averageFractalDimension = tot / (double) grids.size();
            averageFractalDimensionCalculated = true;
        }
        return averageFractalDimension;
    }
}
