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

import org.junit.Test;

import java.io.InputStream;
import java.io.IOException;

import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.app.model.FractalDocumentMetadata;
import uk.co.danielrendall.fractdim.logging.Log;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 16-Jan-2010
 * Time: 17:35:27
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsCalculatorTest {

    @Test
    public void testStatisticsForCubic() throws IOException {
        InputStream is = StatisticsCalculatorTest.class.getResourceAsStream("/svg/single_cubic.svg");
        FractalController controller =  FractalController.fromInputStream(is);
        FractalDocument fractalDocument = controller.getDocument();
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(fractalDocument.getSvgDoc());
        fractalDocument.setMetadata(metadata);

        Log.geom.debug("Approximate bounding box: " + fractalDocument.getMetadata().getBoundingBox());
        Log.geom.debug("Number of curves: " + fractalDocument.getMetadata().getCurveCount());
        StatisticsCalculator sc = new StatisticsCalculatorBuilder().
                minAngle(StatisticsCalculator.TWO_DEGREES).
                fractalDocument(fractalDocument).build();
        Statistics stats = sc.process();
        Log.geom.debug("Accurate bounding box: " + stats.getBoundingBox());
        Log.geom.debug("Shortest line: " + stats.getShortestLine());
        Log.geom.debug("Longest line: " + stats.getLongestLine());

    }
}
