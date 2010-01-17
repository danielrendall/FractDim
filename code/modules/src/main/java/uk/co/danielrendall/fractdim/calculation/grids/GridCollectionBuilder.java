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
