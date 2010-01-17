package uk.co.danielrendall.fractdim.calculation.grids;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 17-Jan-2010
 * Time: 21:53:50
 * To change this template use File | Settings | File Templates.
 */
public interface CollectionVisitor {

    void visit(AngleGridCollection collection);
    void visit(ResolutionGridCollection collection);
    void visit(DisplacementGridCollection collection);
}
