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
