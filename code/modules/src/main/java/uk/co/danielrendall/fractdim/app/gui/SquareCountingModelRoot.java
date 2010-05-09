package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.calculation.grids.AngleGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.DisplacementGridCollection;
import uk.co.danielrendall.fractdim.calculation.grids.Grid;
import uk.co.danielrendall.fractdim.calculation.grids.ResolutionGridCollection;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.util.ArrayList;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: daniel
* Date: 09-May-2010
* Time: 14:56:54
* To change this template use File | Settings | File Templates.
*/
class SquareCountingModelRoot implements SquareCountingModelTreeNode  {

    private final List<AngleModelNode> angleModels;

    public SquareCountingModelRoot(AngleGridCollection angleGridCollection) {
        angleModels = new ArrayList<AngleModelNode>();
        for (double angle : angleGridCollection.getAvailableAngles()) {
            angleModels.add(new AngleModelNode(angle, angleGridCollection.collectionForAngle(angle)));
        }
    }

    public Object getValueAt(int column) {
        return "No value";
    }

    public Object getChild(int index) {
        return angleModels.get(index);
    }

    public int getChildCount() {
        Log.gui.debug("Asked for child count");
        return angleModels.size();
    }

    public int getIndexOfChild(Object child) {
        for (int i=0; i< angleModels.size(); i++) {
            if (child == angleModels.get(i)) return i;
        }
        return -1;
    }

    private class AngleModelNode implements SquareCountingModelTreeNode {
        private final double angle;
        private final List<ResolutionModelNode> resolutionModels;

        private AngleModelNode(double angle, ResolutionGridCollection resolutionGridCollection) {
            this.angle = angle;
            resolutionModels = new ArrayList<ResolutionModelNode>();
            for (double resolution : resolutionGridCollection.getAvailableResolutions()) {
                resolutionModels.add(new ResolutionModelNode(resolution, resolutionGridCollection.collectionForResolution(resolution)));
            }
        }

        public double getAngle() {
            return angle;
        }

        public Object getValueAt(int column) {
            return column == 0 ? String.format("Angle: %5.2f", angle) : "";

        }

        public Object getChild(int index) {
            return resolutionModels.get(index);
        }

        public int getChildCount() {
            return resolutionModels.size();
        }

        public int getIndexOfChild(Object child) {
            for (int i=0; i< resolutionModels.size(); i++) {
                if (child == resolutionModels.get(i)) return i;
            }
            return -1;
        }
    }

    private class ResolutionModelNode implements SquareCountingModelTreeNode {

        private final double resolution;
        private final List<DisplacementModelNode> displacementModels;
        private final double averageSquareCount;

        private ResolutionModelNode(double resolution, DisplacementGridCollection displacementGridCollection) {
            this.resolution = resolution;
            displacementModels = new ArrayList<DisplacementModelNode>();
            for (Vec displacement : displacementGridCollection.getAvailableDisplacements()) {
                displacementModels.add(new DisplacementModelNode(displacement, displacementGridCollection.gridForDisplacement(displacement)));
            }
            double total = 0;
            for (DisplacementModelNode dm : displacementModels) {
                total += dm.getSquareCount();
            }
            averageSquareCount = total / (double) displacementModels.size();
        }

        public double getResolution() {
            return resolution;
        }

        public double getAverageSquareCount() {
            return averageSquareCount;
        }

        public Object getValueAt(int column) {
            return column == 0 ? String.format("Resolution: %5.2f", resolution) : String.format("%5.2f", averageSquareCount);
        }

        public Object getChild(int index) {
            return displacementModels.get(index);
        }

        public int getChildCount() {
            return displacementModels.size();
        }

        public int getIndexOfChild(Object child) {
            for (int i=0; i< displacementModels.size(); i++) {
                if (child == displacementModels.get(i)) return i;
            }
            return -1;
        }
    }

    private class DisplacementModelNode implements SquareCountingModelTreeNode {
        private final Vec displacement;
        private final Grid grid;
        private final double squareCount;

        public DisplacementModelNode(Vec displacement, Grid grid) {
            this.displacement = displacement;
            this.grid = grid;
            squareCount = (double) grid.getSquareCount();
        }

        public Vec getDisplacement() {
            return displacement;
        }

        public double getSquareCount() {
            return squareCount;
        }

        public Object getValueAt(int column) {
            return column == 0 ? String.format("Displacement: %s", displacement) : String.format("%5.2f", squareCount);
        }

        public Object getChild(int index) {
            return null;
        }

        public int getChildCount() {
            return 0;
        }

        public int getIndexOfChild(Object child) {
            return -1;
        }
    }

}

