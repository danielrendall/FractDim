package uk.co.danielrendall.fractdim.app.datamodel;

import uk.co.danielrendall.fractdim.calculation.Statistics;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 20:02:40
 */
public class CalculationSettings {

    private double minimumSquareSize;
    private double maximumSquareSize;
    private int numberOfResolutions;
    private int numberOfAngles;
    private int numberOfDisplacementPoints;

    public CalculationSettings() {
        minimumSquareSize = 1;
        maximumSquareSize = 1;
        numberOfResolutions = 1;
        numberOfAngles = 1;
        numberOfDisplacementPoints = 1;
    }

    public CalculationSettings(Statistics statistics) {
        minimumSquareSize = statistics.getShortestLine() / 10;
        maximumSquareSize = Math.max(statistics.getImageHeight(), statistics.getImageHeight());
        numberOfResolutions = (int) ((Math.log10(maximumSquareSize) - Math.log10(minimumSquareSize)) * 10.0d);
        numberOfAngles = 2;
        numberOfDisplacementPoints = 2;
    }

    public double getMinimumSquareSize() {
        return minimumSquareSize;
    }

    public void setMinimumSquareSize(double minimumSquareSize) {
        this.minimumSquareSize = minimumSquareSize;
    }

    public double getMaximumSquareSize() {
        return maximumSquareSize;
    }

    public void setMaximumSquareSize(double maximumSquareSize) {
        this.maximumSquareSize = maximumSquareSize;
    }

    public int getNumberOfResolutions() {
        return numberOfResolutions;
    }

    public void setNumberOfResolutions(int numberOfResolutions) {
        this.numberOfResolutions = numberOfResolutions;
    }

    public int getNumberOfAngles() {
        return numberOfAngles;
    }

    public void setNumberOfAngles(int numberOfAngles) {
        this.numberOfAngles = numberOfAngles;
    }

    public int getNumberOfDisplacementPoints() {
        return numberOfDisplacementPoints;
    }

    public void setNumberOfDisplacementPoints(int numberOfDisplacementPoints) {
        this.numberOfDisplacementPoints = numberOfDisplacementPoints;
    }
}
