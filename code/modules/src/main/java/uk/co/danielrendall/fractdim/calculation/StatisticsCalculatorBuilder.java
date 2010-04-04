package uk.co.danielrendall.fractdim.calculation;

import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 16-Jan-2010
 * Time: 18:47:31
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsCalculatorBuilder {

    private double minAngle = StatisticsCalculator.TWO_DEGREES;
    private FractalDocument fractalDocument = null;

    public StatisticsCalculatorBuilder minAngle(double minAngle) {
        this.minAngle = minAngle;
        return this;
    }

    public StatisticsCalculatorBuilder fractalDocument(FractalDocument fractalDocument) {
        this.fractalDocument = fractalDocument;
        return this;
    }

    public StatisticsCalculator build() {
        if (fractalDocument == null) throw new IllegalStateException("Can't build - no svgDoc");
        return new StatisticsCalculator(fractalDocument, minAngle);
    }

}
