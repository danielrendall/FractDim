package uk.co.danielrendall.fractdim.calculation.grids;

import uk.co.danielrendall.fractdim.geom.Vec;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 18:03:24
 * To change this template use File | Settings | File Templates.
 */
public class GridCollection {

    private final AngleGridCollection collection;

    GridCollection(AngleGridCollection collection) {
        this.collection = collection;
    }

    public void accept(CollectionVisitor visitor) {
        collection.accept(visitor);
    }

}
