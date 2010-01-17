package uk.co.danielrendall.fractdim.calculation;

import org.junit.Test;

import java.io.InputStream;
import java.io.IOException;

import uk.co.danielrendall.fractdim.svgbridge.SVGWithMetadata;
import uk.co.danielrendall.fractdim.svgbridge.SVGWithMetadataFactory;
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
        SVGWithMetadata svgWithMetadata = (new SVGWithMetadataFactory()).createFromInputStream(is);
        Log.geom.debug("Approximate bounding box: " + svgWithMetadata.getBoundingBox());
        Log.geom.debug("Number of curves: " + svgWithMetadata.getCurveCount());
        StatisticsCalculator sc = new StatisticsCalculatorBuilder().
                minAngle(StatisticsCalculator.TWO_DEGREES).
                svgWithMetadata(svgWithMetadata).build();
        Statistics stats = sc.process();
        Log.geom.debug("Accurate bounding box: " + stats.getBoundingBox());
        Log.geom.debug("Shortest line: " + stats.getShortestLine());
        Log.geom.debug("Longest line: " + stats.getLongestLine());

    }
}
