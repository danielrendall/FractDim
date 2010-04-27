package uk.co.danielrendall.fractdim.app.model;

import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.UnmodifiableBoundedRangeModel;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 20:02:40
 */
public class CalculationSettings {

    public final static Parameter MINIMUM_SQUARES = new Parameter("CALC_SETTINGS", "MINIMUM_SQUARES", "Minimum square size", "The smallest square size to be used for counting");
    public final static Parameter MAXIMUM_SQUARES = new Parameter("CALC_SETTINGS", "MAXIMUM_SQUARES", "Maximum square size", "The largest square size to be used for counting");
    public final static Parameter NUMBER_RESOLUTIONS = new Parameter("CALC_SETTINGS", "NUMBER_RESOLUTIONS", "Number of resolutions", "The number of different square sizes between the minimum and maximum (inclusive)");
    public final static Parameter NUMBER_ANGLES = new Parameter("CALC_SETTINGS", "NUMBER_ANGLES", "Number of angles", "The number of different grid angles to be tried for each resolution");
    public final static Parameter NUMBER_DISPLACEMENTS = new Parameter("CALC_SETTINGS", "NUMBER_DISPLACEMENTS", "Number of displacements", "The number of different offsets within each square to be tried for each angle");

    private final BoundedRangeModel minimumSquareSize;
    private final BoundedRangeModel maximumSquareSize;
    private final BoundedRangeModel numberOfResolutions;
    private final BoundedRangeModel numberOfAngles;
    private final BoundedRangeModel numberOfDisplacements;

    public static CalculationSettings createCalculationSettings(FractalDocumentMetadata metadata) {
        BoundingBox box = metadata.getBoundingBox();
        double biggestDimension = Math.max(box.getWidth(), box.getHeight());
        double smallestDimension = Math.min(box.getWidth(), box.getHeight());

        double min = smallestDimension / 50.0d;
        return new CalculationSettings(new UnmodifiableBoundedRangeModel(min, min, biggestDimension),
                new UnmodifiableBoundedRangeModel(biggestDimension, min, biggestDimension),
                new UnmodifiableBoundedRangeModel(2, 2, 20),
                new UnmodifiableBoundedRangeModel(1, 1, 10),
                new UnmodifiableBoundedRangeModel(1, 1, 3)
                );
    }

    private CalculationSettings(BoundedRangeModel minimumSquareSize, BoundedRangeModel maximumSquareSize, BoundedRangeModel numberOfResolutions, BoundedRangeModel numberOfAngles, BoundedRangeModel numberOfDisplacements) {
        this.minimumSquareSize = minimumSquareSize;
        this.maximumSquareSize = maximumSquareSize;
        this.numberOfResolutions = numberOfResolutions;
        this.numberOfAngles = numberOfAngles;
        this.numberOfDisplacements = numberOfDisplacements;
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

}
