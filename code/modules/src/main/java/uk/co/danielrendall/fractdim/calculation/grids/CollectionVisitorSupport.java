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
