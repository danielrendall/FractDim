package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.svgbridge.SVGWithMetadata;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 16-Jan-2010
 * Time: 18:47:31
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsCalculatorBuilder {

    private double minAngle = StatisticsCalculator.TWO_DEGREES;
    private SVGWithMetadata svgWithMetadata = null;

    public StatisticsCalculatorBuilder minAngle(double minAngle) {
        this.minAngle = minAngle;
        return this;
    }

    public StatisticsCalculatorBuilder svgWithMetadata(SVGWithMetadata svgWithMetadata) {
        this.svgWithMetadata = svgWithMetadata;
        return this;
    }

    public StatisticsCalculator build() {
        if (svgWithMetadata == null) throw new IllegalStateException("Can't build - no svgWithMetadata");
        return new StatisticsCalculator(svgWithMetadata, minAngle);
    }

}
