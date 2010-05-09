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
