package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.calculation.grids.Grid;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 11-May-2010
 * Time: 08:22:18
 * To change this template use File | Settings | File Templates.
 */
public class GridSelectedEvent {

    private final Grid grid;

    public GridSelectedEvent(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }
}
