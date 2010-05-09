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
}
