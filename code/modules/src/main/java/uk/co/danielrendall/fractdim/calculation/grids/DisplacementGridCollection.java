package uk.co.danielrendall.fractdim.calculation.grids;

import uk.co.danielrendall.fractdim.geom.Vec;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
* User: daniel
* Date: 17-Jan-2010
* Time: 21:25:39
* To change this template use File | Settings | File Templates.
*/
public class DisplacementGridCollection {
    private final Map<Vec, Grid> gridMap;

    private final boolean isBuilding;

    DisplacementGridCollection() {
        this.gridMap = new TreeMap<Vec, Grid>(new Comparator<Vec>() {
            public int compare(Vec o1, Vec o2) {
                double yComp = o2.y() - o1.y();
                double xComp = o2.x() - o1.x();
                return yComp > 0.0d ? 1 : yComp < 0.0d ? -1 : xComp > 0.0d ? 1 : xComp < 0.0d ? -1 : 0;

            }
        });
        isBuilding = true;
    }

    private DisplacementGridCollection(Map<Vec, Grid> gridMap) {
        this.gridMap = Collections.unmodifiableMap(gridMap);
        isBuilding = false;
    }

    void addGrid(Grid grid) {
        if (isBuilding) {
            gridMap.put(grid.getFractionalDisplacement(), grid);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Set<Vec> getAvailableDisplacements() {
        return gridMap.keySet();
    }

    public Grid gridForDisplacement(Vec displacement) {
        return gridMap.get(displacement);
    }

    public void accept (CollectionVisitor visitor) {
        visitor.visit(this);
    }

    public DisplacementGridCollection freeze() {
        return new DisplacementGridCollection(gridMap);
    }

    public double getAverageSquareCount() {
        double tot = 0;
        Collection<Grid> grids = gridMap.values();
        for (Grid g : grids) {
            tot += (double)g.getSquareCount();
        }
        return tot / (double) grids.size();
    }
}