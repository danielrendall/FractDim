package uk.co.danielrendall.fractdim.app.model;

import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;

import javax.swing.*;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 20:02:40
 */
@Deprecated
public class CalculationSettings implements ParameterChangeListener {

    private final BoundedRangeModel minimumSquareSize;
    private final BoundedRangeModel maximumSquareSize;
    private final BoundedRangeModel numberOfResolutions;
    private final BoundedRangeModel numberOfAngles;
    private final BoundedRangeModel numberOfDisplacements;

    public CalculationSettings(BoundedRangeModel minimumSquareSize, BoundedRangeModel maximumSquareSize, BoundedRangeModel numberOfResolutions, BoundedRangeModel numberOfAngles, BoundedRangeModel numberOfDisplacements) {
        this.minimumSquareSize = minimumSquareSize;
        this.maximumSquareSize = maximumSquareSize;
        this.numberOfResolutions = numberOfResolutions;
        this.numberOfAngles = numberOfAngles;
        this.numberOfDisplacements = numberOfDisplacements;
        minimumSquareSize.addChangeListener(new SimpleChangeListener(this, FractalController.SQUARE_SIZES));
        numberOfResolutions.addChangeListener(new SimpleChangeListener(this, FractalController.NUMBER_RESOLUTIONS));
        numberOfAngles.addChangeListener(new SimpleChangeListener(this, FractalController.NUMBER_DISPLACEMENTS));
        numberOfDisplacements.addChangeListener(new SimpleChangeListener(this, FractalController.NUMBER_ANGLES));
    }


    public BoundedRangeModel getMinimumSquareSize() {
        return minimumSquareSize;
    }

    public BoundedRangeModel getMaximumSquareSize() {
        return maximumSquareSize;
    }

    public BoundedRangeModel getNumberOfResolutions() {
        return numberOfResolutions;
    }

    public BoundedRangeModel getNumberOfAngles() {
        return numberOfAngles;
    }

    public BoundedRangeModel getNumberOfDisplacements() {
        return numberOfDisplacements;
    }

    public String toString() {
        return String.format("Min: %s Max: %s Res: %s Ang: %s Disp: %s", minimumSquareSize, maximumSquareSize, numberOfResolutions, numberOfAngles, numberOfDisplacements);
    }

    public void valueChanged(Parameter param, int value) {
        if (param.equals(FractalController.SQUARE_SIZES)) {

        }

    }
}
