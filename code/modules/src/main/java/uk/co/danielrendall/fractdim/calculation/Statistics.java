/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.mathlib.geom2d.Line;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

import java.util.Set;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 22:25:30
 */
public class Statistics {


    private final double shortestLine;
    private final double longestLine;
    private final double meanLineLength;
    private final double varianceLineLength;
    private final double meanFragmentOnlyLength;
    private final double varianceFragmentOnlyLength;
    private final double meanFragmentsPerCurve;
    private final double varianceFragmentsPerCurve;
    private final int totalCurveCount;
    private final int fragmentedCurveCount;

    private final BoundingBox boundingBox;


    public Statistics(int totalCurveCount, int fragmentedCurveCount,
                      double shortestLine, double longestLine,
                      double meanLineLength, double varianceLineLength,
                      double meanFragmentOnlyLength, double varianceFragmentOnlyLength,
                      double meanFragmentsPerCurve, double varianceFragmentsPerCurve,
                      BoundingBox boundingBox) {
        //To change body of created methods use File | Settings | File Templates.
        this.totalCurveCount = totalCurveCount;
        this.fragmentedCurveCount = fragmentedCurveCount;
        this.shortestLine = shortestLine;
        this.longestLine = longestLine;
        this.meanLineLength = meanLineLength;
        this.varianceLineLength = varianceLineLength;
        this.meanFragmentOnlyLength = meanFragmentOnlyLength;
        this.varianceFragmentOnlyLength = varianceFragmentOnlyLength;
        this.meanFragmentsPerCurve = meanFragmentsPerCurve;
        this.varianceFragmentsPerCurve = varianceFragmentsPerCurve;
        this.boundingBox = boundingBox;
    }

    public double getShortestLine() {
        return shortestLine;
    }

    public double getLongestLine() {
        return longestLine;
    }

    public double getMeanLineLength() {
        return meanLineLength;
    }

    public double getVarianceLineLength() {
        return varianceLineLength;
    }

    public double getMeanFragmentOnlyLength() {
        return meanFragmentOnlyLength;
    }

    public double getVarianceFragmentOnlyLength() {
        return varianceFragmentOnlyLength;
    }

    public double getMeanFragmentsPerCurve() {
        return meanFragmentsPerCurve;
    }

    public double getVarianceFragmentsPerCurve() {
        return varianceFragmentsPerCurve;
    }

    public int getTotalCurveCount() {
        return totalCurveCount;
    }

    public int getFragmentedCurveCount() {
        return fragmentedCurveCount;
    }

    public double getImageWidth() {
        return boundingBox.getWidth();
    }

    public double getImageHeight() {
        return boundingBox.getHeight();
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public static Statistics create(Set<Set<Line>> curveLines) {
        // the total number of curves in the set
        int totalCurveCount = 0;

        // the number of curves which were split into more than one line
        int fragmentedCurveCount = 0;

        // the length of the shortest line or fragment in the set
        double shortestLine = Double.MAX_VALUE;

        // the length of the longest line or fragment in the set
        double longestLine = Double.MIN_VALUE;


        // total length of all the lines / fragments found
        double totalLineLength = 0;
        // total number of lines / fragments found
        int totalLineCount = 0;

        // total of only those fragments where there were more than one in the curve
        // (i.e. curves which are actually straight lines already are not counted)
        double totalFragmentOnlyLineLength = 0;
        // total of fragments when there were more than one in the curve
        int totalFragmentOnlyCount = 0;

        BoundingBox bbox = BoundingBox.empty();


        for (Set<Line> curveData : curveLines) {
            totalCurveCount++;

            // is this curve split into a number of separate line fragments?
            boolean isFragmented = (curveData.size() > 1);

            if (isFragmented) {
                fragmentedCurveCount++;
            }

            for (Line fragment : curveData) {
                double fragmentLength = fragment.length();

                bbox = bbox.expandToInclude(fragment.getBoundingBox());

                totalLineLength += fragmentLength;
                totalLineCount++;

                if (isFragmented) {
                    totalFragmentOnlyLineLength += fragmentLength;
                    totalFragmentOnlyCount++;
                }


                if (fragmentLength < shortestLine) shortestLine = fragmentLength;
                if (fragmentLength > longestLine) longestLine = fragmentLength;

            }
        }

        double meanLineLength = totalLineLength / (double) totalLineCount;
        double meanFragmentOnlyLength = totalFragmentOnlyLineLength / (double) totalFragmentOnlyCount;
        double meanFragmentsPerCurve = (double) totalLineCount / (double) totalCurveCount;

        double totalSquaredLineLength = 0;
        double totalSquaredFragmentOnlyLength = 0;
        double totalSquaredFragmentsPerCurve = 0;

        for (Set<Line> curveData : curveLines) {

            int fragmentCount = curveData.size();
            boolean isFragmented = (fragmentCount > 1);

            totalSquaredFragmentsPerCurve += ((double) fragmentCount - meanFragmentsPerCurve) *
                    ((double) fragmentCount - meanFragmentsPerCurve);


            for (Line fragment : curveData) {
                double fragmentLength = fragment.length();

                totalSquaredLineLength += (fragmentLength - meanLineLength) *
                        (fragmentLength - meanLineLength);

                if (isFragmented) {
                    totalSquaredFragmentOnlyLength += (fragmentLength - meanFragmentOnlyLength) *
                            (fragmentLength - meanFragmentOnlyLength);
                }
            }
        }

        double varianceLineLength = totalSquaredLineLength / (double) totalLineCount;
        double varianceFragmentOnlyLength = totalSquaredFragmentOnlyLength / (double) totalFragmentOnlyCount;
        double varianceFragmentsPerCurve = totalSquaredFragmentsPerCurve / (double) totalCurveCount;

        return new Statistics(totalCurveCount, fragmentedCurveCount,
                shortestLine, longestLine,
                meanLineLength, varianceLineLength,
                meanFragmentOnlyLength, varianceFragmentOnlyLength,
                meanFragmentsPerCurve, varianceFragmentsPerCurve,
                bbox);
    }

    public String toString() {
        return "total: " + totalCurveCount +
                " shortest: " + shortestLine +
                " longest: " + longestLine;
    }
}
