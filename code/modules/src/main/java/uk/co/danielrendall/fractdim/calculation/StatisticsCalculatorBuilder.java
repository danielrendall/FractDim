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
