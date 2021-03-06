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
* Time: 21:25:26
* To change this template use File | Settings | File Templates.
*/
public class ResolutionGridCollection {
    private final SortedMap<Double, DisplacementGridCollection> gridMap;
    private final boolean isBuilding;

    private boolean fractalDimensionCalculated;
    private double fractalDimension = 0.0d;

    ResolutionGridCollection() {
        this.gridMap = new TreeMap<Double, DisplacementGridCollection>();
        isBuilding = true;
    }

    private ResolutionGridCollection(SortedMap<Double, DisplacementGridCollection> gridMap) {
        this.gridMap = Collections.unmodifiableSortedMap(gridMap);
        isBuilding = false;
    }

    public Set<Double> getAvailableResolutions() {
        return gridMap.keySet();
    }

    public DisplacementGridCollection collectionForResolution(double resolution) {
        if (isBuilding && !gridMap.containsKey(resolution)) {
            gridMap.put(resolution, new DisplacementGridCollection());
        }
        return gridMap.get(resolution);
    }

    public void accept (CollectionVisitor visitor) {
        visitor.visit(this);
    }

    public ResolutionGridCollection freeze() {
        for (Map.Entry<Double, DisplacementGridCollection> next : gridMap.entrySet()) {
            next.setValue(next.getValue().freeze());
        }
        return new ResolutionGridCollection(gridMap);
    }

    public synchronized double getFractalDimension() {
        if (!fractalDimensionCalculated) {
            double logSquareCountsSum = 0.0d; // Y
            double logReciprocalResolutionsSum = 0.0d; // X

            double sumOfXTimesY = 0.0d;
            double sumOfXSquared = 0.0d; // logReciprocalResolution

            int i=0;
            for (Map.Entry<Double, DisplacementGridCollection> entry : gridMap.entrySet()) {
                double resolution = entry.getKey();
                double squareCounts = entry.getValue().getMinimumSquareCount();
                // assume no pesky divide by zero errors...
                double logSquareCount = Math.log(squareCounts);
                double logReciprocalResolution = Math.log(1.0d/resolution);

                sumOfXTimesY += (logReciprocalResolution * logSquareCount);
                sumOfXSquared += (logReciprocalResolution * logReciprocalResolution);

                logSquareCountsSum += logSquareCount;
                logReciprocalResolutionsSum += logReciprocalResolution;
                i++;
            }

            double logSquareCountMean = logSquareCountsSum / (double) i;
            double logReciprocalResolutionMean = logReciprocalResolutionsSum / (double) i;

            fractalDimension = ((sumOfXTimesY - ((double) i * logSquareCountMean * logReciprocalResolutionMean)) /
                    (sumOfXSquared - ((double) i * logReciprocalResolutionMean * logReciprocalResolutionMean)));
        }
        return fractalDimension;
    }
}
